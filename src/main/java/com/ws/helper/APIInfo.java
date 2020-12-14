package com.ws.helper;

import java.io.IOException;
import java.io.InputStream;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class APIInfo {
	
	private final String apiKey;
	private final String apiSecret;
	private final String date;
	private final String charSet = "UTF-8";
	private final String restURL;
	private final String websocketURL;

	private static APIInfo instance = new APIInfo();
	public static APIInfo getInstance() {
		return instance;
	}
	public String getWebsocketURL() {
		return websocketURL;
	}

	public String getRestURL() {
		return restURL;
	}

	public String getApiKey() {
		return apiKey;
	}

	public String getDate() {
		return date;
	}

	public String getCharset() {
		return charSet;
	}
	private APIInfo() {
		Properties properties = load();
		this.restURL = properties.getProperty("REST_API_URL");
		this.websocketURL = properties.getProperty("WEBSOCKET_API_URL");
		this.apiKey = properties.getProperty("API_KEY");
		this.apiSecret = properties.getProperty("API_SECRET_KEY");
		//this.date = ZonedDateTime.now(ZoneId.of("UTC")).format(DateTimeFormatter.RFC_1123_DATE_TIME);
		this.date = ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("EEE,dd MMM yyyy HH:mm:ss"))+" UTC";
	}

	private String getSignature() {
		String message = new String("date: " + date);
		String sign = "";
		Mac hmac = null;
		try {
			hmac = Mac.getInstance("HmacSHA1");
			SecretKeySpec secret_key = new SecretKeySpec(this.apiSecret.getBytes(charSet), "HmacSHA1");
			hmac.init(secret_key);
			sign = Base64.encodeBase64String(hmac.doFinal(message.getBytes(charSet)));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return sign;
	}

	public String getAuthHeaderInfo() {
		String auth = "hmac username=\"" + this.apiKey + "\", algorithm=\"hmac-sha1\", headers=\"date\", signature=\""
				+ this.getSignature() + "\"";
		System.out.println(auth);
		return auth;
	}
	
	private Properties load(){
	        InputStream is = null;
	        Properties properties = new Properties();
            is = this.getClass().getResourceAsStream("/api-info.properties");
	        try {
	        	properties.load(is);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        return properties;
	    }
}
