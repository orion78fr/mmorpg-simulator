package springVisualizer.model.connection;

import springVisualizer.State;

public class SinusoidalConnectionManager implements ConnectionManager {
	private long min;
	private long max;
	private long period;
	
	public SinusoidalConnectionManager(long min, long max, long period) {
		super();
		this.min = min;
		this.max = max;
		this.period = period;
	}
	
	private int getNumberOfPlayer(long tickNumber){
		return (int) (min + ((max-min) / 2) * (Math.sin(((double)tickNumber / period) * 2 * Math.PI) + 1));
	}

	@Override
	public void manage(long tickNumber) {
		int newNumberOfPlayer = getNumberOfPlayer(tickNumber);
		int currentSize = State.playerList.size();
		
		if(newNumberOfPlayer > currentSize){
			State.addRandomPlayers(newNumberOfPlayer - currentSize);
		} else {
			State.removeRandomPlayers(currentSize - newNumberOfPlayer);
		}
	}
}
