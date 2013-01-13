package net.alphaatom.ftpsync.options;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;

import net.alphaatom.ftpsync.FTPSync;
import net.alphaatom.ftpsync.FileIO;
import net.alphaatom.ftpsync.FileChangeListener;

public class FTPSyncObj implements Serializable {
	
	private static final long serialVersionUID = -9186952310078733265L;
	private String searchPath;
	private String ftpDirectory;
	private FTPPreset ftpPreset;
	private transient FileChangeListener listener;
	
	public FTPSyncObj(String search, String directory, FTPPreset preset) {
		this.setSearchPath(search);
		this.setFtpDirectory(directory);
		this.setFtpPreset(preset);
		boolean file = true;
		if (new File(search).isDirectory()) {
			file = false;
		}
		listener = new FileChangeListener(search, file);
		FTPSync.listenerTracker.add(listener);
	}

	public String getSearchPath() {
		return searchPath;
	}

	public void setSearchPath(String searchPath) {
		this.searchPath = searchPath;
	}

	public String getFtpDirectory() {
		return ftpDirectory;
	}

	public void setFtpDirectory(String ftpDirectory) {
		this.ftpDirectory = ftpDirectory;
	}

	public FTPPreset getFtpPreset() {
		return ftpPreset;
	}

	public void setFtpPreset(FTPPreset ftpPreset) {
		this.ftpPreset = ftpPreset;
	}
	
	public void save() {
		if (FTPSync.getFTPSyncByPath(this.getSearchPath()) != null) {
			FTPSync.ftpSyncs.remove(FTPSync.getFTPSyncByPath(this.getSearchPath()));
		}
		FTPSync.ftpSyncs.add(this);
		try {
			FileIO.save(FTPSync.ftpSyncs, "syncs.bin");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.err.println("File not found.");
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("A serious error has occured and FTPSync will close.");
			System.exit(0);
		}
	}

	public FileChangeListener getListener() {
		return listener;
	}

	public void setListener(FileChangeListener listener) {
		this.listener = listener;
	}
	

}
