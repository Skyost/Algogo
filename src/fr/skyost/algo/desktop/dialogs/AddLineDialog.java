package fr.skyost.algo.desktop.dialogs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.JDialog;

import fr.skyost.algo.core.AlgoLine;
import fr.skyost.algo.core.Instruction;
import fr.skyost.algo.desktop.AlgogoDesktop;
import fr.skyost.algo.desktop.frames.EditorFrame;
import fr.skyost.algo.desktop.utils.AlgoTreeNode;
import fr.skyost.algo.desktop.utils.LanguageManager;
import fr.skyost.algo.desktop.utils.Utils;

import java.awt.Component;
import java.awt.Container;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;

public class AddLineDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	public static final List<AlgoLineListener> listeners = new ArrayList<AlgoLineListener>();

	public AddLineDialog() {
		this.setTitle(LanguageManager.getString("addline.title"));
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(AlgogoDesktop.class.getResource("/fr/skyost/algo/desktop/res/icons/app_icon.png")));
		this.setSize(500, 212);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setModalityType(ModalityType.APPLICATION_MODAL);
		this.setModal(true);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		final LinkedHashMap<String, Integer> variables = getVariables();
		final Runnable dispose = new Runnable() {

			@Override
			public final void run() {
				AddLineDialog.this.dispose();
			}

		};
		final JButton btnCreateVariable = new JButton(LanguageManager.getString("addline.createvariable"));
		btnCreateVariable.addActionListener(listenerForInstruction(this, Instruction.CREATE_VARIABLE, dispose));
		final JButton btnAssignVariable = new JButton(LanguageManager.getString("addline.assignvaluetovariable"));
		btnAssignVariable.addActionListener(listenerForInstruction(this, Instruction.ASSIGN_VALUE_TO_VARIABLE, dispose));
		btnAssignVariable.setEnabled(variables.size() > 0);
		final JButton btnShowVariable = new JButton(LanguageManager.getString("addline.showvariable"));
		btnShowVariable.addActionListener(listenerForInstruction(this, Instruction.SHOW_VARIABLE, dispose));
		btnShowVariable.setEnabled(variables.size() > 0);
		final JButton btnReadVariable = new JButton(LanguageManager.getString("addline.readvariable"));
		btnReadVariable.addActionListener(listenerForInstruction(this, Instruction.READ_VARIABLE, dispose));
		btnReadVariable.setEnabled(variables.size() > 0);
		final JButton btnShowMessage = new JButton(LanguageManager.getString("addline.showmessage"));
		btnShowMessage.addActionListener(listenerForInstruction(this, Instruction.SHOW_MESSAGE, dispose));
		final JButton btnIfThenElse = new JButton(LanguageManager.getString("addline.ifelse"));
		btnIfThenElse.addActionListener(listenerForInstruction(this, Instruction.IF, dispose));
		final JButton btnWhile = new JButton(LanguageManager.getString("addline.while"));
		btnWhile.addActionListener(listenerForInstruction(this, Instruction.WHILE, dispose));
		final List<String> variablesNumber = new ArrayList<String>();
		for(final Entry<String, Integer> entry : variables.entrySet()) {
			if(entry.getValue() == 1) {
				variablesNumber.add(entry.getKey());
			}
		}
		final JButton btnFor = new JButton(LanguageManager.getString("addline.for"));
		btnFor.addActionListener(listenerForInstruction(this, Instruction.FOR, dispose, variablesNumber.toArray(new String[variablesNumber.size()])));
		btnFor.setEnabled(variablesNumber.size() > 0);
		final Container content = this.getContentPane();
		final GroupLayout groupLayout = new GroupLayout(content);
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout.createSequentialGroup().addContainerGap().addGroup(groupLayout.createParallelGroup(Alignment.TRAILING, false).addComponent(btnReadVariable, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(btnWhile, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(btnIfThenElse, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(btnAssignVariable, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 177, Short.MAX_VALUE).addComponent(btnCreateVariable, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 216, GroupLayout.PREFERRED_SIZE)).addPreferredGap(ComponentPlacement.RELATED, 49, Short.MAX_VALUE).addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false).addComponent(btnFor, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(btnShowMessage, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(btnShowVariable, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 209, Short.MAX_VALUE)).addContainerGap()));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout.createSequentialGroup().addContainerGap().addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(btnCreateVariable).addComponent(btnShowVariable)).addGap(5).addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(btnAssignVariable).addComponent(btnShowMessage)).addPreferredGap(ComponentPlacement.RELATED).addComponent(btnReadVariable).addPreferredGap(ComponentPlacement.RELATED, 28, Short.MAX_VALUE).addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(btnIfThenElse).addComponent(btnFor)).addPreferredGap(ComponentPlacement.RELATED).addComponent(btnWhile).addGap(12)));
		content.setLayout(groupLayout);
	}

	private static final void notifyListeners(final Instruction instruction, final String... args) {
		for(final AlgoLineListener listener : listeners) {
			listener.lineAdded(instruction, args);
		}
	}

	private static final void notifyListeners(final AlgoTreeNode node, final String... args) {
		for(final AlgoLineListener listener : listeners) {
			listener.lineEdited(node, args);
		}
	}
	
	private static final LinkedHashMap<String, Integer> getVariables() {
		final LinkedHashMap<String, Integer> variables = new LinkedHashMap<String, Integer>();
		for(final AlgoLine variable : EditorFrame.algorithm.getVariables().getChildren()) {
			final String[] args = variable.getArgs();
			variables.put(args[0], Integer.valueOf(args[1]));
		}
		return variables;
	}
	
	/**
	 * Gets an action listener for the specified instruction.
	 * 
	 * @param component The parent component (will be used in dialogs).
	 * @param instruction The instruction.
	 * @param after Will be run after if the user clicks on "OK".
	 * 
	 * @return The action listener.
	 */

	public static final ActionListener listenerForInstruction(final Component component, final Instruction instruction, final Runnable after) {
		final List<String> variables = new ArrayList<String>(getVariables().keySet());
		return listenerForInstruction(component, instruction, after, variables.toArray(new String[variables.size()]));
	}
	
	/**
	 * Gets an action listener for the specified instruction.
	 * 
	 * @param component The parent component (will be used in dialogs).
	 * @param instruction The instruction.
	 * @param after Will be run after if the user clicks on "OK".
	 * @param variables The variables (for instructions like SHOW_VARIABLE, FOR, ...).
	 * 
	 * @return The action listener.
	 */

	public static final ActionListener listenerForInstruction(final Component component, final Instruction instruction, final Runnable after, final String... variables) {
		return listenerForInstruction(component, instruction, null, after, variables);
	}
	
	/**
	 * Gets an action listener for the specified instruction.
	 * 
	 * @param component The parent component (will be used in dialogs).
	 * @param node The algo line.
	 * @param after Will be run after if the user clicks on "OK".
	 * 
	 * @return The action listener.
	 */

	public static final ActionListener listenerForInstruction(final Component component, final AlgoTreeNode node, final Runnable after) {
		final List<String> variables = new ArrayList<String>(getVariables().keySet());
		return listenerForInstruction(component, node, after, variables.toArray(new String[variables.size()]));
	}
	
	/**
	 * Gets an action listener for the specified instruction.
	 * 
	 * @param component The parent component (will be used in dialogs).
	 * @param node The algo line.
	 * @param after Will be run after if the user clicks on "OK".
	 * @param variables The variables (for instructions like SHOW_VARIABLE, FOR, ...).
	 * 
	 * @return The action listener.
	 */

	public static final ActionListener listenerForInstruction(final Component component, final AlgoTreeNode node, final Runnable after, final String... variables) {
		return listenerForInstruction(component, null, node, after, variables);
	}

	private static final ActionListener listenerForInstruction(final Component component, final Instruction instruction, final AlgoTreeNode node, final Runnable after, final String... variables) {
		final boolean editMode = node != null;
		switch(editMode ? node.getAlgoLine().getInstruction() : instruction) {
		case CREATE_VARIABLE:
			return new ActionListener() {

				@Override
				public final void actionPerformed(final ActionEvent event) {
					final JTextField varName = new JTextField();
					final JComboBox<String> varTypes = new JComboBox<String>(new String[]{LanguageManager.getString("addline.createvariable.dialog.object.string"), LanguageManager.getString("addline.createvariable.dialog.object.number")});
					if(editMode) {
						final String[] args = node.getAlgoLine().getArgs();
						varName.setText(args[0]);
						varTypes.setSelectedIndex(Integer.valueOf(args[1]));
					}
					if(Utils.createDialog(component, LanguageManager.getString("addline.createvariable.dialog.title"), LanguageManager.getString("addline.createvariable.dialog.message"), LanguageManager.getString("addline.createvariable.dialog.tip"), varName, varTypes)) {
						final String var = varName.getText();
						if(Arrays.asList(variables).contains(var)) {
							JOptionPane.showMessageDialog(component, LanguageManager.getString("addline.createvariable.error.alreadyexists"), LanguageManager.getString("joptionpane.error"), JOptionPane.ERROR_MESSAGE);
							return;
						}
						if(!Utils.isAlpha(var)) {
							JOptionPane.showMessageDialog(component, LanguageManager.getString("addline.createvariable.error.notalpha"), LanguageManager.getString("joptionpane.error"), JOptionPane.ERROR_MESSAGE);
							return;
						}
						if(editMode) {
							notifyListeners(node, var, String.valueOf(varTypes.getSelectedIndex()));
						}
						else {
							notifyListeners(instruction, var, String.valueOf(varTypes.getSelectedIndex()));
						}
						if(after != null) {
							after.run();
						}
					}
				}

			};
		case ASSIGN_VALUE_TO_VARIABLE:
			return new ActionListener() {

				@Override
				public final void actionPerformed(final ActionEvent event) {
					final JComboBox<String> cmboxVariables = new JComboBox<String>(variables);
					final JTextField value = new JTextField();
					if(editMode) {
						final String[] args = node.getAlgoLine().getArgs();
						if(Arrays.asList(variables).contains(args[0])) {
							cmboxVariables.setSelectedItem(args[0]);
						}
						value.setText(args[1]);
					}
					if(Utils.createDialog(component, LanguageManager.getString("addline.assignvaluetovariable.dialog.title"), LanguageManager.getString("addline.assignvaluetovariable.dialog.message"), LanguageManager.getString("addline.assignvaluetovariable.dialog.tip"), cmboxVariables, new JLabel(LanguageManager.getString("addline.assignvaluetovariable.dialog.object.value")), value)) {
						if(node != null) {
							notifyListeners(node, cmboxVariables.getSelectedItem().toString(), value.getText());
						}
						else {
							notifyListeners(instruction, cmboxVariables.getSelectedItem().toString(), value.getText());
						}
						if(after != null) {
							after.run();
						}
					}
				}

			};
		case SHOW_VARIABLE:
			return new ActionListener() {

				@Override
				public final void actionPerformed(final ActionEvent event) {
					final JComboBox<String> cmboxVariables = new JComboBox<String>(variables);
					final JCheckBox lineBreak = new JCheckBox(LanguageManager.getString("addline.showvariable.dialog.object.linebreak"));
					if(editMode) {
						final String[] args = node.getAlgoLine().getArgs();
						if(Arrays.asList(variables).contains(args[0])) {
							cmboxVariables.setSelectedItem(args[0]);
						}
						lineBreak.setSelected(Boolean.valueOf(args[1]));
					}
					if(Utils.createDialog(component, LanguageManager.getString("addline.showvariable.dialog.title"), LanguageManager.getString("addline.showvariable.dialog.message"), LanguageManager.getString("addline.showvariable.dialog.tip"), cmboxVariables, lineBreak)) {
						if(node != null) {
							notifyListeners(node, cmboxVariables.getSelectedItem().toString(), String.valueOf(lineBreak.isSelected()));
						}
						else {
							notifyListeners(instruction, cmboxVariables.getSelectedItem().toString(), String.valueOf(lineBreak.isSelected()));
						}
						if(after != null) {
							after.run();
						}
					}
				}

			};
		case READ_VARIABLE:
			return new ActionListener() {

				@Override
				public final void actionPerformed(final ActionEvent event) {
					final JComboBox<String> cmboxVariables = new JComboBox<String>(variables);
					if(editMode) {
						final String[] args = node.getAlgoLine().getArgs();
						if(Arrays.asList(variables).contains(args[0])) {
							cmboxVariables.setSelectedItem(args[0]);
						}
					}
					if(Utils.createDialog(component, LanguageManager.getString("addline.readvariable.dialog.title"), LanguageManager.getString("addline.readvariable.dialog.message"), LanguageManager.getString("addline.readvariable.dialog.tip"), cmboxVariables)) {
						if(node != null) {
							notifyListeners(node, cmboxVariables.getSelectedItem().toString());
						}
						else {
							notifyListeners(instruction, cmboxVariables.getSelectedItem().toString());
						}
						if(after != null) {
							after.run();
						}
					}
				}

			};
		case SHOW_MESSAGE:
			return new ActionListener() {

				@Override
				public final void actionPerformed(final ActionEvent event) {
					final JTextField value = new JTextField();
					final JCheckBox lineBreak = new JCheckBox(LanguageManager.getString("addline.showmessage.dialog.object.linebreak"));
					if(editMode) {
						final String[] args = node.getAlgoLine().getArgs();
						value.setText(args[0]);
						lineBreak.setSelected(Boolean.valueOf(args[1]));
					}
					if(Utils.createDialog(component, LanguageManager.getString("addline.showmessage.dialog.title"), LanguageManager.getString("addline.showmessage.dialog.message"), LanguageManager.getString("addline.showmessage.dialog.tip"), value, lineBreak)) {
						if(node != null) {
							notifyListeners(node, value.getText(), String.valueOf(lineBreak.isSelected()));
						}
						else {
							notifyListeners(instruction, value.getText(), String.valueOf(lineBreak.isSelected()));
						}
						if(after != null) {
							after.run();
						}
					}
				}

			};
		case IF:
			return new ActionListener() {

				@Override
				public final void actionPerformed(final ActionEvent event) {
					final JTextField condition = new JTextField();
					final JCheckBox addElse = new JCheckBox(LanguageManager.getString("addline.ifelse.dialog.object.addelse"));
					if(editMode) {
						final String[] args = node.getAlgoLine().getArgs();
						condition.setText(args[0]);
						addElse.setSelected(Boolean.valueOf(args[1]));
					}
					if(Utils.createDialog(component, LanguageManager.getString("addline.ifelse.dialog.title"), LanguageManager.getString("addline.ifelse.dialog.message"), LanguageManager.getString("addline.ifelse.dialog.tip"), condition, addElse)) {
						final boolean selected = addElse.isSelected();
						if(node != null) {
							notifyListeners(node, condition.getText(), String.valueOf(selected));
						}
						else {
							notifyListeners(instruction, condition.getText(), String.valueOf(selected));
						}
						if(selected) {
							notifyListeners(Instruction.ELSE, condition.getText());
						}
						if(after != null) {
							after.run();
						}
					}
				}

			};
		case WHILE:
			return new ActionListener() {

				@Override
				public final void actionPerformed(final ActionEvent event) {
					final JTextField condition = new JTextField();
					if(editMode) {
						condition.setText(node.getAlgoLine().getArgs()[0]);
					}
					if(Utils.createDialog(component, LanguageManager.getString("addline.while.dialog.title"), LanguageManager.getString("addline.while.dialog.message"), LanguageManager.getString("addline.while.dialog.tip"), condition)) {
						if(node != null) {
							notifyListeners(node, condition.getText());
						}
						else {
							notifyListeners(instruction, condition.getText());
						}
						if(after != null) {
							after.run();
						}
					}
				}

			};
		case FOR:
			return new ActionListener() {

				@Override
				public final void actionPerformed(final ActionEvent event) {
					final JComboBox<String> cmboxVariables = new JComboBox<String>(variables);
					final JTextField from = new JTextField();
					final JTextField to = new JTextField();
					if(editMode) {
						final String[] args = node.getAlgoLine().getArgs();
						if(Arrays.asList(variables).contains(args[0])) {
							cmboxVariables.setSelectedItem(args[0]);
						}
						from.setText(args[1]);
						to.setText(args[2]);
					}
					if(Utils.createDialog(component, LanguageManager.getString("addline.for.dialog.title"), LanguageManager.getString("addline.for.dialog.message"), LanguageManager.getString("addline.for.dialog.tip"), cmboxVariables, new JLabel(LanguageManager.getString("addline.for.dialog.object.from")), from, new JLabel(LanguageManager.getString("addline.for.dialog.object.to")), to)) {
						if(node != null) {
							notifyListeners(node, cmboxVariables.getSelectedItem().toString(), from.getText(), to.getText());
						}
						else {
							notifyListeners(instruction, cmboxVariables.getSelectedItem().toString(), from.getText(), to.getText());
						}
						if(after != null) {
							after.run();
						}
					}
				}
				
			};
		default:
			return null;
		}
	}
	
	/**
	 * Interface for this dialog.
	 * 
	 * @author Skyost.
	 */

	public interface AlgoLineListener {
		
		/**
		 * When a line is added.
		 * 
		 * @param instruction The instruction.
		 * @param args The arguments.
		 */

		public void lineAdded(final Instruction instruction, final String... args);
		
		/**
		 * When a line is edited.
		 * 
		 * @param node The AlgoTreeNode.
		 * @param args The arguments.
		 */
		
		public void lineEdited(final AlgoTreeNode node, final String... args);

	}
	
}