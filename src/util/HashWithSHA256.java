package util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.DatatypeConverter;

public class HashWithSHA256 {
	public String hash(String str) {
		String hash = "";
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] digest = md.digest(str.getBytes(StandardCharsets.UTF_8));
			hash = DatatypeConverter.printHexBinary(digest).toLowerCase();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
//		System.out.println("hash:" + hash);
		return hash;
	}
}
