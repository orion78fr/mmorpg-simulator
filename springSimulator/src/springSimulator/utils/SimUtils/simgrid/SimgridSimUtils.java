package springSimulator.utils.SimUtils.simgrid;

import java.util.HashMap;
import java.util.List;

import org.simgrid.msg.Comm;
import org.simgrid.msg.HostFailureException;
import org.simgrid.msg.Msg;
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
	public void compute(double time) {
		if(time < 0) {
			// TODO traitement erreur
		}
		try {
			new Task("compute", getCurrentHost().getSpeed() * time, 0).execute();
		} catch (HostFailureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TaskCancelledException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void computeUntil(double time) {
		compute(time - getCurrentTime());
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
		try {
			Task t = Task.receive(getCurrentHost().getName());
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
		Comm c = Task.irecv(getCurrentHost().getName());
		SimMessage m = retreiveMessageFromTask(c.getTask());
		return new SimgridSimComm(c, m);
	}

	@Override
	public void send(SimHost host, SimMessage message) {
		Task t = new Task("send", 0, 0);
		storeMessageWithTask(t, message);
		try {
			t.send(host.getName());
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
	public void send(SimHost host, SimMessage message, double timeout) {
		Task t = new Task("send", 0, 0);
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
		}		
	}

	@Override
	public SimComm isend(SimHost host, SimMessage message) {
		Task t = new Task("send", 0, 0);
		storeMessageWithTask(t, message);
		return new SimgridSimComm(t.isend(host.getName()), message);
	}
	
	private int currentMessageId = 0;
	private HashMap<Integer, SimMessage> messageMap = new HashMap<Integer, SimMessage>();
	@SuppressWarnings("boxing")
	private void storeMessageWithTask(Task t, SimMessage message){
		// TODO Deal with boxing / unboxing ?
		while(messageMap.containsKey(currentMessageId)){
			currentMessageId++;
		}
		t.setComputeDuration(currentMessageId);
		messageMap.put(currentMessageId, message);
	}
	@SuppressWarnings("boxing")
	private SimMessage retreiveMessageFromTask(Task t){
		return messageMap.remove(t.getComputeDuration());
	}

	@Override
	public List<SimComm> ireceiveAllUntil(double time) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SimComm> ireceiveAllFor(double time) {
		// TODO Auto-generated method stub
		return null;
	}
}