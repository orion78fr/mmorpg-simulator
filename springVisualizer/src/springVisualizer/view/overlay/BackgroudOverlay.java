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
	
	private double oldzoom = -1;
	private AffineTransformOp op;

	public BackgroudOverlay() {
		super();
		this.setImage(new BufferedImage(500, 500, BufferedImage.TYPE_INT_ARGB));
	}

	public void setImage(BufferedImage img){		
		this.img = img;
		
		Dimentions.xSize = img.getWidth();
		Dimentions.ySize = img.getHeight();
		
		Dimentions.ratiox = Parameters.sizex / Dimentions.xSize;
        Dimentions.ratioy = Parameters.sizey / Dimentions.ySize;
	}
	
	private void refreshOp(){
		this.oldzoom = Dimentions.zoom;
		AffineTransform at = new AffineTransform();
        at.scale(Dimentions.zoom, Dimentions.zoom);
        this.op = new AffineTransformOp(at, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
	}
	
	@Override
	public void draw(Graphics2D g2d) {
		if(oldzoom != Dimentions.zoom){
			refreshOp();
		}
        
        g2d.drawImage(img, op, (int)(-Dimentions.posx*Dimentions.zoom), (int)(-Dimentions.posy*Dimentions.zoom));
	}

	@Override
	public String getDisplayName() {
		return "Background Image";
	}
}
