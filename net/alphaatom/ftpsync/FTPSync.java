package net.alphaatom.ftpsync;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

import net.alphaatom.ftpsync.options.*;
import net.alphaatom.ftpsync.ui.*;

public class FTPSync {
	
	public static double VERSION = 0.1;
	public static HashMap<String, String> searchPaths;
	public static ArrayList<FTPPreset> ftpPresets;

	public static void main(String[] args) {
		try {
		    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        if ("Nimbus".equals(info.getName())) {
		            UIManager.setLookAndFeel(info.getClassName());
		            break;
		        }
		    }
		} catch (Exception e) {
			try {
				UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			} catch (InstantiationException e1) {
				e1.printStackTrace();
			} catch (IllegalAccessException e1) {
				e1.printStackTrace();
			} catch (UnsupportedLookAndFeelException e1) {
				e1.printStackTrace();
			}
		}
		createDirectories();
		setDefaultOptions();
		loadSearchPaths();
		//new FileChangeListener("C:\\Users\\Matt\\GitTest\\textdocument.txt", true);
		new UserInterfce();
	}
	
	private static void setDefaultOptions() {
		Options.setCheckEvery(10);
	}
	
	@SuppressWarnings("unchecked")
	private static void loadSearchPaths() {
		searchPaths = new HashMap<String, String>();
		Object loadedObject = null;
		try {
			loadedObject = FileIO.load("searchPaths.bin");
		} catch (FileNotFoundException e) {
			return;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (loadedObject instanceof HashMap) {
			System.out.println("Loaded search paths from file.");
			searchPaths = (HashMap<String, String>) loadedObject;
		}
		ftpPresets = new ArrayList<FTPPreset>();
		Object loadedObj = null;
		try {
			loadedObj = FileIO.load("presets\\presets.bin");
		} catch (FileNotFoundException e) {
			System.err.println("presets.bin not found.");
			return;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (loadedObj instanceof ArrayList) {
			System.out.println("Loaded FTP presets from file.");
			ftpPresets = (ArrayList<FTPPreset>) loadedObj;
		}
	}
	
	private static void createDirectories() {
		try {
			File theDir = new File(System.getProperty("user.home") + "\\AppData\\Roaming\\FTPSync");
			if (!theDir.exists()) {
				theDir.mkdir();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			File theDir = new File(System.getProperty("user.home") + "\\AppData\\Roaming\\FTPSync\\presets");
			if (!theDir.exists()) {
				theDir.mkdir();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String[] getFTPNames() {
		int fNumber = ftpPresets.size();
		String[] names = new String[fNumber];
		int count = 0;
		for (FTPPreset ftpp : ftpPresets) {
			names[count] = ftpp.getPresetName();
			count++;
		}
		return names;
	}
	
	public static FTPPreset getPreset(String name) {
		for (FTPPreset ftpp : FTPSync.ftpPresets) {
			if (ftpp.getPresetName().equals(name)) {
				return ftpp;
			}
		}
		return null;
	}
}
