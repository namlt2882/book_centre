/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package namlt.xml.asm.prj.parser;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.xml.stream.XMLStreamReader;

/**
 *
 * @author ADMIN
 */
public abstract class BaseParser {

    public boolean isTag(String tagName, XMLStreamReader reader) {
        if (tagName == null || "".equals(tagName)) {
            return false;
        }
        if (tagName.equals(reader.getLocalName())) {
            return true;
        } else {
            return false;
        }
    }

    public boolean equalClass(XMLStreamReader reader, String nameSpace, String className) {
        if (className == null || "".equals(className)) {
            return false;
        }
        if (nameSpace == null) {
            nameSpace = "";
        }
        if (className.equals(reader.getAttributeValue(nameSpace, "class"))) {
            return true;
        } else {
            return false;
        }
    }

    public boolean equalClasses(XMLStreamReader reader, String nameSpace, String... className) {
        if (className == null || "".equals(className)) {
            return false;
        }
        if (nameSpace == null) {
            nameSpace = "";
        }
        String classAttr = reader.getAttributeValue(nameSpace, "class");
        if (classAttr == null) {
            return false;
        }
        List<String> classAttrs = Arrays.asList(classAttr.split(" ")).parallelStream().map(s -> {
            if (s != null && !"".equals(s.trim())) {
                return s;
            } else {
                return null;
            }
        }).filter(s -> s != null).collect(Collectors.toList());
        int counter = 0;
        for (String clazz : className) {
            boolean isFound = classAttrs.stream().map(s -> s.equals(clazz))
                    .filter(b -> b).findFirst().orElse(Boolean.FALSE);
            if (isFound) {
                counter++;
            }
        }
        return counter == className.length;
    }
}
