package springCommon;

import java.awt.geom.Point2D;

public class Point2d extends Point2D.Double {
	public Point2d(int node) {
		this(node/Parameters.sizey, node%Parameters.sizey);
	}
	
	public Point2d(double x, double y) {
		super(x,y);
	}

	private static final long serialVersionUID = 1L;

	public void setX(double x) {
		this.setLocation(x, this.getY());
	}

	public void setY(double y) {
		this.setLocation(this.getX(), y);
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("(").append(x).append(",").append(y).append(")");
		return super.toString();
	}

	public boolean isInside() {
		return this.x >= 0 && this.x < Parameters.sizex && this.y >= 0 && this.y < Parameters.sizey;
	}
	
	public int intValue(){
		return (int)x * Parameters.sizey + (int)y;
	}
}
