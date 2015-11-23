package springSimulator.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.simgrid.msg.Comm;
import org.simgrid.msg.Host;
import org.simgrid.msg.HostFailureException;
import org.simgrid.msg.Msg;
import org.simgrid.msg.Task;
import org.simgrid.msg.TaskCancelledException;
import org.simgrid.msg.TimeoutException;
import org.simgrid.msg.TransferFailureException;

import springSimulator.utils.SimUtils.SimException;

/**
 * Utility class for the simulator.<br />
 * It's main purpose is to hide simgrid behind (wrapper) in case of a simulator change is needed.
 * 
 * @author Guillaume Turchini
 */
public class SimUtilsOld {
	private static HashMap<Long, Message> messages = new HashMap<Long, Message>();
	 
	public static double getTime(){
		return Msg.getClock();
	}
	
	public static void compute(double flops) throws SimException{
		try {
			new Task("Compute", flops, 0).execute();
		} catch(HostFailureException e) {
			throw new SimException("The host has failed", e);
		} catch(TaskCancelledException e){
			throw new SimException("The task has been cancelled", e);
		}
	}
	
	/**
	 * Wait until a certain simulation time. If already passed, doesn't wait
	 * @param simTime The simulated time aimed
	 * @param hostPower The power of the host
	 * @throws SimException
	 */
	public static void waitUntil(double simTime) throws SimException{
		SimUtilsOld.waitFor(simTime - Msg.getClock());
	}
	
	/**
	 * Wait for a certain time. If negative, doesn't wait.
	 * @param simTime The time to wait (in seconds)
	 * @param hostPower The power of the host
	 * @throws SimException
	 */
	public static void waitFor(double simTime) throws SimException{
		if(simTime > 0){
			Task t = new Task("Waiter", simTime * Host.currentHost().getSpeed(), 0);
			
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
	 * @param content The data useful in the other
	 * @throws SimException
	 */
	public static void send(String mailbox, double size, MessageType type, Object content) throws SimException{
		Task t = new Task(mailbox, 0, size);
		
		messages.put(t.getId(), new Message(type, content));
		
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
	
	public static void sendTimeout(String mailbox, double size, MessageType type, Object content, double time) throws SimException{
		if(time < 0){
			return;
		}
		Task t = new Task(mailbox, 0, size);
		
		messages.put(t.getId(), new Message(type, content));
		try {
			t.send(mailbox, time);
		} catch (TransferFailureException e) {
			throw new SimException("An error has occured during the task transfer", e);
		} catch (HostFailureException e) {
			throw new SimException("The host has failed", e);
		} catch (TimeoutException e) {
			throw new SimException("The transfer timed out", e);
		}
	}
	
	public static void sendUntil(String mailbox, double size, MessageType type, Object content, double time) throws SimException{
		sendTimeout(mailbox, size, type, content, time - Msg.getClock());
	}
	
	/**
	 * Sends an async message to a mailbox, with data attached
	 * @param mailbox The mailbox to send to
	 * @param size The size of the transfer (to compute speed)
	 * @param content The data useful in the other 
	 * @return The watcher of the send
	 */
	public static MessageWaiter isend(String name, double size, MessageType type, Object content){
		Task t = new Task(name, 0, size);
		
		messages.put(t.getId(), new Message(type, content));
		
		Comm c = t.isend(name);
		
		return new MessageWaiter(c, type, content);
	}
	
	
	public static Message receive(String name) throws SimException{
		Message m = null;
		try {
			Task t = Task.receive(name);
			m = getMessageById(t.getId());
		} catch (TransferFailureException e) {
			throw new SimException("An error has occured during the task transfer", e);
		} catch (HostFailureException e) {
			throw new SimException("The host has failed", e);
		} catch (TimeoutException e) {
			throw new SimException("The transfer timed out", e);
		}
		return m;
	}
	
	public static Message receiveTimeout(String name, double time) throws SimException{
		if(time < 0){
			return null;
		}
		Message m = null;
		try {
			Task t = Task.receive(name, time);
			m = getMessageById(t.getId());
		} catch (TransferFailureException e) {
			throw new SimException("An error has occured during the task transfer", e);
		} catch (HostFailureException e) {
			throw new SimException("The host has failed", e);
		} catch (TimeoutException e) {
			throw new SimException("The transfer timed out", e);
		}
		return m;
	}
	
	public static Message receiveUntil(String name, double time) throws SimException{
		return receiveTimeout(name, time - Msg.getClock());
	}
	
	public static MessageWaiter ireceive(String name){
		if(!Task.listen(name)){
			return null;
		}
		Comm c = Task.irecv(name);
		
		return new MessageWaiter(c);
	}
	
	public static List<MessageWaiter> ireceiveAllUntil(String name, double time, long pollFreq){
		List<MessageWaiter> l = new ArrayList<MessageWaiter>();
		try {
			while(SimUtilsOld.getTime() < time){
				MessageWaiter w;
				while((w = SimUtilsOld.ireceive(name)) != null){
					l.add(w);
				}
				SimUtilsOld.waitFor(1.0 / pollFreq);
			}
		} catch (SimException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return l;
	}

	protected static Message getMessageById(Long id) {
		System.out.println(messages.size());
		 return messages.remove(id);
	}
}