package pt.bvav.sms.misc;

import java.awt.Image;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Properties;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.swing.ImageIcon;

public class SMSConfig {

	public static String dbPath;
	public static String comPort;
	public static String[] comando;
	public static String[] sirene;
	public static String acidente;
	public static String florestal;
	public static String urbano_vila;
	public static String urbano_aldeia;
	public static String plano_emergencia;
	public static Properties gsm;
	
	public static SortedMap<Integer, String> phones;
	
	public static void loadConfig(){
		File f = new File("smsconfig.ini");
		try {
			if(!f.exists()) {
				f.createNewFile();
			} else {
				//f.delete();
				//f.createNewFile();
			}

			Properties p = new Properties();
			 gsm= new Properties();
			 gsm.setProperty("sms.gsm.stopbits", "1");

			BufferedReader br = new BufferedReader(new FileReader(f));
			while(br.ready()) {
				String s = br.readLine();
				if(!s.isEmpty() && !s.startsWith("//")) {
					String[] parts = s.split("=");
					if(parts[0].trim().equals("comando") || parts[0].trim().equals("sirene")) {
						p.put(parts[0].trim(), parts[1].trim().split(","));
					} else {
						p.put(parts[0].trim(), parts[1].trim());
					}

				}
			}
			br.close();

			dbPath = p.getProperty("dbpath", "db.ini");
			comPort = p.getProperty("COM", "COM1");
			gsm.setProperty("sms.gsm.serialport", comPort);
			comando = (String[]) p.get("comando");
			sirene = (String[]) p.get("sirene");

			if(comando == null) {
				comando = new String[] {"1","991"};
			}
			if(sirene == null) {
				/* sirene = new String[] {"1","8","9","10","11","12","13","14","15","16","19","21","26","28","29","30","32","33","34","36","39","40","43","44",
						"46","47","49","50","51","52","55","57","58","61","64","65","66","70","71","74","75","77","83","84","87","90","91","93",
						"94","95","96","97","103","105","115","116","117","119","121","122","124","125","126","127","129","132"};
						*/
				sirene = new String[] {"1","11","97","57","91","14","34","74","77","39","44","10","30","116","36","29","26","58","13","93","84","12","125","124","115","117","129","991","999"};
			}



			acidente 			= p.getProperty("acidente", "BVAV SIRENE <hora>: Acidente");
			florestal			= p.getProperty("florestal", "BVAV SIRENE <hora>: Incendio florestal");
			urbano_vila 		= p.getProperty("urbvila", "BVAV SIRENE <hora>: Incendio urbano - vila");
			urbano_aldeia 		= p.getProperty("urbaldeia", "BVAV SIRENE <hora>: Incendio urbando - aldeia");
			plano_emergencia	= p.getProperty("planoemergencia", "BVAV SIRENE <hora>: Plano emergencia");

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		File db = new File(dbPath);
		phones = new TreeMap<Integer, String>();
		if(db.exists() && db.canRead()) {
			try {
				BufferedReader br = new BufferedReader(new FileReader(db));
				while(br.ready()) {
					String line = br.readLine();
					if(!line.isEmpty() && !line.startsWith("//")) {
						String[] parts = line.split("-");
						String phone = "+351"+parts[0].trim();
						String num = parts[1].trim().split(" ")[0];
						
						Integer i_num = Integer.valueOf(num);
						phones.put(i_num, phone);
					}
				}
				br.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	
	public static void saveConfig() {
		gsm.setProperty("sms.gsm.serialport", comPort);

		File f = new File("smsconfig.ini");
		if(f.exists()) {
			f.delete();
		}
		try {
			f.createNewFile();
			OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(f));

			osw.write("//Formato: <chave>=<valor>\r\n//Comentarios começam com //\r\n");
			osw.write("//Ficheiro nums telemovel\r\ndbpath=" + dbPath + "\r\n");
			osw.write("//Porta COM onde o telemovel de envio esta ligado\r\nCOM=" + comPort + "\r\n");
			osw.write("//Lista elementos comando\r\ncomando=" + concat(comando) + "\r\n");
			osw.write("//Lista elementos que recebem sms sirene\r\nsirene=" + concat(sirene) + "\r\n");
			osw.write("//Texto mensagens. <hora> e substituido pela hora de alerta\r\n");
			osw.write("acidente=" + acidente + "\r\n");
			osw.write("florestal=" + florestal + "\r\n");
			osw.write("urbvila=" + urbano_vila + "\r\n");
			osw.write("urbaldeia=" + urbano_aldeia + "\r\n");
			osw.write("planoemergencia=" + plano_emergencia + "\r\n");
			osw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private static String concat(String[] list) {
		StringBuilder sb = new StringBuilder();
		int i;
		for(i=0;i<list.length-1;i++){
			sb.append(list[i]).append(',');
		}
		sb.append(list[i]);
		return sb.toString();
	}
	
	public static Image createImage(String path, String description) {
        ImageIcon ico = new ImageIcon(path, description);
        return ico.getImage();
    }
	
}
