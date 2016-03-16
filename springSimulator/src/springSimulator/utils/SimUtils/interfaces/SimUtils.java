package springSimulator.utils.SimUtils.interfaces;

import java.util.List;

public interface SimUtils {
	/**
	 * Init all the simulation stuff
	 */
	public void init();

	/**
	 * Gets current simulation time
	 * @return The current simulation time
	 */
	public double getCurrentTime();
	
	/**
	 * Compute a certain amount of flops on current host
	 * @param flops The amount to compute
	 */
	public void compute(double flops);
	/**
	 * Make the host sleep for a certain time
	 * @param time The amount of time to sleep
	 */
	public void waitFor(double time);
	/**
	 * Make the host sleep until a certain time
	 * @param time The time to sleep to
	 */
	public void waitUntil(double time);
	
	/**
	 * Get a host by it's name
	 * @param name The name of the host searched
	 * @return The host of the given name, null if not found
	 */
	public SimHost getHostByName(String name);
	/**
	 * Get the host calling this function
	 * @return The current host
	 */
	public SimHost getCurrentHost();
	
	/**
	 * Receives a message synchronously
	 * @return The message received
	 */
	public SimMessage receive();
	/**
	 * Receives a message synchronously until a certain timeout
	 * @param timeout The timeout
	 * @return The message received
	 */
	public SimMessage receive(double timeout);
	/**
	 * Received a message synchronously until a certain simulation time
	 * @param time The simulation time
	 * @return The message received
	 */
	public SimMessage receiveUntil(double time);
	/**
	 * Receive a message asynchronously
	 * @return The communication corresponding to this message
	 */
	public SimComm ireceive();
	
	/**
	 * Receives all messages until a certain simulation time
	 * @param time The simulation time
	 * @return A list of all pending messages received during the wait
	 */
	public List<SimComm> ireceiveAllUntil(double time);
	/**
	 * Receives all messages for a certain time
	 * @param time The amount of time to receive
	 * @return A list of all pending messages received during the wait
	 */
	public List<SimComm> ireceiveAllFor(double time);
	
	/**
	 * Send synchronously a message to a host
	 * @param host The host to send the message to
	 * @param message The message to send
	 */
	public void send(SimHost host, SimMessage message);
	/**
	 * Send synchronously a message to a host with a certain timeout
	 * @param host The host to send the message to
	 * @param message The message to send
	 * @param timeout The timeout
	 */
	public void send(SimHost host, SimMessage message, double timeout);
	/**
	 * Send synchronously a message to a host until a certain simulation timeout
	 * @param host The host to send the message to
	 * @param message The message to send
	 * @param time The simulation time
	 */
	public void sendUntil(SimHost host, SimMessage message, double time);
	/**
	 * Send asynchronously a message to a host
	 * @param host The host to send the message to
	 * @param message The message to send
	 * @return The comm object to watch the completion
	 */
	public SimComm isend(SimHost host, SimMessage message);
}
