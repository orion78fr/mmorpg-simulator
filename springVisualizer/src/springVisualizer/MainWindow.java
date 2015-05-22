package springVisualizer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;

import springVisualizer.model.Hotspot;
import springVisualizer.model.Player;

/**
 * This is the main window of the application, showing the current state of the world (the State class).<br />
 * It shows the world, a background image (if selected), the current state of the servers, the hotspots and the players.
 * 
 * @author Guillaume Turchini
 */
public class MainWindow {
	/** */
	private MainWindow() {
		throw new RuntimeException("You can't instanciate this class!");
	}
	
	static public JFrame win;
	static private JMenuBar bar;
	static private JLabel imagelabel, serverOverlay, playerOverlay;
	static private BufferedImage fond;
	static private JScrollBar hBar, vBar;
	
	static private boolean moving = false;
	static private int movingoldx, movingoldy;

	static private double zoom = 1,
			ratiox = 0,
			ratioy = 0;
	static private int posx = 0,
			posy = 0,
			layoutw = 0,
			layouth = 0,
			taillex = 0,
			tailley = 0,
			yoffset = 0;
	
	/** Refresh all the drawing of the state in the window */
	public static void refresh(){
		refreshDimensions();
		refreshImage();
		/* Setting the bounds of these overlays automatically triggers the redrawing */
		serverOverlay.setBounds(0, yoffset, layoutw, layouth);
		playerOverlay.setBounds(0, yoffset, layoutw, layouth);
	}
	
	/** Refresh all the dimensions of the window, useful for the drawing */
	private static void refreshDimensions(){
		layoutw = imagelabel.getWidth();
        layouth = imagelabel.getHeight();
        
        if(zoom > Math.pow(2, 5)){
			zoom = Math.pow(2, 5);
		}
        if(zoom < (1.0 / Math.pow(2, 5))){
			zoom = 1.0 / Math.pow(2, 5);
		}
        
        taillex = (int) (layoutw/zoom) + 1;
        if(posx + taillex > fond.getWidth()){
        	posx = fond.getWidth() - taillex;
        	if(posx < 0){
        		posx = 0;
        		taillex = fond.getWidth();
        	}
        }
        tailley = (int) (layouth/zoom) + 1;
        if(posy + tailley > fond.getHeight()){
        	posy = fond.getHeight() - tailley;
        	if(posy < 0){
        		posy = 0;
        		tailley = fond.getHeight();
        	}
        }
        
        ratiox = (double)Parameters.sizex / fond.getWidth();
        ratioy = (double)Parameters.sizey / fond.getHeight();
        
        if(hBar.getValue() != posx){
        	hBar.setValue(posx);
        }
        if(hBar.getVisibleAmount() != taillex){
        	hBar.setVisibleAmount(taillex);
        }
        if(vBar.getValue() != posy){
        	vBar.setValue(posy);
        }
        if(vBar.getVisibleAmount() != tailley){
        	vBar.setVisibleAmount(tailley);
        }
        
        yoffset = bar.getHeight();
	}
	
