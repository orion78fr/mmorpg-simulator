package springVisualizer.view.overlay;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import springVisualizer.util2D.Polygon;

public class ZonesOverlay extends AbstractOverlay {
	private static final long serialVersionUID = 1L;
	
	Polygon currentPoly = new Polygon();
	List<Polygon> polys = new ArrayList<Polygon>();
	
	public ZonesOverlay() {
		super();
		polys.add(currentPoly);
	}

	@Override
	public void draw(Graphics2D g2d) {
		for(Polygon p : polys){
			fillPoly(g2d, p);
		}
	}
	
	public void fillPoly(Graphics2D g2d, Polygon p){
		g2d.setColor(p.getColor());
		g2d.fill(p.drawCoords());
	}
	
	public void addPoint(double x, double y){
		currentPoly.addPoint((int)x, (int)y);
	}
	
	public void endPoly(){
		currentPoly = new Polygon();
		polys.add(currentPoly);
	}
}
