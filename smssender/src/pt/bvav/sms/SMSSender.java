package pt.bvav.sms;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.marre.SmsSender;
import org.marre.sms.SmsDcs;
import org.marre.sms.SmsException;
import org.marre.sms.SmsTextMessage;

import pt.bvav.sms.misc.SMSConfig;
import pt.bvav.sms.ui.Main;
import pt.bvav.sms.ui.ProgressIcon;

public class SMSSender {
	
	private static String oldS = "·‡‚ÈËÍÌÛÙÚ˙„ıÁ¡¿¬…» Õ”‘“⁄√’«";
	private static String newS = "aaaeeeiooouaocAAAEEEIOOOUAOC";
	private static Executor exec = Executors.newSingleThreadExecutor();

	public static void sendSMS(List<Integer> targets, final String message) {

		Main.runningMsgs.addTask();

		final Map<Integer,String> nums = new LinkedHashMap<>();
		if(targets != null) {
		    for(Integer i : targets) {
		        nums.put(i, SMSConfig.phones.get(i));
		    }
		} else {
		    for(String s : SMSConfig.sirene) {
                Integer i = Integer.valueOf(s);
                nums.put(i , SMSConfig.phones.get(i));
            }
		    for(Map.Entry<Integer, String> en : SMSConfig.phones.entrySet()) {
		        if(!nums.containsKey(en.getKey())) {
		            nums.put(en.getKey(), en.getValue());
		        }
		    }
		}

		Runnable r = new Runnable() {

			@Override
			public void run() {
				ProgressIcon pi = new ProgressIcon("BVAV SMS");
				pi.startProgress("Progresso envio", nums.size());
				int i=0;
				try {
					SmsSender ss = new SmsSender("pt.bvav.sms.PlainTextSMS", SMSConfig.gsm);

					String sms = message;
					for(int j = 0; j<oldS.length(); j++){
						sms = sms.replace(oldS.charAt(j), newS.charAt(j));
					}
					SmsTextMessage textMessage = new SmsTextMessage(sms, SmsDcs.ALPHABET_GSM, SmsDcs.MSG_CLASS_UNKNOWN);
					long now = System.currentTimeMillis();
					System.err.println("Start at " + (new Date()).toString());
					if(!Main.TEST) {
					    ss.connect();
					}
					for(Map.Entry<Integer, String> en : nums.entrySet()) {
					    Integer id = en.getKey();
					    String n  = en.getValue();
					    try {
					        if(!Main.TEST) {
					            ss.sendSms(textMessage, n, null);
					        }
					        System.err.println("[" + (System.currentTimeMillis() - now) + "]MESSAGE to: " + id + " (" + n + ")\n" + sms + "\n");
					        if(Main.TEST) {
					            Thread.sleep(300);
					        }

					    } catch(Exception ex) {
					        System.err.println("Error sending message to: " + id + " (" + n +").: " + ex.getMessage());
					    }
						pi.setWorkDone(++i);
					}
					if(!Main.TEST) {
					    ss.disconnect();
					}
					
					System.err.println("Completed in " + (System.currentTimeMillis() - now) + "ms");
				} catch (SmsException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				System.err.println("Removing icon..");
				pi.removeIcon();
				System.err.println("Done.");
				Main.runningMsgs.release();
				synchronized (Main.runningMsgs) {
					Main.runningMsgs.notify();
				}

			}
		};
		exec.execute(r);
	}

}
