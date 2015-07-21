package springVisualizer.view.overlay;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

import springCommon.Parameters;
import springVisualizer.State;
import springVisualizer.model.Player;
import springVisualizer.view.ViewCommon;
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

	OverlayMouseMode playerAdderMouseMode = new OverlayMouseMode() {
		
		@Override
		public void mouseReleased(MouseEvent e) {}
		
		@Override
		public void mousePressed(MouseEvent e) {}
		
		@Override
		public void mouseExited(MouseEvent e) {}
		
		@Override
		public void mouseEntered(MouseEvent e) {}
		
		@Override
		public void mouseClicked(MouseEvent e) {
			double x = Dimentions.xDrawToInternal(e.getX());
			double y = Dimentions.yDrawToInternal(e.getY());
			
			if(0 <= x && x <= Parameters.sizex && 0 <= y && y <= Parameters.sizey){
				if(e.getButton() == MouseEvent.BUTTON1){
					State.addPlayer(x, y);
					ViewCommon.needsRefresh = true;
				}
			}
		}
		
		@Override
		public String getName() {
			return "Player";
		}

		@Override
		public String getDescription() {
			return "Left click to add a player";
		}
	};
	
	@Override
	public OverlayMouseMode[] getMouseModes() {
		return new OverlayMouseMode[] {playerAdderMouseMode};
	}
}
