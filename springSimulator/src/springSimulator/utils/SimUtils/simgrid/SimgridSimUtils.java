package springSimulator.utils.SimUtils.simgrid;

import org.simgrid.msg.Msg;

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
		// TODO Auto-generated method stub
		// Init all async mailboxes ?
	}

	@Override
	public void compute(double time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void computeUntil(double time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public SimHost getHostByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SimHost getCurrentHost() {
		// TODO Auto-generated method stub
		return null;
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
	public SimComm isend(SimHost host, SimMessage message) {
		// TODO Auto-generated method stub
		return null;
	}
}