package springCommon.QTree;

import java.util.ArrayList;
import java.util.List;

import springCommon.Point2d;

// TODO
public class TravelPath{
	private Point2d from, to;
	private List<Point2d> path = new ArrayList<Point2d>();
	
	public TravelPath(double fromx, double fromy, double tox, double toy) {
		this.from = new Point2d(fromx, fromy);
		this.to = new Point2d(tox, toy);
	}
	
	public Point2d getFrom() {
		return from;
	}

	public Point2d getTo() {
		return to;
	}

	public List<Point2d> getPath() {
		return path;
	}

	public void addPointReverse(double x, double y){
		addPoint(new Point2d(x, y));
	}
	
	public void addPoint(Point2d p){
		// Reverse adding
		path.add(0, p);
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		sb.append(from.toString());
		
		for(Point2d p : path){
			sb.append(" -> ").append(p.toString());
		}
		
		sb.append(" -> ").append(to.toString());
		
		return sb.toString();
	}
}