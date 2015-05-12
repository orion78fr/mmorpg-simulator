package springVisualizer;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.Timer;

public class ControlDialog extends JDialog{
	public ControlDialog() {
		super(MainWindow.win, "Controls");
		
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
		/*Timer o = new Timer(delay, listener);
		o.*/
		this.add(new JButton("\u25BA"));
		this.add(new JButton("\u25AE\u25AE"));
		this.add(new JButton("\u25BA\u25BA"));
		this.pack();
	}

}
