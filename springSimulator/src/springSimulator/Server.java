package springSimulator;

import java.util.ArrayList;
import java.util.List;

import org.simgrid.msg.Host;
import org.simgrid.msg.Process;

import springCommon.Point2d;
import springSimulator.utils.SimgridLogger;
import springSimulator.utils.SimUtils.Sim;
import springSimulator.utils.SimUtils.SimMessageState;
import springSimulator.utils.SimUtils.interfaces.SimComm;

public class Server extends Process {
	List<String> clientMailBox = new ArrayList<String>();
	
	public Server(Host host, String name, String[]args) {
		super(host,name,args);
	}
	
	@Override
	public void main(String[] args) {
		if(!this.getHost().getName().equals("server_0")){
			SimgridLogger.logWarning("Master 0 test only, stopping...");
			return;
		}
		if (args.length < 1) {
			SimgridLogger.logCritical("Args for Master : <tickrate>");
			System.exit(1);
		}
		
		double tickrate = Double.valueOf(args[0]).doubleValue();
		
		double nextTick = 1/tickrate;
		
		List<SimComm> l = new ArrayList<SimComm>();
		int emptyTick = 0;
		
		while(true){
			SimgridLogger.logVerbose("Tick begin!");

			nextTick += 1 / tickrate;
			
			l.addAll(Sim.ireceiveAllUntil(nextTick));
			
			/* Treatment of messages */
			for(int j = 0; j < l.size();){
				if(l.get(j).getState() == SimMessageState.PENDING){
					/* Do nothing */
					j++;
				} else {
					/* Remove received and buggy messages */
					SimComm w = l.remove(j);
					if(w.getState() == SimMessageState.SUCCESS){
						switch(((Message)(w.getMessage())).getType()){
						case MSG_CONNECT:
							SimgridLogger.logInfo(w.getMessage().getContent().toString() + " connected !");
							clientMailBox.add(w.getMessage().getContent().toString());
							break;
						case MSG_DISCONNECT:
							SimgridLogger.logInfo(w.getMessage().getContent().toString() + " disconnected !");
							clientMailBox.remove(w.getMessage().getContent().toString());
							break;
						case MSG_MOVE:
							Point2d p = (Point2d) w.getMessage().getContent();
							SimgridLogger.logInfo("Someone moved to (" + p.getX() + ", " + p.getY());// Traitement du message
							break;
						case MSG_WORLD_UPDATE:
							break;
						default:
							break;
						}
					} else {
						SimgridLogger.logWarning("A message crashed!");
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
				Sim.isend(Sim.getHostByName(c), new Message(MessageType.MSG_WORLD_UPDATE, null, 100));
			}
			
			
			SimgridLogger.logDebug("Tick end.");
		}
		
		Sim.waitFor(5);
		SimgridLogger.logInfo("Simulation Ended");
	}
}
