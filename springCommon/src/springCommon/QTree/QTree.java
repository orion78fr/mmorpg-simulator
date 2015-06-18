package springCommon.QTree;

import springCommon.Point2d;

public class QTree {
	private boolean isLeaf;
	private boolean traversable;
	private Point2d center;
	private Point2d demisize;
	
	private QTree ne = null,
				nw = null,
				se = null,
				sw = null;
	
	//private int owner;
	
	public QTree(double x, double y, double w, double h){
		this(x + w/2, y + h/2, w/2, h/2, true);
	}
	
	private QTree(double medx, double medy, double demiw, double demih, boolean traversable){
		this.center = new Point2d(medx, medy);
		this.demisize = new Point2d(demiw, demih);
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

	public void split(){
		this.isLeaf = false;
		this.ne = new QTree(this.center.getX(), this.center.getY() - this.demisize.getY(), this.demisize.getX(), this.demisize.getY());
		this.se = new QTree(this.center.getX(), this.center.getY(), this.demisize.getX(), this.demisize.getY());
		this.nw = new QTree(this.center.getX() - this.demisize.getX(), this.center.getY() - this.demisize.getY(), this.demisize.getX(), this.demisize.getY());
		this.sw = new QTree(this.center.getX() - this.demisize.getX(), this.center.getY(), this.demisize.getX(), this.demisize.getY());
	}

	public QTree getDirectedNode(double x, double y){
		if(this.isLeaf){ 
			return null;
		} else if(x > center.getX() && y > center.getY()){
			return ne;
		} else if(x < center.getX() && y > center.getY()){
			return nw;
		} else if(x > center.getX() && y < center.getY()){
			return se;
		} else {
			return sw;
		}
	}
}
