package springVisualizer.view;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import springCommon.Parameters;
import springVisualizer.State;
import springVisualizer.view.overlay.AbstractOverlay;
import springVisualizer.view.overlay.OverlayMouseMode;

public class ControlDialog{
	private static Timer t = null;
	private static boolean playing = false;
	private static JLabel tick = new JLabel();
	private static JDialog win;
	
	public static void refresh(){
		setTick();
	}
	
	private static void setTick(){
		tick.setText("Tick n° " + State.tickNumber);
	}
	
	public static void start() {
		win = new JDialog(MainWindow.win, "Controls");
		
		win.getContentPane().setLayout(new BoxLayout(win.getContentPane(), BoxLayout.Y_AXIS));
		
		// Tick number
		tick.setHorizontalAlignment(JLabel.CENTER);
		tick.setAlignmentX(Component.CENTER_ALIGNMENT);
		setTick();
		win.add(tick);
		
		// Button bar
		JPanel buttons = new JPanel(new GridLayout(1,0));
		{
			JButton nextStep = new JButton("Next");
			nextStep.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					State.moveAll();
					ViewCommon.needsRefresh = true;
				}
			});
			buttons.add(nextStep);
			
			JButton playPause = new JButton("\u25BA");
			playPause.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					playing = !playing;
					setText();
				}
				private void setText(){
					if(playing){
						playPause.setText("\u25AE\u25AE"); // Pause ||
					} else {
						playPause.setText("\u25BA"); // Play |>
					}
				}
			});
			buttons.add(playPause);
			
			JButton ff = new JButton("\u25BA\u25BA");
			ff.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String num = JOptionPane.showInputDialog(win, "How many steps?", "Fast Forward", JOptionPane.QUESTION_MESSAGE);
	            	if(num == null){
	            		return;
	            	}
	            	try {
	            		int numSteps = Integer.parseInt(num);
	            		if(numSteps < 0){
	            			JOptionPane.showMessageDialog(win, "The input is negative", "Error", JOptionPane.ERROR_MESSAGE);
	            			return;
	            		}
	            		for(int i = 0; i < numSteps; i++){
	            			State.moveAll();
	    					ViewCommon.needsRefresh = true;
	            		}
	            	} catch (Exception ex) {
	            		JOptionPane.showMessageDialog(win, "The input is not a number", "Error", JOptionPane.ERROR_MESSAGE);
	            		ex.printStackTrace();
	            		
	            	}
				}
			});
			buttons.add(ff);
		}
		buttons.setAlignmentX(Component.CENTER_ALIGNMENT);
		win.add(buttons);
		
		JLabel tmpLabel = new JLabel("Speed", JLabel.CENTER);
		tmpLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		win.add(tmpLabel);
		
		
		JSlider speed = new JSlider(Parameters.minPlaySpeed, Parameters.maxPlaySpeed, Parameters.defaultPlaySpeed);
		
		speed.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if(t != null){
					t.setDelay(1000/speed.getValue());
				}
			}
		});
		speed.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		t = new Timer(1000/Parameters.defaultPlaySpeed, new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(playing){
					State.moveAll();
					ViewCommon.needsRefresh = true;
				}
			}
		});
		t.start();
		
		win.add(speed);
		
		tmpLabel = new JLabel("Hotness", JLabel.CENTER);
		tmpLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		win.add(tmpLabel);
		
		win.add(new JTextField("50"));
		
		JPanel overlays = new JPanel(new GridLayout(0, 1));
		for(AbstractOverlay o : MainWindow.overlays){
			JCheckBox chkb = new JCheckBox("Show " + o.getDisplayName(), o.isShown());
			chkb.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					o.setShown(chkb.isSelected());
					ViewCommon.needsRefresh = true;
				}
			});
			overlays.add(chkb);
		}
		overlays.setAlignmentX(Component.CENTER_ALIGNMENT);
		win.add(overlays);
		
		JComboBox<String> cbMouseMode = new JComboBox<String>();
		MultiLineJLabel mouseModeDesc = new MultiLineJLabel("");
		mouseModeDesc.setAlignmentX(Component.CENTER_ALIGNMENT);
		for(String mm : MainWindow.mouseModes.keySet()){
			cbMouseMode.addItem(mm);
		}
		cbMouseMode.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<String> cb = (JComboBox<String>)e.getSource();
				String elem = (String)cb.getSelectedItem();
				
				OverlayMouseMode m = MainWindow.mouseModes.get(elem);
				
				MainWindow.setMouseMode(m);
				mouseModeDesc.setText(m.getDescription());
				win.pack();
			}
		});
		cbMouseMode.setSelectedIndex(0);
		win.add(cbMouseMode);
		win.add(mouseModeDesc);
		
		win.addComponentListener(new ComponentListener() {
			@Override
			public void componentShown(ComponentEvent e) {}
			@Override
			public void componentResized(ComponentEvent e) {}
			@Override
			public void componentMoved(ComponentEvent e) {}
			@Override
			public void componentHidden(ComponentEvent e) {
				// Close the main window when closing the dialog
				MainWindow.win.dispatchEvent(new WindowEvent(MainWindow.win, WindowEvent.WINDOW_CLOSING));
			}
		});
		
		win.pack();
		win.setResizable(false);
		win.setLocation(MainWindow.win.getX() + MainWindow.win.getWidth(), MainWindow.win.getY());
		win.setVisible(true);
	}
}
