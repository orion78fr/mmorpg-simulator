package springSimulator;

import java.awt.Polygon;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.List;

import org.simgrid.msg.Host;
import org.simgrid.msg.Process;

import utils.Logger;
import utils.MessageWaiter;
import utils.MessageState;
import utils.SimException;
import utils.SimUtils;

public class Server extends Process {
	List<String> clientMailBox = new ArrayList<String>();
	
	public Server(Host host, String name, String[]args) {
		super(host,name,args);
	}
	
	@Override
	public void main(String[] args) {
		if(!this.getHost().getName().equals("server_0")){
			Logger.logWarning("Master 0 test only");
			return;
		}
		if (args.length < 1) {
			Logger.logCritical("Args for Master : <tickrate>");
			System.exit(1);
		}
		
		for(int i = 0; i < 10; i++){
			clientMailBox.add("client_" + i);
		}
		
		double tickrate = Double.valueOf(args[0]).doubleValue();
		
		double nextTick = 1/tickrate;
		
		List<MessageWaiter> l = new ArrayList<MessageWaiter>();
		
		
		for(int i = 0; i < 1000; i++){
			Logger.logVerbose("Tick " + i + " begin!");
			
			l.addAll(SimUtils.ireceiveAllUntil(this.getHost().getName(), nextTick, 500));
			
			for(MessageWaiter w : l){
				if(w.getState() == MessageState.SUCCESS){
					Logger.logInfo("Message : (" + w.getMessage().getType() + "," + w.getMessage().getContent() + ")");// Traitement du message
				}
			}
			
			/* retirer les messages recus */
			for(int j = 0; j < l.size();){
				if(l.get(j).getState() != MessageState.PENDING){
					l.remove(j);
				} else {
					j++;
				}
			}
			
			for(String c : clientMailBox){
				SimUtils.isend(c, 100, 1, "" + i);
			}
			
			
			Logger.logDebug("Tick " + i + " end.");
			
			nextTick += 1 / tickrate;
			
			// Traitement des entrées des autres process
		}
		
		Logger.logInfo("Simulation Ended");
		
		try {
			SimUtils.waitFor(5);
		} catch (SimException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
