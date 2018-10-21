/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package namlt.xml.asm.prj.crawler;

import java.util.Map;

/**
 *
 * @author ADMIN
 */
public interface ValueIdentifier {

    void indentify(String key, String value);

    Map values();
}
