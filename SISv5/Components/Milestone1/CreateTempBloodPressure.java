import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.*;
import java.io.*;
import java.net.*;
import java.lang.*;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class CreateTempBloodPressure {
	
	// socket for connection to SISServer
	private static Socket universal;
	private static int port = 53217;
	// message writer
	private static MsgEncoder encoder;
	// message reader
	private static MsgDecoder decoder;
	
	// scope of this component
	private static final String SCOPE = "SIS.Scope1";
	// name of this component
	private static final String NAME = "Milestone1";
	// messages types that can be handled by this component
	private static final List<String> TYPES = new ArrayList<String>(
			Arrays.asList(new String[] { "Setting", "Alert", "Confirm" }));

	// summary for all incoming / outgoing messages
	private static final String incomingMessages = "IN\tConfirm|Setting:Kill||Alert:NotWorking";
	private static final String outgoingMessages = "OUT\t Connect|Emergency";

	// shared by all kinds of emergencies that can be generated by this component
	private static KeyValueList emergency = new KeyValueList();
	
	static String alert_msg = "Not working";
	static ArrayList<Double> tempRecord = new ArrayList<Double>();

	/*
	 * Main program
	 */
	public static void main(String[] args) {
		while (true) {
			try {
				// try to establish a connection to SISServer
				universal = connect();

				// bind the message reader to inputstream of the socket
				decoder = new MsgDecoder(universal.getInputStream());
				// bind the message writer to outputstream of the socket
				encoder = new MsgEncoder(universal.getOutputStream());

				/*
				 * construct a Connect message to establish the connection
				 */
				KeyValueList conn = new KeyValueList();
				conn.putPair("Scope", SCOPE);
				conn.putPair("MessageType", "Connect");
				conn.putPair("IncomingMessages", incomingMessages);
                conn.putPair("OutgoingMessages", outgoingMessages);
				conn.putPair("Role", "Controller");
				conn.putPair("Name", NAME);
				conn.putPair("Test", "Tester");
				encoder.sendMsg(conn);
				System.out.println("Hello");

				initRecord();

				// KeyValueList for inward messages, see KeyValueList for
				// details
				KeyValueList kvList;

				while (true) {
					System.out.println("Hello");
					// attempt to read and decode a message, see MsgDecoder for
					// details
					kvList = decoder.getMsg();

					// process that message
					ProcessMsg(kvList);
				}

			} catch (Exception e) {
				// if anything goes wrong, try to re-establish the connection
				try {
					// wait for 1 second to retry
					Thread.sleep(1000);
				} catch (InterruptedException e2) {
				}
				System.out.println("Try to reconnect");
				try {
					universal = connect();
				} catch (IOException e1) {
				}
			}
		}
	}

	/*
	 * used for connect(reconnect) to SISServer
	 */
	static Socket connect() throws IOException {
		Socket socket = new Socket("127.0.0.1", port);
		return socket;
	}
	
	private static void initRecord() {

		emergency.putPair("Scope", SCOPE);
		emergency.putPair("MessageType", "Emergency");
		emergency.putPair("Sender", NAME);

		// Receiver may be different for each message, so it doesn't make sense
		// to set here
		// alert.putPair("Receiver", "RECEIVER");
	}

	/*
	 * process a certain message, execute corresponding actions
	 */
	static void ProcessMsg(KeyValueList kvList) throws IOException {

		String scope = kvList.getValue("Scope");
		
		String broadcast = kvList.getValue("Broadcast");
		String direction = kvList.getValue("Direction");
		
		if(broadcast!=null&&broadcast.equals("True")){
			
			if(direction!=null&&direction.equals("Up")){
				if(!scope.startsWith(SCOPE)){
					return;
				}
			}else if(direction!=null&&direction.equals("Down")){
				if(!SCOPE.startsWith(scope)){
					return;
				}
			}
		}else{
			if(!SCOPE.equals(scope)){
				return;
			}
		}
		
		String messageType = kvList.getValue("MessageType");
		if(!TYPES.contains(messageType)){
			return;
		}
		
		String sender = kvList.getValue("Sender");
		
		String receiver = kvList.getValue("Receiver");
		
		String purpose = kvList.getValue("Purpose");
		
		switch (messageType) {
		case "Alert":
			System.out.println("Testing");
			emergency.putPair("MainComponent", "BloodPressure");
			emergency.putPair("AuxComponents", "Temp");
				
			encoder.sendMsg(emergency);
			switch (sender) {
			case "BloodPressure":
				switch (purpose) {
				case "BloodPressureAlert":
					System.out.println("BloodPressureToTempBloodPressure received, start processing...");
					System.out.println("Start to check the Slope of Temperature...n");
					double tempSlopeAboutBP = calculateSlope();
					String alertMsgAboutBP = "Complex Alert!";
					
					String systString = kvList.getValue("Systolic");
					String diasString = kvList.getValue("Diastolic");
					int syst = 0, dias = 0;
					if (systString != null && !systString.equals(""))
					{
					    try
					    {
					        syst = Integer.parseInt(systString);
					    }
					    catch(Exception e)
					    {
					        syst = 0;
					    }
					}
					
					if (diasString != null && !diasString.equals(""))
					{
					    try
					    {
					        dias = Integer.parseInt(diasString);
					    }
					    catch(Exception e)
					    {
					        dias = 0;
					    }
					}
					// 140/90, 120/80 this is for demo purposes
					if(syst > 100 || dias > 70)
					{
					    if(tempSlopeAboutBP > 0.2)
					    {
					        alertMsgAboutBP = "The Patient's Blood Pressure is too high, it is possible because of the high rate of temperature increasing! please check the temperature!!!";
					    }
					    if(tempSlopeAboutBP < -0.2)
					    {
					        alertMsgAboutBP = "The Patient's Blood Pressure is too high. it is possible because of the high rate of temperature decreasing! please check the temperature!!! ";
					    }
					}
					else if(syst < 70 || dias < 50)
					{
					    if(tempSlopeAboutBP > 0.2)
					    {
					        alertMsgAboutBP = "The Patient's Blood Pressure is too low, it is possible because of the high rate of temperature increasing! please check the temperature!!!";
					    }
					    if(tempSlopeAboutBP < -0.2)
					    {
					        alertMsgAboutBP = "The Patient's Blood Pressure is too low. it is possible because of the high rate of temperature decreasing! please check the temperature!!! ";
					    }
					}
					
					if(tempSlopeAboutBP > 0.2 || tempSlopeAboutBP < -0.2)
					{
					    System.out.println("========= Send out Emergency message =========");
					
					    emergency.putPair("MainComponent", "BloodPressure");
					    emergency.putPair("AuxComponents", "Temp");
					    emergency.putPair("Note", alertMsgAboutBP);
					    emergency.putPair("Date", System.currentTimeMillis() + "");
					
					    encoder.sendMsg(emergency);
					}

					break;
				}
				break;
			case "Temp":
				switch (purpose) {
				case "TempAlert":
					System.out.println("TemptoTempBloodPressure received, start processing...");
					String date = kvList.getValue("Date");
					String temp = kvList.getValue("Temp");
					
					System.out.println("The temp is " + temp);
					System.out.println("The date is " + date);

					break;
				}
				break;
			
			}
			break;
		case "Confirm":
			System.out.println("Connect to SISServer successful.");
			break;
		case "Setting":
			if (receiver.equals(NAME)) {
				switch (purpose) {

				case "Kill":
					System.exit(0);
					break;
				}
			}
			break;
		}
	}
	

	// The following function is used to calculate the slope of temperature
	public static double calculateSlope() throws IOException
	{
	    String fileName = "temperatureRecord.csv";
	    BufferedReader br  = new BufferedReader(new FileReader(fileName));
	
	    double tempSlope = 0.0;
	
	    String temp;
	    while((temp = br.readLine()) != null)
	    {
	        String[] str = temp.split(",");
	        if(tempRecord.size() <= 20)
	        {
	            double newEntry = Double.parseDouble(str[1]);
	            tempRecord.add(newEntry);
	        }
	        else
	        {
	            tempRecord.remove(0);
	            double newEntry = Double.parseDouble(str[1]);
	            tempRecord.add(newEntry);
	        }
	    }
	    System.out.println("");
	    System.out.print("The twenty minute temperature record are as follows: ");
	    System.out.println(tempRecord);
	    System.out.println("");
	    Collections.sort(tempRecord);
	    double min = tempRecord.get(0);
	    double max = tempRecord.get(tempRecord.size() - 1);
	    tempSlope = (max - min) / max;
	    System.out.printf("tempSlope is %.2f", tempSlope);
	    System.out.println("n");
	
	    return tempSlope;
	}
}

