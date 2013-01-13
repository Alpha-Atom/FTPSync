package net.alphaatom.ftpsync;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import net.alphaatom.ftpsync.options.*;
import net.alphaatom.ftpsync.utils.*;

public class FileChangeListener extends Thread {
	
	private FTPSyncObj syncSettings;
	private String path;
	private boolean file;
	private boolean running = true;

	public FTPSyncObj getSyncSettings() {
		return syncSettings;
	}

	public void setSyncSettings(FTPSyncObj syncSettings) {
		this.syncSettings = syncSettings;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public boolean isFile() {
		return file;
	}

	public void setFile(boolean file) {
		this.file = file;
	}

	/**
	 * 
	 * @param path Path to the file or folder to listen on
	 * @param file True if the path is a single file, false if the path to a folder
	 */
	public FileChangeListener(String path, boolean file) {
		System.out.println("Added file listener for " + path);
		this.path = path;
		this.file = file;
		start();
	}
	
	public void run() {
		if (file) {
			while (running) {
				File fileToCheck = new File(path);
				if (fileToCheck.exists()) {
					String fileHash = null;
					try {
						fileHash = SHA1Hasher.hash(path);
					} catch (NoSuchAlgorithmException e) {
						System.err.println("Your system does not support SHA-1 and thus you may not use FTPSync");
						e.printStackTrace();
						System.exit(0);
					} catch (FileNotFoundException e) {
						System.err.println("The file you are trying to check no longer exists.");
						e.printStackTrace();
						break;
					} catch (IOException e) {
						System.err.println("A serious error has occured and FTPSync will close");
						e.printStackTrace();
						System.exit(0);
					}
					if (fileHash != null) {
						if (FTPSync.searchPaths.containsKey(path)) {
							System.out.println("Checking for file change at " + path);
							if (!FTPSync.searchPaths.get(path).equals(fileHash)) {
								System.out.println("File has changed, reuploading to FTP");
								FTPSync.searchPaths.put(path, fileHash);
							}
						} else {
							System.out.println("Adding new file to database.");
							FTPSync.searchPaths.put(path, fileHash);
						}
					}
					try {
						FileIO.save(FTPSync.searchPaths, "searchPaths.bin");
					} catch (FileNotFoundException e) {
						System.err.println("Unable to save search paths!");
						e.printStackTrace();
					} catch (IOException e) {
						System.err.println("Unable to save search paths!");
						e.printStackTrace();
					}
				} else {
					System.err.println("File not found, aborting checks.");
					break;
				}
				try {
					Thread.sleep(Options.getCheckEvery() * 1000);
				} catch (InterruptedException e) {
					System.err.println("Thread sleep option was interrupted.");
					e.printStackTrace();
				}
			}
		} else {
			//TODO: Add options for folders
		}
		System.out.println("FileChangeListener ended for " + path);
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

}
