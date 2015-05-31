package springVisualizer.view.overlay;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import springVisualizer.Parameters;
import springVisualizer.view.MainWindow;
import springVisualizer.view.MainWindow.Dimentions;

public class BackgroudOverlay extends AbstractOverlay {
	private static final long serialVersionUID = 1L;
	
	private volatile BufferedImage img;
	
	public BackgroudOverlay() {
		this.setVerticalAlignment(JLabel.TOP);
		this.setImage(new BufferedImage(500, 500, BufferedImage.TYPE_INT_ARGB));
	}

	public synchronized void setImage(BufferedImage img){		
		this.img = img;
		
		Dimentions.xSize = img.getWidth();
		Dimentions.ySize = img.getHeight();
		
		Dimentions.ratiox = Parameters.sizex / Dimentions.xSize;
        Dimentions.ratioy = Parameters.sizey / Dimentions.ySize;
	}
	
	@Override
	public synchronized void draw(Graphics2D g2d) {
		AffineTransform at = new AffineTransform();
        at.scale(Dimentions.zoom, Dimentions.zoom);
        AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        
        BufferedImage sub = this.img.getSubimage(Dimentions.posx, Dimentions.posy, Dimentions.viewWidth, Dimentions.viewHeight);
        
        ImageIcon icon = new ImageIcon(op.filter(sub, null));
        this.setIcon(icon);
	}
}
