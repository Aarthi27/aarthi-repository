package com.ws.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Helper {

	public static String  read(InputStream inputStream) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		String response ="";
		String data;
		try {
			data = reader.readLine();

			while (data != null) {
				System.out.println(data);
				response +=data;
				data = reader.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response;
	}
}
