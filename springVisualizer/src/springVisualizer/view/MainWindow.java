package springVisualizer.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
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
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

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

import springVisualizer.Parameters;
import springVisualizer.State;
import springVisualizer.model.Hotspot;
import springVisualizer.view.overlay.AbstractOverlay;
import springVisualizer.view.overlay.BackgroudOverlay;
import springVisualizer.view.overlay.HotspotOverlay;
import springVisualizer.view.overlay.PlayerOverlay;
import springVisualizer.view.overlay.ServerOverlay;
import springVisualizer.view.overlay.ZonesOverlay;

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
	
	public static class Dimentions {
		/** Zoom factor */
		static public double zoom = 1;
		
		/** x position of the scrolling */
		static public int posx = 0;
		/** y position of the scrolling */
		static public int posy = 0;
		
		/** x size of the image */
		static public int xSize = 0;
		/** y size of the image */
		static public int ySize = 0;
		
		/** x layout size */
		static public int layoutw = 0;
		/** y layout size */
		static public int layouth = 0;
		
		/** x view size (shown image pixels) */
		static public int viewWidth = 0;
		/** y view size (shown image pixels) */
		static public int viewHeight = 0;
		
		/** x ratio between internal and image */
		static public double ratiox = 0;
		/** y ratio between internal and image */
		static public double ratioy = 0;
		
		/** y offset for the drawing */
		static public int yoffset = 0;
		
		static void debugPrint(){
			for(Field f : Dimentions.class.getDeclaredFields()){
				try {
					System.out.println(f.getName() + " = " + f.getDouble(null));
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
			System.out.println("");
		}
	}
	
	static public JFrame win;
	static private JMenuBar bar;
	static private JScrollBar hBar, vBar;
	
	static private boolean moving = false;
	static private int movingoldx, movingoldy;

	/** Refresh all the drawing of the state in the window */
	public static void refresh(){
		refreshDimensions();

		for(AbstractOverlay o : overlays){
			o.redraw();
		}
	}
	
	/** Refresh all the dimensions of the window, useful for the drawing */
	private static void refreshDimensions(){
		/* Size of the viewport */
		Dimentions.layouth = fakeLabel.getHeight();
		Dimentions.layoutw = fakeLabel.getWidth();
		
		/* To not draw on the menubar */
		Dimentions.yoffset = bar.getHeight();
		
		/* Limits of the zoom factor */
        if(Dimentions.zoom > Math.pow(2, 5)){
			Dimentions.zoom = Math.pow(2, 5);
		}
        if(Dimentions.zoom < (1.0 / Math.pow(2, 5))){
			Dimentions.zoom = 1.0 / Math.pow(2, 5);
		}
        
        /* Set the view dimention, ceiling for partial pixel */
        Dimentions.viewWidth = (int) Math.ceil(Dimentions.layoutw/Dimentions.zoom);
        Dimentions.viewHeight = (int) Math.ceil(Dimentions.layouth/Dimentions.zoom);
        
        /* If the view dimentions exceeds the size, correct the initial position */
        if(Dimentions.posx + Dimentions.viewWidth > Dimentions.xSize) {
        	Dimentions.posx = Dimentions.xSize - Dimentions.viewWidth;
        	/* If this is still too large, the picture is too small to fill the whole window */
        	if(Dimentions.posx < 0){
        		Dimentions.posx = 0;
        		Dimentions.viewWidth = Dimentions.xSize;
        	}
        }
        if(Dimentions.posy + Dimentions.viewHeight > Dimentions.ySize){
        	Dimentions.posy = Dimentions.ySize - Dimentions.viewHeight;
        	if(Dimentions.posy < 0){
        		Dimentions.posy = 0;
        		Dimentions.viewHeight = Dimentions.ySize;
        	}
        }
        
        /* Disable bar refresh */
        barRefresh = false;
        hBar.setValue(Dimentions.posx);
        hBar.setVisibleAmount(Dimentions.viewWidth);
        hBar.setMaximum(Dimentions.xSize);
        vBar.setValue(Dimentions.posy);
        vBar.setVisibleAmount(Dimentions.viewHeight);
        vBar.setMaximum(Dimentions.ySize);
        barRefresh = true;
        
        //Dimentions.debugPrint();
	}
	
	static private boolean barRefresh = true;
	
	static private BackgroudOverlay background;
	static private JLabel fakeLabel;
	
	public static void start(){
		win = new JFrame();
		
		win.setTitle("Visualizer");
		win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		bar = new JMenuBar();
        generateMenu(bar);
        win.setJMenuBar(bar);
        
        JPanel panel = new JPanel(new BorderLayout());
        
        fakeLabel = new JLabel();
        panel.add(fakeLabel, BorderLayout.CENTER);
        
        background = new BackgroudOverlay();
        addOverlay(background);
        
        addOverlay(new HotspotOverlay());
        addOverlay(new PlayerOverlay());
        addOverlay(new ZonesOverlay());
        addOverlay(new ServerOverlay());
        
        win.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {}
			@Override
			public void keyReleased(KeyEvent e) {}
			
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_E){
					Dimentions.zoom *= 2;
				} else if(e.getKeyCode() == KeyEvent.VK_A){
					Dimentions.zoom /= 2;
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
						Dimentions.zoom *= -(2*e.getWheelRotation());
						/*posx += e.getX();
						posy += e.getY();*/
					} else {
						Dimentions.zoom /= (2*e.getWheelRotation());
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
					Dimentions.posx -= (e.getX() - movingoldx) / Dimentions.zoom;
					if(Dimentions.posx < 0){
						Dimentions.posx = 0;
					}
					Dimentions.posy -= (e.getY() - movingoldy) / Dimentions.zoom;
					if(Dimentions.posy < 0){
						Dimentions.posy = 0;
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
				int x = (int)(((e.getX()/Dimentions.zoom) + Dimentions.posx)*Dimentions.ratiox);
				int y = (int)(((e.getY()/Dimentions.zoom) + Dimentions.posy)*Dimentions.ratioy);
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
				if(!barRefresh){
					return;
				}
				Dimentions.posx = e.getValue();
				refresh();
			}
		});
        vBar.addAdjustmentListener(new AdjustmentListener() {
			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
				if(!barRefresh){
					return;
				}
				Dimentions.posy = e.getValue();
				refresh();
			}
		});
        
        win.getContentPane().add(panel);
        
        win.setSize(new Dimension(800, 600));
        
        win.setMinimumSize(new Dimension(500, 500));
        
		win.setVisible(true);
		
		refresh();
	}
	
	private static int currentIndex = 1;
	private static List<AbstractOverlay> overlays = new ArrayList<AbstractOverlay>();
	private static void addOverlay(AbstractOverlay overlay) {
		overlays.add(overlay);
		win.getLayeredPane().add(overlay, new Integer(currentIndex));
		currentIndex++;
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
                            	background.setImage(ImageIO.read(fc.getSelectedFile()));
                            	
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
