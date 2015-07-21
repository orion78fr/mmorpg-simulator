package springVisualizer.view.overlay;

import java.awt.Color;
import java.awt.Graphics2D;

public class ServerOverlay extends AbstractOverlay {
	private static final long serialVersionUID = 1L;

	@Override
	public void draw(Graphics2D g2d) {
		g2d.setColor(Color.BLACK);
		
		// TODO
	}

	@Override
	public String getDisplayName() {
		return "Server Zones";
	}

	@Override
	public OverlayMouseMode[] getMouseModes() {
		return null;
	}
}
