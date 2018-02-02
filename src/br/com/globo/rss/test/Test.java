package br.com.globo.rss.test;

import br.com.globo.rss.model.Feed;
import br.com.globo.rss.model.Message;
import br.com.globo.rss.read.Parser;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Test {
	private static JSONObject jsonObject;
	private static URL url;
	
	public static void main(String[] args) {
        Parser parser = new Parser(
                "http://revistaautoesporte.globo.com/rss/ultimas/feed.xml");
        Feed feed = parser.readFeed();
        
        // Original
        System.out.println("<< Feed Capturado >>");
        System.out.println(feed);
        for (Message message : feed.getMessages()) {
            System.out.println(message);

        }
        
        // JSON
        System.out.println("<< Feed Convertido em JSON >>");
        InputStream xml = getInputStreamForURLData("http://revistaautoesporte.globo.com/rss/ultimas/feed.xml");
        String xmlString = "";
		try {
			xmlString = IOUtils.toString(xml);
		} catch (IOException e) {
			System.out.println("Erro na captura do feed!");
		}
        jsonObject = null;
        // Inserindo o nome da pagina
        /*try {
			jsonObject.append(FilenameUtils.getBaseName(url.getPath()), "0");
		} catch (JSONException e1) {
			System.out.println("Erro na entrada do nome da pagina!");
		}*/
		try {
			jsonObject = XML.toJSONObject(xmlString);
		} catch (JSONException e) {
			System.out.println("Erro na captura no XML!");
		}
        ObjectMapper objectMapper = new ObjectMapper();
        Object json = null;
		try {
			json = objectMapper.readValue(jsonObject.toString(), Object.class);
		} catch (IOException e) {
			System.out.println("Erro na captura no JSON!");
		}
        String response = "";
		try {
			response = objectMapper.writeValueAsString(json);
		} catch (JsonProcessingException e) {
			System.out.println("Erro na conversao para String!");
		}
        
        System.out.println(response);
        
    }
	
	public static InputStream getInputStreamForURLData(String targetUrl) {
        HttpURLConnection httpConnection = null;
        InputStream content = null;

        try {
            url = new URL(targetUrl);
            URLConnection conn = url.openConnection();
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");
            httpConnection = (HttpURLConnection) conn;
            httpConnection.getResponseCode();
            content = (InputStream) httpConnection.getInputStream();
        } 
        catch (MalformedURLException e) {
            e.printStackTrace();
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }
}
