package springVisualizer.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import springVisualizer.State;

public class MenuBarGenerator {
	/**
	 * Generate the menu of the main window
	 * @param bar The JMenuBar of the main window
	 */
	public static void generateMenu(JMenuBar bar) {
		JMenu menu;
        JMenuItem item;

        menu = new JMenu("File");

        item = new JMenuItem("Load background image");
        item.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                        JFileChooser fc = new JFileChooser(new File("").getAbsolutePath());
                        int ret = fc.showOpenDialog(MainWindow.win);
                        if (ret == JFileChooser.APPROVE_OPTION) {
                        	if(!fc.getSelectedFile().isFile()){
                        		JOptionPane.showMessageDialog(MainWindow.win, "This is not a file", "Error", JOptionPane.ERROR_MESSAGE);
                        	}
                            try {
                            	MainWindow.background.setImage(ImageIO.read(fc.getSelectedFile()));
                            	
                                ViewCommon.needsRefresh = true;
                            } catch (Exception ex) {
                            		JOptionPane.showMessageDialog(MainWindow.win, "The image file is invalid", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                }
        });
        menu.add(item);
        
        item = new JMenuItem("Change Seed");
        item.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                	String num = JOptionPane.showInputDialog(MainWindow.win, "How many players?", "Add players", JOptionPane.QUESTION_MESSAGE);
                	if(num == null){
                		return;
                	}
                	try {
                		long seed = Long.parseLong(num);
                		State.r.setSeed(seed);
                	} catch (Exception ex) {
                		JOptionPane.showMessageDialog(MainWindow.win, "The input is not a number", "Error", JOptionPane.ERROR_MESSAGE);
                		ex.printStackTrace();
                		
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
            	String num = JOptionPane.showInputDialog(MainWindow.win, "How many players?", "Add players", JOptionPane.QUESTION_MESSAGE);
            	if(num == null){
            		return;
            	}
            	try {
            		int numPlayers = Integer.parseInt(num);
            		if(numPlayers < 0){
            			JOptionPane.showMessageDialog(MainWindow.win, "The input is negative", "Error", JOptionPane.ERROR_MESSAGE);
            			return;
            		}
            		State.addHaltonPlayers(numPlayers);
            		ViewCommon.needsRefresh = true;
            	} catch (Exception ex) {
            		JOptionPane.showMessageDialog(MainWindow.win, "The input is not a number", "Error", JOptionPane.ERROR_MESSAGE);
            		ex.printStackTrace();
            		
            	}
            }
        });
        menu.add(item);
        
        item = new JMenuItem("Remove random players");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	String num = JOptionPane.showInputDialog(MainWindow.win, "How many players?", "Add players", JOptionPane.QUESTION_MESSAGE);
            	if(num == null){
            		return;
            	}
            	try {
            		int numPlayers = Integer.parseInt(num);
            		if(numPlayers < 0){
            			JOptionPane.showMessageDialog(MainWindow.win, "The input is negative", "Error", JOptionPane.ERROR_MESSAGE);
            			return;
            		}
            		State.removeRandomPlayers(numPlayers);
            		ViewCommon.needsRefresh = true;
            	} catch (Exception ex) {
            		JOptionPane.showMessageDialog(MainWindow.win, "The input is not a number", "Error", JOptionPane.ERROR_MESSAGE);
            	}
            }
        });
        menu.add(item);
        
        item = new JMenuItem("Export platform");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	JFileChooser fc = new JFileChooser(new File("").getAbsolutePath());
                int ret = fc.showSaveDialog(MainWindow.win);
                if (ret == JFileChooser.APPROVE_OPTION) {
                	if(fc.getSelectedFile().exists() && !fc.getSelectedFile().isFile()){
                		JOptionPane.showMessageDialog(MainWindow.win, "This is not a file", "Error", JOptionPane.ERROR_MESSAGE);
                	} else {
	                    try {
	                            Writer w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fc.getSelectedFile()), "utf-8"));
	                            if(State.exportPlatform(w)){
	                            	JOptionPane.showMessageDialog(MainWindow.win, "Export successful to " + fc.getSelectedFile(), "Export", JOptionPane.INFORMATION_MESSAGE);
	                            } else {
	                            	JOptionPane.showMessageDialog(MainWindow.win, "Something went wrong during export", "Error", JOptionPane.ERROR_MESSAGE);
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
