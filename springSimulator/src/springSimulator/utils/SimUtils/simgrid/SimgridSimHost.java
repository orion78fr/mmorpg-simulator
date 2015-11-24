package springSimulator.utils.SimUtils.simgrid;

import java.util.HashMap;

import org.simgrid.msg.Host;

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
	@Override
	public String getName() {
		return this.h.getName();
	}
	
	private static HashMap<String, SimgridSimHost> hosts;
	protected static void init() {
		hosts = new HashMap<String, SimgridSimHost>();
		Host[] hs = Host.all();
		for(Host h : hs){
			hosts.put(h.getName(), new SimgridSimHost(h));
		}
	}
	
	protected static SimHost getCurrentHost() {
		return getHostByName(Host.currentHost().getName());
	}
	
	protected static SimHost getHostByName(String name) {
		return hosts.get(name);
	}
}
