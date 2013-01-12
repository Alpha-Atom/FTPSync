package net.alphaatom.ftpsync;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA1Hasher {
	
	public static String hash(String file) throws IOException, NoSuchAlgorithmException, FileNotFoundException {
		MessageDigest digestType = MessageDigest.getInstance("SHA1");
	    FileInputStream fileInput = null;
		fileInput = new FileInputStream(file);
	    byte[] dataBytes = new byte[1024];
	    int nread = 0; 
	    while ((nread = fileInput.read(dataBytes)) != -1) {
	      digestType.update(dataBytes, 0, nread);
	    };
	    byte[] mdbytes = digestType.digest();
	    StringBuffer sb = new StringBuffer("");
	    for (int i = 0; i < mdbytes.length; i++) {
	    	sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
	    }
	    fileInput.close();
	    return sb.toString();
	}

}
