package xyz.algogo.desktop.dialogs;

import javax.swing.JDialog;

import java.awt.Component;
import java.awt.Container;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.LayoutStyle.ComponentPlacement;

import xyz.algogo.desktop.AlgogoDesktop;
import xyz.algogo.desktop.utils.LanguageManager;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;

public class PreferencesDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	public PreferencesDialog(final Component component) {
		this.setTitle(LanguageManager.getString("preferences.title"));
		this.setSize(420, 346);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(AlgogoDesktop.class.getResource("/xyz/algogo/desktop/res/icons/app_icon.png")));
		this.setModalityType(ModalityType.APPLICATION_MODAL);
		this.setModal(true);
		final Map<String, String> languages = LanguageManager.getAvailableLanguages();
		final JCheckBox chckbxCheckForUpdates = new JCheckBox(LanguageManager.getString("preferences.checkforupdates"));
		chckbxCheckForUpdates.setSelected(!AlgogoDesktop.SETTINGS.updaterDoNotAutoCheckAgain);
		final JLabel lblLanguage = new JLabel(LanguageManager.getString("preferences.language"));
		final JComboBox<String> cboxLanguages = new JComboBox<String>();
		for(final String language : languages.values()) {
			cboxLanguages.addItem(language);
		}
		cboxLanguages.setSelectedItem(LanguageManager.getCurrentLanguageName());
		final JButton btnSave = new JButton(LanguageManager.getString("preferences.save"));
		btnSave.addActionListener(new ActionListener() {

			@Override
			public final void actionPerformed(final ActionEvent event) {
				boolean hasSettingsChanged = false;
				final boolean checkForUpdates = chckbxCheckForUpdates.isSelected();
				final String language = getLanguageFromName(languages, cboxLanguages.getSelectedItem().toString());
				if(checkForUpdates == AlgogoDesktop.SETTINGS.updaterDoNotAutoCheckAgain) {
					AlgogoDesktop.SETTINGS.updaterDoNotAutoCheckAgain = !checkForUpdates;
					hasSettingsChanged = true;
				}
				if(!language.equals(AlgogoDesktop.SETTINGS.customLanguage)) {
					AlgogoDesktop.SETTINGS.customLanguage = language;
					hasSettingsChanged = true;
				}
				if(hasSettingsChanged) {
					try {
						AlgogoDesktop.SETTINGS.save();
						JOptionPane.showMessageDialog(PreferencesDialog.this, LanguageManager.getString("joptionpane.settingssaved"), AlgogoDesktop.APP_NAME, JOptionPane.INFORMATION_MESSAGE);
					}
					catch(final Exception ex) {
						ex.printStackTrace();
					}
				}
				PreferencesDialog.this.dispose();
			}
			
		});
		final Container content = this.getContentPane();
		final GroupLayout groupLayout = new GroupLayout(content);
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout.createSequentialGroup().addContainerGap().addGroup(groupLayout.createParallelGroup(Alignment.LEADING).addComponent(btnSave, GroupLayout.DEFAULT_SIZE, 346, Short.MAX_VALUE).addComponent(lblLanguage).addComponent(cboxLanguages, 0, 346, Short.MAX_VALUE).addComponent(chckbxCheckForUpdates)).addContainerGap()));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.TRAILING).addGroup(groupLayout.createSequentialGroup().addContainerGap().addComponent(chckbxCheckForUpdates).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(lblLanguage).addPreferredGap(ComponentPlacement.RELATED).addComponent(cboxLanguages, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED, 196, Short.MAX_VALUE).addComponent(btnSave).addContainerGap()));
		content.setLayout(groupLayout);
		this.setLocationRelativeTo(component);
	}
	
	private final String getLanguageFromName(final Map<String, String> languages, final String name) {
		for(final Entry<String, String> entry : languages.entrySet()) {
			if(name.equals(entry.getValue())) {
				return entry.getKey();
			}
		}
		return null;
	}
	
}