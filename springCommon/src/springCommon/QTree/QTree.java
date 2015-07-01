package springCommon.QTree;

import java.awt.Shape;
import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.DelayQueue;

import springCommon.Parameters;
import springCommon.Point2d;

public class QTree implements Serializable {
	private static final long serialVersionUID = 1L;
	private boolean isLeaf;
	private boolean traversable;
	private double centerx, centery, width, height, x, y;
	//private QTree parent;
	
	private QTree ne = null,
				nw = null,
				se = null,
				sw = null;
	
	//private int owner;
	
	public QTree(double x, double y, double w, double h, boolean traversable){
		this(x, y, w, h, traversable, null);
	}
	
	public QTree(double x, double y, double w, double h, boolean traversable, QTree parent){
		this.x = x;
		this.y = y;
		this.centerx = x + w/2;
		this.centery = y + h/2;
		this.width = w;
		this.height = h;
		this.traversable = traversable;
		this.isLeaf = true;
		//this.parent = parent;
	}
	
	public boolean isTraversable() {
		return traversable;
	}

	public void setTraversable(boolean traversable) {
		this.traversable = traversable;
	}
	
	public void toggleTraversableZone(double x, double y){
		ArrayDeque<QTree> queue = new ArrayDeque<QTree>();
		QTree current = this.getContainingNode(x, y);
		queue.addLast(current);
		
		
		boolean trav = current.isTraversable();
		
		while((current = queue.pollFirst()) != null){
			for(QTree e : current.getNeighbors(this)){
				if(e.isTraversable() == trav && !queue.contains(e)){
					queue.addLast(e);
				}
			}
			current.toggleTraversable();
		}
		
		this.consolidate();
	}

	private List<QTree> getNeighbors(QTree node) {
		List<QTree> result = new ArrayList<QTree>();
		
		List<QTree> a = getTopNeighbors(node);
		result.addAll(a);
		List<QTree> b = getBottomNeighbors(node);
		result.addAll(b);
		List<QTree> c = getLeftNeighbors(node);
		result.addAll(c);
		List<QTree> d = getRightNeighbors(node);
		result.addAll(d);
		
		return result;
	}
	
	private List<QTree> getTopNeighbors(QTree node) {
		List<QTree> result = new ArrayList<QTree>();
		
		double y = this.getY() - 1;
		
		if(y >= 0){
			double x = this.getX();
			double maxX = this.getX() + this.getWidth();
			
			QTree temptree;
			while(x < maxX){
				temptree = node.getContainingNode(x, y);
				result.add(temptree);
				x += temptree.getWidth();
			}
		}
		return result;
	}
	
	private List<QTree> getBottomNeighbors(QTree node) {
		List<QTree> result = new ArrayList<QTree>();
		
		double y = this.getY() + this.getHeight();
		
		if(y < Parameters.sizey){
			double x = this.getX();
			double maxX = this.getX() + this.getWidth();
			
			QTree temptree;
			while(x < maxX){
				temptree = node.getContainingNode(x, y);
				result.add(temptree);
				x += temptree.getWidth();
			}
		}
		
		return result;
	}
	
	private List<QTree> getLeftNeighbors(QTree node) {
		List<QTree> result = new ArrayList<QTree>();

		double x = this.getX() - 1;
		
		if(x >= 0){
			double y = this.getY();
			double maxY = this.getY() + this.getHeight();
			
			QTree temptree;
			while(y < maxY){
				temptree = node.getContainingNode(x, y);
				result.add(temptree);
				y += temptree.getHeight();
			}
		}
		return result;
	}
	
	private List<QTree> getRightNeighbors(QTree node) {
		List<QTree> result = new ArrayList<QTree>();
		
		double x = this.getX() + this.getWidth();
		
		if(x < Parameters.sizex){
			double y = this.getY();
			double maxY = this.getY() + this.getHeight();
			
			QTree temptree;
			while(y < maxY){
				temptree = node.getContainingNode(x, y);
				result.add(temptree);
				y += temptree.getHeight();
			}
		}
		
		return result;
	}
	
	private void toggleTraversable() {
		this.traversable ^= true; // Black magic, don't touch :D
	}

	public boolean isLeaf() {
		return isLeaf;
	}

