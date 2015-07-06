package springCommon.QTree;

import java.util.ArrayList;
import java.util.List;

import javax.management.RuntimeErrorException;

import springCommon.Parameters;
import springCommon.Point2d;
import springCommon.QTree.QTree.Directions;
import springCommon.QTree.QTree.TravelPath;

public class AStar_JPS {
	private Directions[] ancesters;
	private double[] g_score;
	private double[] f_score;
	private QTree tree;
	private TravelPath path;
	private boolean used;
	
	private class MySortedList extends ArrayList<Point2d>{
		@Override
		public boolean add(Point2d e) {
			if(this.isEmpty()){
				super.add(0,e);
			}
			this.add(e, 0, this.size()-1);
			return true;
		}
		private void add(Point2d e, int startIndex, int endIndex){
			if(startIndex == endIndex){
				if(tab_get(f_score, e) < tab_get(f_score, this.get(startIndex))){
					super.add(startIndex, e);
				} else {
					super.add(startIndex+1, e);
				}
			} else {
				int midIndex = (startIndex + endIndex) / 2;
				
				if(tab_get(f_score, e) < tab_get(f_score, this.get(midIndex))){
					add(e, startIndex, midIndex);
				} else {
					add(e, midIndex + 1, endIndex);
				}
			}
		}
		@Override
		public void add(int index, Point2d element) {
			throw new RuntimeException("You can't choose the index");
		}
	}
	private MySortedList openSet;
	
	public AStar_JPS(QTree tree) {
		super();
		this.tree = tree;
		this.openSet = new MySortedList();
	}
	
	private void do_allocs(){
		int size =  (int) (Parameters.sizex * Parameters.sizey);
		this.ancesters = new Directions[size];
		this.g_score = new double[size];
		this.f_score = new double[size];
		this.openSet.clear();
	}
	
	public synchronized TravelPath findPath(double fromx, double fromy, double tox, double toy){
		do_allocs();
		
		path = new TravelPath((long)fromx + 0.5, (long)fromy + 0.5, (long)tox + 0.5, (long)toy + 0.5);
		
		//Set<Point2d> closedSet = new HashSet<Point2d>();
		
		Point2d currentp;
		
		while((currentp = openSet.remove(0)) != null){
			if(currentp.equals(path.getTo())){
				backward_path_construct();
				return path;
			}
			
			explore_node(currentp);
			
			if(openSet.isEmpty()){
				break;
			}
		}
		
		System.err.println("exploration ended, no path found");
		return null;
	}
	
	private static double manhattan_distance(Point2d a, Point2d b){
		return Math.abs(a.getX() - b.getX()) + Math.abs(a.getY() - b.getY());
	}
	
	private void explore_node(Point2d node){
		// Find "best", non explored direction
		Directions d = get_best_direction(node);
		
		// Explore this diagonal
		explore_node(node, d);
		
		// If not finished, re-add it to the open set
		if(!totally_explored(node)){
			this.openSet.add(node);
		}
	}
	
	private boolean totally_explored(Point2d node){		
		for(Directions d : Directions.values()){
			if(tab_get(ancesters, getDirectedPoint(node, d)) == null){
				return false;
			}
		}
		return true;
	}
	
	private void explore_node(Point2d node, Directions d){
		if(!tree.isTraversable(node)){
			// If not traversable, do nothing!
			return;
		}
		
		if(is_jmp_point(node, d)){
			// This is a Jump Point, add it to open and stop here
			openSet.add(node);
		}
		
		boolean ltr = (d == Directions.NE || d == Directions.SE);
		boolean utd = (d == Directions.SE || d == Directions.SW);
		
		// Explore horizontally
		explore_node_horizontal(node, ltr);
		
		// Explore vertically
		explore_node_vertical(node, utd);
		
		// Explore diagonally, if needed
		Point2d diag = getDirectedPoint(node, d);
		if(tab_get(ancesters, diag) == null){
			tab_set(ancesters, diag, d);
			explore_node(diag, d);
		}
	}
	
	private boolean is_jmp_point(Point2d p, Directions d){
		return is_jmp_point(p.getX(), p.getY(), d);
	}
	
