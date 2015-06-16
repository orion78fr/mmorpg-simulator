package springVisualizer.view.overlay;

import java.awt.Graphics2D;

import springCommon.Parameters;
import springVisualizer.State;
import springVisualizer.model.Player;
import springVisualizer.view.ViewCommon.Dimentions;

public class PlayerOverlay extends AbstractOverlay {
	private static final long serialVersionUID = 1L;

	@Override
	public void draw(Graphics2D g2d) {
		for(Player p : State.playerList){
			g2d.setColor(p.getColor());
			g2d.fillOval((int)(((p.getX()/Dimentions.ratiox)-Dimentions.posx)*Dimentions.zoom) - Parameters.playerOverlayPlayerRadius / 2,
					(int)(((p.getY()/Dimentions.ratioy)-Dimentions.posy)*Dimentions.zoom) - Parameters.playerOverlayPlayerRadius / 2, 
					Parameters.playerOverlayPlayerRadius, 
					Parameters.playerOverlayPlayerRadius);
		}
	}

	@Override
	public String getDisplayName() {
		return "Players";
	}
}
