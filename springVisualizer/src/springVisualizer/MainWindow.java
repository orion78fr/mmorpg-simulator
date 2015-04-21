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
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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

	private static void refreshImage() {
        AffineTransform at = new AffineTransform();
        at.scale(zoom, zoom);
        AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        ImageIcon icon = new ImageIcon(op.filter(fond, null));
        imagelabel.setIcon(icon);
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
        panel.add(imagelabel, BorderLayout.NORTH);
        
        panel.addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				if((e.getModifiersEx() & InputEvent.CTRL_DOWN_MASK) != 0){
					System.out.println(e.getWheelRotation());
					zoom -= e.getWheelRotation();
					if(zoom <= 0){
						zoom = 1;
					}
					refreshImage();
				}
			}
		});
		
        JScrollPane scroll = new JScrollPane(panel);
        refreshImage();
        
        win.getContentPane().add(scroll);
        
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
                                        refreshImage();
                                } catch (Exception ex) {
                                        actionPerformed(e);
                                }
                        }
                }
        });
        menu.add(item);
        
        bar.add(menu);
	}

}
