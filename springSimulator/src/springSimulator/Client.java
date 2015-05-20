package springSimulator;

import org.simgrid.msg.Host;
import org.simgrid.msg.HostFailureException;
import org.simgrid.msg.Msg;
import org.simgrid.msg.Task;
import org.simgrid.msg.TaskCancelledException;
import org.simgrid.msg.TimeoutException;
import org.simgrid.msg.TransferFailureException;
import org.simgrid.msg.Process;

public class Client extends Process {
	public Client(Host host, String name, String[]args) {
		super(host,name,args);
	}
	public void main(String[] args) throws TransferFailureException, HostFailureException, TimeoutException {
		/*
		 * lecture_input
		 * envoyer_input
		 */
		
		/*if (args.length < 1) {
			Msg.info("Slave needs 1 argument (its number)");
			System.exit(1);
		}

		int num = Integer.valueOf(args[0]).intValue();
		Msg.info("Receiving on 'slave_"+num+"'");

		int f = 0;
		
		while(true) {  
			Task task = Task.receive("slave_"+num);	

			if (task instanceof FinalizeTask) {
				f++;
				if(f == 4)
					break;
			} else {
				Msg.info("Received \"" + task.getName() +  "\". Processing it.");
				try {
					task.execute();
				} catch (TaskCancelledException e) {
	
				}
				
				Msg.info("\"" + task.getName() + "\" done ");
			}
		}

		Msg.info("Received Finalize. I'm done. See you!");*/
	}
}