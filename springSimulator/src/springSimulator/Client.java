package springSimulator;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.simgrid.msg.Host;
import org.simgrid.msg.Process;

import springCommon.LogType;
import springCommon.Point2d;
import springSimulator.utils.SimgridLogger;
import springSimulator.utils.SimUtils.Sim;

public class Client extends Process {
	public Client(Host host, String name, String[]args) {
		super(host,name,args);
	}
	
	private String currentServer = "server_0";
	
	private void init(){
		Host.setAsyncMailbox(this.getHost().getName());
	}
	
	@Override
	public void main(String[] args) {
		this.init();
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
					Sim.waitUntil(Integer.parseInt(splitStr[0]) * (1 / tickrate));
				} catch (NumberFormatException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				/* Send to the server the action */
				switch(LogType.fromString(splitStr[1])){
				case connect:
					Sim.isend(Sim.getHostByName(this.currentServer), new Message(MessageType.MSG_CONNECT, this.getHost().getName(), 200));
					break;
				case move:
					Sim.isend(Sim.getHostByName(this.currentServer), new Message(MessageType.MSG_MOVE, new Point2d(Double.parseDouble(splitStr[2]), Double.parseDouble(splitStr[3])), 50));
					break;
				case disconnect:
					Sim.isend(Sim.getHostByName(this.currentServer), new Message(MessageType.MSG_DISCONNECT, this.getHost().getName(), 10));
					break;
				default:
					System.out.println("Unknown command " + splitStr[1] + " : ignored!");
					break;
				}
				
				Message m = (Message)Sim.receive();
				SimgridLogger.logInfo("Message : (" + m.getType() + "," + m.getContent() + ")");// Traitement du message
			}
			
			SimgridLogger.logInfo("Disconnected and no more moves!");
			
			Sim.waitFor(5);
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}