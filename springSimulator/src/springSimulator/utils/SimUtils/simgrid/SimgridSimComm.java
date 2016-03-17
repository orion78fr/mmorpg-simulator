package springSimulator.utils.SimUtils.simgrid;

import org.simgrid.msg.Comm;
import org.simgrid.msg.HostFailureException;
import org.simgrid.msg.TimeoutException;
import org.simgrid.msg.TransferFailureException;

import springSimulator.utils.SimUtils.SimMessageState;
import springSimulator.utils.SimUtils.interfaces.SimComm;
import springSimulator.utils.SimUtils.interfaces.SimMessage;

public class SimgridSimComm implements SimComm {
	private Comm c;
	private SimMessage m;
	private SimMessageState s;
	//private SimException e;
	
	protected SimgridSimComm(Comm c, SimMessage m) {
		this.c = c;
		this.m = m;
		this.s = SimMessageState.PENDING;
		//this.e = null;
	}
	
	@Override
	public SimMessage getMessage() {
		if(this.m == null && this.s == SimMessageState.SUCCESS){
			this.m = SimgridSimUtils.retreiveMessageFromTask(c.getTask());
		}
		return this.m;
	}

	@Override
	public SimMessageState getState() {
		if(this.s == SimMessageState.PENDING){
			try{
				if(this.c.test()){
					this.s = SimMessageState.SUCCESS;
				}
			} catch (TransferFailureException e){
				this.s = SimMessageState.TRANSFER_FAILURE;
				// TODO Proper exceptions
				//this.e = e;
			} catch (HostFailureException e) {
				this.s = SimMessageState.HOST_FAILURE;
				//this.e = e;
			} catch (TimeoutException e) {
				//this.e = e;
				this.s = SimMessageState.TIMEOUT;
			}
		}
		return this.s;
	}

	@Override
	public void waitCompletion() {
		try {
			this.c.waitCompletion();
		} catch (TransferFailureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (HostFailureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void waitCompletion(double timeout) {
		try {
			this.c.waitCompletion(timeout);
		} catch (TransferFailureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (HostFailureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
