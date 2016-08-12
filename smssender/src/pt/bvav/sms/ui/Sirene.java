package pt.bvav.sms.ui;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;

import pt.bvav.sms.SMSSender;
import pt.bvav.sms.misc.SMSConfig;

public class Sirene extends JDialog implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4923460680425500012L;

	private JDialog frmBvavSms;

	private List<Integer> targets = new ArrayList<>();
	/**
	 * Create the application.
	 */
	public Sirene() {
		initialize();
		for(String s : SMSConfig.sirene) {
			this.targets.add(Integer.valueOf(s));
		}
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmBvavSms = this;
		frmBvavSms.setTitle("BVAV - Sirene");
		frmBvavSms.setResizable(false);
		frmBvavSms.setBounds(100, 100, 941, 211);
		frmBvavSms.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmBvavSms.getContentPane().setLayout(new GridLayout(1, 0, 0, 0));
		
		JButton btnAcidente = new JButton("Acidente");
		btnAcidente.addActionListener(this);
		btnAcidente.setFont(new Font("Tahoma", Font.BOLD, 18));
		frmBvavSms.getContentPane().add(btnAcidente);
		
		JButton btnIncFlorestal = new JButton("Inc. Florestal");
		btnIncFlorestal.addActionListener(this);
			
		btnIncFlorestal.setFont(new Font("Tahoma", Font.BOLD, 18));
		frmBvavSms.getContentPane().add(btnIncFlorestal);
		
		JButton btnIncUrbanoVila = new JButton("Inc. Urbano Vila");
		btnIncUrbanoVila.addActionListener(this);
		btnIncUrbanoVila.setFont(new Font("Tahoma", Font.BOLD, 18));
		frmBvavSms.getContentPane().add(btnIncUrbanoVila);
		
		JButton btnIncUrbanoAldeia = new JButton("Inc. Urbano Aldeia");
		btnIncUrbanoAldeia.addActionListener(this);
		btnIncUrbanoAldeia.setFont(new Font("Tahoma", Font.BOLD, 16));
		frmBvavSms.getContentPane().add(btnIncUrbanoAldeia);
		
		JButton btnPlanoEmergncia = new JButton("Plano Emerg\u00EAncia");
		btnPlanoEmergncia.addActionListener(this);
		btnPlanoEmergncia.setFont(new Font("Tahoma", Font.BOLD, 16));
		frmBvavSms.getContentPane().add(btnPlanoEmergncia);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String message;
		if(e.getActionCommand().equals("Acidente")) {
			message = SMSConfig.acidente;
			
		} else if(e.getActionCommand().equals("Inc. Florestal")) {
			message = SMSConfig.florestal;

		} else if(e.getActionCommand().equals("Inc. Urbano Vila")) {
			message = SMSConfig.urbano_vila;
			
		} else if(e.getActionCommand().equals("Inc. Urbano Aldeia")) {
			message = SMSConfig.urbano_aldeia;
			
		} else if(e.getActionCommand().equals("Plano Emerg\u00EAncia")) {
			message = SMSConfig.plano_emergencia;
			targets = null;
			
		} else {
			throw new RuntimeException("Uh?");
		}
		String date = DateFormat.getTimeInstance(DateFormat.SHORT).format(new Date());
		SMSSender.sendSMS(targets, message.replace("<hora>", date));
		
		this.setVisible(false);
		this.dispose();
		
	}

}
