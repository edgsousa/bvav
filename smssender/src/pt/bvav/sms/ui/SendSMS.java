package pt.bvav.sms.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.text.MaskFormatter;

import pt.bvav.sms.SMSSender;
import pt.bvav.sms.misc.SMSConfig;

public class SendSMS extends JDialog implements ActionListener{

	private final JPanel contentPanel = new JPanel();
	private JFormattedTextField inputNumber = null;
	private JLabel label;
	private List<Integer> targets = new ArrayList<>();
	private JTextArea textArea;
	private JLabel lblCount;

	/**
	 * Create the dialog.
	 */
	public SendSMS() {
		setTitle("Enviar SMS");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[] {0, 0, 0, 0};
		gbl_contentPanel.rowHeights = new int[]{0, 0, 0};
		gbl_contentPanel.columnWeights = new double[]{1.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_contentPanel.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		contentPanel.setLayout(gbl_contentPanel);

		final JPanel numbers = new JPanel();
		FlowLayout flowLayout = (FlowLayout) numbers.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		GridBagConstraints gbc_numbers = new GridBagConstraints();
		gbc_numbers.gridwidth = -1;
		gbc_numbers.insets = new Insets(0, 0, 5, 5);
		gbc_numbers.fill = GridBagConstraints.BOTH;
		gbc_numbers.gridx = 0;
		gbc_numbers.gridy = 0;
		contentPanel.add(numbers, gbc_numbers);
		{
			label = new JLabel(" ");
			numbers.add(label);
		}
		{
			
		}


		
		try {


			inputNumber = new JFormattedTextField(new MaskFormatter("#####"));
			inputNumber.setColumns(2);
			inputNumber.setText("");
			inputNumber.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent arg0) {
					if(arg0.getKeyCode() == KeyEvent.VK_ENTER) {
						
						if(tryAdd(inputNumber.getText())) {
							final JLabel lbl = new JLabel(inputNumber.getText().trim());
							lbl.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(getForeground()), BorderFactory.createEmptyBorder(1, 3, 1, 3)));
							lbl.addMouseListener(new MouseAdapter() {
								@Override
								public void mouseClicked(MouseEvent arg0) {
									targets.remove(Integer.valueOf(lbl.getText()));
									lbl.setVisible(false);
									numbers.remove(lbl);
									numbers.validate();
									numbers.getParent().validate();
								}
							});
							numbers.add(lbl);
							inputNumber.setText("");
							numbers.getParent().validate();
						} else {
							inputNumber.setText("");
						}
					}
				}
			});

			GridBagConstraints gbc_inputNumber = new GridBagConstraints();
			gbc_inputNumber.insets = new Insets(0, 0, 5, 0);
			gbc_inputNumber.fill = GridBagConstraints.HORIZONTAL;
			gbc_inputNumber.gridx = 3;
			gbc_inputNumber.gridy = 0;
			contentPanel.add(inputNumber, gbc_inputNumber);
			{
				textArea = new JTextArea();
				textArea.setWrapStyleWord(true);
				textArea.addKeyListener(new KeyAdapter() {
					@Override
					public void keyTyped(KeyEvent arg0) {
						lblCount.setText(Integer.toString(textArea.getText().length()));
					}
				});
				textArea.setLineWrap(true);
				GridBagConstraints gbc_textArea = new GridBagConstraints();
				gbc_textArea.gridwidth = GridBagConstraints.REMAINDER;
				gbc_textArea.fill = GridBagConstraints.BOTH;
				gbc_textArea.gridx = 0;
				gbc_textArea.gridy = 1;
				contentPanel.add(textArea, gbc_textArea);
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				lblCount = new JLabel("0");
				buttonPane.add(lblCount);
			}
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
				okButton.addActionListener(this);

			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
				cancelButton.addActionListener(this);

			}
		}
	}

	public void actionPerformed(ActionEvent e) {
		
		if(e.getActionCommand().equals("OK")) {
			SMSSender.sendSMS(targets, textArea.getText().trim());
		}
		
		this.setVisible(false);
		this.dispose();
	}
	protected boolean tryAdd(String text) {
		Integer i = Integer.valueOf(text.trim());
		if(SMSConfig.phones.containsKey(i) && !this.targets.contains(i) && this.targets.size()<13) {
			this.targets.add(i);
			return true;
		} else {
			return false;
		}
	}
	
	public void setComando(){
		this.inputNumber.setVisible(false);
		this.label.setText("COMANDO");
		this.label.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(getForeground()), BorderFactory.createEmptyBorder(1, 3, 1, 3)));
		this.label.getParent().validate();
		for(String s : SMSConfig.comando) {
			this.targets.add(Integer.valueOf(s));
		}
	}
	
	public void setTodos(){
		this.inputNumber.setVisible(false);
		this.label.setText("TODOS");
		this.label.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(getForeground()), BorderFactory.createEmptyBorder(1, 3, 1, 3)));
		this.label.getParent().validate();
		for(Integer i : SMSConfig.phones.keySet()) {
			this.targets.add(i);
		}
	}

}
