package xyz.algogo.desktop.frames.listeners.menu;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.net.URL;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

import xyz.algogo.desktop.AlgogoDesktop;
import xyz.algogo.desktop.frames.EditorFrame;
import xyz.algogo.desktop.frames.listeners.AlgorithmEditorActionListener;
import xyz.algogo.desktop.utils.GithubUpdater;
import xyz.algogo.desktop.utils.JLabelLink;
import xyz.algogo.desktop.utils.LanguageManager;

public class HelpCheckForUpdatesListener extends AlgorithmEditorActionListener {
	
	public HelpCheckForUpdatesListener(final EditorFrame editor) {
		super(editor);
	}

	@Override
	public final void actionPerformed(final ActionEvent event, final EditorFrame editor) {
		new GithubUpdater(AlgogoDesktop.APP_VERSION, editor.new DefaultGithubUpdater() {
			
			private final JDialog dialog = new JDialog();
			
			@Override
			public final void updaterStarted() {
				dialog.setTitle(LanguageManager.getString("wait.title"));
				dialog.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				dialog.setLocationRelativeTo(editor);
				dialog.setAlwaysOnTop(true);
				dialog.setResizable(false);
				final JLabel message = new JLabel(LanguageManager.getString("wait.message"));
				message.setHorizontalAlignment(SwingConstants.CENTER);
				message.setFont(message.getFont().deriveFont(Font.ITALIC));
				dialog.add(message, BorderLayout.CENTER);
				dialog.pack();
				dialog.setSize(dialog.getWidth() + 50, dialog.getHeight() + 30);
				dialog.setVisible(true);
			}
			
			@Override
			public final void updaterResponse(final String response) {
				dialog.dispose();
			}				

			@Override
			public final void updaterUpdateAvailable(final String localVersion, final String remoteVersion) {
				try {
					JOptionPane.showMessageDialog(editor, new Object[]{new JLabelLink(LanguageManager.getString("joptionpane.updateavailable.message", remoteVersion, AlgogoDesktop.APP_WEBSITE), new URL(AlgogoDesktop.APP_WEBSITE))}, LanguageManager.getString("joptionpane.updateavailable.title"), JOptionPane.INFORMATION_MESSAGE);
				}
				catch(final Exception ex) {
					ex.printStackTrace();
				}
			}

			@Override
			public final void updaterNoUpdate(final String localVersion, final String remoteVersion) {
				JOptionPane.showMessageDialog(editor, LanguageManager.getString("joptionpane.updatenotavailable.message"), LanguageManager.getString("joptionpane.updatenotavailable.title"), JOptionPane.INFORMATION_MESSAGE);
			}

		}).start();
	}

}