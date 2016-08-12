package pt.bvav.sms.ui;

import java.awt.AWTException;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;

import pt.bvav.sms.misc.SMSConfig;

public class ProgressIcon {

	private TrayIcon icon;
	private volatile int workDone;
	
	public ProgressIcon(String tooltip) {
		addIcon(tooltip);
	}
	
	public void addIcon(String tooltip) {
		if(icon!=null) {
			removeIcon();
		}
		icon = new TrayIcon(SMSConfig.createImage("mail.png", "BVAV SMS"));
		icon.setToolTip(tooltip);
		icon.setImageAutoSize(true);
		try {
			SystemTray.getSystemTray().add(icon);
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void removeIcon(){
		SystemTray.getSystemTray().remove(icon);
		this.icon = null;
	}
	
	public void setWorkDone(int work) {
		synchronized (icon) {
			this.workDone = work;
			this.icon.notify();
		}
		
	}
	
	public void startProgress(final String message, final int totalWork) {
		synchronized (icon) {

			this.workDone = 0;
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					synchronized (icon) {
						do {
							icon.displayMessage(message, ( ((int)(100*(float)workDone / (float)totalWork)) + "%"), MessageType.INFO);
							try {
								icon.wait();
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} 
						} while(workDone<=totalWork);
					}
				}
			});
			t.start();
		}
	}
	

}
