package springSimulator.utils.SimUtils.simgrid;

import org.simgrid.msg.Host;
import org.simgrid.msg.HostFailureException;
import org.simgrid.msg.Msg;
import org.simgrid.msg.Task;
import org.simgrid.msg.TaskCancelledException;

import springSimulator.utils.SimUtils.Sim;
import springSimulator.utils.SimUtils.interfaces.SimComm;
import springSimulator.utils.SimUtils.interfaces.SimHost;
import springSimulator.utils.SimUtils.interfaces.SimMessage;
import springSimulator.utils.SimUtils.interfaces.SimUtils;

public class SimgridSimUtils implements SimUtils {
	private SimgridSimUtils() {
	}
	
	static{
		Sim.setSu(new SimgridSimUtils());
	}
	
	@Override
	public double getCurrentTime(){
		return Msg.getClock();
	}

	@Override
	public void init() {
		SimgridSimHost.init();
		// TODO Init all async mailboxes ?
	}

	@Override
	public void compute(double time) {
		if(time < 0) {
			// TODO traitement erreur
		}
		try {
			new Task("compute", getCurrentHost().getSpeed() * time, 0).execute();
		} catch (HostFailureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TaskCancelledException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void computeUntil(double time) {
		compute(time - getCurrentTime());
	}

	@Override
	public SimHost getHostByName(String name) {
		return SimgridSimHost.getHostByName(name);
	}

	@Override
	public SimHost getCurrentHost() {
		return SimgridSimHost.getCurrentHost();
	}

	@Override
	public SimMessage receive() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SimMessage receive(double timeout) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SimComm ireceive() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void send(SimHost host, SimMessage message) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void send(SimHost host, SimMessage message, double timeout) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public SimComm isend(SimHost host, SimMessage message) {
		// TODO Auto-generated method stub
		return null;
	}
}