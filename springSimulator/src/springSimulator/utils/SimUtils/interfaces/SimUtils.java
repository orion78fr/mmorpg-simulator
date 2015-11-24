package springSimulator.utils.SimUtils.interfaces;

import java.util.List;

public interface SimUtils {
	public void init();
	
	public double getCurrentTime();
	
	public void compute(double time);
	public void computeUntil(double time);
	
	public SimHost getHostByName(String name);
	public SimHost getCurrentHost();
	
	public SimMessage receive();
	public SimMessage receive(double timeout);
	public SimComm ireceive();
	
	public List<SimComm> ireceiveAllUntil(double time);
	public List<SimComm> ireceiveAllFor(double time);
	
	public void send(SimHost host, SimMessage message);
	public void send(SimHost host, SimMessage message, double timeout);
	public SimComm isend(SimHost host, SimMessage message);
}
