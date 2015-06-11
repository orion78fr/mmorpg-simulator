package springSimulator;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.simgrid.msg.Host;
import org.simgrid.msg.Process;

import springCommon.LogType;
import springCommon.Point2d;
import springSimulator.utils.Logger;
import springSimulator.utils.Message;
import springSimulator.utils.MessageType;
import springSimulator.utils.SimException;
import springSimulator.utils.SimUtils;

public class Client extends Process {
	public Client(Host host, String name, String[]args) {
		super(host,name,args);
	}
	
	private String currentServer = "server_0";
	
	@Override
	public void main(String[] args) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader("../springVisualizer/movements/" + this.getHost().getName()));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
		double tickrate = Double.valueOf(args[0]).doubleValue();
		try {
			String str;
		
			while((str = reader.readLine()) != null){
				String[] splitStr = str.split(";");
				try {
					SimUtils.waitUntil(Integer.parseInt(splitStr[0]) * (1 / tickrate));
				} catch (NumberFormatException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SimException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				/* Send to the server the action */
				switch(LogType.fromString(splitStr[1])){
				case connect:
					SimUtils.isend(this.currentServer, 200, MessageType.MSG_CONNECT, this.getHost().getName()/*new Point2d(Double.parseDouble(splitStr[2]), Double.parseDouble(splitStr[3]))*/);
					break;
				case move:
					SimUtils.isend(this.currentServer, 50, MessageType.MSG_MOVE, new Point2d(Double.parseDouble(splitStr[2]), Double.parseDouble(splitStr[3])));
					break;
				case disconnect:
					SimUtils.isend(this.currentServer, 10, MessageType.MSG_DISCONNECT, this.getHost().getName());
					break;
				default:
					System.out.println("Unknown command " + splitStr[1] + " : ignored!");
					break;
				}
				
				try {
					Message m = SimUtils.receive(this.getHost().getName());
					Logger.logInfo("Message : (" + m.getType() + "," + m.getContent() + ")");// Traitement du message
				} catch (SimException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			Logger.logInfo("Disconnected and no more moves!");
			
			try {
				SimUtils.waitFor(5);
			} catch (SimException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}