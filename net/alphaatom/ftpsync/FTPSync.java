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
import net.alphaatom.ftpsync.utils.*;

public class FTPSync {
	
	public static double VERSION = 0.1;
	public static HashMap<String, String> searchPaths;
	public static ArrayList<FTPPreset> ftpPresets;
	public static ArrayList<FTPSyncObj> ftpSyncs;
	public static ArrayList<FileChangeListener> listenerTracker = new ArrayList<FileChangeListener>();

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
		loadObjectsFromFile();
		createListeners();
		new UserInterfce();
	}
	
	private static void setDefaultOptions() {
		Options.setCheckEvery(10);
	}
	
	@SuppressWarnings("unchecked")
	private static void loadObjectsFromFile() {
		searchPaths = new HashMap<String, String>();
		Object loadedObject = null;
		try {
			loadedObject = FileIO.load("searchPaths.bin");
		} catch (FileNotFoundException e) {
			System.err.println("searchPaths.bin not found.");
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
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (loadedObj instanceof ArrayList) {
			System.out.println("Loaded FTP presets from file.");
			ftpPresets = (ArrayList<FTPPreset>) loadedObj;
		}
		ftpSyncs = new ArrayList<FTPSyncObj>();
		Object loadedObj2 = null;
		try {
			loadedObj2 = FileIO.load("syncs.bin");
		} catch (FileNotFoundException e) {
			System.err.println("syncs.bin not found.");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (loadedObj2 instanceof ArrayList) {
			System.out.println("Loaded FTP syncs from file.");
			ftpSyncs = (ArrayList<FTPSyncObj>) loadedObj2;
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
		if (ftpPresets == null) {
			return null;
		}
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
		if (ftpPresets == null) {
			return null;
		}
		for (FTPPreset ftpp : FTPSync.ftpPresets) {
			if (ftpp.getPresetName().equals(name)) {
				return ftpp;
			}
		}
		return null;
	}
	
	public static FTPSyncObj getFTPSyncByPath(String path) {
		if (ftpSyncs == null) {
			return null;
		}
		for (FTPSyncObj ftpo : FTPSync.ftpSyncs) {
			if (ftpo.getSearchPath().equals(path)) {
				return ftpo;
			}
		}
		return null;
	}
	
	public static String[] getFTPSyncPaths() {
		if (ftpSyncs == null) {
			return null;
		}
		int fNumber = ftpSyncs.size();
		String[] names = new String[fNumber];
		int count = 0;
		for (FTPSyncObj ftpo : ftpSyncs) {
			names[count] = ftpo.getSearchPath();
			count++;
		}
		return names;
	}
	
	public static void createListeners() {
		if (ftpSyncs == null) {
			return;
		}
		for (FTPSyncObj fso : ftpSyncs) {
			FileChangeListener list = new FileChangeListener(fso.getSearchPath(), !(new File(fso.getSearchPath()).isDirectory()));
			fso.setListener(list);
			FTPSync.listenerTracker.add(list);
		}
	}
}
