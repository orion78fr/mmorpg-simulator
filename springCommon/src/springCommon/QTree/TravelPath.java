package springCommon.QTree;

import java.util.ArrayList;
import java.util.List;

import springCommon.Point2d;

/**
 * Find a path that do not cross any forbidden zone. Not especially the best path?
 * @param fromx
 * @param fromy
 * @param tox
 * @param toy
 * @return A path between from and to
 */
/*public TravelPath findPath(double fromx, double fromy, double tox, double toy){
	// We start from (fromx, fromy) then we explore from this every possibilities.
	// We add them to a sorted list for the heuristic and take the first out.
	// Then we recurse on this.
	TravelPath path = new TravelPath((long)fromx + 0.5, (long)fromy + 0.5, (long)tox + 0.5, (long)toy + 0.5);
	
	List<Point2d> openSet = new ArrayList<Point2d>();
	openSet.add(path.getFrom());
	
	List<Point2d> closedSet = new ArrayList<Point2d>();
	
	HashMap<Point2d, PathValue> values = new HashMap<Point2d, PathValue>();
	values.put(path.getFrom(), new PathValue(null, 0, path.getFrom().distance(path.getTo())));
	
	Point2d currentp;
	
	Directions[] directionsValues = Directions.values();
	
	while((currentp = openSet.remove(0)) != null){
		if(currentp.equals(path.getTo())){
			backward_construct(path, values);
			return path;
		}
		
		closedSet.add(currentp);
		
		PathValue currentpValue = values.get(currentp);
		
		for(Directions d : directionsValues){
			Point2d neighbor = getDirectedPoint(currentp, d);
			
			if(!neighbor.isInside()){
				// Outside map
				continue;
			}
			
			if(closedSet.contains(neighbor)){
				// It has already been explored and expanded
				continue;
			}
			
			if(!getContainingNode(neighbor.getX(), neighbor.getY()).isTraversable()){
				// Not traversable
				continue;
			}
			
			double tentative_g_score = currentpValue.getG_score() + d.getDistance();
			
			// Get the current value or add it
			PathValue neighborValue = values.get(neighbor);
			if(neighborValue == null){
				neighborValue = new PathValue();
				values.put(neighbor, neighborValue);
			}
			
			if(!openSet.contains(neighbor) || tentative_g_score < neighborValue.getG_score()){
				neighborValue.setFrom(d);
				neighborValue.setG_score(tentative_g_score);
				neighborValue.setF_score(tentative_g_score + neighbor.distance(path.getTo()));
				
				openSet.remove(neighbor);
				
				add_f_order(neighbor, openSet, values);
			}
		}
		
		if(openSet.size() == 0){
			break;
		}
	}
	
	
	// No path found
	System.out.println("exploration ended, no path found");
	return null;
}

private static void add_f_order(Point2d neighbor, List<Point2d> openSet, Map<Point2d, PathValue> values){
	// Add it in distance order
	// TODO dichotomize
	int i;
	double neighbor_f_score = values.get(neighbor).getF_score();
	for(i = 0; i < openSet.size(); i++){
		if(neighbor_f_score < values.get(openSet.get(i)).getF_score()){
			break;
		}
	}
	openSet.add(i, neighbor);
}

private static void backward_construct(TravelPath path, HashMap<Point2d, PathValue> values){
	// So now we have found a path, iterate backwards and push it in the path
	Point2d currentp = path.getTo();
	Directions d;
	
	while((d = Directions.getOpposite(values.get(currentp).getFrom())) != null){
		currentp = getDirectedPoint(currentp, d);
		if(!currentp.equals(path.getFrom())){
			path.addPoint(currentp);
		}
	}
}*/







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