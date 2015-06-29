package springVisualizer.view.overlay;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;

import javax.swing.JOptionPane;

import springCommon.Parameters;
import springCommon.QTree.QTree;
import springVisualizer.util2D.Polygon;
import springVisualizer.util2D.Polygon.ReentrantPolygonException;
import springVisualizer.view.MainWindow;
import springVisualizer.view.ViewCommon.Dimentions;

public class ZonesOverlay extends AbstractOverlay {
	private static final long serialVersionUID = 1L;
	
	private Polygon currentPoly = new Polygon();
	public static QTree tree;
	
	private Color green, dgreen, red, dred, poly, dpoly;
	
	private boolean firstPoint, traversable;
	
	public ZonesOverlay() {
		super();
		
		tree = new QTree(0, 0, Parameters.sizex, Parameters.sizey, true);
		
		firstPoint = true;
		
		green = new Color(0, 1, 0, 0.5f);
		dgreen = green.darker();
		red = new Color(1, 0, 0, 0.5f);
		dred = red.darker();
		poly = new Color(0, 0, 1, 0.5f);
		dpoly = poly.darker();
	}

	@Override
	public void draw(Graphics2D g2d) {
		drawQTree(g2d, tree);
		
		fillShape(g2d, currentPoly.toDrawCoords(), poly, dpoly);
	}
	
	void drawQTree(Graphics2D g2d, QTree tree){
		if(tree == null){
			throw new RuntimeException("Trying to draw null tree!");
		}
		
		// Iterate over childrens
		if(!tree.isLeaf()){
			for(QTree child : tree.getChildren()){
				drawQTree(g2d, child);
			}
		} else {
			int x = Dimentions.xToDrawCoords(tree.getX());
			int y = Dimentions.yToDrawCoords(tree.getY());
			int w = Dimentions.xToDrawCoords(tree.getX() + tree.getWidth()) - x;
			int h = Dimentions.yToDrawCoords(tree.getY() + tree.getHeight()) - y;
			if(tree.isTraversable()){
				g2d.setColor(green);
				g2d.fillRect(x, y, w, h);
				g2d.setColor(dgreen);
				g2d.drawRect(x, y, w, h);
			} else {
				g2d.setColor(red);
				g2d.fillRect(x, y, w, h);
				g2d.setColor(dred);
				g2d.drawRect(x, y, w, h);
			}
			
			
		}
	}
	
	public void fillShape(Graphics2D g2d, Shape s, Color inside, Color outline){
		g2d.setColor(inside);
		g2d.fill(s);
		
		g2d.setColor(outline);
		g2d.draw(s);
	}
	
	public void addPoint(double x, double y){
		if(firstPoint){
			traversable = !tree.getContainingNode(x, y).isTraversable();
			firstPoint = false;
		}
		currentPoly.addPoint((int)x, (int)y);
	}
	
	public void endPoly(){
		try {
			Polygon.verifyNonReentrant(currentPoly, true);
			
			tree.setShape(currentPoly, traversable);
			
			firstPoint = true;
			
			currentPoly = new Polygon();
		} catch (ReentrantPolygonException e){
			String[] options = {"Discard", "Continue"};
			int result = JOptionPane.showOptionDialog(MainWindow.win,
					"The polygon is reentrant.\n Do you want to discard the polygon or add points?",
					"Polygon problem", 0, JOptionPane.WARNING_MESSAGE, null, options, null);
			
			if(result == 0){
				currentPoly = new Polygon();
			}
		}
	}
	
	public void toggle(double x, double y){
		//tree.toggleTraversableZone(x, y);
		System.out.println(tree.isEverythingConnected());
	}

	@Override
	public String getDisplayName() {
		return "Moving Zones";
	}
}
