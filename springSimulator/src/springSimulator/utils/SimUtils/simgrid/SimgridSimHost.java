package springSimulator.utils.SimUtils.simgrid;

import org.simgrid.msg.Host;
import org.simgrid.msg.HostNotFoundException;

import springSimulator.utils.SimUtils.interfaces.SimHost;

public class SimgridSimHost implements SimHost {
	public static SimgridSimHost getCurrentHost(){
		return new SimgridSimHost(Host.currentHost());
	}
	
	public static SimgridSimHost getHostByName(String name){
		try {
			return new SimgridSimHost(Host.getByName(name));
		} catch (HostNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private Host h;
	
	private SimgridSimHost(Host h){
		this.h = h;
	}
	
	@Override
	public double getSpeed(){
		return this.h.getSpeed();
	}	
}
