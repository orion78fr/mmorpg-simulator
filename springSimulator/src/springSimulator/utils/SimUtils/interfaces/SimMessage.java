package springSimulator.utils.SimUtils.interfaces;

public interface SimMessage {
	/**
	 * Get the content of the message.
	 * @return The content of the message.
	 */
	public Object getContent();
	/**
	 * Get the size of the message.
	 * @return The size of the message (in bytes)
	 */
	public double getSize();
}
