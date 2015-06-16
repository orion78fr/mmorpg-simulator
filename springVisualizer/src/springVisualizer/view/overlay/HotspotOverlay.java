package springVisualizer.view.overlay;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;

import springVisualizer.State;
import springVisualizer.model.Hotspot;
import springVisualizer.view.ViewCommon.Dimentions;

public class HotspotOverlay extends AbstractOverlay {
	private static final long serialVersionUID = 1L;

	@Override
	public void draw(Graphics2D g2d) {
		for (Hotspot h : State.hotspots) {
			int radius = (int)(h.getHotness()*2);
			
			Color c = h.getColor();
			Color cTrans = new Color(c.getRed(), c.getGreen(), c.getBlue(), 0);
			
			RadialGradientPaint rgp = new RadialGradientPaint(
						(int)(((h.getX()/Dimentions.ratiox)-Dimentions.posx)*Dimentions.zoom),
						(int)(((h.getY()/Dimentions.ratioy)-Dimentions.posy)*Dimentions.zoom),
						radius/2,
						new float[] { 0f, 1f },
						new Color[] { c, cTrans });
			
			g2d.setPaint(rgp);
			
			g2d.fillOval((int)(((h.getX()/Dimentions.ratiox)-Dimentions.posx)*Dimentions.zoom) - radius/2,
						(int)(((h.getY()/Dimentions.ratioy)-Dimentions.posy)*Dimentions.zoom) - radius/2,
						radius,
						radius);
		}
	}

	@Override
	public String getDisplayName() {
		return "Hotspots";
	}
}
