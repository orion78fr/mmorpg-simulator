package springSimulator;

import java.util.ArrayList;
import java.util.List;

import org.simgrid.msg.Host;
import org.simgrid.msg.Process;

import springCommon.Point2d;
import springSimulator.utils.Logger;
import springSimulator.utils.MessageType;
import springSimulator.utils.MessageWaiter;
import springSimulator.utils.MessageState;
import springSimulator.utils.SimException;
import springSimulator.utils.SimUtils;

public class Server extends Process {
	List<String> clientMailBox = new ArrayList<String>();
	
	public Server(Host host, String name, String[]args) {
		super(host,name,args);
	}
	
	@Override
	public void main(String[] args) {
		if(!this.getHost().getName().equals("server_0")){
			Logger.logWarning("Master 0 test only, stopping...");
			return;
		}
		if (args.length < 1) {
			Logger.logCritical("Args for Master : <tickrate>");
			System.exit(1);
		}
		
		double tickrate = Double.valueOf(args[0]).doubleValue();
		
		double nextTick = 1/tickrate;
		
		List<MessageWaiter> l = new ArrayList<MessageWaiter>();
		int emptyTick = 0;
		
		while(true){
			Logger.logVerbose("Tick begin!");

			nextTick += 1 / tickrate;
			
			l.addAll(SimUtils.ireceiveAllUntil(this.getHost().getName(), nextTick, 500));
			
			/* Treatment of messages */
			for(int j = 0; j < l.size();){
				if(l.get(j).getState() == MessageState.PENDING){
					/* Do nothing */
					j++;
				} else {
					/* Remove received and buggy messages */
					MessageWaiter w = l.remove(j);
					if(w.getState() == MessageState.SUCCESS){
						switch(w.getMessage().getType()){
						case MSG_CONNECT:
							Logger.logInfo(w.getMessage().getContent().toString() + " connected !");
							clientMailBox.add(w.getMessage().getContent().toString());
							break;
						case MSG_DISCONNECT:
							Logger.logInfo(w.getMessage().getContent().toString() + " disconnected !");
							clientMailBox.remove(w.getMessage().getContent().toString());
							break;
						case MSG_MOVE:
							Point2d p = (Point2d) w.getMessage().getContent();
							Logger.logInfo("Someone moved to (" + p.getX() + ", " + p.getY());// Traitement du message
							break;
						case MSG_WORLD_UPDATE:
							break;
						default:
							break;
						}
					} else {
						Logger.logWarning("A message crashed!");
					}
				}
			}
			
			/* To end simulation */
			if(clientMailBox.size() == 0){
				emptyTick += 1;
				if(emptyTick == 10){
					break;
				}
			} else {
				emptyTick = 0;
			}
			
			for(String c : clientMailBox){
				SimUtils.isend(c, 100, MessageType.MSG_WORLD_UPDATE, null);
			}
			
			
			Logger.logDebug("Tick end.");
		}
		
		try {
			SimUtils.waitFor(5);
		} catch (SimException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Logger.logInfo("Simulation Ended");
	}
}
