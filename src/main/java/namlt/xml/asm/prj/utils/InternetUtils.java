/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package namlt.xml.asm.prj.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;
import namlt.xml.asm.prj.crawler.Constant;

/**
 *
 * @author ADMIN
 */
public class InternetUtils {

    public static String crawl(String link) throws IOException {
        return crawl(link, null);
    }

    public static String crawl(String link, UrlConnectionConfig config) throws IOException {
        String rs = null;
        InputStream inputStream = null;
        BufferedReader bufferedReader = null;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            URL url = new URL(link);
            URLConnection con = url.openConnection();
            con.addRequestProperty("User-agent", Constant.USER_AGENT);
            if (config != null) {
                config.config(con);
            }
            inputStream = con.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            return stringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return rs;
    }

    public static String identifyHost(String link) {
        URL url;
        String host = "";
        try {
            url = new URL(link);
            host = url.getHost();
        } catch (MalformedURLException ex) {
            Logger.getLogger(InternetUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return host;
    }

}
