package springCommon.QTree;

import java.util.ArrayList;

import springCommon.Parameters;
import springCommon.Point2d;
import static springCommon.QTree.TableUtils.*;

public class AStar_JPS {
	private Directions[] ancesters;
	private double[] g_score;
	private double[] f_score;
	private boolean[] tree;
	private boolean[] potential_jump_point;
	private TravelPath path;
	
	private class MySortedList extends ArrayList<Integer>{
		private static final long serialVersionUID = 1L;
		public MySortedList() {
			super(Parameters.sizex * Parameters.sizey);
		}
		@Override
		public boolean add(Integer e) {
			if(this.isEmpty()){
				super.add(0,e);
			}
			this.add(e, 0, this.size()-1);
			return true;
		}
		private void add(Integer e, int startIndex, int endIndex){
			if(startIndex == endIndex){
				if(tab_get(f_score, e) > tab_get(f_score, this.get(startIndex))){
					super.add(startIndex, e);
				} else {
					super.add(startIndex+1, e);
				}
			} else {
				int midIndex = (startIndex + endIndex) / 2;
				
				if(tab_get(f_score, e) > tab_get(f_score, this.get(midIndex))){
					add(e, startIndex, midIndex);
				} else {
					add(e, midIndex + 1, endIndex);
				}
			}
		}
		@Override
		public void add(int index, Integer element) {
			throw new RuntimeException("You can't choose the index");
		}
		
		public Integer removeBest(){
			return size() == 0 ? null : this.remove(size()-1);
		}
	}
	private MySortedList openSet;
	
	public AStar_JPS(QTree tree) {
		super();
		this.tree = tree.qTreeTraversableToArray();
		this.potential_jump_point = tree.qTreePotentialJumpPoints(this.tree);
		this.openSet = new MySortedList();

		int size =  Parameters.sizex * Parameters.sizey;
		this.g_score = new double[size];
		this.f_score = new double[size];
		this.ancesters = new Directions[size];
	}
	
	private void do_allocs(){
		// Arraycopy method faster than just new
		int size =  g_score.length;
		
		g_score[0] = 0;
		ancesters[0] = null;

		for (int i = 1; i < size; i += i) {
			System.arraycopy(g_score, 0, g_score, i, ((size - i) < i) ? (size - i) : i);
			System.arraycopy(ancesters, 0, ancesters, i, ((size - i) < i) ? (size - i) : i);
		}
		System.arraycopy(g_score, 0, f_score, 0, size);
		
		this.openSet.clear();
	}
	
	public TravelPath findPath(Point2d from, Point2d to){
		return findPath(from.getX(), from.getY(), to.getX(), to.getY());
	}
	
	public synchronized TravelPath findPath(double fromx, double fromy, double tox, double toy){
		do_allocs();
		
		path = new TravelPath((int)fromx, (int)fromy, (int)tox, (int)toy);
		
		int from = (int)fromx * Parameters.sizey + (int)fromy;
		
		tab_set(g_score, from, 0);
		tab_set(f_score, from, manhattan_distance(path.getFrom(), path.getTo()));
		
		openSet.add(from);
		
		Integer currentp;
		
		while((currentp = openSet.removeBest()) != null){
			if((new Point2d(currentp)).equals(path.getTo())){
				backward_path_construct();
				return path;
			}
			
			explore_node(currentp);
		}
		
		return null;
	}
	
	private static double manhattan_distance(Point2d a, Point2d b){
		return manhattan_distance((int)a.getX(), (int)a.getY(), (int)b.getX(), (int)b.getY());
	}
	
	private static double manhattan_distance(int a, int b){
		return manhattan_distance(a/Parameters.sizey,
								a%Parameters.sizey,
								b/Parameters.sizey,
								b%Parameters.sizey);
	}
	
	private static double manhattan_distance(int xa, int ya, int xb, int yb){
		return Math.abs(xa - xb) + Math.abs(ya - yb);
	}
	
