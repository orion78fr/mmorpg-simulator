package springCommon.QTree;

import java.awt.Shape;

public class QTree {
	private boolean isLeaf;
	private boolean traversable;
	private double centerx, centery, width, height, x, y;
	
	private QTree ne = null,
				nw = null,
				se = null,
				sw = null;
	
	//private int owner;
	
	public QTree(double x, double y, double w, double h, boolean traversable){
		this.x = x;
		this.y = y;
		this.centerx = x + w/2;
		this.centery = y + h/2;
		this.width = w;
		this.height = h;
		this.traversable = traversable;
		this.isLeaf = true;
	}
	
	public boolean isTraversable() {
		return traversable;
	}

	public void setTraversable(boolean traversable) {
		this.traversable = traversable;
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

	public void split(){
		this.isLeaf = false;
		
		double demiwidth = this.width / 2;
		double demiheight = this.height / 2;
		
		this.ne = new QTree(this.centerx, this.centery - demiheight, demiwidth, demiheight, this.isTraversable());
		this.se = new QTree(this.centerx, this.centery, demiwidth, demiheight, this.isTraversable());
		this.nw = new QTree(this.centerx - demiwidth, this.centery - demiheight, demiwidth, demiheight, this.isTraversable());
		this.sw = new QTree(this.centerx - demiwidth, this.centery, demiwidth, demiheight, this.isTraversable());
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

	public QTree getDirectedNode(double x, double y){
		if(this.isLeaf){ 
			return null;
		} else if(x > this.centerx && y > this.centery){
			return se;
		} else if(x < this.centerx && y > this.centery){
			return sw;
		} else if(x > this.centerx && y < this.centery){
			return ne;
		} else {
			return nw;
		}
	}
	
	public void setShape(Shape s, boolean traversable){
		if(!this.isLeaf){
			for(QTree tree : this.getChildrens()){
				tree.setShape(s, traversable);
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
							this.ne.setShape(s, traversable);
							this.nw.setShape(s, traversable);
							this.se.setShape(s, traversable);
							this.sw.setShape(s, traversable);
						} else {
							this.traversable = traversable;
						}
					}
				}
			}
		}
	}
	
	public boolean consolidate(){
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
	
	public long size(){
		if(this.isLeaf){
			return 1;
		} else {
			return ne.size() + nw.size() + se.size() + sw.size();
		}
	}
	
	public QTree[] getChildrens(){
		return new QTree[] {ne, se, sw, nw};
	}
}
