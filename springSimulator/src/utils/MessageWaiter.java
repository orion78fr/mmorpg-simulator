package utils;

import org.simgrid.msg.Comm;
import org.simgrid.msg.HostFailureException;
import org.simgrid.msg.TimeoutException;
import org.simgrid.msg.TransferFailureException;

public class MessageWaiter{
	private MessageState state;
	private Message message;
	private Comm comm;
	private Exception e;
	
	protected MessageWaiter(Comm comm, Message m){
		this.state = MessageState.PENDING;
		this.comm = comm;
		this.message = m;
		this.e = null;
	}
	
	protected MessageWaiter(Comm comm, long type, Object content){
		this(comm, new Message(type, content));
	}

	public MessageState getState() {
		try{
			if(this.comm.test()){
				this.state = MessageState.SUCCESS;
			}
		} catch (TransferFailureException e){
			this.state = MessageState.TRANSFER_FAILURE;
			this.e = e;
		} catch (HostFailureException e) {
			this.state = MessageState.HOST_FAILURE;
			this.e = e;
		} catch (TimeoutException e) {
			this.e = e;
			this.state = MessageState.TIMEOUT;
		}
		return this.state;
	}

	public Message getMessage() {
		return message;
	}

	public Exception getE() {
		return e;
	}
	
	
}