package pt.bvav.sms.ui;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.concurrent.Semaphore;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;

import pt.bvav.sms.misc.SMSConfig;

public class Main extends JFrame implements WindowListener{
    
    public static boolean TEST = false;

	/**
     * 
     */
    private static final long serialVersionUID = -3496767732989827465L;

    public static class MySemaphore extends Semaphore{

		/**
         * 
         */
        private static final long serialVersionUID = 6216632501930200836L;

        public MySemaphore(int permits) {
			super(permits);
		}

		public void addTask(){
			super.reducePermits(1);
		}
		
	}

	private JFrame frmBvavSms;
	
	public static MySemaphore runningMsgs = new MySemaphore(1);

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
	    Main.TEST = args.length>0 && args[0].equals("--test");
	        
		SMSConfig.loadConfig();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main window = new Main();
					window.frmBvavSms.setIconImage(SMSConfig.createImage("mail.png", "BVAV SMS"));
					window.frmBvavSms.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Main() {
		initialize();
	}
	

	
	
	public void atClose() {
		
		Thread t = new Thread(new Runnable(){
			public void run(){
				while(!runningMsgs.tryAcquire()){
					try {
						synchronized(runningMsgs) {
							runningMsgs.wait();
						}
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			System.exit(0);
			}
		});
		
		t.start();
		
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmBvavSms = this;
		frmBvavSms.setTitle("BVAV - SMS");
		frmBvavSms.setResizable(false);
		frmBvavSms.setBounds(100, 100, 941, 211);
		frmBvavSms.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frmBvavSms.addWindowListener(this);
		frmBvavSms.getContentPane().setLayout(new GridLayout(1, 0, 0, 0));
		
		JButton btnSmsVoluntrios = new JButton("SMS Volunt\u00E1rios");
		btnSmsVoluntrios.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					SendSMS dialog = new SendSMS();
					dialog.setIconImage(SMSConfig.createImage("mail.png", "BVAV SMS"));
					dialog.setModal(true);
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		btnSmsVoluntrios.setFont(new Font("Tahoma", Font.BOLD, 18));
		frmBvavSms.getContentPane().add(btnSmsVoluntrios);
		
		JButton btnSmsComando = new JButton("SMS Comando");
		btnSmsComando.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					SendSMS dialog = new SendSMS();
					dialog.setIconImage(SMSConfig.createImage("mail.png", "BVAV SMS"));
					dialog.setModal(true);
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setComando();
					dialog.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		JButton btnSmsGeral = new JButton("SMS Geral");
		btnSmsGeral.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					SendSMS dialog = new SendSMS();
					dialog.setIconImage(SMSConfig.createImage("mail.png", "BVAV SMS"));
					dialog.setModal(true);
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setTodos();
					dialog.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		btnSmsGeral.setFont(new Font("Tahoma", Font.BOLD, 18));
		frmBvavSms.getContentPane().add(btnSmsGeral);
		btnSmsComando.setFont(new Font("Tahoma", Font.BOLD, 18));
		frmBvavSms.getContentPane().add(btnSmsComando);
		
		JButton btnSmsAlerta = new JButton("SMS Sirene");
		btnSmsAlerta.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try{
					Sirene dialog = new Sirene();
					dialog.setIconImage(SMSConfig.createImage("mail.png", "BVAV SMS"));
					dialog.setModal(true);
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
				}  catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		btnSmsAlerta.setFont(new Font("Tahoma", Font.BOLD, 18));
		frmBvavSms.getContentPane().add(btnSmsAlerta);
		
		JButton btnConfigura = new JButton("Configura\u00E7\u00E3o");
		btnConfigura.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					Config dialog = new Config();
					dialog.setIconImage(SMSConfig.createImage("mail.png", "BVAV SMS"));
					dialog.setModal(true);
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		btnConfigura.setFont(new Font("Tahoma", Font.BOLD, 16));
		frmBvavSms.getContentPane().add(btnConfigura);
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent e) {

		this.setVisible(false);
		atClose();		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	
	
}
