package springCommon;

public enum LogType{
	connect("c"),
	move("m"),
	disconnect("d");
	
	private final String s;
	
	private LogType(String s) {
		this.s = s;
	}
	@Override
	public String toString() {
		return s;
	}
	
	public static LogType fromString(String s){
		for(LogType lt : LogType.values()){
			if(lt.toString().equals(s)){
				return lt;
			}
		}
		return null;
	}
}