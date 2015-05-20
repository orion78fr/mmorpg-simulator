package utils;

import org.simgrid.msg.Comm;
import org.simgrid.msg.HostFailureException;
import org.simgrid.msg.Msg;
import org.simgrid.msg.Task;
import org.simgrid.msg.TaskCancelledException;
import org.simgrid.msg.TimeoutException;
import org.simgrid.msg.TransferFailureException;

import utils.MessageSender.State;

/**
 * Utility class for the simulator.<br />
 * It's main purpose is to hide simgrid behind (wrapper) in case of a simulator change is needed.
 * 
 * @author Guillaume Turchini
 */
public class SimUtils {
	/**
	 * Wait until a certain simulation time. If already passed, doesn't wait
	 * @param simTime The simulated time aimed
	 * @param hostPower The power of the host
	 * @throws SimException
	 */
	public static void waitUntil(double simTime, double hostPower) throws SimException{
		SimUtils.wait(simTime - Msg.getClock(), hostPower);
	}
	
	/**
	 * Wait for a certain time. If negative, doesn't wait.
	 * @param simTime The time to wait (in seconds)
	 * @param hostPower The power of the host
	 * @throws SimException
	 */
	public static void wait(double simTime, double hostPower) throws SimException{
		if(simTime > 0){
			Task t = new Task("Tickwait", simTime * hostPower, 0);
			
			try{
				t.execute();
			} catch(HostFailureException e) {
				throw new SimException("The host has failed", e);
			} catch(TaskCancelledException e){
				throw new SimException("The task has been cancelled", e);
			}
		}
	}

	/**
	 * Send a message to a mailbox, with data attached
	 * @param mailbox The mailbox to send to
	 * @param size The size of the transfer (to compute speed)
	 * @param extdata The data useful in the other
	 * @throws SimException
	 */
	public static void send(String mailbox, double size, Object extdata) throws SimException{
		Task t = new Task(mailbox, 0, size);
		
		// TODO
		
		try {
			t.send(mailbox);
		} catch (TransferFailureException e) {
			throw new SimException("An error has occured during the task transfer", e);
		} catch (HostFailureException e) {
			throw new SimException("The host has failed", e);
		} catch (TimeoutException e) {
			throw new SimException("The transfer timed out", e);
		}
	}
	
	/**
	 * Sends an async message to a mailbox, with data attached
	 * @param mailbox The mailbox to send to
	 * @param size The size of the transfer (to compute speed)
	 * @param extdata The data useful in the other 
	 * @return The watcher of the send
	 */
	public static MessageSender isend(String name, double size, Object extdata){
		Task t = new Task(name, 0, size);
		
		// TODO
		
		Comm c = t.isend(name);
		
		return new MessageSender(c);
	}
	
	public static MessageSender isendUntil(String name, double size, Object extdata, double time){
		// TODO
		return null;
	}
	
	public static MessageSender isendTimeout(String name, double size, Object extdata, double time){
		// TODO
		return null;
	}
	
	
	public static Object receive(String name){
		// TODO
		return null;
	}
	
	public static Object receiveTimeout(String name, double time){
		// TODO
		return null;
	}
	
	public static Object receiveUntil(String name, double time){
		// TODO
		return null;
	}
	
	public static MessageReceiver ireceive(String name){
		// TODO
		return null;
	}
	
	public static MessageReceiver ireceiveUntil(String name, double time){
		// TODO
		return null;
	}
	
	public static MessageReceiver ireceiveTimeout(String name, double time){
		// TODO
		return null;
	}
	
	public static class MessageReceiver{
		public Message message;
		public State status;
		// TODO
	}
	
	public static class Message{
		// TODO
	}
}