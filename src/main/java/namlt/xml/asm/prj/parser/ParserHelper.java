/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package namlt.xml.asm.prj.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.xml.stream.XMLStreamConstants;
import static javax.xml.stream.XMLStreamConstants.END_ELEMENT;
import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;
import static javax.xml.stream.XMLStreamConstants.CHARACTERS;
import static javax.xml.stream.XMLStreamConstants.CDATA;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import namlt.xml.asm.prj.crawler.NxbTreCrawler;
import namlt.xml.asm.prj.crawler.NxbTreCrawler;

/**
 *
 * @author ADMIN
 */
public class ParserHelper {

    private int maxTag = -1;

    public int getMaxTag() {
        return maxTag;
    }

    public void setMaxTag(int maxTag) {
        this.maxTag = maxTag;
    }

    private void checkMaxTag(int i) {
        if (maxTag == -1) {
            return;
        }
        if (maxTag + i <= 0) {
            throw new BoundReachedException();
        }
    }

    public int readTextInside(XMLStreamReader reader, StringBuilder sb) {
        return readTextInside(reader, 0, sb);
    }

    public int readTextInside(XMLStreamReader reader, int layerNumber, StringBuilder sb) {
        int counter = 0;
        try {
            counter += skipTag(reader, layerNumber);
        } catch (XMLStreamException ex) {
        }
        int evntType;
        try {
            while (reader.hasNext()) {
                evntType = reader.getEventType();
                if (evntType == XMLStreamConstants.CDATA || evntType == XMLStreamConstants.CHARACTERS) {
                    sb.append(reader.getText());
                }
                evntType = reader.next();
                if (evntType == START_ELEMENT) {
                    counter++;
                    break;
                } else if (evntType == END_ELEMENT) {
                    counter--;
                    break;
                }
            }
        } catch (XMLStreamException ex) {
            ex.printStackTrace();
        }
        return counter;
    }

    public int skipTag(XMLStreamReader reader, int time) throws XMLStreamException {
        int counter = 0;
        while (reader.hasNext()) {
            if (time <= 0) {
                break;
            }
            try {
                reader.nextTag();
            } catch (Exception e) {
                reader.next();
            }
            if (reader.getEventType() == START_ELEMENT) {
                time--;
                counter++;
            } else if (reader.getEventType() == END_ELEMENT) {
                counter--;
            }
        }
        return counter;
    }

    public int skipTag(XMLStreamReader reader, String tagName) throws XMLStreamException {
        return skipTag(reader, tagName, null);
    }

    public int skipTag(XMLStreamReader reader, String tagName, StringBuilder sb) throws XMLStreamException {
        if (tagName == null || "".equals(tagName)) {
            return 0;
        }
        if (reader.getEventType() == START_ELEMENT && reader.getLocalName().equals(tagName)) {
            return 0;
        }
        int counter = 0;
        int eventType;
        boolean readAllText = sb != null;
        while (reader.hasNext()) {
            try {
                reader.next();
            } catch (Exception e) {
                break;
            }
            eventType = reader.getEventType();
            if (readAllText && (eventType == CHARACTERS || eventType == CDATA)) {
                sb.append(reader.getText());
            }
            if (reader.getEventType() == START_ELEMENT) {
                counter++;
                if (reader.getLocalName().equals(tagName)) {
                    break;
                }
            } else if (reader.getEventType() == END_ELEMENT) {
                counter--;
            }
            checkMaxTag(counter);
        }
        return counter;
    }

    public int skipToCharacter(XMLStreamReader reader) throws XMLStreamException {
        int counter = 0;
        int evntType = reader.getEventType();
        if (evntType == XMLStreamConstants.CHARACTERS || evntType == XMLStreamConstants.CDATA) {
            return 0;
        }
        while (reader.hasNext()) {
            try {
                evntType = reader.next();
            } catch (Exception e) {
                break;
            }
            if (evntType == XMLStreamConstants.CHARACTERS || evntType == XMLStreamConstants.CDATA) {
                break;
            } else if (evntType == START_ELEMENT) {
                counter++;
            } else if (evntType == END_ELEMENT) {
                counter--;
            }
            checkMaxTag(counter);
        }
        return counter;
    }

    public void skipOutTag(XMLStreamReader reader, int time) throws XMLStreamException {
        while (reader.hasNext()) {
            if (time <= 0) {
                return;
            }
            try {
                reader.nextTag();
            } catch (Exception e) {
                reader.next();
            }
            if (reader.getEventType() == START_ELEMENT) {
                time++;
            } else if (reader.getEventType() == END_ELEMENT) {
                time--;
            }
        }
    }

    public static void writeToFile(String xmlSource) throws UnsupportedEncodingException, FileNotFoundException, IOException {
        ClassLoader classLoader = NxbTreCrawler.class.getClassLoader();
        String fileLocation = classLoader.getResource("crawler/schema/output.xml").getFile();
        File file = new File(URLDecoder.decode(fileLocation, "UTF-8"));
        FileOutputStream fw = new FileOutputStream(file);
        StringReader reader = new StringReader(xmlSource);
        int a;
        while ((a = reader.read()) != -1) {
            fw.write((char) a);
        }
        fw.flush();
        fw.close();
    }

}
