package net.alphaatom.ftpsync.options;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;

import net.alphaatom.ftpsync.FTPSync;
import net.alphaatom.ftpsync.utils.*;

public class FTPPreset implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7531699810784727278L;

	private String _server;
	
	private String _username;
	
	private String _password;
	
	private int _port;
	
	private String presetName;
	
	public FTPPreset(String server, String username, String password, int port, String presetName) {
		this.setServer(server);
		this.setUsername(username);
		this.setPassword(password);
		this.setPort(port);
		this.setPresetName(presetName);
	}

	public String getServer() {
		return _server;
	}

	public void setServer(String _server) {
		this._server = _server;
	}

	public String getPresetName() {
		return presetName;
	}

	public void setPresetName(String presetNumber) {
		this.presetName = presetNumber;
	}

	public String getUsername() {
		return _username;
	}

	public void setUsername(String _username) {
		this._username = _username;
	}

	public String getPassword() {
		return _password;
	}

	public void setPassword(String _password) {
		this._password = _password;
	}

	public int getPort() {
		return _port;
	}

	public void setPort(int _port) {
		this._port = _port;
	}
	
	public void save() {
		if (FTPSync.getPreset(this.getPresetName()) != null) {
			FTPSync.ftpPresets.remove(FTPSync.getPreset(this.getPresetName()));
		}
		FTPSync.ftpPresets.add(this);
		try {
			FileIO.save(FTPSync.ftpPresets, "presets\\presets.bin");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.err.println("File not found.");
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("A serious error has occured and FTPSync will close.");
			System.exit(0);
		}
	}

}
