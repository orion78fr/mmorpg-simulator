package springCommon;

import java.awt.geom.Point2D;

public class Point2d extends Point2D.Double {
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

}
