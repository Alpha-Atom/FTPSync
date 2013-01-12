package net.alphaatom.ftpsync.ui;

import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import net.alphaatom.ftpsync.FTPSync;

public class UI {

	public UI(int width, int height, boolean visible) {
		JFrame ui_container = new JFrame();
		Container contentPane = ui_container.getContentPane();
		JTextField ftp_server = new JTextField("");
		JTextField ftp_user = new JTextField("");
		JPasswordField ftp_pass = new JPasswordField();
		JTextField ftp_port = new JTextField("21");
		contentPane.add(ftp_server);
		contentPane.add(ftp_user);
		contentPane.add(ftp_pass);
		contentPane.add(ftp_port);
		ui_container.setTitle("FTPSync v" + FTPSync.VERSION);
		ui_container.setSize(width, height);
		ui_container.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ui_container.setVisible(visible);
	}
	
	
	
}
