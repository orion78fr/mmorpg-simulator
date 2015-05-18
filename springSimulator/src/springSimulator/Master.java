package springSimulator;

import java.util.ArrayList;
import java.util.List;

import org.simgrid.msg.Comm;
import org.simgrid.msg.Host;
import org.simgrid.msg.Msg;
import org.simgrid.msg.MsgException;
import org.simgrid.msg.Task;
import org.simgrid.msg.Process;

public class Master extends Process {
	public Master(Host host, String name, String[]args) {
		super(host,name,args);
	} 
	public void main(String[] args) throws MsgException {
		if (args.length != 1) {
			Msg.info("Args for Master : <tick time in ms>");
			System.exit(1);
		}
		
		// But in seconds!
		double ticktime = Integer.valueOf(args[0]).intValue()/1000.0;
		
		for(int i = 0; i < 10; i++){
			Task t = new Task("Tickwait", this.getHost().getSpeed() * ticktime, 0);
			
			t.execute();
			
			Msg.info("Tick" + i + " !");
			
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
