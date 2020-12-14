package com.ws.tasks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import com.ws.helper.APIInfo;

public class RestApiCaller {

	private static void call(APIInfo apiInfo) {
		String response = "";
		HttpURLConnection connection = null;
		BufferedReader input = null;

		try {
			connection = (HttpURLConnection) new URL(apiInfo.getRestURL()).openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("authorization", apiInfo.getAuthHeaderInfo());
			connection.setRequestProperty("content-type", "application/json");
			connection.setRequestProperty("date", apiInfo.getDate());
			connection.setRequestProperty("apikey", apiInfo.getApiKey());
			connection.setRequestProperty("charset", apiInfo.getCharset());
			connection.setRequestProperty("User-Agent", "Java API");
			String data = "";
			input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			while ((data = input.readLine()) != null) {
				response += data;
			}
			System.out.println(response);
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		call(APIInfo.getInstance());
	}
}
