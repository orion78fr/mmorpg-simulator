package springSimulator;

import java.util.ArrayList;
import java.util.List;

import org.simgrid.msg.Host;
import org.simgrid.msg.Msg;
import org.simgrid.msg.MsgException;
import org.simgrid.msg.Process;

import utils.MessageWaiter;
import utils.MessageState;
import utils.SimUtils;

public class Server extends Process {
	public Server(Host host, String name, String[]args) {
		super(host,name,args);
	}
	
	@Override
	public void main(String[] args) throws MsgException {
		if (args.length < 1) {
			Msg.info("Args for Master : <tickrate>");
			System.exit(1);
		}
		
		int tickrate = Integer.valueOf(args[0]).intValue();
		
		double nextTick = 1/tickrate;
		
		List<MessageWaiter> l = new ArrayList<MessageWaiter>();
		
		for(int i = 0; i < 10; i++){
			/*try {
				SimUtils.wait(1.0 / tickrate, this.getHost().getSpeed());
			} catch (SimException e) {
				e.printStackTrace();
			}*/
			
			Msg.info("Tick " + i + " begin!");
			
			while(Msg.getClock() < nextTick){
				l.add(SimUtils.ireceiveUntil(host.getName(), nextTick));
			}
			
			Msg.info("Tick " + i + " ended, " + l.size() + " messages got...");
			
			for(int j = 0; j < l.size();){
				MessageWaiter r = l.get(j);
				
				if(r.getState() == MessageState.SUCCESS){
					l.remove(j);
					// Traitement du message
				} else {
					j++;
				}
			}
			
			
			Msg.info("Tick " + i + " end.");
			
			// Traitement des entrées des autres process
			
			/*
			 * while(probe_message)
			 * {
			 * 	traitement_message
			 * }
			 * envoi_a_tous(résultats)
			 * 
			 * attendre(next_tick - temps_traitement)
			 */
		}

		/*int tasksCount = Integer.valueOf(args[0]).intValue();		
		double taskComputeSize = Double.valueOf(args[1]).doubleValue();		
		double taskCommunicateSize = Double.valueOf(args[2]).doubleValue();

		int slavesCount = Integer.valueOf(args[3]).intValue();

		Msg.info("Hello! Got "+  slavesCount + " slaves and "+tasksCount+" tasks to process");

		List<Comm> jobs = new ArrayList<Comm>();
		
		for (int i = 0; i < tasksCount; i++) {
			Task task = new Task("Task_" + i, taskComputeSize, taskCommunicateSize); 
			
			Msg.info("Sending \"" + task.getName()+ "\" to \"slave_" + i % slavesCount + "\"");
			jobs.add(task.isend("slave_"+(i%slavesCount)));
		}
		
		for(Comm c : jobs){
			c.waitCompletion();
		}

		Msg.info("All tasks have been dispatched. Let's tell everybody the computation is over.");

		for (int i = 0; i < slavesCount; i++) {
			FinalizeTask task = new FinalizeTask();
			task.send("slave_"+(i%slavesCount));
		}

		Msg.info("Goodbye now!");*/
	}
}
