package fr.skyost.algo.desktop.frames;

import javax.swing.JFrame;
import javax.swing.JTextArea;

import java.awt.Color;
import java.awt.Container;

import javax.swing.JTextField;

import fr.skyost.algo.core.AlgoLine;
import fr.skyost.algo.core.AlgoRunnable;
import fr.skyost.algo.core.Instruction;
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
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ImageIcon;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.DefaultCaret;

public class ConsoleFrame extends JFrame implements AlgorithmThreadListener {

	private static final long serialVersionUID = 1L;

	private final JCheckBox chckbxDebug = new JCheckBox(LanguageManager.getString("console.buttons.debug"));
	private final JTextArea output = new JTextArea();
	private final JButton btnRun = changeButtonState(null, true);

	private AlgoRunnable currentThread;

	public ConsoleFrame() {
		this.setTitle(LanguageManager.getString("console.title"));
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(AlgogoDesktop.class.getResource("/fr/skyost/algo/desktop/res/icons/app_icon.png")));
		this.setSize(500, 376);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
		final Container content = this.getContentPane();
		output.setFont(AlgogoDesktop.CONSOLE_FONT);
		output.setLineWrap(true);
		output.setWrapStyleWord(true);
		output.setBackground(Color.BLACK);
		output.setForeground(Color.WHITE);
		output.setEditable(false);
		((DefaultCaret)output.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
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
		final JButton btnSaveReport = new JButton(LanguageManager.getString("console.buttons.saveoutput"));
		btnSaveReport.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent event) {
				try {
					final String separator = System.lineSeparator();
					final StringBuilder builder = new StringBuilder();
					builder.append("<html>" + separator);
					builder.append("<head>" + separator);
					builder.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/>" + separator);
					builder.append("<title>" + EditorFrame.algorithm.getTitle() + " by " + EditorFrame.algorithm.getAuthor() + "</title>" + separator);
					builder.append("<meta name=\"generator\" content=\"" + AlgogoDesktop.APP_NAME + "\">" + separator);
					builder.append("</head>" + separator);
					builder.append("<body style=\"background-color: black; color: white; font-family: 'DejaVuSansMono', 'Consolas', 'Courier', 'Courier New'; font-size: 1.1em;\">" + separator);
					builder.append(output.getText().replace(separator, "<br>") + separator);
					builder.append("</body>" + separator);
					builder.append("</html>");
					final JFileChooser chooser = new JFileChooser();
					final File currentDir = Utils.getParentFolder();
					chooser.setFileFilter(new FileNameExtensionFilter(LanguageManager.getString("console.buttons.saveoutput.filter"), "html"));
					chooser.setMultiSelectionEnabled(false);
					chooser.setCurrentDirectory(currentDir);
					chooser.setSelectedFile(new File(currentDir, EditorFrame.algorithm.getTitle()));
					if(chooser.showSaveDialog(ConsoleFrame.this) == JFileChooser.APPROVE_OPTION) {
						String path = chooser.getSelectedFile().getPath();
						if(!path.endsWith(".html")) {
							path += ".html";
						}
						final File file = new File(path);
						if(file.exists()) {
							file.delete();
							file.createNewFile();
						}
						Files.write(Paths.get(path), builder.toString().getBytes(StandardCharsets.UTF_8));
					}
				}
				catch(final Exception ex) {
					ex.printStackTrace();
					ErrorDialog.errorMessage(ConsoleFrame.this, ex);
				}
			}
			
		});
		final GroupLayout groupLayout = new GroupLayout(content);
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout.createSequentialGroup().addContainerGap().addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 369, Short.MAX_VALUE).addPreferredGap(ComponentPlacement.RELATED).addGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, groupLayout.createParallelGroup(Alignment.LEADING, false).addComponent(chckbxDebug, Alignment.TRAILING, 0, 0, Short.MAX_VALUE).addComponent(btnRun, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addComponent(btnSaveReport, Alignment.TRAILING)).addContainerGap()));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout.createSequentialGroup().addContainerGap().addGroup(groupLayout.createParallelGroup(Alignment.LEADING).addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 315, Short.MAX_VALUE).addGroup(groupLayout.createSequentialGroup().addComponent(btnRun).addPreferredGap(ComponentPlacement.RELATED).addComponent(chckbxDebug).addPreferredGap(ComponentPlacement.RELATED, 244, Short.MAX_VALUE).addComponent(btnSaveReport))).addContainerGap()));
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
	public final void lineExecuted(final AlgoRunnable runnable, final AlgoLine line, final boolean before) {
		if(chckbxDebug.isSelected() && before) {
			output.append("Executing line \"" + getLine(line.getInstruction(), line.getArgs()) + "\"..." + System.lineSeparator());
		}
	}

	@Override
	public final void threadInterrupted(final AlgoRunnable thread, final Exception ex) {
		if(thread != currentThread) {
			return;
		}
		changeButtonState(btnRun, true);
		currentThread = null;
		if(ex != null) {
			output.append("Exception occurred :\"" + ex.getClass().getName() + "\", please check the error log." + System.lineSeparator());
			ex.printStackTrace();
			ErrorDialog.errorMessage(this, ex);
		}
	}

	private final JButton changeButtonState(JButton button, final boolean state) {
		if(button == null) {
			button = new JButton();
		}
		if(state) {
			button.setText(LanguageManager.getString("console.buttons.run"));
			button.setIcon(new ImageIcon(AlgogoDesktop.class.getResource("/fr/skyost/algo/desktop/res/icons/btn_run.png")));
		}
		else {
			button.setText(LanguageManager.getString("console.buttons.stop"));
			button.setIcon(new ImageIcon(AlgogoDesktop.class.getResource("/fr/skyost/algo/desktop/res/icons/btn_stop.png")));
		}
		return button;
	}

	private final String getLine(final Instruction instruction, final String... args) {
		final StringBuilder builder = new StringBuilder(Utils.escapeHTML(instruction.toString()) + " ");
		switch(instruction) {
		case CREATE_VARIABLE:
			builder.append("TYPE" + (args[0].equals("0") ? " String" : " Number"));
			break;
		case ASSIGN_VALUE_TO_VARIABLE:
			builder.append(Utils.escapeHTML(args[0]) + " → " + Utils.escapeHTML(args[1]));
			break;
		case SHOW_VARIABLE:
		case READ_VARIABLE:
		case SHOW_MESSAGE:
		case IF:
		case WHILE:
			builder.append(Utils.escapeHTML(args[0]));
			break;
		case FOR:
			builder.append(Utils.escapeHTML(args[0]) + " " + "FROM" + " " + args[1] + " " + "TO" + " " + args[2]);
			break;
		case ELSE:
			break;
		default:
			builder.delete(0, builder.length() - 1);
			break;
		}
		return builder.toString();
	}
	
}