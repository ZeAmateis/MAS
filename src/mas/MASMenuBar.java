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
	private JMenu filesMenu = new JMenu(MASLang.translate("menu.file"));
	private JMenu editMenu = new JMenu(MASLang.translate("menu.edit"));

	public MASMenuBar() {
		super();

		this.initFilesMenu();
		this.initEditMenu();
	}

	private void initFilesMenu() {
		this.filesMenu.add(new JMenuItem(MASLang.translate("menu.file.new")));
		this.filesMenu.add(new JMenuItem(MASLang.translate("menu.file.open")));

		this.filesMenu.addSeparator();

		JMenuItem closeItem = new JMenuItem(MASLang.translate("menu.file.quit"));
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
