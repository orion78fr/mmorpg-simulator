package springVisualizer.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
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
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollBar;

import springCommon.Parameters;
import springVisualizer.MovementLogger;
import springVisualizer.State;
import springVisualizer.view.overlay.AbstractOverlay;
import springVisualizer.view.overlay.BackgroudOverlay;
import springVisualizer.view.overlay.HotspotOverlay;
import springVisualizer.view.overlay.OverlayMouseMode;
import springVisualizer.view.overlay.PlayerOverlay;
import springVisualizer.view.overlay.ServerOverlay;
import springVisualizer.view.overlay.ZonesOverlay;
import springVisualizer.view.ViewCommon.Dimentions;

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
	static JMenuBar bar;
	static JScrollBar hBar;
	static JScrollBar vBar;
	
	static private boolean moving = false;
	static private int movingoldx, movingoldy;

	static boolean barRefresh = true;
	
	static BackgroudOverlay background;
	static private ZonesOverlay zones;
	static JLabel fakeLabel;
	
	public static void refresh(){
		for(AbstractOverlay o : MainWindow.overlays){
			o.redraw();
		}
	}
	
	public static void start(){
		win = new JFrame();
		
		win.setTitle("Visualizer");
		win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		bar = new JMenuBar();
        MenuBarGenerator.generateMenu(bar);
        win.setJMenuBar(bar);
        
        JPanel panel = new JPanel(new BorderLayout());
        
        fakeLabel = new JLabel();
        panel.add(fakeLabel, BorderLayout.CENTER);
        
        
        addOverlay(background = new BackgroudOverlay());
        addOverlay(zones = new ZonesOverlay());
        addOverlay(new ServerOverlay());
        addOverlay(new HotspotOverlay());
        addOverlay(new PlayerOverlay());
        
        win.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {}
			@Override
			public void keyReleased(KeyEvent e) {}
			
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_E){
					ViewCommon.Dimentions.zoom *= 2;
				} else if(e.getKeyCode() == KeyEvent.VK_A){
					ViewCommon.Dimentions.zoom /= 2;
				} else if(e.getKeyCode() == KeyEvent.VK_R){
					State.moveAll();
				} else {
					return;
				}
				ViewCommon.needsRefresh = true;
			}
		});
        
        panel.addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				if(e.isControlDown()){
					int rot = e.getWheelRotation();
					if(rot < 0){
						ViewCommon.Dimentions.zoom *= -(2*e.getWheelRotation());
					} else {
						ViewCommon.Dimentions.zoom /= (2*e.getWheelRotation());
					}
					ViewCommon.needsRefresh = true;
				}
			}
		});
        
        panel.addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseMoved(MouseEvent e) {}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				if(moving){
					Dimentions.posx -= (e.getX() - movingoldx) / ViewCommon.Dimentions.zoom;
					if(ViewCommon.Dimentions.posx < 0){
						ViewCommon.Dimentions.posx = 0;
					}
					ViewCommon.Dimentions.posy -= (e.getY() - movingoldy) / ViewCommon.Dimentions.zoom;
					if(ViewCommon.Dimentions.posy < 0){
						ViewCommon.Dimentions.posy = 0;
					}
					movingoldx = e.getX();
					movingoldy = e.getY();
					ViewCommon.needsRefresh = true;
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
				currentMouseMode.mouseClicked(e);
			}
		});
        
        panel.addComponentListener(new ComponentListener() {
			@Override
			public void componentShown(ComponentEvent e) {}
			
			@Override
			public void componentResized(ComponentEvent e) {
				ViewCommon.needsRefresh = true;
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
				ViewCommon.Dimentions.posx = e.getValue();
				ViewCommon.needsRefresh = true;
			}
		});
        vBar.addAdjustmentListener(new AdjustmentListener() {
			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
				if(!barRefresh){
					return;
				}
				ViewCommon.Dimentions.posy = e.getValue();
				ViewCommon.needsRefresh = true;
			}
		});
        
        win.getContentPane().add(panel);
        
        win.setSize(new Dimension(800, 600));
        
        win.setMinimumSize(new Dimension(500, 500));
        
		win.setVisible(true);
		
		win.addWindowListener(new WindowListener() {
			@Override
			public void windowOpened(WindowEvent e) {}
			@Override
			public void windowIconified(WindowEvent e) {}
			@Override
			public void windowDeiconified(WindowEvent e) {}
			@Override
			public void windowDeactivated(WindowEvent e) {}
			@Override
			public void windowClosing(WindowEvent e) {
				MovementLogger.clean();
			}
			@Override
			public void windowClosed(WindowEvent e) {}
			@Override
			public void windowActivated(WindowEvent e) {}
		});
		
		ViewCommon.needsRefresh = true;
	}
	
	private static int currentIndex = 1;
	static List<AbstractOverlay> overlays = new ArrayList<AbstractOverlay>();
	static Map<String, OverlayMouseMode> mouseModes = new HashMap<String, OverlayMouseMode>();
	private static OverlayMouseMode currentMouseMode = null;
	private static void addOverlay(AbstractOverlay overlay) {
		overlays.add(overlay);
		if(overlay.getMouseModes() != null){
			for(OverlayMouseMode m : overlay.getMouseModes()){
				mouseModes.put(m.getName(), m);
			}
		}
		win.getLayeredPane().add(overlay, new Integer(currentIndex));
		currentIndex++;
	}

	public static void setMouseMode(OverlayMouseMode mode){
		currentMouseMode = mode;
	}
	
}
