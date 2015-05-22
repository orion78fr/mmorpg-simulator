package utils;

public class Message {
	private Object content;
	private long type;
	
	public Message(long type, Object content) {
		super();
		this.content = content;
		this.type = type;
	}

	public Object getContent() {
		return content;
	}

	public long getType() {
		return type;
	}
}