	private Directions get_best_direction(int node){
		double dx = path.getTo().getX() - node/Parameters.sizey;
		double dy = path.getTo().getY() - node%Parameters.sizey;
		
		Directions d;
		boolean cw;
		
		if(dx > 0){
			if(dy > 0){
				d = Directions.SE;
				cw = dy > dx;
			} else {
				d = Directions.NE;
				cw = dx > -dy;
			}
		} else {
			if(dy > 0){
				d = Directions.SW;
				cw = -dx > dy;
			} else {
				d = Directions.NW;
				cw = -dy > -dx;
			}
		}
		
		return get_best_direction(node, d, cw);
	}
	
	private Directions get_best_direction(int node, Directions d, boolean cw){
		if(!is_explored(node, d)){
			return d;
		} else if(!is_explored(node,Directions.getPerpendicular(d, cw))){
			return Directions.getPerpendicular(d, cw);
		} else if(!is_explored(node,Directions.getPerpendicular(d, !cw))){
			return Directions.getPerpendicular(d, !cw);
		} else if(!is_explored(node, Directions.getOpposite(d))){
			return Directions.getOpposite(d);
		} else {
			return null;
		}
	}
	
	private void explore_node(int node){
		// Find "best", non explored direction
		Directions d = get_best_direction(node);
		if(d == null){
			return;
		}
		
		// Explore this diagonal
		explore_node(node, d);
		
		// If not finished, re-add it to the open set
		if(!is_totally_explored(node)){
			this.openSet.add(node);
		}
	}
	
	private boolean is_totally_explored(int node){		
		for(Directions d : Directions.values()){
			if(!is_explored(node, d)){
				return false;
			}
		}
		return true;
	}
	
	private boolean is_explored(int node, Directions d){
		return is_explored(getDirectedPoint(node, d));
	}
	
	private boolean is_explored(int node){
		return (new Point2d(node)).equals(path.getFrom()) || !tab_get(tree, node) || tab_get(ancesters, node) != null;
	}
	
	private boolean is_explored(double x, double y){
		return is_explored((int)x * Parameters.sizey + (int)y);
	}
	
	private void explore_node(int node, Directions d){
		if(!tab_get(tree, node)){
			// If not traversable, do nothing!
			return;
		}
		
		boolean ltr = (d == Directions.NE || d == Directions.SE);
		boolean utd = (d == Directions.SE || d == Directions.SW);
		
		// Explore horizontally and vertically
		if(explore_node_horizontal(node, ltr) || explore_node_vertical(node, utd)){
			return;
		}
		
		// Explore diagonally, if needed
		if(ltr ? node/Parameters.sizey+1 >= Parameters.sizex : node/Parameters.sizey-1 < 0 ||
				utd ? node%Parameters.sizey+1 >= Parameters.sizey : node%Parameters.sizey-1 < 0){
			return;
		}
		int diag = getDirectedPoint(node, d);
		if(tab_get(tree, diag) && !is_explored(diag)){
			tab_set(ancesters, diag, d);
			tab_set(g_score, diag, tab_get(g_score, node) + Math.sqrt(2));
			if(is_jmp_point(diag, d)){
				// This is a Jump Point, add it to open and stop here
				tab_set(f_score, diag, tab_get(g_score, diag) + manhattan_distance(new Point2d(node), path.getTo()));
				openSet.add(diag);
				return;
			}
			explore_node(diag, d);
		}
	}
	
	private boolean is_jmp_point(int p, Directions d){
		if(path.getTo().equals(new Point2d(p))){
			return true;
		}
		if(!this.potential_jump_point[p]){
			return false;
		}
		
		int full1, full2, empty1, empty2;
		
		if(Directions.isDiagonal(d)){
			empty1 = getDirectedPoint(p, Directions.getPerpendicular(d, true));
			full1 = getDirectedPoint(p, Directions.get135deg(d, true));
			
			empty2 = getDirectedPoint(p, Directions.getPerpendicular(d, false));
			full2 = getDirectedPoint(p, Directions.get135deg(d, false));
		} else {
			full1 = getDirectedPoint(p, Directions.getPerpendicular(d, true));
			empty1 = getDirectedPoint(p, Directions.get45deg(d, true));
			
			full2 = getDirectedPoint(p, Directions.getPerpendicular(d, false));
			empty2 = getDirectedPoint(p, Directions.get45deg(d, false));
		}
		
		return (!tab_get(tree, full1) && !is_explored(empty1)) || (!tab_get(tree, full2) && !is_explored(empty2));
	}
	
