package pt.bvav.sms;

import java.io.IOException;

import org.marre.sms.SmsAddress;
import org.marre.sms.SmsException;
import org.marre.sms.SmsMessage;
import org.marre.sms.SmsTextMessage;
import org.marre.sms.transport.gsm.GsmException;
import org.marre.sms.transport.gsm.GsmTransport;

public class PlainTextSMS extends GsmTransport {

	@Override
	public void connect() throws SmsException, IOException {
		super.connect();
		serialComm_.send("AT+CMGF=1\r\n");
		if(!serialComm_.readLine().startsWith("OK")){
			throw new SmsException("Set to text mode failed");
		}
	}

	@Override
	public String send(SmsMessage msg, SmsAddress dest, SmsAddress sender)
			throws SmsException, IOException {
		
		serialComm_.send("AT+CMGS=\"+" + dest.getAddress() + "\"\r\n");
		serialComm_.readLine("> ");
		serialComm_.send(((SmsTextMessage)msg).getText() + "\032");
		String cmgs = serialComm_.readLine();
		if (cmgs.startsWith("+CMGS"))
		{
			//("Expecting a empty row");
			String empty = serialComm_.readLine();
			if (empty.trim().length() != 0) {
				throw new SmsException("AT+CMGF failed." + empty);
			}

			// log_.debug("Expecting a OK");
			String ok = serialComm_.readLine();
			//log_.debug("Expecting a OK");
			if (! ok.startsWith("OK")) {
				throw new SmsException("AT+CMGF failed." + ok);
			}
		}
		return null;

	}

	
	
}
