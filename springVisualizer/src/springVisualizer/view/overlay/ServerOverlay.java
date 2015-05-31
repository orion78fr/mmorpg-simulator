package springVisualizer.view.overlay;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.Area;

public class ServerOverlay extends AbstractOverlay {
	private static final long serialVersionUID = 1L;

	@Override
	public void draw(Graphics2D g2d) {
		g2d.setColor(Color.BLACK);
		
		int[] polyx = {20, 65, 10}, polyy = {100, 45, 12};
		Area a = new Area(new Polygon(polyx, polyy, 3));

		g2d.fill(a);
	}
}
