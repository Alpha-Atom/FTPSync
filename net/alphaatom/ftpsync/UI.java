package net.alphaatom.ftpsync;

import javax.swing.JFrame;

public class UI {

	public UI(int width, int height, boolean visible) {
		JFrame ui_container = new JFrame();
		ui_container.setTitle("FTPSync v" + FTPSync.VERSION);
		ui_container.setSize(width, height);
		ui_container.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ui_container.setVisible(visible);
	}
}
