package springVisualizer.view.overlay;

import java.awt.Graphics2D;

import springVisualizer.State;
import springVisualizer.model.Player;
import springVisualizer.view.MainWindow.Dimentions;

public class PlayerOverlay extends AbstractOverlay {
	private static final long serialVersionUID = 1L;

	@Override
	public void draw(Graphics2D g2d) {
		for(Player p : State.playerList){
			g2d.setColor(p.getColor());
			g2d.fillOval((int)(((p.getX()/Dimentions.ratiox)-Dimentions.posx)*Dimentions.zoom) - 5,
					(int)(((p.getY()/Dimentions.ratioy)-Dimentions.posy)*Dimentions.zoom)- 5, 10, 10);
		}
	}
}
