package springVisualizer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;

import springCommon.LogType;
import springCommon.Parameters;
import springVisualizer.model.Player;

public class MovementLogger {
	private static HashMap<Long, Writer> writers = new HashMap<Long, Writer>();
	
	static{
		File folder = new File(Parameters.exportedMoveFolder);
		if((folder.exists() && !folder.isDirectory()) && !folder.mkdir()){
			throw new RuntimeException("Can't create the export move folder");
		} else {
			File[] childs = folder.listFiles();
			for(File f : childs){
				f.delete();
			}
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
			if(!writers.containsKey(id)){
				writers.put(id, new BufferedWriter(new FileWriter(Parameters.exportedMoveFolder + File.separator + "client_" + id)));
			}
			Writer w = writers.get(id);
			w.write(logMessage);
			w.write('\n');
		} catch (IOException e) {
			throw new RuntimeException("Can't log?!?");
		}
	}
	
	public static void clean(){
		/* Disconnect all remaining player */
		for(Player p : State.playerList){
			p.disconnect();
		}
		
		/* Close all writers */
		for(Writer w : writers.values()){
			try {
				w.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
