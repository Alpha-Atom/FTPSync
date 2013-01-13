package net.alphaatom.ftpsync.ui;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JComboBox;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.JButton;

import net.alphaatom.ftpsync.FTPSync;
import net.alphaatom.ftpsync.FileIO;
import net.alphaatom.ftpsync.options.*;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

public class UserInterfce {

	private JFrame frmFtpsyncV;
	private JTextField textField;
	private JTextField textField_1;
	private JPasswordField passwordField;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	private JTextField textField_5;
	/**
	 * Create the application.
	 */
	public UserInterfce() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void initialize() {
		frmFtpsyncV = new JFrame();
		frmFtpsyncV.setResizable(false);
		frmFtpsyncV.setTitle("FTPSync v0.1");
		frmFtpsyncV.setBounds(100, 100, 450, 286);
		frmFtpsyncV.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBorder(null);
		GroupLayout groupLayout = new GroupLayout(frmFtpsyncV.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(tabbedPane, GroupLayout.PREFERRED_SIZE, 444, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 272, Short.MAX_VALUE)
		);
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("New FTPSync", null, panel, null);
		panel.setLayout(null);
		
		textField_4 = new JTextField();
		textField_4.setBounds(115, 6, 221, 28);
		panel.add(textField_4);
		textField_4.setColumns(10);
		
		JLabel lblChooseFilefolder = new JLabel("Choose file/folder");
		lblChooseFilefolder.setBounds(6, 12, 97, 16);
		panel.add(lblChooseFilefolder);
		
		JButton btnBrowse = new JButton("Browse");
		btnBrowse.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				JFrame frame = new JFrame();
				frame.setBounds(100, 100, 450, 300);
				frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				JFileChooser chooser = new JFileChooser();
				chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				int choice = chooser.showOpenDialog(frame);
				if (choice == JFileChooser.APPROVE_OPTION) {
					String path = chooser.getSelectedFile().getAbsolutePath();
					textField_4.setText(path);
				}
			}
		});
		btnBrowse.setBounds(348, 6, 90, 28);
		panel.add(btnBrowse);
		
		final JComboBox comboBox_1 = new JComboBox();
		comboBox_1.setBounds(115, 35, 221, 26);
		panel.add(comboBox_1);
		for (int i = 0; i < FTPSync.getFTPNames().length; i++) {
			comboBox_1.addItem(FTPSync.getFTPNames()[i]);
		}
		
		JLabel lblChooseAnFtp = new JLabel("Choose FTP");
		lblChooseAnFtp.setBounds(6, 40, 97, 16);
		panel.add(lblChooseAnFtp);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(6, 133, 432, 2);
		panel.add(separator_1);
		
		final JComboBox comboBox_2 = new JComboBox();
		comboBox_2.setBounds(115, 142, 221, 26);
		panel.add(comboBox_2);
		if (FTPSync.getFTPSyncPaths() != null) {
			for (int i = 0; i < FTPSync.getFTPSyncPaths().length; i++) {
				comboBox_2.addItem(FTPSync.getFTPSyncPaths()[i]);
			}
		}
		
		JButton btnAddFtpsync = new JButton("Add FTPSync");
		btnAddFtpsync.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				String server = (String) comboBox_1.getSelectedItem();
				if (!textField_4.getText().equals("") && server != null && !textField_5.getText().equals("")) {
					String searchPath = textField_4.getText();
					FTPPreset ftpServer = FTPSync.getPreset(server);
					String pathOnFTP = textField_5.getText();
					new FTPSyncObj(searchPath, pathOnFTP, ftpServer).save();
					for (int i = 0; i < FTPSync.getFTPSyncPaths().length; i++) {
						comboBox_2.addItem(FTPSync.getFTPSyncPaths()[i]);
					}
					textField_4.setText("");
					textField_5.setText("");
				}
			}
		});
		btnAddFtpsync.setBounds(6, 96, 114, 28);
		panel.add(btnAddFtpsync);
		
		JLabel lblChooseFtpsync = new JLabel("Choose FTPSync");
		lblChooseFtpsync.setBounds(6, 147, 104, 16);
		panel.add(lblChooseFtpsync);
		
		JButton btnDelete_1 = new JButton("Delete");
		btnDelete_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				String path = (String) comboBox_2.getSelectedItem();
				FTPSyncObj syncObj = FTPSync.getFTPSyncByPath(path);
				FTPSync.ftpSyncs.remove(syncObj);
				comboBox_2.removeAllItems();
				for (int i = 0; i < FTPSync.getFTPSyncPaths().length; i++) {
					comboBox_2.addItem(FTPSync.getFTPSyncPaths()[i]);
				}
				try {
					FileIO.save(FTPSync.ftpSyncs, "syncs.bin");
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
					System.err.println("File not found.");
				} catch (IOException e1) {
					e1.printStackTrace();
					System.err.println("A serious error has occured and FTPSync will exit.");
					System.exit(0);
				}
			}
		});
		btnDelete_1.setBounds(6, 175, 90, 28);
		panel.add(btnDelete_1);
		
		JLabel lblFtpDirectory = new JLabel("FTP Directory");
		lblFtpDirectory.setToolTipText("Path on the FTP server to sync with.");
		lblFtpDirectory.setBounds(6, 68, 97, 16);
		panel.add(lblFtpDirectory);
		
		textField_5 = new JTextField();
		textField_5.setColumns(10);
		textField_5.setBounds(115, 62, 221, 28);
		panel.add(textField_5);
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("FTP Servers", null, panel_1, null);
		panel_1.setLayout(null);
		
		textField = new JTextField();
		textField.setBounds(66, 6, 122, 28);
		panel_1.add(textField);
		textField.setColumns(10);
		
		JLabel lblFtpHost = new JLabel("FTP Host");
		lblFtpHost.setBounds(6, 12, 55, 16);
		panel_1.add(lblFtpHost);
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(66, 37, 122, 28);
		panel_1.add(textField_1);
		
		JLabel lblFtpUser = new JLabel("FTP User");
		lblFtpUser.setBounds(6, 43, 55, 16);
		panel_1.add(lblFtpUser);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(94, 70, 94, 28);
		panel_1.add(passwordField);
		
		JLabel lblFtpPassword = new JLabel("FTP Password");
		lblFtpPassword.setBounds(6, 76, 90, 16);
		panel_1.add(lblFtpPassword);
		
		JLabel lblFtpPort = new JLabel("FTP Port");
		lblFtpPort.setBounds(6, 110, 55, 16);
		panel_1.add(lblFtpPort);
		
		textField_2 = new JTextField();
		textField_2.setText("21");
		textField_2.setBounds(66, 104, 44, 28);
		panel_1.add(textField_2);
		textField_2.setColumns(10);
		
		JLabel lblPresetName = new JLabel("Preset Name");
		lblPresetName.setBounds(6, 149, 90, 16);
		panel_1.add(lblPresetName);
		
		textField_3 = new JTextField();
		textField_3.setBounds(91, 143, 112, 28);
		panel_1.add(textField_3);
		textField_3.setColumns(10);
		
		JList list_1 = new JList();
		list_1.setBounds(284, 149, 49, -90);
		panel_1.add(list_1);
		
		final JComboBox comboBox = new JComboBox(FTPSync.getFTPNames());
		comboBox.setBounds(239, 33, 184, 26);
		panel_1.add(comboBox);
		comboBox.removeAllItems();
		for (int i = 0; i < FTPSync.getFTPNames().length; i++) {
			comboBox.addItem(FTPSync.getFTPNames()[i]);
		}
		
		JSeparator separator = new JSeparator();
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.setBounds(213, 9, 2, 217);
		panel_1.add(separator);
		
		JLabel lblEditAndView = new JLabel("Edit/View Existing FTP Servers");
		lblEditAndView.setBounds(239, 6, 189, 16);
		panel_1.add(lblEditAndView);
		
		JButton btnEdit = new JButton("Edit");
		btnEdit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				String preset = (String) comboBox.getSelectedItem();
				if (preset != null) {
					FTPPreset presetObj = FTPSync.getPreset(preset);
					textField.setText(presetObj.getServer());
					textField_1.setText(presetObj.getUsername());
					textField_2.setText(String.valueOf(presetObj.getPort()));
					textField_3.setText(presetObj.getPresetName());
					passwordField.setText(presetObj.getPassword());
				}
			}
		});
		btnEdit.setBounds(239, 70, 82, 28);
		panel_1.add(btnEdit);
		
		JButton btnNewButton = new JButton("Save");
		btnNewButton.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				if (!textField.getText().equals("") && !textField_1.getText().equals("") && !textField_2.getText().equals("") && !textField_3.getText().equals("")) {
					String pswd = null;
					if (Arrays.toString(passwordField.getPassword()).equals(Arrays.toString(new char[0]))) {
						pswd = "";
					} else {
						pswd = new String(passwordField.getPassword());
					}
					String host = textField.getText();
					String user = textField_1.getText();
					int port = 21;
					try {
						port = Integer.parseInt(textField_2.getText());
					} catch (NumberFormatException ex) {
						return;
					}
					String presetName = textField_3.getText();
					new FTPPreset(host, user, pswd, port, presetName).save();
					textField.setText("");
					textField_1.setText("");
					textField_2.setText("21");
					textField_3.setText("");
					passwordField.setText("");
					comboBox.removeAllItems();
					for (int i = 0; i < FTPSync.getFTPNames().length; i++) {
						comboBox.addItem(FTPSync.getFTPNames()[i]);
					}
					comboBox_1.removeAllItems();
					for (int i = 0; i < FTPSync.getFTPNames().length; i++) {
						comboBox_1.addItem(FTPSync.getFTPNames()[i]);
					}
				}
			}
		});
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnNewButton.setBounds(54, 183, 90, 28);
		panel_1.add(btnNewButton);
		
		JButton btnDelete = new JButton("Delete");
		btnDelete.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				String preset = (String) comboBox.getSelectedItem();
				if (preset != null) {
					FTPPreset presetObj = FTPSync.getPreset(preset);
					FTPSync.ftpPresets.remove(presetObj);
					comboBox.removeAllItems();
					for (int i = 0; i < FTPSync.getFTPNames().length; i++) {
						comboBox.addItem(FTPSync.getFTPNames()[i]);
					}
					comboBox_1.removeAllItems();
					for (int i = 0; i < FTPSync.getFTPNames().length; i++) {
						comboBox_1.addItem(FTPSync.getFTPNames()[i]);
					}
					try {
						FileIO.save(FTPSync.ftpPresets, "presets\\presets.bin");
					} catch (FileNotFoundException ex2) {
						ex2.printStackTrace();
						System.err.println("File not found.");
					} catch (IOException ex1) {
						ex1.printStackTrace();
						System.err.println("A serious error has occured and FTPSync will close.");
						System.exit(0);
					}
				}
			}
		});
		btnDelete.setBounds(341, 70, 82, 28);
		panel_1.add(btnDelete);
		frmFtpsyncV.getContentPane().setLayout(groupLayout);
		frmFtpsyncV.setVisible(true);
	}
}
