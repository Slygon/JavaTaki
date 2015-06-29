package taki.common;

import java.io.Serializable;

public class SystemMessage implements Serializable {

	private static final long serialVersionUID = 1604564457452503089L;
	
	public enum MsgType {
		USERNAME_TAKEN
	}

	
	private MsgType type;
	private String message;
	
	public MsgType getMsgType() {
		return type;
	}

	// constructor
	public SystemMessage(MsgType type, String message) {
		this.type = type;
		this.message = message;
	}
	
	// getters
	public MsgType getType() {
		return type;
	}
	public String getMessage() {
		return message;
	}
	
}
