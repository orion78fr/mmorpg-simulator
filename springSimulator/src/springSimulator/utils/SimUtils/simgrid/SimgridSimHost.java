package springSimulator.utils.SimUtils.simgrid;

import org.simgrid.msg.Host;
import org.simgrid.msg.HostNotFoundException;

import springSimulator.utils.SimUtils.interfaces.SimHost;

public class SimgridSimHost implements SimHost {
	private SimgridSimHost(Host h){
		this.h = h;
	}
	
	private Host h;
	
	@Override
	public double getSpeed(){
		return this.h.getSpeed();
	}	
}
