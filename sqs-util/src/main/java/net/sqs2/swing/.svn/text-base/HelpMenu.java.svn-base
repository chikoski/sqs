package net.sqs2.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import net.sqs2.browser.Browser;

public class HelpMenu extends JMenu {

	private static final long serialVersionUID = 0L;
	protected JFrame frame;

	public HelpMenu(JFrame frame, final Icon helpIcon, final URL helpSiteURL, final Icon informationIcon,
			final MessageDialogModel versionDocumentModel, final Icon aboutIcon,
			final DocumentDialogModel licenseDocumentModel) {
		super("Help");
		this.frame = frame;

		setMnemonic(KeyEvent.VK_H);

		JMenuItem helpMenuItem = new JMenuItem("Help Menu", helpIcon);
		JMenuItem licenseMenuItem = new JMenuItem("license terms...", informationIcon);
		JMenuItem aboutMenuItem = new JMenuItem("About...", aboutIcon);

		helpMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				Browser.showDocument(null, helpSiteURL);
			}
		});

		aboutMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				new DocumentDialog(getFrame(), licenseDocumentModel).setVisible(true);
			}
		});

		licenseMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				JOptionPane.showMessageDialog(getFrame(), versionDocumentModel.messages);
			}
		});
		add(helpMenuItem);
		addSeparator();
		add(aboutMenuItem);
		add(licenseMenuItem);
	}

	protected JFrame getFrame() {
		return this.frame;
	}

}
