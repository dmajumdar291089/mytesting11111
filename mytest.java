package com.dipankar.test;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;

public class AdmUpgradeLauncher extends JFrame {
	
	private JButton btnSchemaSelect;
	private JButton btnSchemaUnselect;
	private JButton btnUpdate;
	private JComboBox<String> selDatabase;
	private JLabel lblNewVersion;
	private JLabel lblDbCheckoutPath;
	private JLabel lblSelectDatabase;
	private JLabel lblSelectSchema;
	private JList<String> lstSelected;
	private JList<String> lstUnselected;
	private JScrollPane jScrollPane1;
	private JScrollPane jScrollPane2;
	private JTextField txtNewVersion;
	private JTextField txtDbheckoutPath;

	private String databaseFolder;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				new AdmUpgradeLauncher().setVisible(true);
			}
		});
	}

	/**
	 * Create the application.
	 */
	public AdmUpgradeLauncher() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		lblNewVersion = new JLabel();
		lblDbCheckoutPath = new JLabel();
		lblSelectDatabase = new JLabel();
		lblSelectSchema = new JLabel();
		txtNewVersion = new JTextField();
		txtDbheckoutPath = new JTextField();
		txtDbheckoutPath.setEnabled(false);
		lblNewVersion.setText("New Version:");
		lblDbCheckoutPath.setText("Database check-out path:");
		lblSelectDatabase.setText("Select database:");
		lblSelectSchema.setText("Select schema(s):");
		txtNewVersion.setToolTipText("Enter new version in x.y format");
		txtDbheckoutPath.setToolTipText("Select database check-out path");

		DefaultComboBoxModel<String> selDbModel = new DefaultComboBoxModel<String>();

		txtDbheckoutPath.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent me) {
				JFileChooser filechooser = new JFileChooser();
				filechooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

				filechooser.setApproveButtonText("Browse");
				int value = filechooser.showOpenDialog(null);
				if (value == JFileChooser.APPROVE_OPTION) {
					try {
						txtDbheckoutPath.setText(filechooser.getSelectedFile().getCanonicalPath());
						if (null != txtDbheckoutPath.getText() && txtDbheckoutPath.getText() != "") {
							databaseFolder = txtDbheckoutPath.getText();
						}
					} catch (IOException e) {
						txtDbheckoutPath.setText(filechooser.getSelectedFile().getAbsolutePath());
						if (null != txtDbheckoutPath.getText() && txtDbheckoutPath.getText() != "") {
							databaseFolder = txtDbheckoutPath.getText();
						}
					}

					if (null != databaseFolder && databaseFolder != "") {
						File dbFolderPath = new File(databaseFolder);
						selDbModel.removeAllElements();
						for (final File fileEntry : dbFolderPath.listFiles()) {
							if (fileEntry.isDirectory()) {
								selDbModel.addElement(fileEntry.getName());
							}
						}
					}
				}
			}

		});

		selDatabase = new JComboBox<String>(selDbModel);
		selDatabase.setToolTipText("Select database");

		DefaultListModel<String> schemaListModel = new DefaultListModel<String>();
		DefaultListModel<String> schemaUnselectListModel = new DefaultListModel<String>();
		
		selDatabase.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				Object item = selDatabase.getSelectedItem();

				if (null != item) {
					if ((null != item.toString()) && (item.toString() != "")) {
						String schemaPath = databaseFolder + File.separator + item.toString();

						if (null != schemaPath && schemaPath != "") {
							File schemaFolderPath = new File(schemaPath);
							schemaListModel.removeAllElements();
							schemaUnselectListModel.removeAllElements();
							for (final File fileEntry : schemaFolderPath.listFiles()) {
								if (fileEntry.isDirectory()) {
									schemaListModel.addElement(fileEntry.getName());
								}
							}
						}
					} 
				} else {
					schemaListModel.removeAllElements();
					schemaUnselectListModel.removeAllElements();
				}
			}
		});

		lstSelected = new JList<String>(schemaListModel);
		lstSelected.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		jScrollPane1 = new JScrollPane();
		jScrollPane1.setViewportView(lstSelected);
		lstUnselected = new JList<String>(schemaUnselectListModel);
		lstUnselected.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		jScrollPane2 = new JScrollPane();
		jScrollPane2.setViewportView(lstUnselected);
		btnSchemaSelect = new JButton();
		btnSchemaSelect.setText(">>");
		btnSchemaSelect.setToolTipText("Select");
		btnSchemaUnselect = new JButton();
		btnSchemaUnselect.setText("<<");
		btnSchemaUnselect.setToolTipText("Un-select");
		btnUpdate = new JButton();
		//btnUpdate.setEnabled(false);
		btnUpdate.setToolTipText("Start Adm Upgrade");
		btnUpdate.setText("Update");

		btnSchemaSelect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (lstSelected.getSelectedIndices().length > 0) {
					int[] selectedIndices = lstSelected.getSelectedIndices();
					for (int i = selectedIndices.length - 1; i >= 0; i--) {
						String selVal = schemaListModel.getElementAt(selectedIndices[i]);
						schemaListModel.removeElementAt(selectedIndices[i]);
						
						schemaUnselectListModel.addElement(selVal);
					}
				}
			}
		});
		
		btnSchemaUnselect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (lstUnselected.getSelectedIndices().length > 0) {
					int[] unSelectedIndices = lstUnselected.getSelectedIndices();
					for (int i = unSelectedIndices.length - 1; i >= 0; i--) {
						String unSelVal = schemaUnselectListModel.getElementAt(unSelectedIndices[i]);
						schemaUnselectListModel.removeElementAt(unSelectedIndices[i]);
						
						schemaListModel.addElement(unSelVal);
					}
				}
			}
		});
		
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String newVersion = txtNewVersion.getText();
				
				if (AdmUpgradeLauncher.isNumeric(newVersion)) {
					if (schemaUnselectListModel.isEmpty()) {
						JOptionPane.showMessageDialog(new JFrame(), "Enter all the details!!!");
					} else {
						// TODO
						
						JOptionPane.showMessageDialog(new JFrame(), "Adm Upgrade completed.");
					}
				} else {
					JOptionPane.showMessageDialog(new JFrame(), "Enter new version in x.y format!!!");
				}
			}
		});

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addGroup(
						layout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout
										.createParallelGroup(GroupLayout.Alignment.LEADING)
										.addGroup(layout.createSequentialGroup()
												.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
														.addComponent(lblNewVersion).addComponent(lblDbCheckoutPath)
														.addComponent(lblSelectDatabase))
												.addGap(25, 25, 25)
												.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
														.addComponent(txtDbheckoutPath)
														.addGroup(layout.createSequentialGroup().addGroup(layout
																.createParallelGroup(GroupLayout.Alignment.LEADING)
																.addComponent(txtNewVersion, GroupLayout.PREFERRED_SIZE,
																		130, GroupLayout.PREFERRED_SIZE)
																.addComponent(selDatabase, GroupLayout.PREFERRED_SIZE,
																		268, GroupLayout.PREFERRED_SIZE))
																.addGap(0, 0, Short.MAX_VALUE))))
										.addGroup(layout.createSequentialGroup().addComponent(lblSelectSchema).addGap(0,
												0, Short.MAX_VALUE))))
								.addGroup(layout.createSequentialGroup().addGap(38, 38, 38)
										.addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 195,
												GroupLayout.PREFERRED_SIZE)
										.addGap(13, 13, 13)
										.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
												.addComponent(btnSchemaUnselect, GroupLayout.DEFAULT_SIZE,
														GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addComponent(btnSchemaSelect, GroupLayout.DEFAULT_SIZE,
														GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
										.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
										.addComponent(jScrollPane2, GroupLayout.PREFERRED_SIZE, 195,
												GroupLayout.PREFERRED_SIZE)
										.addGap(0, 38, Short.MAX_VALUE)))
						.addContainerGap())
				.addGroup(layout.createSequentialGroup().addGap(185, 185, 185)
						.addComponent(btnUpdate, GroupLayout.PREFERRED_SIZE, 152, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addContainerGap()
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(lblNewVersion)
								.addComponent(txtNewVersion, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(lblDbCheckoutPath).addComponent(txtDbheckoutPath,
										GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(lblSelectDatabase).addComponent(selDatabase, GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGap(18, 18, 18).addComponent(lblSelectSchema)
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addGroup(layout.createSequentialGroup()
										.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
										.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
												.addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 184,
														Short.MAX_VALUE)
												.addComponent(jScrollPane2)))
								.addGroup(
										layout.createSequentialGroup().addGap(46, 46, 46).addComponent(btnSchemaSelect)
												.addGap(53, 53, 53).addComponent(btnSchemaUnselect)))
						.addGap(18, 18, 18).addComponent(btnUpdate).addContainerGap(15, Short.MAX_VALUE)));

		pack();
	}
	
	public static boolean isNumeric(String str) {
		return str.matches("^[1-9]\\d*(\\.[0-9]\\d*)$");
	}
}
