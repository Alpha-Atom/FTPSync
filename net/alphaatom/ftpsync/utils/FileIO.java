package net.alphaatom.ftpsync.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class FileIO {
	
	public static void save(Object obj,String path) throws FileNotFoundException, IOException
	{
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(System.getProperty("user.home") + "\\AppData\\Roaming\\FTPSync\\" + path));
		oos.writeObject(obj);
		oos.flush();
		oos.close();
	}
	public static Object load(String path) throws FileNotFoundException, IOException, ClassNotFoundException
	{
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(System.getProperty("user.home") + "\\AppData\\Roaming\\FTPSync\\" + path));
		Object result = ois.readObject();
		ois.close();
		return result;
	}

}
