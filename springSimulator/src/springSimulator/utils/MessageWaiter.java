package springSimulator.utils;

import org.simgrid.msg.Comm;
import org.simgrid.msg.HostFailureException;
import org.simgrid.msg.TimeoutException;
import org.simgrid.msg.TransferFailureException;

public class MessageWaiter{
	private MessageState state;
	private Message message = null;
	private Comm comm;
	private Exception e;
	
	protected MessageWaiter(Comm comm){
		this(comm, null);
	}
	
	protected MessageWaiter(Comm comm, Message m){
		this.state = MessageState.PENDING;
		this.comm = comm;
		this.message = m;
		this.e = null;
	}
	
	protected MessageWaiter(Comm comm, MessageType type, Object content){
		this(comm, new Message(type, content));
	}

	public MessageState getState() {
		if(this.state == MessageState.PENDING){
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
		}
		return this.state;
	}

	public Message getMessage() {
		if(this.message == null){
			if(this.state == MessageState.SUCCESS){
				this.message = SimUtilsOld.getMessageById(this.comm.getTask().getId());
			}
		}
		return message;
	}

	public Exception getException() {
		return e;
	}
	
	public void waitMessage(){
		try {
			this.comm.waitCompletion();
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
	}
	
	public void waitMessageTimeout(double timeout){
		try {
			this.comm.waitCompletion(timeout);
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
	}
}