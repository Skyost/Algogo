package fr.skyost.algo.desktop.dialogs;

import fr.skyost.algo.core.AlgogoCore;
import fr.skyost.algo.desktop.AlgogoDesktop;
import fr.skyost.algo.desktop.utils.JLabelLink;
import fr.skyost.algo.desktop.utils.LanguageManager;
import fr.skyost.algo.desktop.utils.Utils;

import javax.swing.JLabel;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

public class AboutDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	public AboutDialog() {
		try {
			final String authors = Utils.join(' ', AlgogoDesktop.APP_AUTHORS);
			this.setTitle(String.format(LanguageManager.getString("about.title"), AlgogoDesktop.APP_NAME, AlgogoDesktop.APP_VERSION, authors));
			this.setSize(430, 406);
			this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			this.setModalityType(ModalityType.APPLICATION_MODAL);
			this.setModal(true);
			this.setResizable(false);
			this.setLocationRelativeTo(null);
			final JLabel lblAppName = new JLabel(" " + AlgogoDesktop.APP_NAME);
			lblAppName.setHorizontalAlignment(SwingConstants.CENTER);
			lblAppName.setFont(lblAppName.getFont().deriveFont(Font.BOLD).deriveFont(30.0f));
			lblAppName.setForeground(Color.decode("#3b3b3b"));
			lblAppName.setIcon(new ImageIcon(AlgogoDesktop.class.getResource("/fr/skyost/algo/desktop/res/icons/app_icon.png")));
			final JLabelLink lblAppDesktopInfos = new JLabelLink(String.format(LanguageManager.getString("about.desktopinfos"), AlgogoDesktop.APP_NAME, AlgogoDesktop.APP_VERSION, authors), new URL(AlgogoDesktop.APP_WEBSITE));
			lblAppDesktopInfos.setHorizontalAlignment(SwingConstants.CENTER);
			final JLabelLink lblAppCoreInfos = new JLabelLink(String.format(LanguageManager.getString("about.coreinfos"), AlgogoCore.APP_NAME, AlgogoCore.APP_VERSION, Utils.join(' ', AlgogoCore.APP_AUTHORS)), new URL(AlgogoCore.APP_WEBSITE));
			lblAppCoreInfos.setHorizontalAlignment(SwingConstants.CENTER);
			final JPanel panel = new JPanel();
			final JLabelLink lblAnimation = new JLabelLink(new ImageIcon(AlgogoDesktop.class.getResource("/fr/skyost/algo/desktop/res/images/about_animation.gif")), new URL(AlgogoDesktop.APP_WEBSITE));
			lblAnimation.setHorizontalAlignment(SwingConstants.CENTER);
			lblAnimation.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			panel.add(lblAnimation);
			final JLabel lblBuiltUsing = new JLabel(String.format(LanguageManager.getString("about.builtusing"), "minimal-json v0.9.2", "Heartbeat v0.1", "JTattoo v1.6.11"));
			lblBuiltUsing.setHorizontalAlignment(SwingConstants.CENTER);
			final Container content = this.getContentPane();
			final GroupLayout groupLayout = new GroupLayout(content);
			groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout.createSequentialGroup().addContainerGap().addComponent(lblAppDesktopInfos, GroupLayout.DEFAULT_SIZE, 352, Short.MAX_VALUE).addContainerGap()).addGroup(groupLayout.createSequentialGroup().addComponent(lblAppName, GroupLayout.DEFAULT_SIZE, 372, Short.MAX_VALUE).addGap(0)).addGroup(groupLayout.createSequentialGroup().addContainerGap().addComponent(lblAppCoreInfos, GroupLayout.DEFAULT_SIZE, 352, Short.MAX_VALUE).addContainerGap()).addGroup(groupLayout.createSequentialGroup().addContainerGap().addComponent(panel, GroupLayout.DEFAULT_SIZE, 352, Short.MAX_VALUE).addContainerGap()).addGroup(groupLayout.createSequentialGroup().addContainerGap().addComponent(lblBuiltUsing, GroupLayout.DEFAULT_SIZE, 352, Short.MAX_VALUE).addContainerGap()));
			groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout.createSequentialGroup().addContainerGap().addComponent(lblAppName).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(lblAppDesktopInfos).addPreferredGap(ComponentPlacement.RELATED).addComponent(lblAppCoreInfos).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(panel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED).addComponent(lblBuiltUsing).addContainerGap(34, Short.MAX_VALUE)));
			content.setLayout(groupLayout);
		}
		catch(final Exception ex) {
			ErrorDialog.errorMessage(this, ex);
			ex.printStackTrace();
		}
	}
	
}