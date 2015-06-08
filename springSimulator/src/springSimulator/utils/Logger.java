package springSimulator.utils;

import org.simgrid.msg.Msg;

public class Logger {
	private static LogLevel currentLogLevel = LogLevel.DEBUG;
	
	private enum LogLevel{
		DEBUG(0),
		VERBOSE(1),
		INFO(2),
		WARN(3),
		ERROR(4),
		CRITICAL(5);
		
		private final int level;
		private LogLevel(int level){
			this.level = level;
		}
		public boolean greaterThan(LogLevel l){
			return this.level > l.level;
		}
	}
	
	public static void logDebug(String s){
		if(LogLevel.DEBUG.greaterThan(currentLogLevel)){
			Msg.debug(s);
		}
	}
	
	public static void logVerbose(String s){
		if(LogLevel.VERBOSE.greaterThan(currentLogLevel)){
			Msg.verb(s);
		}
	}
	
	public static void logInfo(String s){
		if(LogLevel.INFO.greaterThan(currentLogLevel)){
			Msg.info(s);
		}
	}
	
	public static void logWarning(String s){
		if(LogLevel.WARN.greaterThan(currentLogLevel)){
			Msg.warn(s);
		}
	}
	
	public static void logError(String s){
		if(LogLevel.ERROR.greaterThan(currentLogLevel)){
			Msg.error(s);
		}
	}
	
	public static void logCritical(String s){
		if(LogLevel.CRITICAL.greaterThan(currentLogLevel)){
			Msg.critical(s);
		}
	}
}