	/** Refresh the background image */
	private static void refreshImage() {
        AffineTransform at = new AffineTransform();
        at.scale(zoom, zoom);
        AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        
        BufferedImage sub = fond.getSubimage(posx, posy, taillex, tailley);
        
        ImageIcon icon = new ImageIcon(op.filter(sub, null));
        imagelabel.setIcon(icon);
	}
    
	
	public static void start(){
		win = new JFrame();
		
		win.setTitle("Visualizer");
		win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		bar = new JMenuBar();
        generateMenu(bar);
        win.setJMenuBar(bar);
        
        imagelabel = new JLabel();
        imagelabel.setVerticalAlignment(JLabel.TOP);
        fond = new BufferedImage(500, 500, BufferedImage.TYPE_INT_ARGB);
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(imagelabel, BorderLayout.CENTER);
        
        serverOverlay = new JLabel(){
    			private static final long serialVersionUID = 1L;
    			@Override
    			protected void paintComponent(Graphics g){
    				super.paintComponent(g);
    				
    				Graphics2D g2d = (Graphics2D) g.create();
    				
    				/*g2d.setColor(new Color(255,0,0,20));
    				g2d.fillRect(0, 0, layoutw, layouth);*/
    				
					for (Hotspot h : State.hotspots) {
						int radius = (int)(h.getHotness()*2);
						RadialGradientPaint rgp = new RadialGradientPaint((int)(((h.getX()/ratiox)-posx)*zoom), (int)(((h.getY()/ratioy)-posy)*zoom),
								radius/2, new float[] { 0f, 1f },
								new Color[] { h.getColor(), new Color(255,255,255,0) });
						 g2d.setPaint(rgp);
						 g2d.fillOval((int)(((h.getX()/ratiox)-posx)*zoom) - radius/2, (int)(((h.getY()/ratioy)-posy)*zoom) - radius/2, radius, radius);
					}
    				
    				g2d.dispose();
    			}
        };
        playerOverlay = new JLabel(){
			private static final long serialVersionUID = 1L;
			@Override
			protected void paintComponent(Graphics g){
				super.paintComponent(g);
				Graphics2D g2d = (Graphics2D) g.create();
				
				for(Player p : State.playerList){
					g2d.setColor(p.getColor());
					g2d.fillOval((int)(((p.getX()/ratiox)-posx)*zoom) - 5,
							(int)(((p.getY()/ratioy)-posy)*zoom)- 5, 10, 10);
				}
				
				g2d.dispose();
			}
        };
        
        win.getLayeredPane().add(serverOverlay, new Integer(1));
        win.getLayeredPane().add(playerOverlay, new Integer(2));
        
        win.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {}
			@Override
			public void keyReleased(KeyEvent e) {}
			
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_E){
					zoom *= 2;
				} else if(e.getKeyCode() == KeyEvent.VK_A){
					zoom /= 2;
				} else if(e.getKeyCode() == KeyEvent.VK_R){
					State.moveAll();
				} else {
					return;
				}
				refresh();
			}
		});
        
        panel.addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				if(e.isControlDown()){
					int rot = e.getWheelRotation();
					if(rot < 0){
						zoom *= -(2*e.getWheelRotation());
						/*posx += e.getX();
						posy += e.getY();*/
					} else {
						zoom /= (2*e.getWheelRotation());
					}
					refresh();
				}
			}
		});
        
        panel.addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseMoved(MouseEvent e) {}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				if(moving){
					posx -= (e.getX() - movingoldx) / zoom;
					if(posx < 0){
						posx = 0;
					}
					posy -= (e.getY() - movingoldy) / zoom;
					if(posy < 0){
						posy = 0;
					}
					movingoldx = e.getX();
					movingoldy = e.getY();
					refresh();
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
			public void mouseClicked(MouseEvent e) {
				int x = (int)(((e.getX()/zoom) + posx)*ratiox);
				int y = (int)(((e.getY()/zoom) + posy)*ratioy);
				if(0 <= x && x <= Parameters.sizex && 0 <= y && y <= Parameters.sizey){
					State.hotspots.add(new Hotspot(x, y, 50));
					refresh();
				}
			}
		});
        
        panel.addComponentListener(new ComponentListener() {
			@Override
			public void componentShown(ComponentEvent e) {}
			
			@Override
			public void componentResized(ComponentEvent e) {
				refresh();
			}
			
			@Override
			public void componentMoved(ComponentEvent e) {}
			@Override
			public void componentHidden(ComponentEvent e) {}
		});
        
        
        hBar = new JScrollBar(JScrollBar.HORIZONTAL, 0, 500, 0, 500);
        vBar = new JScrollBar(JScrollBar.VERTICAL, 0, 500, 0, 500);
        panel.add(hBar, BorderLayout.SOUTH);
        panel.add(vBar, BorderLayout.EAST);
        
        hBar.addAdjustmentListener(new AdjustmentListener() {
			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
				posx = e.getValue();
				refresh();
			}
		});
        vBar.addAdjustmentListener(new AdjustmentListener() {
			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
				posy = e.getValue();
				refresh();
			}
		});
		
        refresh();
        
        win.getContentPane().add(panel);
        
        win.setSize(new Dimension(800, 600));
        
        win.setMinimumSize(new Dimension(500, 500));
        
		win.setVisible(true);
	}

	/**
	 * Generate the menu of the main window
	 * @param bar The JMenuBar of the main window
	 */
	private static void generateMenu(JMenuBar bar) {
		JMenu menu;
        JMenuItem item;

        menu = new JMenu("File");

        item = new JMenuItem("Load background image");
        item.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                        JFileChooser fc = new JFileChooser(new File("").getAbsolutePath());
                        int ret = fc.showOpenDialog(win);
                        if (ret == JFileChooser.APPROVE_OPTION) {
                        	if(!fc.getSelectedFile().isFile()){
                        		JOptionPane.showMessageDialog(win, "This is not a file", "Error", JOptionPane.ERROR_MESSAGE);
                        	}
                            try {
                                    fond = ImageIO.read(fc.getSelectedFile());
                                    hBar.setMaximum(fond.getWidth());
                                    vBar.setMaximum(fond.getHeight());
                                    refresh();
                            } catch (Exception ex) {
                            		JOptionPane.showMessageDialog(win, "The image file is invalid", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                }
        });
        menu.add(item);
        
        bar.add(menu);
        
        // -------------------------
        
        menu = new JMenu("Generate");
        
        item = new JMenuItem("Add distributed players");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	String num = JOptionPane.showInputDialog(win, "How many players?", "Add players", JOptionPane.QUESTION_MESSAGE);
            	if(num == null){
            		return;
            	}
            	try {
            		int numPlayers = Integer.parseInt(num);
            		if(numPlayers < 0){
            			JOptionPane.showMessageDialog(win, "The input is negative", "Error", JOptionPane.ERROR_MESSAGE);
            			return;
            		}
            		State.addHaltonPlayers(numPlayers);
            		refresh();
            	} catch (Exception ex) {
            		JOptionPane.showMessageDialog(win, "The input is not a number", "Error", JOptionPane.ERROR_MESSAGE);
            	}
            }
        });
        menu.add(item);
        
        item = new JMenuItem("Remove random players");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	String num = JOptionPane.showInputDialog(win, "How many players?", "Add players", JOptionPane.QUESTION_MESSAGE);
            	if(num == null){
            		return;
            	}
            	try {
            		int numPlayers = Integer.parseInt(num);
            		if(numPlayers < 0){
            			JOptionPane.showMessageDialog(win, "The input is negative", "Error", JOptionPane.ERROR_MESSAGE);
            			return;
            		}
            		State.removeRandomPlayers(numPlayers);
            		refresh();
            	} catch (Exception ex) {
            		JOptionPane.showMessageDialog(win, "The input is not a number", "Error", JOptionPane.ERROR_MESSAGE);
            	}
            }
        });
        menu.add(item);
        
        item = new JMenuItem("Export platform");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	JFileChooser fc = new JFileChooser(new File("").getAbsolutePath());
                int ret = fc.showSaveDialog(win);
                if (ret == JFileChooser.APPROVE_OPTION) {
                	if(fc.getSelectedFile().exists() && !fc.getSelectedFile().isFile()){
                		JOptionPane.showMessageDialog(win, "This is not a file", "Error", JOptionPane.ERROR_MESSAGE);
                	} else {
	                    try {
	                            Writer w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fc.getSelectedFile()), "utf-8"));
	                            if(State.exportPlatform(w)){
	                            	JOptionPane.showMessageDialog(win, "Export successful to " + fc.getSelectedFile(), "Export", JOptionPane.INFORMATION_MESSAGE);
	                            } else {
	                            	JOptionPane.showMessageDialog(win, "Something went wrong during export", "Error", JOptionPane.ERROR_MESSAGE);
	                            }
	                            w.close();
	                    } catch (Exception ex) {
	                    		ex.printStackTrace();
	                    }
                	}
                }
            }
        });
        menu.add(item);
        
        bar.add(menu);
	}

}
