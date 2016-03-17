package springSimulator.utils.SimUtils.simgrid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.simgrid.msg.Comm;
import org.simgrid.msg.HostFailureException;
import org.simgrid.msg.Msg;
import org.simgrid.msg.NativeException;
import org.simgrid.msg.Task;
import org.simgrid.msg.TaskCancelledException;
import org.simgrid.msg.TimeoutException;
import org.simgrid.msg.TransferFailureException;

import springSimulator.utils.SimUtils.Sim;
import springSimulator.utils.SimUtils.interfaces.SimComm;
import springSimulator.utils.SimUtils.interfaces.SimHost;
import springSimulator.utils.SimUtils.interfaces.SimMessage;
import springSimulator.utils.SimUtils.interfaces.SimUtils;

public class SimgridSimUtils implements SimUtils {
	private SimgridSimUtils() {
	}
	
	static{
		System.out.println("Simgrid simulation engine loaded");
		Sim.setSu(new SimgridSimUtils());
	}
	
	@Override
	public double getCurrentTime(){
		return Msg.getClock();
	}

	@Override
	public void init() {
		SimgridSimHost.init();
		// TODO Init all async mailboxes ?
	}

	@Override
	public void waitFor(double time) {
		if(time > 0) {
			compute(getCurrentHost().getSpeed() * time);
		}
	}

	@Override
	public void waitUntil(double time) {
		waitFor(time - getCurrentTime());
	}

	@Override
	public SimHost getHostByName(String name) {
		return SimgridSimHost.getHostByName(name);
	}

	@Override
	public SimHost getCurrentHost() {
		return SimgridSimHost.getCurrentHost();
	}

	@Override
	public SimMessage receive() {
		return receive(-1);
	}

	@Override
	public SimMessage receive(double timeout) {
		Task t;
		try {
			t = Task.receive(getCurrentHost().getName(), timeout);
			return retreiveMessageFromTask(t);
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
		return null;
	}

	@Override
	public SimComm ireceive() {
		if(Task.listen(getCurrentHost().getName())){
			return null;
		}
		Comm c = Task.irecv(getCurrentHost().getName());
		return new SimgridSimComm(c, null);
	}

	@Override
	public void send(SimHost host, SimMessage message) {
		send(host, message, -1);
	}
	
	@Override
	public void send(SimHost host, SimMessage message, double timeout) {
		Task t = new Task("send", 0, message.getSize());
		storeMessageWithTask(t, message);
		try {
			t.send(host.getName(), timeout);
		} catch (TransferFailureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (HostFailureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NativeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	@Override
	public SimComm isend(SimHost host, SimMessage message) {
		Task t = new Task("send", 0, 0);
		storeMessageWithTask(t, message);
		return new SimgridSimComm(t.isend(host.getName()), message);
	}
	
	private static int currentMessageId = 0;
	private static HashMap<Integer, SimMessage> messageMap = new HashMap<Integer, SimMessage>();
	@SuppressWarnings("boxing")
	private static void storeMessageWithTask(Task t, SimMessage message){
		// TODO Deal with boxing / unboxing ?
		while(messageMap.containsKey(currentMessageId)){
			currentMessageId++;
		}
		t.setFlopsAmount(currentMessageId);
		messageMap.put(currentMessageId, message);
	}
	@SuppressWarnings("boxing")
	protected static SimMessage retreiveMessageFromTask(Task t){
		return messageMap.remove(t.getFlopsAmount());
	}

	@Override
	public List<SimComm> ireceiveAllUntil(double time) {
		return ireceiveAllFor(time - getCurrentTime());
	}

	@Override
	public List<SimComm> ireceiveAllFor(double time) {
		waitFor(time);
		SimComm w;
		List<SimComm> l = new ArrayList<SimComm>();
		while((w = ireceive()) != null){
			l.add(w);
		}
		return l;
	}

	@Override
	public void compute(double flops) {
		try {
			new Task("compute", flops, 0).execute();
		} catch (HostFailureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TaskCancelledException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public SimMessage receiveUntil(double time) {
		return receive(time - getCurrentTime());
	}

	@Override
	public void sendUntil(SimHost host, SimMessage message, double time) {
		send(host, message, time - getCurrentTime());
	}
}