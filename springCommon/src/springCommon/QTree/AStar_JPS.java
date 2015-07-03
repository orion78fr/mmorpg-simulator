package springCommon.QTree;

import java.util.ArrayList;
import java.util.List;

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
	
	private List<Point2d> openSet;
	
	public AStar_JPS(QTree tree) {
		super();
		this.tree = tree;
		this.openSet = new ArrayList<Point2d>();
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
		}
		
		System.err.println("exploration ended, no path found");
		return null;
	}
	
	private void explore_node(Point2d node){
		// Find "best", non explored direction
		Directions d = get_best_direction(node);
		
		// Explore
		explore_node(node, d);
		
		// If not finished, re-add it to the open set
		if(!totally_explored(node)){
			this.openSet.addSorted(node);
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
		
		if(is_diag_jmp_point(node, d)){
			// This is a Jump Point, add it to open and stop here
			openSet.addSorted(node);
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
	
	private static <T> T tab_get(T[] tab, double x, double y){
		return tab[(int)x * (int)Parameters.sizex + (int) y];
	}
	
	private static <T> void tab_set(T[] tab, Point2d p, T value){
		tab_set(tab, p.getX(), p.getY(), value);
	}
	
	private static <T> void tab_set(T[] tab, double x, double y, T value){
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
