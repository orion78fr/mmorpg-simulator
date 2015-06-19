package springVisualizer.view;

import javax.swing.Icon;
import javax.swing.JLabel;

public class MultiLineJLabel extends JLabel {
	private static final long serialVersionUID = 1L;

	@Override
	public void setText(String text) {
		super.setText("<html>" + text.replace("\n", "<br>"));
	}

	public MultiLineJLabel() {
		super();
	}

	public MultiLineJLabel(Icon image, int horizontalAlignment) {
		super(image, horizontalAlignment);
	}

	public MultiLineJLabel(Icon image) {
		super(image);
	}

	public MultiLineJLabel(String text, Icon icon, int horizontalAlignment) {
		super(text, icon, horizontalAlignment);
	}

	public MultiLineJLabel(String text, int horizontalAlignment) {
		super(text, horizontalAlignment);
	}

	public MultiLineJLabel(String text) {
		super(text);
	}
}
