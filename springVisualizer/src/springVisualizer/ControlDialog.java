package springVisualizer;

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
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ControlDialog extends JDialog{
	private static final long serialVersionUID = 1L;
	private Timer t = null;
	private boolean playing = false;
	private JLabel tick = new JLabel();
	
	private void setTick(){
		tick.setText("Tick n° " + State.tickNumber);
	}
	
	public ControlDialog() {
		super(MainWindow.win, "Controls");
		
		tick.setHorizontalAlignment(JLabel.CENTER);
		setTick();
		
		this.setLayout(new GridLayout(0,1));
		JButton nextStep = new JButton("Next Step");
		nextStep.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				State.moveAllBetweenHotspots();
				MainWindow.refresh();
			}
		});
		this.add(nextStep);
		this.addComponentListener(new ComponentListener() {
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
					playPause.setText("\u25AE\u25AE");
				} else {
					playPause.setText("\u25BA");
				}
			}
		});
		
		this.add(playPause);
		
		this.add(new JButton("\u25BA\u25BA"));
		
		this.add(new JLabel("Speed", JLabel.CENTER));
		
		
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
				setTick();
				if(playing){
					State.moveAllBetweenHotspots();
					MainWindow.refresh();
				}
			}
		});
		t.start();
		
		this.add(speed);
		this.add(new JCheckBox("Draw hotspots", true));
		this.add(new JCheckBox("Draw players", true));
		this.add(new JCheckBox("Draw servers", true));
		this.add(new JCheckBox("Draw map", true));
		this.add(tick);
		this.pack();
	}

}
