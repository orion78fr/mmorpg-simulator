package springVisualizer.view;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import springVisualizer.Parameters;
import springVisualizer.State;

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
		
		tick.setHorizontalAlignment(JLabel.CENTER);
		setTick();
		
		win.setLayout(new GridLayout(0,1));
		JButton nextStep = new JButton("Next Step");
		nextStep.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				State.moveAll();
				ViewCommon.needsRefresh = true;
			}
		});
		win.add(nextStep);
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
		
		win.add(playPause);
		
		win.add(new JButton("\u25BA\u25BA"));
		
		win.add(new JLabel("Speed", JLabel.CENTER));
		
		
		JSlider speed = new JSlider(Parameters.minPlaySpeed, Parameters.maxPlaySpeed, Parameters.defaultPlaySpeed);
		
		speed.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if(t != null){
					t.setDelay(1000/speed.getValue());
				}
			}
		});
		
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
		
		win.add(new JLabel("Hotness", JLabel.CENTER));
		
		win.add(new JTextField("50"));
		
		win.add(new JCheckBox("Draw hotspots", true));
		win.add(new JCheckBox("Draw players", true));
		win.add(new JCheckBox("Draw servers", true));
		win.add(new JCheckBox("Draw map", true));
		win.add(tick);
		win.pack();
		
		win.setResizable(false);
		win.setLocation(MainWindow.win.getX() + MainWindow.win.getWidth(), MainWindow.win.getY());
		win.setVisible(true);
	}
}
