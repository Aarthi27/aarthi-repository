package com.ws.helper;

import java.io.FileOutputStream;
import java.security.KeyStore;

public class KeyWriter {

	private KeyStore ks;

	private String password;

	public KeyWriter() {
		try {
			ks = KeyStore.getInstance(KeyStore.getDefaultType());

			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void write(String aliasName, String privateKey, String secretKey) {
		try (FileOutputStream fos = new FileOutputStream("api-keys.jks")) {
			ks.store(fos, password.toCharArray());
			/* KeyStore.PrivateKeyEntry privateKey = new KeyStore.PrivateKeyEntry() */
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
