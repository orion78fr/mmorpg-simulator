package springCommon.QTree;

import java.awt.Shape;
import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
	
	public boolean isTraversable(double x, double y){
		return this.getContainingNode(x, y).isTraversable();
	}
	
	public boolean isTraversable(Point2d p){
		return this.isTraversable(p.getX(), p.getY());
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
		
		List<QTree> l = this.getAllLeaves();

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
	
	public boolean[] qTreeTraversableToArray(){
		boolean[] result = new boolean[Parameters.sizex * Parameters.sizey];
		List<QTree> l = this.getAllLeaves();
		for(QTree t : l){
			if(t.isTraversable()){
				double x = t.x;
				double y = t.y;
				double w = t.width;
				double h = t.height;
				for(int i = 0; i < w; i++){
					Arrays.fill(result, ((int)x+i) * Parameters.sizex + (int)y, ((int)x+i) * Parameters.sizex + (int)y + (int)h, true);
				}
			}
		}
		return result;
	}

	public static boolean[] qTreePotentialJumpPoints(boolean[] tree) {
		boolean[] result = new boolean[tree.length];
		
		for(int i = 0; i < tree.length; i++){
			if(tree[i] && !(
					tree[i+Parameters.sizey] &&
					tree[i-1] &&
					tree[i+Parameters.sizey-1] &&
					tree[i-Parameters.sizey-1] &&
					tree[i+1] &&
					tree[i+Parameters.sizey+1] &&
					tree[i-Parameters.sizey+1] &&
					tree[i-Parameters.sizey])){
				result[i] = true;
			}
		}
		
		return result;
	}
}
