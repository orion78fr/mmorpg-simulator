package springVisualizer;

import java.awt.Color;

public class SpringVisualizer {
	
	public static void addPlayer(int x, int y){
		State.playerList.add(new Player(x, y, new Color(State.r.nextInt(255), State.r.nextInt(255), State.r.nextInt(255))));
	}
	
	public static void addRandomPlayers(int numPlayer){
		for(int i = 0; i < numPlayer; i++){
			addPlayer(State.r.nextInt(Parameters.size), State.r.nextInt(Parameters.size));
		}
	}
	
	public static void addPowerLawPlayers(int numPlayer){
		HaltonSequence h = new HaltonSequence(2);
		h.skipTo(State.r.nextInt(1000));
		for(int i = 0; i < numPlayer; i++){
			double[] v = h.nextVector();
			addPlayer((int) (v[0] * Parameters.size), (int)( v[1] * Parameters.size));
		}
	}
	
	public SpringVisualizer() {
	}
	
	public static void main(String args[]){
		int numPlayer = 5000;
		int maxIter = 100;
		
		//State.hotspots.add(new Hotspot(Parameters.size/2, Parameters.size/2, 50, Color.RED));
		
		/*for(int i = 0; i < 500; i++){
			State.hotspots.add(new Hotspot(State.r.nextInt(Parameters.size), State.r.nextInt(Parameters.size), State.r.nextInt(50) + 20));
		}*/
		
		addPowerLawPlayers(numPlayer);
		
		/*addPlayer(10,10);
		addPlayer(4990,4990);
		addPlayer(10,4990);
		addPlayer(4990,10);*/
		
		MainWindow.start();
		
		/*for(int i = 0; i < maxIter; i++){
			move();
			printPlayer();
		}*/
	}

}
