package xyz.algogo.desktop.dialogs;

import xyz.algogo.core.AlgogoCore;
import xyz.algogo.desktop.AlgogoDesktop;
import xyz.algogo.desktop.utils.JLabelLink;
import xyz.algogo.desktop.utils.LanguageManager;
import xyz.algogo.desktop.utils.Utils;

import javax.swing.JLabel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import fr.skyost.heartbeat.Heartbeat;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;

public class AboutDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	public AboutDialog(final Component parent) {
		try {
			final String authors = Utils.join(" ", AlgogoDesktop.APP_AUTHORS);
			this.setTitle(LanguageManager.getString("about.title", AlgogoDesktop.APP_NAME, AlgogoDesktop.APP_VERSION, authors));
			this.setIconImages(AlgogoDesktop.ICONS);
			this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			this.setModalityType(ModalityType.APPLICATION_MODAL);
			this.setModal(true);
			this.setResizable(false);
			final JLabel lblAppName = new JLabel(" " + AlgogoDesktop.APP_NAME);
			lblAppName.setHorizontalAlignment(SwingConstants.CENTER);
			lblAppName.setFont(lblAppName.getFont().deriveFont(Font.BOLD).deriveFont(30.0f));
			lblAppName.setForeground(Color.decode("#3b3b3b"));
			lblAppName.setIcon(new ImageIcon(AlgogoDesktop.ICONS.get(2)));
			final JLabelLink lblAppDesktopInfos = new JLabelLink(LanguageManager.getString("about.desktopinfos", AlgogoDesktop.APP_NAME, AlgogoDesktop.APP_VERSION, authors), new URL(AlgogoDesktop.APP_WEBSITE));
			lblAppDesktopInfos.setHorizontalAlignment(SwingConstants.CENTER);
			final JLabelLink lblAppCoreInfos = new JLabelLink(LanguageManager.getString("about.coreinfos", AlgogoCore.APP_NAME, AlgogoCore.APP_VERSION, Utils.join(" ", AlgogoCore.APP_AUTHORS)), new URL(AlgogoCore.APP_WEBSITE));
			lblAppCoreInfos.setHorizontalAlignment(SwingConstants.CENTER);
			final JPanel panel = new JPanel();
			final JLabelLink lblAnimation = new JLabelLink(new ImageIcon(AlgogoDesktop.class.getResource("/xyz/algogo/desktop/res/images/about_animation.gif")), new URL(AlgogoDesktop.APP_WEBSITE));
			lblAnimation.setHorizontalAlignment(SwingConstants.CENTER);
			lblAnimation.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			panel.add(lblAnimation);
			final JLabel lblBuiltUsing = new JLabel(LanguageManager.getString("about.builtusing", "minimal-json v0.9.2", "ComponentBorder", "JTattoo v1.6.11", Heartbeat.APP_NAME + " v" + Heartbeat.APP_VERSION));
			lblBuiltUsing.setHorizontalAlignment(SwingConstants.CENTER);
			final Container content = this.getContentPane();
			final JButton btnDonate = new JButton(LanguageManager.getString("about.donate"));
			btnDonate.setFont(btnDonate.getFont().deriveFont(Font.BOLD));
			btnDonate.setIcon(new ImageIcon(AlgogoDesktop.class.getResource("/xyz/algogo/desktop/res/icons/btn_donate.png")));
			btnDonate.addActionListener(new ActionListener() {

				@Override
				public final void actionPerformed(final ActionEvent event) {
					if(Desktop.isDesktopSupported()) {
						try {
							Desktop.getDesktop().browse(new URL("https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=4KYYXZTK4HQME").toURI());
						}
						catch(final Exception ex) {
							ErrorDialog.errorMessage(AboutDialog.this, ex);
						}
					}
				}

			});
			final GroupLayout groupLayout = new GroupLayout(content);
			groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout.createSequentialGroup().addContainerGap().addComponent(lblAppDesktopInfos, GroupLayout.DEFAULT_SIZE, 404, Short.MAX_VALUE).addContainerGap()).addGroup(groupLayout.createSequentialGroup().addComponent(lblAppName, GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE).addGap(0)).addGroup(groupLayout.createSequentialGroup().addContainerGap().addComponent(lblAppCoreInfos, GroupLayout.DEFAULT_SIZE, 404, Short.MAX_VALUE).addContainerGap()).addGroup(groupLayout.createSequentialGroup().addContainerGap().addComponent(panel, GroupLayout.DEFAULT_SIZE, 404, Short.MAX_VALUE).addContainerGap()).addGroup(groupLayout.createSequentialGroup().addContainerGap().addComponent(lblBuiltUsing, GroupLayout.DEFAULT_SIZE, 404, Short.MAX_VALUE).addContainerGap()).addGroup(groupLayout.createSequentialGroup().addContainerGap().addComponent(btnDonate, GroupLayout.DEFAULT_SIZE, 404, Short.MAX_VALUE).addContainerGap()));
			groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout.createSequentialGroup().addContainerGap().addComponent(lblAppName).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(lblAppDesktopInfos, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED).addComponent(lblAppCoreInfos, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(panel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED).addComponent(lblBuiltUsing).addGap(18).addComponent(btnDonate, GroupLayout.DEFAULT_SIZE, 55, Short.MAX_VALUE).addContainerGap()));
			content.setLayout(groupLayout);
			this.pack();
			this.setLocationRelativeTo(parent);
		}
		catch(final Exception ex) {
			ErrorDialog.errorMessage(this, ex);
		}
	}

}