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

/**
 *
 * @author ADMIN
 */
public class ParserHelper extends BaseParser {

    private XMLStreamReader reader;
    private int counter;

    public ParserHelper(XMLStreamReader reader) {
        this.reader = reader;
        counter = -1;
    }

    public void mark() {
        counter = 1;
    }

    public void unmark() {
        counter = -1;
    }

    private void checkBound() {
        if (counter == -1) {
            return;
        }
        if (counter == 0) {
            throw new BoundReachedException();
        }
    }

    public String readTextInside() {
        return readTextInside(0);
    }

    public String readTextInside(int layerNumber) {
        StringBuilder sb = new StringBuilder();
        try {
            skipTag(layerNumber);
        } catch (XMLStreamException ex) {
        }
        int evntType;
        try {
            while (reader.hasNext()) {
                evntType = reader.getEventType();
                if (evntType == XMLStreamConstants.CDATA || evntType == XMLStreamConstants.CHARACTERS) {
                    sb.append(reader.getText());
                }
                checkBound();
                evntType = reader.next();
                try {
                } catch (BoundReachedException e) {
                    break;
                }
                if (evntType == START_ELEMENT) {
                    counterIncrement();
                    break;
                } else if (evntType == END_ELEMENT) {
                    counterDecrement();
                    break;
                }
            }
        } catch (XMLStreamException ex) {
            ex.printStackTrace();
        }
        return sb.toString();
    }

    private void counterIncrement() {
        counter += (counter == -1) ? 0 : 1;
    }

    public void addCounter(int i) {
        counter += (counter == -1) ? 0 : i;
    }

    private void counterDecrement() {
        counter -= (counter == -1) ? 0 : 1;
    }

    public int skipTag(int time) throws XMLStreamException {
        while (reader.hasNext()) {
            if (time <= 0) {
                break;
            }
            checkBound();
            try {
                reader.nextTag();
            } catch (Exception e) {
                reader.next();
            }
            if (reader.getEventType() == START_ELEMENT) {
                time--;
                counterIncrement();
            } else if (reader.getEventType() == END_ELEMENT) {
                counterDecrement();
            }
        }
        return counter;
    }

    public int skipTo(String tagName) throws XMLStreamException {
        return skipTo(tagName, null);
    }

    public int skipToWithClassName(String tagName, String... className) throws XMLStreamException {
        while (true) {
            skipTo(tagName);
            if (equalClasses(reader, "", className)) {
                return counter;
            } else {
                int eventType = reader.next();
                if (eventType == START_ELEMENT) {
                    counterIncrement();
                } else if (eventType == END_ELEMENT) {
                    counterDecrement();
                }
            }
        }
    }

    public int skipTo(String tagName, StringBuilder sb) throws XMLStreamException {
        if (tagName == null || "".equals(tagName)) {
            return counter;
        }
        int eventType = reader.getEventType();
        if (eventType == START_ELEMENT && reader.getLocalName().equals(tagName)) {
            return counter;
        }
        boolean readAllText = sb != null;
        while (reader.hasNext()) {
            checkBound();
            try {
                eventType = reader.next();
            } catch (Exception e) {
                break;
            }
            if (readAllText && (eventType == CHARACTERS || eventType == CDATA)) {
                sb.append(reader.getText());
            }
            if (eventType == START_ELEMENT) {
                counterIncrement();
                if (reader.getLocalName().equals(tagName)) {
                    break;
                }
            } else if (eventType == END_ELEMENT) {
                counterDecrement();
            }
        }
        return counter;
    }

    public int skipToCharacter() throws XMLStreamException {
        int evntType = reader.getEventType();
        if (evntType == XMLStreamConstants.CHARACTERS || evntType == XMLStreamConstants.CDATA) {
            return 0;
        }
        while (reader.hasNext()) {
            checkBound();
            try {
                evntType = reader.next();
            } catch (Exception e) {
                break;
            }
            if (evntType == XMLStreamConstants.CHARACTERS || evntType == XMLStreamConstants.CDATA) {
                break;
            }
            if (evntType == START_ELEMENT) {
                counterIncrement();
            } else if (evntType == END_ELEMENT) {
                counterDecrement();
            }
        }
        return counter;
    }

    public void skipToBound(StringBuilder sb) throws XMLStreamException {
        if (counter == -1) {
            return;
        }
        int eventType = reader.getEventType();
        boolean readAllText = sb != null;
        while (reader.hasNext()) {
            try {
                checkBound();
            } catch (BoundReachedException e) {
                break;
            }
            if (readAllText && (eventType == CHARACTERS || eventType == CDATA)) {
                sb.append(reader.getText());
            }
            try {
                eventType = reader.next();
            } catch (Exception e) {
                break;
            }
            if (eventType == START_ELEMENT) {
                counterIncrement();
            } else if (eventType == END_ELEMENT) {
                counterDecrement();
            }
        }
    }

    public static void writeToFile(String xmlSource) throws UnsupportedEncodingException, FileNotFoundException, IOException {
        ClassLoader classLoader = ParserHelper.class.getClassLoader();
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
