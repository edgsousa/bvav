package pt.bvav.sms.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.comm.CommPortIdentifier;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JComboBox;

import pt.bvav.sms.misc.SMSConfig;

import java.awt.GridLayout;
import java.util.Enumeration;
import java.util.Vector;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Config extends JDialog implements ActionListener {

	private JComboBox comboBox;

	private ComboBoxModel<String> getCOMPorts(){
		@SuppressWarnings("unchecked")
		Enumeration<CommPortIdentifier> ports = CommPortIdentifier.getPortIdentifiers();
		Vector<String> v = new Vector<String>();

		while(ports.hasMoreElements()) {
			CommPortIdentifier cpi = ports.nextElement();
			if(!cpi.isCurrentlyOwned() && cpi.getPortType()==CommPortIdentifier.PORT_SERIAL) {
				v.add(cpi.getName());
			}
		}
		
		return new DefaultComboBoxModel<String>(v);
	}
	
	public void actionPerformed(ActionEvent e) {
		
		if(e.getActionCommand().equals("OK")) {
			SMSConfig.comPort = comboBox.getSelectedItem().toString();
			SMSConfig.saveConfig();
		}
		
		this.setVisible(false);
		this.dispose();
	}
	
	/**
	 * Create the dialog.
	 */
	public Config() {
		setBounds(100, 100, 564, 386);
		getContentPane().setLayout(new BorderLayout());
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				okButton.addActionListener(this);
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				cancelButton.addActionListener(this);
				buttonPane.add(cancelButton);
			}
		}
		{
			JPanel panel = new JPanel();
			getContentPane().add(panel, BorderLayout.CENTER);
			GridBagLayout gbl_panel = new GridBagLayout();
			gbl_panel.columnWidths = new int[]{0, 0, 100, 0};
			gbl_panel.rowHeights = new int[]{0, 0, 0};
			gbl_panel.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
			gbl_panel.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
			panel.setLayout(gbl_panel);
			{
				JLabel lblPortaCom = new JLabel("Porta COM:");
				GridBagConstraints gbc_lblPortaCom = new GridBagConstraints();
				gbc_lblPortaCom.insets = new Insets(0, 0, 0, 5);
				gbc_lblPortaCom.anchor = GridBagConstraints.EAST;
				gbc_lblPortaCom.gridx = 1;
				gbc_lblPortaCom.gridy = 1;
				panel.add(lblPortaCom, gbc_lblPortaCom);
			}
			{
				comboBox = new JComboBox();
				GridBagConstraints gbc_comboBox = new GridBagConstraints();
				gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
				gbc_comboBox.gridx = 2;
				gbc_comboBox.gridy = 1;
				comboBox.setModel(getCOMPorts());
				panel.add(comboBox, gbc_comboBox);
			}
		}
	}

}