	private boolean is_jmp_point(double x, double y, Directions d){
		return is_jmp_point((int)x * Parameters.sizey + (int)y, d);
	}
	
	private boolean explore_node_horizontal(int node, boolean ltr){
		int increment;
		Directions d;
		if(ltr){
			increment = 1;
			d = Directions.E;
		} else {
			increment = -1;
			d = Directions.W;
		}
		
		int x = node/Parameters.sizey + increment;
		int y = node%Parameters.sizey;
		double oldgscore = tab_get(g_score, node/Parameters.sizey, y);
		
		while(tab_get(tree, x - increment, y)){
			if((0 <= x && x < Parameters.sizex) && !is_explored(x, y)){
				// If not explored, we continue
				tab_set(ancesters, x, y, d);
				oldgscore++;
				tab_set(g_score, x, y, oldgscore);
				
				if(is_jmp_point(x,y,d)){
					// This is a Jump Point as one of it's backward neighbors are not traversable and wasn't already explored
					Point2d newPoint = new Point2d(x, y);
					tab_set(f_score, x, y, oldgscore + manhattan_distance(newPoint, path.getTo()));
					openSet.add(x*Parameters.sizey + y);
					return path.getTo().equals(newPoint);
				}
				
				x += increment;
			} else {
				// Already explored
				break;
			}
		}
		return false;
	}
	
	private boolean explore_node_vertical(int node, boolean utd){
		int increment;
		Directions d;
		if(utd){
			increment = 1;
			d = Directions.S;
		} else {
			increment = -1;
			d = Directions.N;
		}
		
		int x = node/Parameters.sizey;
		int y = node%Parameters.sizey + increment;
		double oldgscore = tab_get(g_score, x, node%Parameters.sizey);
		
		while(tab_get(tree, x, y - increment)){
			if((0 <= y && y < Parameters.sizey) && !is_explored(x, y)){
				// If not explored, we continue
				tab_set(ancesters, x, y, d);
				oldgscore++;
				tab_set(g_score, x, y, oldgscore);
				
				if(is_jmp_point(x,y,d)){
					// This is a Jump Point as one of it's backward neighbors are not traversable and wasn't already explored
					Point2d newPoint = new Point2d(x, y);
					tab_set(f_score, x, y, oldgscore + manhattan_distance(newPoint, path.getTo()));
					openSet.add(x * Parameters.sizey + y);
					return path.getTo().equals(newPoint);
				}
				
				y += increment;
			} else {
				// Already explored
				break;
			}
		}
		return false;
	}
	
	private void backward_path_construct(){
		// So now we have found a path, iterate backwards and push it in the path
		// Only add when direction change
		int currentp = path.getTo().intValue();
		int from = path.getFrom().intValue();
		Directions d, olddir = null;
		
		while((d = tab_get(ancesters, currentp)) != null){
			if(d != olddir){
				if(currentp != from){
					path.addPoint(new Point2d(currentp));
				}
				olddir = d;
			}
			currentp = getDirectedPoint(currentp, Directions.getOpposite(d));
		}
	}
	
	private static int getDirectedPoint(int p, Directions d){
		switch(d){
		case E:
			return p+Parameters.sizey;
		case N:
			return p-1;
		case NE:
			return p+Parameters.sizey-1;
		case NW:
			return p-Parameters.sizey-1;
		case S:
			return p+1;
		case SE:
			return p+Parameters.sizey+1;
		case SW:
			return p-Parameters.sizey+1;
		case W:
			return p-Parameters.sizey;
		default:
			return -1;
		}
	}
}