	public QTree getNe() {
		return ne;
	}

	public QTree getNw() {
		return nw;
	}

	public QTree getSe() {
		return se;
	}

	public QTree getSw() {
		return sw;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	private void split(){
		this.isLeaf = false;
		
		double demiwidth = this.width / 2;
		double demiheight = this.height / 2;
		
		this.ne = new QTree(this.centerx, this.centery - demiheight, demiwidth, demiheight, this.isTraversable(), this);
		this.se = new QTree(this.centerx, this.centery, demiwidth, demiheight, this.isTraversable(), this);
		this.nw = new QTree(this.centerx - demiwidth, this.centery - demiheight, demiwidth, demiheight, this.isTraversable(), this);
		this.sw = new QTree(this.centerx - demiwidth, this.centery, demiwidth, demiheight, this.isTraversable(), this);
	}
	
	public double getCenterx() {
		return centerx;
	}

	public double getCentery() {
		return centery;
	}

	public double getWidth() {
		return width;
	}

	public double getHeight() {
		return height;
	}

	public boolean canSplit(){
		return this.width > 1 && this.height > 1;
	}
	
	public QTree getContainingNode(double x, double y){
		if(this.isLeaf){
			return this;
		} else {
			return this.getDirectedNode(x, y).getContainingNode(x, y);
		}
	}

	private QTree getDirectedNode(double x, double y){
		if(this.isLeaf){ 
			return null;
		} else if(x < this.centerx && y < this.centery){
			return nw;
		} else if(x < this.centerx && y >= this.centery){
			return sw;
		} else if(x >= this.centerx && y < this.centery){
			return ne;
		} else {
			return se;
		}
	}
	
	/**
	 * Set a shape to a certain state
	 * @param s
	 * @param traversable
	 */
	public void setShape(Shape s, boolean traversable){
		this._setShape(s, traversable);
		this.consolidate();
	}
	
	/**
	 * Helper method for setShape
	 * @param s
	 * @param traversable
	 */
	private void _setShape(Shape s, boolean traversable){
		// Verify that a leaf is fully inside the shape. If so, set the state.
		// If not, split it if possible, and recurse to the children
		if(!this.isLeaf){
			for(QTree tree : this.getChildren()){
				tree._setShape(s, traversable);
			}
		} else {
			if(this.traversable == traversable){
				// No changes needed
			} else {
				if(s.intersects(this.x, this.y, this.width, this.height)){
					if(s.contains(this.x, this.y, this.width, this.height)){
						this.traversable = traversable;
					} else {
						if(this.canSplit()){
							this.split();
							this.ne._setShape(s, traversable);
							this.nw._setShape(s, traversable);
							this.se._setShape(s, traversable);
							this.sw._setShape(s, traversable);
						} else {
							this.traversable = traversable;
						}
					}
				}
			}
		}
	}
	
	private boolean consolidate(){
		if(this.isLeaf){
			return true;
		} else {
			boolean stop = false;
			if(!this.ne.consolidate()) {
				stop = true;
			}
			if(!this.nw.consolidate()) {
				stop = true;
			}
			if(!this.se.consolidate()) {
				stop = true;
			}
			if(!this.sw.consolidate()) {
				stop = true;
			}
			
			if(stop){
				return false;
			} else {
				if(ne.isLeaf && nw.isLeaf && se.isLeaf && sw.isLeaf){
					if((ne.isTraversable() && se.isTraversable() && sw.isTraversable() && nw.isTraversable()) ||
							(!ne.isTraversable() && !se.isTraversable() && !sw.isTraversable() && !nw.isTraversable())){
						this.traversable = ne.isTraversable();
						this.isLeaf = true;
						this.ne = null;
						this.nw = null;
						this.se = null;
						this.sw = null;
						return true;
					}
				}
				return false;
			}
		}
	}
	
	public long nbLeaf(){
		if(this.isLeaf){
			return 1;
		} else {
			return ne.nbLeaf() + nw.nbLeaf() + se.nbLeaf() + sw.nbLeaf();
		}
	}
	
	public QTree[] getChildren(){
		return new QTree[] {ne, se, sw, nw};
	}
	
	public List<QTree> getAllLeaves(){
		// Use a unique list that I pass to the recursion to avoid creating and merging multiple lists
		List<QTree> result = new ArrayList<QTree>();
		
		this._getAllLeaves(result);
		
		return result;
	}
	
	public void _getAllLeaves(List<QTree> result){
		if(this.isLeaf()){
			result.add(this);
		} else {
			for(QTree t : this.getChildren()){
				t._getAllLeaves(result);
			}
		}
	}
	
	/**
	 * If everything is connected, we may be able to find a path!
	 * @return true if everything is connected
	 */
	public boolean isEverythingConnected(){
		// The algorithm add all the nodes of the QTree to a list and then removes only connected nodes, starting from (0,0).
		// All non traversable nodes are always connected.
		// From the first traversable node, all his neighbors are connected.
		
		boolean firstTraversable = true;
		
		List<QTree> l = new ArrayList<QTree>();
		l.addAll(this.getAllLeaves());

		ArrayDeque<QTree> queue = new ArrayDeque<QTree>();
		QTree current = this.getContainingNode(0, 0);
		queue.addLast(current);
		
		l.remove(current);
		
		if(current.isTraversable()){
			firstTraversable = false;
		}
		
		while((current = queue.pollFirst()) != null){
			for(QTree e : current.getNeighbors(this)){
				if(l.contains(e)){
					if(!e.isTraversable() || current.isTraversable()){
						// If not traversable, doesn't count so remove it
						// If traversable, remove only if the current node is traversable
						queue.addLast(e);
						l.remove(e);
					} else if(firstTraversable) {
						// If it's the *first* traversable node, continue
						firstTraversable = false;
						queue.addLast(e);
						l.remove(e);
					}
				}
			}
		}
		
		return l.size() == 0;
	}
	
	private static enum Directions{
		NE(Math.sqrt(2)), N(1), NW(Math.sqrt(2)), W(1), SW(Math.sqrt(2)), S(1), SE(Math.sqrt(2)), E(1);
		
		public static Directions getOpposite(Directions d){
			if(d == null){
				return null;
			}
			switch(d){
			case E:
				return W;
			case N:
				return S;
			case NE:
				return SW;
			case NW:
				return SE;
			case S:
				return N;
			case SE:
				return NW;
			case SW:
				return NE;
			case W:
				return E;
			default:
				return null;
			}
		}
		
		private double distance;
		
		private Directions(double distance) {
			this.distance = distance;
		}

		public double getDistance() {
			return distance;
		}
	}
	
	private static class PathValue {
		private Directions from;
		private double g_score;
		private double f_score;
		
		public PathValue(){
			this(null, Double.MAX_VALUE, Double.MAX_VALUE);
		}
		public Directions getFrom() {
			return from;
		}
		public void setFrom(Directions from) {
			this.from = from;
		}
		public double getG_score() {
			return g_score;
		}
		public void setG_score(double g_score) {
			this.g_score = g_score;
		}
		public double getF_score() {
			return f_score;
		}
		public void setF_score(double f_score) {
			this.f_score = f_score;
		}
		public PathValue(Directions from, double g_score, double f_score) {
			super();
			this.from = from;
			this.g_score = g_score;
			this.f_score = f_score;
		}
	}
	
	/**
	 * Find a path that do not cross any forbidden zone. Not especially the best path?
	 * @param fromx
	 * @param fromy
	 * @param tox
	 * @param toy
	 * @return A path between from and to
	 */
	public TravelPath findPath(double fromx, double fromy, double tox, double toy){
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
	}
	
	private static Point2d getDirectedPoint(Point2d p, Directions d){
		double x = p.getX();
		double y = p.getY();
		switch(d){
		case E:
			return new Point2d(x+1,y);
		case N:
			return new Point2d(x,y-1);
		case NE:
			return new Point2d(x+1,y-1);
		case NW:
			return new Point2d(x-1,y-1);
		case S:
			return new Point2d(x,y+1);
		case SE:
			return new Point2d(x+1,y+1);
		case SW:
			return new Point2d(x-1,y+1);
		case W:
			return new Point2d(x-1,y);
		default:
			return null;
		}
	}
	
	// TODO
	public static class TravelPath{
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
}
