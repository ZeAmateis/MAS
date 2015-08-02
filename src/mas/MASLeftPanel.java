package mas;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * @author SCAREX
 * 
 */
public class MASLeftPanel extends JPanel
{
	private static final long serialVersionUID = 7629643125798843681L;

	public MASLeftPanel() {
		super();

		this.initGui();
	}

	private void initGui() {
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.add(new JButton("First test button"));
		this.add(new JButton("Second test button"));
	}
}
