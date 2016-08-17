package xyz.algogo.desktop.dialogs;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;

import xyz.algogo.desktop.AlgogoDesktop;
import xyz.algogo.desktop.frames.EditorFrame;
import xyz.algogo.desktop.utils.LanguageManager;

public class OptionsDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	public OptionsDialog(final Component component, final EditorFrame editor) {
		final String algoTitle = editor.getAlgorithm().getTitle();
		final String algoAuthor = editor.getAlgorithm().getAuthor();
		this.setTitle(LanguageManager.getString("options.title"));
		this.setIconImages(AlgogoDesktop.ICONS);
		this.setSize(476, 198);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setModalityType(ModalityType.APPLICATION_MODAL);
		this.setModal(true);
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
				boolean changed = false;
				final String title = txtfldTitle.getText();
				final String author = txtfldAuthor.getText();
				if(title.length() == 0 || author.length() == 0) {
					JOptionPane.showMessageDialog(OptionsDialog.this, LanguageManager.getString("joptionpane.fillfields"), LanguageManager.getString("joptionpane.error"), JOptionPane.ERROR_MESSAGE);
					return;
				}
				editor.addAlgorithmToStack();
				if(!algoTitle.equals(title)) {
					editor.getAlgorithm().setTitle(title);
					changed = true;
				}
				if(!algoAuthor.equals(author)) {
					editor.getAlgorithm().setAuthor(author);
					changed = true;
				}
				if(changed) {
					editor.algorithmChanged(true);
				}
				else {
					editor.popFromStack();
				}
				OptionsDialog.this.dispose();
			}

		});
		final Container content = this.getContentPane();
		final GroupLayout groupLayout = new GroupLayout(content);
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout.createSequentialGroup().addContainerGap().addGroup(groupLayout.createParallelGroup(Alignment.LEADING).addComponent(lblAlgorithmOptions, GroupLayout.DEFAULT_SIZE, 440, Short.MAX_VALUE).addGroup(groupLayout.createSequentialGroup().addGroup(groupLayout.createParallelGroup(Alignment.LEADING).addComponent(lblTitle).addComponent(lblAuthor)).addPreferredGap(ComponentPlacement.RELATED).addGroup(groupLayout.createParallelGroup(Alignment.LEADING).addComponent(txtfldTitle, GroupLayout.DEFAULT_SIZE, 396, Short.MAX_VALUE).addComponent(txtfldAuthor, GroupLayout.DEFAULT_SIZE, 396, Short.MAX_VALUE))).addComponent(btnSave, GroupLayout.DEFAULT_SIZE, 440, Short.MAX_VALUE)).addContainerGap()));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout.createSequentialGroup().addContainerGap().addComponent(lblAlgorithmOptions).addPreferredGap(ComponentPlacement.UNRELATED).addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(txtfldTitle, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(lblTitle)).addPreferredGap(ComponentPlacement.RELATED).addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(txtfldAuthor, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(lblAuthor)).addPreferredGap(ComponentPlacement.RELATED, 18, Short.MAX_VALUE).addComponent(btnSave).addContainerGap()));
		content.setLayout(groupLayout);
		this.setLocationRelativeTo(component);
	}

}