	private boolean is_jmp_point(double x, double y, Directions d){
		switch(d){
		case E:
			return ((y-1 >= 0) ? !tree.isTraversable(x-1, y-1) : false) || ((y+1 < Parameters.sizey) ? !tree.isTraversable(x-1, y+1) : false);
		case N:
			return ((x-1 >= 0) ? !tree.isTraversable(x-1, y+1) : false) || ((x+1 < Parameters.sizex) ? !tree.isTraversable(x+1, y+1) : false);
		case NE:
			return !tree.isTraversable(x-1, y) || !tree.isTraversable(x, y+1);
		case NW:
			return !tree.isTraversable(x+1, y) || !tree.isTraversable(x, y+1);
		case S:
			return ((x-1 >= 0) ? !tree.isTraversable(x-1, y-1) : false) || ((x+1 < Parameters.sizex) ? !tree.isTraversable(x+1, y-1) : false);
		case SE:
			return !tree.isTraversable(x-1, y) || !tree.isTraversable(x, y-1);
		case SW:
			return !tree.isTraversable(x+1, y) || !tree.isTraversable(x, y-1);
		case W:
			return ((y-1 >= 0) ? !tree.isTraversable(x+1, y-1) : false) || ((y+1 < Parameters.sizey) ? !tree.isTraversable(x+1, y+1) : false);
		default:
			throw new RuntimeException("Testing if is jump point without giving a direction!");
		}
	}
	
	private void explore_node_horizontal(Point2d node, boolean ltr){
		int increment;
		Directions d;
		if(ltr){
			increment = 1;
			d = Directions.E;
		} else {
			increment = -1;
			d = Directions.W;
		}
		
		double x = node.getX();
		double y = node.getY();
		double oldgscore = tab_get(g_score, x, y);
		
		while(tree.isTraversable(x, y)){
			// TODO verify that x + increment is still on the map!
			if(tab_get(ancesters, x+increment, y) == null){
				// If not explored, we continue
				tab_set(ancesters, x+increment, y, d);
				oldgscore++;
				tab_set(g_score, x+increment, y, oldgscore);
				
				if(is_jmp_point(x+increment,y,d)){
					// This is a Jump Point as one of it's backward neighbors are not traversable and wasn't already explored
					Point2d newPoint = new Point2d(x+increment, y);
					tab_set(f_score, x+increment, y, oldgscore + manhattan_distance(newPoint, path.getTo()));
					openSet.add(newPoint);
					break;
				}
				
				x += increment;
			} else {
				// Already explored
				break;
			}
		}
	}
	
	private void backward_path_construct(){
		// So now we have found a path, iterate backwards and push it in the path
		Point2d currentp = path.getTo();
		Directions d;
		
		while((d = tab_get(ancesters, currentp)) != null){
			currentp = getDirectedPoint(currentp, Directions.getOpposite(d));
			path.addPoint(currentp);
		}
		
		// Removes the beginning, as it's "from"
		path.getPath().remove(0);
	}
	
	private static <T> T tab_get(T[] tab, Point2d p){
		return tab_get(tab, p.getX(), p.getY());
	}
	private static double tab_get(double[] tab, Point2d p){
		return tab_get(tab, p.getX(), p.getY());
	}
	
	private static <T> T tab_get(T[] tab, double x, double y){
		return tab[(int)x * (int)Parameters.sizex + (int) y];
	}
	private static double tab_get(double[] tab, double x, double y){
		return tab[(int)x * (int)Parameters.sizex + (int) y];
	}
	
	private static <T> void tab_set(T[] tab, Point2d p, T value){
		tab_set(tab, p.getX(), p.getY(), value);
	}
	private static void tab_set(double[] tab, Point2d p, double value){
		tab_set(tab, p.getX(), p.getY(), value);
	}
	
	private static <T> void tab_set(T[] tab, double x, double y, T value){
		tab[(int)x * (int)Parameters.sizex + (int) y] = value;
	}
	private static void tab_set(double[] tab, double x, double y, double value){
		tab[(int)x * (int)Parameters.sizex + (int) y] = value;
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
}
