package springSimulator;

import org.simgrid.msg.Host;
import org.simgrid.msg.Process;

import utils.Logger;
import utils.Message;
import utils.SimException;
import utils.SimUtils;

public class Client extends Process {
	public Client(Host host, String name, String[]args) {
		super(host,name,args);
	}
	@Override
	public void main(String[] args) {
		double tickrate = Double.valueOf(args[0]).doubleValue();
		for(int i = 0; i < 40; i++){
			SimUtils.isend("server_0", 10, 1, i);
			
			try {
				SimUtils.waitFor(1.0/tickrate);
			} catch (SimException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				Message m = SimUtils.receive(this.getHost().getName());
				Logger.logInfo("Message : (" + m.getType() + "," + m.getContent() + ")");// Traitement du message
			} catch (SimException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		/*
		 * lecture_input
		 * envoyer_input
		 */
		
		/*if (args.length < 1) {
			Logger.logInfo("Slave needs 1 argument (its number)");
			System.exit(1);
		}

		int num = Integer.valueOf(args[0]).intValue();
		Logger.logInfo("Receiving on 'slave_"+num+"'");

		int f = 0;
		
		while(true) {  
			Task task = Task.receive("slave_"+num);	

			if (task instanceof FinalizeTask) {
				f++;
				if(f == 4)
					break;
			} else {
				Logger.logInfo("Received \"" + task.getName() +  "\". Processing it.");
				try {
					task.execute();
				} catch (TaskCancelledException e) {
	
				}
				
				Logger.logInfo("\"" + task.getName() + "\" done ");
			}
		}

		Logger.logInfo("Received Finalize. I'm done. See you!");*/
	}
}