package fr.skyost.algo.desktop.frames;

import javax.swing.JFrame;
import javax.swing.JTextArea;

import java.awt.Color;
import java.awt.Container;

import javax.swing.JTextField;

import fr.skyost.algo.core.AlgoLine;
import fr.skyost.algo.core.AlgoRunnable;
import fr.skyost.algo.core.AlgorithmListener.AlgorithmThreadListener;
import fr.skyost.algo.core.utils.VariableHolder.VariableValue;
import fr.skyost.algo.desktop.AlgogoDesktop;
import fr.skyost.algo.desktop.dialogs.ErrorDialog;
import fr.skyost.algo.desktop.utils.LanguageManager;
import fr.skyost.algo.desktop.utils.Utils;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ImageIcon;
import javax.swing.text.DefaultCaret;
// import javax.swing.JCheckBox;

public class ConsoleFrame extends JFrame implements AlgorithmThreadListener {

	private static final long serialVersionUID = 1L;
	
	private final JTextArea output = new JTextArea();
	private final JButton btnRun = changeButtonState(null, true);
	
	private AlgoRunnable currentThread;

	public ConsoleFrame() {
		this.setTitle(LanguageManager.getString("console.title"));
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(AlgogoDesktop.class.getResource("/fr/skyost/algo/desktop/res/icons/app_icon.png")));
		this.setSize(500, 376);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		final Container content = this.getContentPane();
		output.setFont(AlgogoDesktop.CONSOLE_FONT);
		output.setLineWrap(true);
		output.setWrapStyleWord(true);
		output.setBackground(Color.BLACK);
		output.setForeground(Color.WHITE);
		output.setEditable(false);
		((DefaultCaret)output.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		/*final JCheckBox chckbxDebug = new JCheckBox("Debug"); TODO: Enable it*/
		final JScrollPane scrollPane = new JScrollPane(output);
		btnRun.addActionListener(new ActionListener() {

			@Override
			public final void actionPerformed(final ActionEvent event) {
				if(currentThread != null) {
					currentThread.interrupt();
					return;
				}
				output.setText(null);
				currentThread = EditorFrame.algorithm.createNewRunnable(ConsoleFrame.this);
				currentThread.start();
			}

		});
		this.addWindowListener(new WindowListener() {
			
			@Override
			public final void windowClosing(final WindowEvent event) {
				if(currentThread != null) {
					currentThread.interrupt();
				}
			}

			@Override
			public final void windowActivated(final WindowEvent event) {}
			
			@Override
			public final void windowDeactivated(final WindowEvent event) {}

			@Override
			public final void windowClosed(final WindowEvent event) {}

			@Override
			public final void windowIconified(final WindowEvent event) {}

			@Override
			public final void windowDeiconified(final WindowEvent event) {}

			@Override
			public final void windowOpened(final WindowEvent event) {}
			
		});
		final GroupLayout groupLayout = new GroupLayout(content);
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.TRAILING).addGroup(groupLayout.createSequentialGroup().addComponent(scrollPane).addPreferredGap(ComponentPlacement.RELATED).addGroup(groupLayout.createParallelGroup(Alignment.TRAILING, false)/*.addComponent(chckbxDebug, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)*/.addComponent(btnRun, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addContainerGap()));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout.createSequentialGroup().addGap(11).addComponent(btnRun).addPreferredGap(ComponentPlacement.RELATED)/*.addComponent(chckbxDebug).addContainerGap(276, Short.MAX_VALUE)*/).addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 337, Short.MAX_VALUE));
		content.setLayout(groupLayout);
	}

	@Override
	public final void threadLaunched(final AlgoRunnable thread) {
		if(thread != currentThread) {
			return;
		}
		changeButtonState(btnRun, false);
	}

	@Override
	public final void lineOutputed(final AlgoRunnable thread, final String line, final boolean lineBreak) {
		if(thread != currentThread) {
			return;
		}
		output.append(line);
		if(lineBreak) {
			output.append(System.lineSeparator());
		}
	}

	@Override
	public final String actionRequired(final AlgoRunnable thread, final AlgoLine line, final VariableValue variable) {
		if(thread != currentThread) {
			return null;
		}
		final JTextField value = new JTextField();
		final String currentValue = variable.getValue();
		if(currentValue != null) {
			value.setText(currentValue);
		}
		if(Utils.createDialog(this, LanguageManager.getString("console.actionrequired.dialog.title"), String.format(LanguageManager.getString("console.actionrequired.dialog.message"), line.getArgs()[0]), LanguageManager.getString("console.actionrequired.dialog.tip"), value)) {
			return value.getText();
		}
		return null;
	}
	
	@Override
	public final void lineExecuted(final AlgoRunnable runnable, final AlgoLine line, final boolean before) {}

	@Override
	public final void threadInterrupted(final AlgoRunnable thread, final Exception ex) {
		if(thread != currentThread) {
			return;
		}
		changeButtonState(btnRun, true);
		currentThread = null;
		if(ex != null) {
			ex.printStackTrace();
			ErrorDialog.errorMessage(this, ex);
		}
	}
	
	private final JButton changeButtonState(JButton button, final boolean state) {
		if(button == null) {
			button = new JButton();
		}
		if(state) {
			button.setText(LanguageManager.getString("console.button.run"));
			button.setIcon(new ImageIcon(AlgogoDesktop.class.getResource("/fr/skyost/algo/desktop/res/icons/btn_run.png")));
		}
		else {
			button.setText(LanguageManager.getString("console.button.stop"));
			button.setIcon(new ImageIcon(AlgogoDesktop.class.getResource("/fr/skyost/algo/desktop/res/icons/btn_stop.png")));
		}
		return button;
	}

}