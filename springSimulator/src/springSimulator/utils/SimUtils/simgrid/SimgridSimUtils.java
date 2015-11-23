package springSimulator.utils.SimUtils.simgrid;

import org.simgrid.msg.Msg;

import springSimulator.utils.SimUtils.interfaces.SimUtils;

public class SimgridSimUtils implements SimUtils {
	@Override
	public double getCurrentTime(){
		return Msg.getClock();
	}
}