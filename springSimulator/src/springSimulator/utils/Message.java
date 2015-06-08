package springSimulator.utils;

public class Message {
	private Object content = null;
	private MessageType type;

	public Message(MessageType type, Object content) {
		super();
		this.content = content;
		this.type = type;
	}

	public Object getContent() {
		return content;
	}

	public MessageType getType() {
		return type;
	}
}
