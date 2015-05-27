package fr.skyost.algo.desktop.dialogs;

import java.awt.Container;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.SwingConstants;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTextField;
import javax.swing.JButton;

import fr.skyost.algo.desktop.AlgogoDesktop;
import fr.skyost.algo.desktop.frames.EditorFrame;
import fr.skyost.algo.desktop.utils.LanguageManager;

public class OptionsDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	public OptionsDialog() {
		final String algoTitle = EditorFrame.algorithm.getTitle();
		final String algoAuthor = EditorFrame.algorithm.getAuthor();
		this.setTitle(LanguageManager.getString("options.title"));
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(AlgogoDesktop.class.getResource("/fr/skyost/algo/desktop/res/icons/app_icon.png")));
		this.setSize(476, 198);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setModalityType(ModalityType.APPLICATION_MODAL);
		this.setModal(true);
		this.setLocationRelativeTo(null);
		final JLabel lblAlgorithmOptions = new JLabel(LanguageManager.getString("options.algorithm.options"));
		lblAlgorithmOptions.setHorizontalAlignment(SwingConstants.CENTER);
		lblAlgorithmOptions.setFont(lblAlgorithmOptions.getFont().deriveFont(32.0f));
		final JLabel lblTitle = new JLabel(LanguageManager.getString("options.algorithm.title"));
		final JTextField txtfldTitle = new JTextField(algoTitle == null ? "" : algoTitle);
		txtfldTitle.setColumns(10);
		final JLabel lblAuthor = new JLabel(LanguageManager.getString("options.algorithm.author"));
		final JTextField txtfldAuthor = new JTextField(algoAuthor == null ? "" : algoAuthor);
		txtfldAuthor.setColumns(10);
		final JButton btnSave = new JButton(LanguageManager.getString("options.save"));
		btnSave.addActionListener(new ActionListener() {

			@Override
			public final void actionPerformed(final ActionEvent event) {
				final String title = txtfldTitle.getText();
				if(!algoTitle.equals(title)) {
					EditorFrame.algorithm.setTitle(title);
				}
				final String author = txtfldAuthor.getText();
				if(!algoAuthor.equals(author)) {
					EditorFrame.algorithm.setAuthor(author);
				}
				OptionsDialog.this.dispose();
			}

		});
		final Container content = this.getContentPane();
		final GroupLayout groupLayout = new GroupLayout(content);
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout.createSequentialGroup().addContainerGap().addGroup(groupLayout.createParallelGroup(Alignment.LEADING).addComponent(lblAlgorithmOptions, GroupLayout.DEFAULT_SIZE, 440, Short.MAX_VALUE).addGroup(groupLayout.createSequentialGroup().addGroup(groupLayout.createParallelGroup(Alignment.LEADING).addComponent(lblTitle).addComponent(lblAuthor)).addPreferredGap(ComponentPlacement.RELATED).addGroup(groupLayout.createParallelGroup(Alignment.LEADING).addComponent(txtfldTitle, GroupLayout.DEFAULT_SIZE, 396, Short.MAX_VALUE).addComponent(txtfldAuthor, GroupLayout.DEFAULT_SIZE, 396, Short.MAX_VALUE))).addComponent(btnSave, GroupLayout.DEFAULT_SIZE, 440, Short.MAX_VALUE)).addContainerGap()));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout.createSequentialGroup().addContainerGap().addComponent(lblAlgorithmOptions).addPreferredGap(ComponentPlacement.UNRELATED).addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(txtfldTitle, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(lblTitle)).addPreferredGap(ComponentPlacement.RELATED).addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(txtfldAuthor, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(lblAuthor)).addPreferredGap(ComponentPlacement.RELATED, 18, Short.MAX_VALUE).addComponent(btnSave).addContainerGap()));
		content.setLayout(groupLayout);
	}

}