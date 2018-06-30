package xyz.algogo.desktop.dialog;

import org.kordamp.ikonli.materialdesign.MaterialDesign;
import org.kordamp.ikonli.swing.FontIcon;
import xyz.algogo.desktop.AlgogoDesktop;
import xyz.algogo.desktop.AppLanguage;
import xyz.algogo.desktop.editor.EditorFrame;
import xyz.algogo.desktop.utils.Utils;

import javax.swing.*;
import java.awt.*;

/**
 * Represents an about dialog which shows information about the software and its author.
 */

public class AboutDialog extends JDialog {

	/**
	 * Creates a new about dialog.
	 *
	 * @param editor The editor.
	 */

	public AboutDialog(final EditorFrame editor) {
		super(editor);
		final AppLanguage appLanguage = editor.getAppLanguage();

		final JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));

		final JPanel app = new JPanel(new FlowLayout());
		app.setAlignmentX(Component.CENTER_ALIGNMENT);

		final JLabel icon = new JLabel(new ImageIcon(AlgogoDesktop.ICONS.get(1)));
		app.add(icon);

		app.add(Box.createRigidArea(new Dimension(5, 0)));

		final JLabel title = new JLabel(appLanguage.getString(AlgogoDesktop.APP_NAME + " " + AlgogoDesktop.APP_VERSION), SwingConstants.CENTER);
		title.setFont(title.getFont().deriveFont(24f).deriveFont(Font.BOLD));
		app.add(title);

		panel.add(app);
		panel.add(Box.createRigidArea(new Dimension(0, 10)));

		final JLabel message = new JLabel(appLanguage.getString("aboutDialog.message"), SwingConstants.CENTER);
		message.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel.add(message);
		panel.add(Box.createRigidArea(new Dimension(0, 10)));

		final JLabel credits = new JLabel(appLanguage.getString("aboutDialog.credits"));
		credits.setAlignmentX(Component.CENTER_ALIGNMENT);
		credits.setFont(credits.getFont().deriveFont(11f).deriveFont(Font.ITALIC));
		panel.add(credits);
		panel.add(Box.createRigidArea(new Dimension(0, 20)));

		final JPanel buttons = new JPanel(new GridLayout());
		buttons.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

		final JButton projectWebsite = new JButton(appLanguage.getString("aboutDialog.button.projectWebsite"));
		projectWebsite.setIcon(FontIcon.of(MaterialDesign.MDI_HEART));
		projectWebsite.addActionListener(actionEvent -> Utils.visitIfPossible("https://www.algogo.xyz"));
		buttons.add(projectWebsite);

		final JButton skyostWebsite = new JButton(appLanguage.getString("aboutDialog.button.skyostWebsite"));
		skyostWebsite.setIcon(FontIcon.of(MaterialDesign.MDI_HOME));
		skyostWebsite.addActionListener(actionEvent -> Utils.visitIfPossible("https://www.skyost.eu"));
		buttons.add(skyostWebsite);
		
		final JButton license = new JButton(appLanguage.getString("aboutDialog.button.license"));
		license.setIcon(FontIcon.of(MaterialDesign.MDI_FILE_DOCUMENT));
		license.addActionListener(actionEvent -> Utils.visitIfPossible("https://choosealicense.com/licenses/gpl-3.0/"));
		buttons.add(license);

		panel.add(buttons);

		final JPanel footer = new JPanel(new BorderLayout());
		footer.setBorder(buttons.getBorder());

		final JButton close = new JButton(appLanguage.getString("aboutDialog.button.close"));
		close.addActionListener(actionEvent -> this.dispose());
		footer.add(close, BorderLayout.CENTER);

		panel.add(footer);

		this.setContentPane(panel);
		this.setTitle(appLanguage.getString("aboutDialog.title"));
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setIconImages(AlgogoDesktop.ICONS);
		this.setResizable(false);
		this.setModal(true);
		this.pack();
		this.setLocationRelativeTo(null);
	}

}