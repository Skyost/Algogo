package xyz.algogo.desktop.dialogs;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

import xyz.algogo.desktop.AlgogoDesktop;
import xyz.algogo.desktop.utils.LanguageManager;
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
			final DefaultListModel<String> model = new DefaultListModel<String>();
			final JList<String> list = new JList<String>();
			list.setModel(model);
			
			model.addElement("sin(expression)");
			model.addElement("cos(expression)");
			model.addElement("tan(expression)");
			model.addElement("asin(expression)");
			model.addElement("acos(expression)");
			model.addElement("atan(expression)");

			model.addElement("sinh(expression)");
			model.addElement("cosh(expression)");
			model.addElement("tanh(expression)");
			model.addElement("asinh(expression)");
			model.addElement("acosh(expression)");
			model.addElement("atanh(expression)");

			model.addElement("log(expression)");
			model.addElement("ln(expression)");

			model.addElement("sqrt(expression)");
			model.addElement("angle(x, y)");
			model.addElement("abs(expression)");
			model.addElement("mod(dividen, divisor)");
			model.addElement("sum(expression)");

			model.addElement("rand()");
			list.setCellRenderer(new DefaultListCellRenderer() {
	
				private static final long serialVersionUID = 1L;
				
				@Override
				public final Component getListCellRendererComponent(final JList<?> list, final Object value, final int index, final boolean isSelected, final boolean cellHasFocus) {
					final String function = (String)value;
					final int firstParenthesis = function.indexOf("(");
					final StringBuilder builder = new StringBuilder("<html><span><b>" + function.substring(0, firstParenthesis) + "(</b><i>");
					builder.append(function.substring(firstParenthesis + 1, function.length() - 1));
					builder.append("</i><b>)</b></span></html>");
					final JLabel label = new JLabel(builder.toString());
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
					textField.setText(textField.getText() + model.getElementAt(list.locationToIndex(event.getPoint())));
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

}