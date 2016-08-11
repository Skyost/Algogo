package xyz.algogo.desktop.dialogs;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

import fr.skyost.heartbeat.Heartbeat;
import fr.skyost.heartbeat.Heartbeat.Function;
import xyz.algogo.desktop.AlgogoDesktop;
import xyz.algogo.desktop.utils.LanguageManager;
import xyz.algogo.desktop.utils.Utils;

import javax.swing.JList;
import javax.swing.JScrollPane;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PickerDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	
	public PickerDialog(final Component parent, final JTextField textField) {
		try {
			this.setTitle(LanguageManager.getString("picker.title"));
			this.setIconImages(AlgogoDesktop.ICONS);
			this.setSize(260, 318);
			this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			this.setModalityType(ModalityType.TOOLKIT_MODAL);
			this.setModal(true);
			final Heartbeat heartbeat = new Heartbeat();
			final PickerModel model = new PickerModel(heartbeat.getFunctions());
			final JList<Function> list = new JList<Function>();
			list.setModel(model);
			list.setCellRenderer(new DefaultListCellRenderer() {

				private static final long serialVersionUID = 1L;
				
				@Override
				public final Component getListCellRendererComponent(final JList<?> list, final Object value, final int index, final boolean isSelected, final boolean cellHasFocus) {
					final Function function = (Function)value;
					final String[] params = function.getParameters();
					final JLabel label = new JLabel("<html><span><b>" + function.getName() + "(</b><i>" + (params == null ? "..." : Utils.join(", ", params)) + "</i><b>)</b></span></html>");
					if(isSelected) {
						label.setText(label.getText().replace("<span>", "<span style=\"background-color: #BDC3C7;\">"));
					}
					return label;
				}
				
			});
			list.addMouseListener(new MouseAdapter() {
				
				@Override
				public final void mouseClicked(final MouseEvent event) {
					if(event.getClickCount() != 2) {
						return;
					}
					textField.setText(model.getElementAt(list.locationToIndex(event.getPoint())).getName() + "(" + textField.getText() + ")");
					PickerDialog.this.dispose();
				}
				
			});
			this.getContentPane().add(new JScrollPane(list), BorderLayout.CENTER);
			this.setLocationRelativeTo(parent);
		}
		catch(final Exception ex) {
			ErrorDialog.errorMessage(this, ex);
		}
	}
	
	private class PickerModel extends DefaultListModel<Function> {

		private static final long serialVersionUID = 1L;
		
		private PickerModel(final Heartbeat.Function... functions) {
			for(final Function function : functions) {
				this.addElement(function);
			}
		}
		
	}

}