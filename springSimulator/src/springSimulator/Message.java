package springSimulator;

import springSimulator.utils.SimUtils.interfaces.SimMessage;

public class Message implements SimMessage {
	private Object content;
	private MessageType type;
	private double size;

	public Message(MessageType type, Object content, double size) {
		super();
		this.content = content;
		this.type = type;
		this.size = size;
	}

	@Override
	public Object getContent() {
		return content;
	}

	public MessageType getType() {
		return type;
	}

	@Override
	public double getSize() {
		return this.size;
	}
}
