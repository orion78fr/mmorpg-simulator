package springVisualizer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RadialGradientPaint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class MainWindow {

	public MainWindow() {
		// TODO Auto-generated constructor stub
	}

	public static void doRepaint(){
		if(p == null){
			throw new RuntimeException("can't repaint");
		}
		p.repaint();
	}
	static JPanel p = null;
	
	static JTextField jtf = null;
	public static int getHotness(){
		if(jtf == null){
			throw new RuntimeException("can't get hotness");
		}
		try{
			return Integer.parseInt(jtf.getText());
		} catch (NumberFormatException e){
			return 50;
		}
	}
	
	static private JFrame win;
	static private JLabel imagelabel;
	static private BufferedImage fond;
	static private double zoom = 1;
	static private int posx = 0, posy = 0;
	static private JScrollBar hBar, vBar;
	
	static private boolean moving = false;
	static private int movingoldx, movingoldy;

	private static void refreshImage() {
        AffineTransform at = new AffineTransform();
        at.scale(zoom, zoom);
        AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_BICUBIC);
        
        int layoutx = 500;
        int layouty = 500;
        
        int taillex = (int) (layoutx/zoom) + 1;
        if(posx + taillex > fond.getWidth()){
        	taillex = fond.getWidth() - posx;
        }
        int tailley = (int) (layouty/zoom) + 1;
        if(posy + tailley > fond.getHeight()){
        	tailley = fond.getHeight() - posy;
        }
        
        if(taillex == 0 || tailley == 0){
        	// Too Much Zoom!
        	System.err.println("STOP ZOOMER PUTAIN");
        	return;
        }
        
        BufferedImage sub = fond.getSubimage(posx, posy, taillex, tailley);
        
        ImageIcon icon = new ImageIcon(op.filter(sub, null));
        imagelabel.setIcon(icon);
        
        hBar.setValue(posx);
        hBar.setVisibleAmount(taillex);
        vBar.setValue(posy);
        vBar.setVisibleAmount(tailley);
	}
        
	public static void start(){
		win = new JFrame();
		
		win.setTitle("Visualizer");
		win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JMenuBar bar = new JMenuBar();
        generateMenu(bar);
        win.setJMenuBar(bar);
        
        imagelabel = new JLabel();
        fond = new BufferedImage(500, 500, BufferedImage.TYPE_INT_ARGB);
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(imagelabel, BorderLayout.CENTER);
        
        panel.addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				if(e.isControlDown()){
					int rot = e.getWheelRotation();
					if(rot < 0){
						zoom *= -(2*e.getWheelRotation());
						if(zoom > Math.pow(2, 5)){
							zoom = Math.pow(2, 5);
						}
						/*posx += e.getX();
						posy += e.getY();*/
					} else {
						zoom /= (2*e.getWheelRotation());
						if(zoom < 1.0 / Math.pow(2, 5)){
							zoom = 1.0 / Math.pow(2, 5);
						}
					}
					refreshImage();
				}
			}
		});
        panel.addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseMoved(MouseEvent e) {}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				if(moving){
					posx -= (e.getX() - movingoldx);
					if(posx < 0){
						posx = 0;
					}
					posy -= (e.getY() - movingoldy);
					if(posy < 0){
						posy = 0;
					}
					movingoldx = e.getX();
					movingoldy = e.getY();
					refreshImage();
				}
			}
		});
        panel.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
				moving = false;
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				movingoldx = e.getX();
				movingoldy = e.getY();
				moving = true;
			}
			
			@Override
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseClicked(MouseEvent e) {}
		});
        
        
        hBar = new JScrollBar(JScrollBar.HORIZONTAL, 0, 500, 0, 500);
        vBar = new JScrollBar(JScrollBar.VERTICAL, 0, 500, 0, 500);
        panel.add(hBar, BorderLayout.SOUTH);
        panel.add(vBar, BorderLayout.EAST);
        
        hBar.addAdjustmentListener(new AdjustmentListener() {
			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
				posx = e.getValue();
				refreshImage();
			}
		});
        vBar.addAdjustmentListener(new AdjustmentListener() {
			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
				posy = e.getValue();
				refreshImage();
			}
		});
		
        refreshImage();
        
        win.getContentPane().add(panel);
        
        win.pack();
        
        
		/*JPanel options = new JPanel(new GridLayout(0, 1));
		JCheckBox hotspot_chkbx = new JCheckBox("Draw hotspots", true);
		hotspot_chkbx.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				doRepaint();
			}
		});
		options.add(hotspot_chkbx);
		JButton nextStep = new JButton("Next Step");
		nextStep.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				State.moveAllToNearestHotspot();
				doRepaint();
			}
		});
		options.add(nextStep);
		jtf = new JTextField("50", 4);
		options.add(jtf);
		JCheckBox tricolor = new JCheckBox("TriColor", false);
		tricolor.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(tricolor.isSelected()){
					Random r = new Random();
					Color[] colors = {Color.GREEN, Color.BLUE, Color.RED};
					for(Player p : State.playerList){
						p.setColor(colors[r.nextInt(3)]);
					}
				} else {
					for(Player p : State.playerList){
						p.setColor(Color.BLUE);
					}
				}
				doRepaint();
			}
		});
		options.add(tricolor);
			
		
		BufferedImage bi = new BufferedImage(Parameters.size, Parameters.size, BufferedImage.TYPE_INT_ARGB);
		
		
		
		
		
		p = new JPanel(){
			private static final long serialVersionUID = 1L;
			@Override
	        public Dimension getPreferredSize() {
	            return new Dimension(Parameters.size, Parameters.size);
	        }
			@Override
			protected void paintComponent(Graphics g){
				super.paintComponent(g);
				Graphics2D g2d = (Graphics2D) g.create();
				g2d.setColor(Color.WHITE);
				g2d.fillRect(0, 0, Parameters.size, Parameters.size);
				if(hotspot_chkbx.isSelected()){
					for (Hotspot h : State.hotspots) {
						int radius = (int)(h.getHotness()*2);
						RadialGradientPaint rgp = new RadialGradientPaint(h.getX(), h.getY(),
								radius/2, new float[] { 0f, 1f },
								new Color[] { h.getColor(), new Color(0,0,0,0) });
						 g2d.setPaint(rgp);
						 g2d.fillOval(h.getX() - radius/2, h.getY() - radius/2, radius, radius);
					}
				}
				
				
				for(Player p : State.playerList){
					g2d.setColor(p.getColor());
					g2d.fillRect(p.getX(), p.getY(), 1, 1);
				}
				
				g2d.dispose();
			}
		};
		
		p.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {}
			
			@Override
			public void mousePressed(MouseEvent e) {}
			
			@Override
			public void mouseExited(MouseEvent e) {}
			
			@Override
			public void mouseEntered(MouseEvent e) {}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				int x = e.getX();
				int y = e.getY();
				
				State.hotspots.add(new Hotspot(x, y, getHotness()));
				doRepaint();
			}
		});
		
		JScrollPane conteneurP = new JScrollPane(p);
		conteneurP.setPreferredSize(new Dimension(500, 500));
		
		JPanel contenu = new JPanel(new BorderLayout());
		contenu.add(conteneurP, BorderLayout.NORTH);
		contenu.add(options, BorderLayout.SOUTH);
		win.setContentPane(contenu);
		win.pack();*/
        
		win.setVisible(true);
	}

	private static void generateMenu(JMenuBar bar) {
		JMenu menu;
        JMenuItem item;

        menu = new JMenu("Fichier");

        item = new JMenuItem("Charger image de fond...");
        item.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                        JFileChooser fc = new JFileChooser(new File("").getAbsolutePath());
                        int ret = fc.showDialog(win, "Ouvrir");
                        if (ret == JFileChooser.APPROVE_OPTION) {
                                try {
                                        fond = ImageIO.read(fc.getSelectedFile());
                                        hBar.setMaximum(fond.getWidth());
                                        vBar.setMaximum(fond.getHeight());
                                        refreshImage();
                                } catch (Exception ex) {
                                		ex.printStackTrace();
                                        actionPerformed(e);
                                }
                        }
                }
        });
        menu.add(item);
        
        bar.add(menu);
	}

}
