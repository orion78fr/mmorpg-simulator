package springVisualizer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class MovementLogger {
	private static Writer w;
	
	private static enum LogType{
		connect("c"),
		move("m"),
		disconnect("d");
		
		private String s;
		private LogType(String s) {
			this.s = s;
		}
		@Override
		public String toString() {
			return s;
		}
	}
	
	static{
		try{
			w = new BufferedWriter(new FileWriter(new File(Parameters.exportedMoveFolder)));
		} catch(IOException e){
			throw new RuntimeException("Can't open the exported move file");
		}
	}
	
	public static void logConnect(long id, double x, double y){
		log(LogType.connect, id, x, y);
	}
	public static void logDisonnect(long id){
		log(id, State.tickNumber + ";" + LogType.disconnect.toString());
	}
	public static void logMove(long id, double x, double y){
		log(LogType.move, id, x, y);
	}
	
	private static void log(LogType type, long id, double x, double y){
		StringBuffer sb = new StringBuffer();
		sb.append(State.tickNumber)
			.append(";")
			.append(type)
			.append(";")
			.append(x)
			.append(";")
			.append(y);
		log(id, sb.toString());
	}

	private static void log(long id, String logMessage){
		try {
			// TODO Write to the right file!
			w.write(id + ";");
			w.write(logMessage);
			w.write('\n');
		} catch (IOException e) {
			throw new RuntimeException("Can't log?!?");
		}
	}
}
