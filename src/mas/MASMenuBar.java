package mas;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 * @author SCAREX
 * 
 */
public class MASMenuBar extends JMenuBar
{
	private static final long serialVersionUID = -6394631766691329465L;
	private JMenu filesMenu = new JMenu("Files");
	private JMenu editMenu = new JMenu("Edit");

	public MASMenuBar() {
		super();

		this.initFilesMenu();
		this.initEditMenu();
	}

	private void initFilesMenu() {
		this.filesMenu.add(new JMenuItem("New"));
		this.filesMenu.add(new JMenuItem("Open"));

		this.filesMenu.addSeparator();

		JMenuItem closeItem = new JMenuItem("Close");
		closeItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent paramActionEvent) {
				MAS.getMAS().shutDown();
			}
		});
		this.filesMenu.add(closeItem);

		this.add(this.filesMenu);
		this.add(this.editMenu);
	}

	private void initEditMenu() {

	}
}
