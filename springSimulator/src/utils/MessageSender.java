package utils;

import org.simgrid.msg.Comm;
import org.simgrid.msg.HostFailureException;
import org.simgrid.msg.TimeoutException;
import org.simgrid.msg.TransferFailureException;

/**
 * When you send an async message, to know if it have been received.
 * 
 * @author Guillaume Turchini
 */
public class MessageSender {
	public static enum State{
		PENDING,
		SUCCESS,
		TRANSFER_FAILURE,
		HOST_FAILURE,
		TIMEOUT;
	};
	
	private State state;
	private Comm comm;
	private Exception e;
	
	protected MessageSender(Comm comm){
		this.state = State.PENDING;
		this.comm = comm;
		this.e = null;
	}
	
	public State getState(){
		// If this is a isend
		if(this.comm != null){
			try{
				if(this.comm.test()){
					this.state = State.SUCCESS;
				}
			} catch (TransferFailureException e){
				this.state = State.TRANSFER_FAILURE;
				this.e = e;
			} catch (HostFailureException e) {
				this.state = State.HOST_FAILURE;
				this.e = e;
			} catch (TimeoutException e) {
				this.e = e;
				this.state = State.TIMEOUT;
			}
		}
		return this.state;
	}
	
	public Exception getException(){
		return e;
	}
}
