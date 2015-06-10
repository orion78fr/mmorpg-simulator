package springVisualizer.view.overlay;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import springCommon.Parameters;
import springVisualizer.util2D.Polygon;

public class ZonesOverlay extends AbstractOverlay {
	private static final long serialVersionUID = 1L;
	
	Polygon currentPoly = new Polygon();
	List<Polygon> polys = new ArrayList<Polygon>();
	
	public ZonesOverlay() {
		super();
		
		Polygon total = new Polygon();
		total.addPoint(0, 0);
		total.addPoint(0, (int)Parameters.sizey);
		total.addPoint((int)Parameters.sizex, (int)Parameters.sizey);
		total.addPoint((int)Parameters.sizex, 0);
		total.setColor(new Color(0, 255, 0, 127));
		
		polys.add(total);
	}

	@Override
	public void draw(Graphics2D g2d) {
		for(Polygon p : polys){
			fillPoly(g2d, p);
		}
		fillPoly(g2d, currentPoly);
	}
	
	public void fillPoly(Graphics2D g2d, Polygon p){
		g2d.setColor(p.getColor());
		g2d.fill(p.toDrawCoords());
		g2d.setColor(p.getColor().darker());
		g2d.draw(p.toDrawCoords());
	}
	
	public void addPoint(double x, double y){
		currentPoly.addPoint((int)x, (int)y);
	}
	
	public void endPoly(){
		polys.add(currentPoly);
		
		// TODO minus for all
		
		currentPoly = new Polygon();
	}

	@Override
	public String getName() {
		return "Moving Zones";
	}
}
