/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package namlt.xml.asm.prj.utils;

import java.util.Optional;

/**
 *
 * @author ADMIN
 */
public class CommonUtils {

    public static Optional<Integer> parseInt(String s) {
        Optional<Integer> rs = Optional.empty();
        if (s == null) {
            return rs;
        }
        StringBuilder numberOnly = new StringBuilder();
        int length = s.length();
        char c;
        for (int i = 0; i < length; i++) {
            c = s.charAt(i);
            if (c >= '0' && c <= '9') {
                numberOnly.append(c);
            }
        }
        try {
            Integer integerValue = new Integer(numberOnly.toString());
            rs = Optional.of(integerValue);
        } catch (Exception e) {
            System.out.println("[ERROR] [Common utility]: Can not parse value \"" + s + "\" as Integer!");
        }
        return rs;
    }

    public static Optional<Double> parseDouble(String s) {
        Optional<Double> rs = Optional.empty();
        if (s == null) {
            return rs;
        }
        StringBuilder floatingNumberOnly = new StringBuilder();
        int length = s.length();
        char c;
        for (int i = 0; i < length; i++) {
            c = s.charAt(i);
            if ((c >= '0' && c <= '9') || c == '.') {
                floatingNumberOnly.append(c);
            }
        }
        try {
            Double doubleValue = new Double(floatingNumberOnly.toString());
            rs = Optional.of(doubleValue);
        } catch (Exception e) {
            System.out.println("[ERROR] [Common utility]: Can not parse value \"" + s + "\" as Double!");
        }
        return rs;
    }
}
