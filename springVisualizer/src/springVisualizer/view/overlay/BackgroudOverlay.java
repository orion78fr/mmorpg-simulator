package springVisualizer.view.overlay;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import springCommon.Parameters;
import springVisualizer.view.ViewCommon.Dimentions;

public class BackgroudOverlay extends AbstractOverlay {
	private static final long serialVersionUID = 1L;
	
	private BufferedImage img;

	public BackgroudOverlay() {
		this.setImage(new BufferedImage(500, 500, BufferedImage.TYPE_INT_ARGB));
	}

	public void setImage(BufferedImage img){		
		this.img = img;
		
		Dimentions.xSize = img.getWidth();
		Dimentions.ySize = img.getHeight();
		
		Dimentions.ratiox = Parameters.sizex / Dimentions.xSize;
        Dimentions.ratioy = Parameters.sizey / Dimentions.ySize;
	}
	
	@Override
	public void draw(Graphics2D g2d) {
		//System.out.println("redraw");
		AffineTransform at = new AffineTransform();
        at.scale(Dimentions.zoom, Dimentions.zoom);
        AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        
        BufferedImage sub = this.img.getSubimage(Dimentions.posx, Dimentions.posy, Dimentions.viewWidth, Dimentions.viewHeight);
        
        g2d.drawImage(sub, op, 0, 0);
	}
}
