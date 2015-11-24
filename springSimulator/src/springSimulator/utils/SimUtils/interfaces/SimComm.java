package springSimulator.utils.SimUtils.interfaces;

import springSimulator.utils.SimUtils.SimMessageState;

public interface SimComm {
	public SimMessage getMessage();
	
	public SimMessageState getState();
	
	public void waitCompletion();
	public void waitCompletion(double timeout);
}
