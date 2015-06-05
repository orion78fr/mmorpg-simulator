package springVisualizer.view.overlay;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import springVisualizer.view.ViewCommon.Dimentions;

public abstract class AbstractOverlay extends JPanel {
	private static final long serialVersionUID = 1L;
	
	public AbstractOverlay() {
		this.setOpaque(false);
	}
	
	@Override
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g.create();
		
		this.draw(g2d);
		
		g.dispose();
	}

	public abstract void draw(Graphics2D g2d);
	
	public final void redraw(){
		// Triggers the paintComponent method
		this.setBounds(0, Dimentions.yoffset, Dimentions.layoutw, Dimentions.layouth);
		this.repaint(1000);
	}
}
