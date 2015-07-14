package fr.skyost.algo.desktop.frames;

import java.awt.Container;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.print.DocFlavor;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.ServiceUI;
import javax.print.SimpleDoc;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.GroupLayout;
import javax.swing.JTree;
import javax.swing.GroupLayout.Alignment;
import javax.swing.KeyStroke;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;

import fr.skyost.algo.core.AlgoLine;
import fr.skyost.algo.core.AlgogoCore;
import fr.skyost.algo.core.Algorithm;
import fr.skyost.algo.core.Instruction;
import fr.skyost.algo.core.Keyword;
import fr.skyost.algo.core.AlgorithmListener.AlgorithmOptionsListener;
import fr.skyost.algo.core.language.AlgorithmLanguage;
import fr.skyost.algo.core.language.JavaLanguage;
import fr.skyost.algo.core.language.JavaScriptLanguage;
import fr.skyost.algo.core.language.PHPLanguage;
import fr.skyost.algo.desktop.AlgogoDesktop;
import fr.skyost.algo.desktop.dialogs.AboutDialog;
import fr.skyost.algo.desktop.dialogs.AddLineDialog;
import fr.skyost.algo.desktop.dialogs.ErrorDialog;
import fr.skyost.algo.desktop.dialogs.OptionsDialog;
import fr.skyost.algo.desktop.dialogs.AddLineDialog.AlgoLineListener;
import fr.skyost.algo.desktop.utils.AlgoTreeNode;
import fr.skyost.algo.desktop.utils.GithubUpdater;
import fr.skyost.algo.desktop.utils.JLabelLink;
import fr.skyost.algo.desktop.utils.LanguageManager;
import fr.skyost.algo.desktop.utils.Utils;
import fr.skyost.algo.desktop.utils.GithubUpdater.GithubUpdaterResultListener;

import javax.swing.JMenuBar;

public class EditorFrame extends JFrame implements AlgoLineListener, AlgorithmOptionsListener, GithubUpdaterResultListener {

	private static final long serialVersionUID = 1L;

	public static Algorithm algorithm;

	private static String algoPath;
	private static boolean algoChanged;

	public static JTree tree;
	public static AlgoTreeNode variables;
	public static AlgoTreeNode beginning;
	public static AlgoTreeNode end;
	
	public static final List<AlgoTreeNode> clipboard = new ArrayList<AlgoTreeNode>();
	
	private final JScrollPane scrollPane = new JScrollPane();
	private final JButton btnAddLine = new JButton(LanguageManager.getString("editor.button.addline"));
	private final JButton btnRemoveLine = new JButton(LanguageManager.getString("editor.button.removelines"));
	private final JButton btnEditLine = new JButton(LanguageManager.getString("editor.button.editline"));
	private final JButton btnUp = new JButton(LanguageManager.getString("editor.button.up"));
	private final JButton btnDown = new JButton(LanguageManager.getString("editor.button.down")); 

	public EditorFrame() {
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(AlgogoDesktop.class.getResource("/fr/skyost/algo/desktop/res/icons/app_icon.png")));
		this.setSize(800, 600);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setLocationRelativeTo(null);
		resetEditor();
		btnAddLine.setIcon(new ImageIcon(AlgogoDesktop.class.getResource("/fr/skyost/algo/desktop/res/icons/btn_add.png")));
		btnRemoveLine.setIcon(new ImageIcon(AlgogoDesktop.class.getResource("/fr/skyost/algo/desktop/res/icons/btn_remove.png")));
		btnRemoveLine.setEnabled(false);
		btnEditLine.setIcon(new ImageIcon(AlgogoDesktop.class.getResource("/fr/skyost/algo/desktop/res/icons/btn_edit.png")));
		btnEditLine.setEnabled(false);
		btnUp.setIcon(new ImageIcon(AlgogoDesktop.class.getResource("/fr/skyost/algo/desktop/res/icons/btn_up.png")));
		btnUp.setEnabled(false);
		btnDown.setIcon(new ImageIcon(AlgogoDesktop.class.getResource("/fr/skyost/algo/desktop/res/icons/btn_down.png")));
		btnDown.setEnabled(false);
		final JButton btnTest = new JButton(LanguageManager.getString("editor.button.test"));
		btnTest.setIcon(new ImageIcon(AlgogoDesktop.class.getResource("/fr/skyost/algo/desktop/res/icons/btn_test.png")));
		final Container content = this.getContentPane();
		final GroupLayout groupLayout = new GroupLayout(content);
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout.createSequentialGroup().addContainerGap().addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE).addPreferredGap(ComponentPlacement.RELATED).addGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false).addComponent(btnAddLine, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 163, Short.MAX_VALUE).addComponent(btnUp, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(btnEditLine, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(btnRemoveLine, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 163, Short.MAX_VALUE).addComponent(btnDown, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 163, Short.MAX_VALUE)).addComponent(btnTest, GroupLayout.PREFERRED_SIZE, 163, GroupLayout.PREFERRED_SIZE)).addContainerGap()));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout.createSequentialGroup().addContainerGap().addGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout.createSequentialGroup().addComponent(btnAddLine).addPreferredGap(ComponentPlacement.RELATED).addComponent(btnRemoveLine).addPreferredGap(ComponentPlacement.RELATED).addComponent(btnEditLine).addGap(18).addComponent(btnUp).addPreferredGap(ComponentPlacement.RELATED).addComponent(btnDown).addPreferredGap(ComponentPlacement.RELATED, 334, Short.MAX_VALUE).addComponent(btnTest)).addComponent(scrollPane, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 508, Short.MAX_VALUE)).addGap(21)));
		content.setLayout(groupLayout);
		this.addWindowListener(new WindowAdapter() {

			@Override
			public final void windowClosing(final WindowEvent event) {
				if(algoChanged) {
					final int result = JOptionPane.showConfirmDialog(EditorFrame.this, String.format(LanguageManager.getString("editor.closedialog"), algorithm.getTitle()), AlgogoCore.APP_NAME, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
					if(result == JOptionPane.CANCEL_OPTION) {
						return;
					}
					if(result == JOptionPane.YES_OPTION) {
						if(algoPath == null || !Files.isWritable(Paths.get(algoPath))) {
							saveAs();
							return;
						}
						save(new File(algoPath));
					}
				}
				EditorFrame.this.dispose();
			}

		});
		btnAddLine.addActionListener(new ActionListener() {

			@Override
			public final void actionPerformed(final ActionEvent event) {
				new AddLineDialog(EditorFrame.this).setVisible(true);
			}

		});
		btnRemoveLine.addActionListener(new ActionListener() {

			@Override
			public final void actionPerformed(final ActionEvent event) {
				for(final TreePath path : tree.getSelectionPaths()) {
					removeNode((AlgoTreeNode)path.getLastPathComponent());
				}
			}

		});
		btnEditLine.addActionListener(new ActionListener() {

			@Override
			public final void actionPerformed(final ActionEvent event) {
				AddLineDialog.listenerForInstruction(EditorFrame.this, EditorFrame.this, (AlgoTreeNode)tree.getSelectionPaths()[0].getLastPathComponent(), null).actionPerformed(event);
			}

		});
		btnUp.addActionListener(new ActionListener() {

			@Override
			public final void actionPerformed(final ActionEvent event) {
				final AlgoTreeNode node = (AlgoTreeNode)tree.getSelectionPaths()[0].getLastPathComponent();
				final AlgoLine line = node.getAlgoLine();
				if(line.getInstruction() == Instruction.ELSE) {
					return;
				}
				int step = -1;
				final AlgoTreeNode parent = (AlgoTreeNode)node.getParent();
				final AlgoTreeNode before = (AlgoTreeNode)parent.getChildBefore(node);
				if(before != null && before.getAlgoLine().getInstruction() == Instruction.ELSE) {
					step--;
				}
				final AlgoTreeNode after = (AlgoTreeNode)parent.getChildAfter(node);
				boolean refreshAfter = after != null && line.getInstruction() == Instruction.IF && after.getAlgoLine().getInstruction() == Instruction.ELSE;
				if(!moveNode(node, step, !refreshAfter)) {
					return;
				}
				if(refreshAfter) {
					moveNode(after, step, true);
				}
			}

		});
		btnDown.addActionListener(new ActionListener() {

			@Override
			public final void actionPerformed(final ActionEvent event) {
				final AlgoTreeNode node = (AlgoTreeNode)tree.getSelectionPaths()[0].getLastPathComponent();
				final AlgoLine line = node.getAlgoLine();
				if(line.getInstruction() == Instruction.ELSE) {
					return;
				}
				int step = 1;
				final AlgoTreeNode parent = (AlgoTreeNode)node.getParent();
				final AlgoTreeNode after = (AlgoTreeNode)parent.getChildAfter(node);
				if(after != null) {
					final Instruction afterInstruction = after.getAlgoLine().getInstruction();
					if(line.getInstruction() == Instruction.IF && afterInstruction == Instruction.ELSE) {
						final AlgoTreeNode afterAfter = (AlgoTreeNode)parent.getChildAfter(after);
						if(afterAfter == null) {
							return;
						}
						else if(afterAfter.getAlgoLine().getInstruction() == Instruction.IF && ((AlgoTreeNode)parent.getChildAfter(afterAfter)).getAlgoLine().getInstruction() == Instruction.ELSE) {
							step++;
						}
						moveNode(after, step, false);
					}
					else if(afterInstruction == Instruction.IF) {
						final AlgoTreeNode afterAfter = (AlgoTreeNode)parent.getChildAfter(after);
						if(afterAfter != null && afterAfter.getAlgoLine().getInstruction() == Instruction.ELSE) {
							step++;
						}
					}
				}
				moveNode(node, step, true);
			}

		});
		btnTest.addActionListener(new ActionListener() {

			@Override
			public final void actionPerformed(final ActionEvent event) {
				new ConsoleFrame().setVisible(true);
			}

		});
		this.setJMenuBar(createEditorMenuBar(tree));
		if(!AlgogoDesktop.SETTINGS.updaterDoNotAutoCheckAgain && !AlgogoDesktop.DEBUG) {
			new GithubUpdater("Skyost", "Algogo", AlgogoDesktop.APP_VERSION, this).start();
		}
	}

	@Override
	public final void lineAdded(final Instruction instruction, final String... args) {
		addNode(new AlgoTreeNode(instruction, args));
	}

	@Override
	public final void lineEdited(final AlgoTreeNode node, final String... args) {
		final AlgoLine line = node.getAlgoLine();
		final String[] currentArgs = line.getArgs();
		if(currentArgs.equals(args)) {
			return;
		}
		if(line.getInstruction() == Instruction.IF && (Boolean.valueOf(currentArgs[1]) && !Boolean.valueOf(args[1]))) {
			final AlgoTreeNode els = ((AlgoTreeNode)((AlgoTreeNode)node.getParent()).getChildAfter(node));
			if(els != null && els.getAlgoLine().getInstruction() == Instruction.ELSE) {
				els.removeFromParent();
			}
		}
		line.setArgs(args);
		Utils.reloadTree(tree, node.getParent());
		algorithmChanged(true);
	}

	@Override
	public final boolean titleChanged(final Algorithm algorithm, final String title, final String newTitle) {
		if(newTitle != null && !newTitle.isEmpty()) {
			this.setTitle(buildTitle(newTitle, algorithm.getAuthor()));
			algorithmChanged(false);
			return true;
		}
		JOptionPane.showMessageDialog(this, String.format(LanguageManager.getString("joptionpane.invalidtitle"), newTitle), LanguageManager.getString("joptionpane.error"), JOptionPane.ERROR_MESSAGE);
		return false;
	}

	@Override
	public final boolean authorChanged(final Algorithm algorithm, final String author, final String newAuthor) {
		if(newAuthor != null && !newAuthor.isEmpty()) {
			this.setTitle(buildTitle(algorithm.getTitle(), newAuthor));
			algorithmChanged(false);
			return true;
		}
		JOptionPane.showMessageDialog(this, String.format(LanguageManager.getString("joptionpane.invalidauthor"), newAuthor), LanguageManager.getString("joptionpane.error"), JOptionPane.ERROR_MESSAGE);
		return false;
	}

	@Override
	public final void updaterStarted() {}

	@Override
	public final void updaterException(final Exception ex) {
		ex.printStackTrace();
		ErrorDialog.errorMessage(this, ex);
	}

	@Override
	public final void updaterResponse(final String response) {}

	@Override
	public final void updaterUpdateAvailable(final String localVersion, final String remoteVersion) {
		try {
			final JCheckBox doNotShowItAgain = new JCheckBox(LanguageManager.getString("joptionpane.updateavailable.objects.donotautocheckagain"));
			doNotShowItAgain.setSelected(AlgogoDesktop.SETTINGS.updaterDoNotAutoCheckAgain);
			JOptionPane.showMessageDialog(this, new Object[]{new JLabelLink(String.format(LanguageManager.getString("joptionpane.updateavailable.message"), remoteVersion, AlgogoDesktop.APP_WEBSITE), new URL(AlgogoDesktop.APP_WEBSITE)), doNotShowItAgain}, LanguageManager.getString("joptionpane.updateavailable.title"), JOptionPane.INFORMATION_MESSAGE);
			final boolean value = doNotShowItAgain.isSelected();
			if(AlgogoDesktop.SETTINGS.updaterDoNotAutoCheckAgain != value) {
				AlgogoDesktop.SETTINGS.updaterDoNotAutoCheckAgain = value;
				AlgogoDesktop.SETTINGS.save();
			}
		}
		catch(final Exception ex) {
			ex.printStackTrace();
			ErrorDialog.errorMessage(this, ex);
		}
	}

	@Override
	public final void updaterNoUpdate(final String localVersion, final String remoteVersion) {}

	/**
	 * Creates the menu bar of the editor.
	 * 
	 * @return The menu.
	 */

	private final JMenuBar createEditorMenuBar(final JComponent component) {
		final int ctrl = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
		final JMenuItem neww = new JMenuItem(LanguageManager.getString("editor.menu.file.new"));
		neww.addActionListener(new ActionListener() {

			@Override
			public final void actionPerformed(final ActionEvent event) {
				resetEditor();
			}

		});
		neww.setIcon(new ImageIcon(AlgogoDesktop.class.getResource("/fr/skyost/algo/desktop/res/icons/menu_new.png")));
		neww.setAccelerator(KeyStroke.getKeyStroke('N', ctrl));
		final JMenuItem open = new JMenuItem(LanguageManager.getString("editor.menu.file.open"));
		open.addActionListener(new ActionListener() {

			@Override
			public final void actionPerformed(final ActionEvent event) {
				final JFileChooser chooser = new JFileChooser();
				chooser.setFileFilter(new FileNameExtensionFilter(LanguageManager.getString("editor.menu.file.filter"), "agg"));
				chooser.setMultiSelectionEnabled(false);
				if(chooser.showOpenDialog(EditorFrame.this) == JFileChooser.APPROVE_OPTION) {
					open(chooser.getSelectedFile());
				}
			}

		});
		open.setIcon(new ImageIcon(AlgogoDesktop.class.getResource("/fr/skyost/algo/desktop/res/icons/menu_open.png")));
		open.setAccelerator(KeyStroke.getKeyStroke('O', ctrl));
		final JMenuItem save = new JMenuItem(LanguageManager.getString("editor.menu.file.save"));
		save.addActionListener(new ActionListener() {

			@Override
			public final void actionPerformed(final ActionEvent event) {
				if(algoPath == null || !Files.isWritable(Paths.get(algoPath))) {
					saveAs();
					return;
				}
				save(new File(algoPath));
			}

		});
		save.setIcon(new ImageIcon(AlgogoDesktop.class.getResource("/fr/skyost/algo/desktop/res/icons/menu_save.png")));
		save.setAccelerator(KeyStroke.getKeyStroke('S', ctrl));
		final JMenuItem saveAs = new JMenuItem(LanguageManager.getString("editor.menu.file.saveas"));
		saveAs.addActionListener(new ActionListener() {

			@Override
			public final void actionPerformed(final ActionEvent event) {
				saveAs();
			}

		});
		final JMenu export = new JMenu(LanguageManager.getString("editor.menu.file.export"));
		for(final AlgorithmLanguage language : new AlgorithmLanguage[]{new PHPLanguage(), new JavaScriptLanguage(), new JavaLanguage()}) {
			final String name = language.getName();
			final JMenuItem subMenu = new JMenuItem(name);
			subMenu.addActionListener(new ActionListener() {
				
				@Override
				public final void actionPerformed(final ActionEvent event) {
					try {
						final String extension = language.getExtension();
						final JFileChooser chooser = new JFileChooser();
						chooser.setFileFilter(new FileNameExtensionFilter(String.format(LanguageManager.getString("editor.menu.file.export.filter"), name, extension), extension));
						chooser.setMultiSelectionEnabled(false);
						if(chooser.showSaveDialog(EditorFrame.this) == JFileChooser.APPROVE_OPTION) {
							String path = chooser.getSelectedFile().getPath();
							if(!path.endsWith("." + extension)) {
								path += "." + extension;
							}
							Files.write(Paths.get(path), algorithm.toLanguage(language).getBytes(StandardCharsets.UTF_8));
						}
					}
					catch(final Exception ex) {
						ex.printStackTrace();
						ErrorDialog.errorMessage(EditorFrame.this, ex);
					}
				}
				
			});
			export.add(subMenu);
		}
		export.setIcon(new ImageIcon(AlgogoDesktop.class.getResource("/fr/skyost/algo/desktop/res/icons/menu_export.png")));
		final JMenuItem print = new JMenuItem(LanguageManager.getString("editor.menu.file.print"));
		print.addActionListener(new ActionListener() {

			@Override
			public final void actionPerformed(final ActionEvent event) {
				try {
					final StringBuilder builder = new StringBuilder();
					builder.append(Utils.getNodeContent(variables, new StringBuilder()));
					builder.append(Utils.getNodeContent(beginning, new StringBuilder()));
					builder.append(new AlgoTreeNode(Keyword.END));
					final PrintRequestAttributeSet printRequest = new HashPrintRequestAttributeSet();
					final DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
					final PrintService service = ServiceUI.printDialog(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration(), 200, 200, PrintServiceLookup.lookupPrintServices(flavor, printRequest), PrintServiceLookup.lookupDefaultPrintService(), flavor, printRequest);
					if(service != null) {
						service.createPrintJob().print(new SimpleDoc(new ByteArrayInputStream(new String("<html>" + builder.toString().replace("<html>", "").replace("</html>", "") + "</html>").getBytes(StandardCharsets.UTF_8)), flavor, new HashDocAttributeSet()), printRequest);
					}
				}
				catch(final Exception ex) {
					ex.printStackTrace();
					ErrorDialog.errorMessage(EditorFrame.this, ex);
				}
			}

		});
		print.setIcon(new ImageIcon(AlgogoDesktop.class.getResource("/fr/skyost/algo/desktop/res/icons/menu_print.png")));
		print.setAccelerator(KeyStroke.getKeyStroke('P', ctrl));
		final JMenuItem close = new JMenuItem(LanguageManager.getString("editor.menu.file.close"));
		close.addActionListener(new ActionListener() {

			@Override
			public final void actionPerformed(final ActionEvent event) {
				System.exit(0);
			}

		});
		close.setIcon(new ImageIcon(AlgogoDesktop.class.getResource("/fr/skyost/algo/desktop/res/icons/menu_close.png")));
		close.setAccelerator(KeyStroke.getKeyStroke('Q', ctrl));
		final JMenuItem options = new JMenuItem(LanguageManager.getString("editor.menu.edit.options"));
		options.addActionListener(new ActionListener() {

			@Override
			public final void actionPerformed(final ActionEvent event) {
				new OptionsDialog().setVisible(true);
			}

		});
		options.setIcon(new ImageIcon(AlgogoDesktop.class.getResource("/fr/skyost/algo/desktop/res/icons/menu_options.png")));
		final JMenuItem paste = new JMenuItem(LanguageManager.getString("editor.menu.edit.paste"));
		final ActionListener pasteActionListener = new ActionListener() {

			@Override
			public final void actionPerformed(final ActionEvent event) {
				final TreePath[] paths = tree.getSelectionPaths();
				if(paths == null) {
					return;
				}
				for(final AlgoTreeNode node : clipboard) {
					addNode(node.clone()); // We clone it twice.
				}
			}
			
		};
		paste.addActionListener(pasteActionListener);
		paste.setIcon(new ImageIcon(AlgogoDesktop.class.getResource("/fr/skyost/algo/desktop/res/icons/menu_paste.png")));
		paste.setAccelerator(KeyStroke.getKeyStroke('V', ctrl));
		component.getInputMap().put(KeyStroke.getKeyStroke('V', ctrl), "pasteActionListener");
		paste.setEnabled(false);
		final JMenuItem cut = new JMenuItem(LanguageManager.getString("editor.menu.edit.cut"));
		final ActionListener cutActionListener = new ActionListener() {

			@Override
			public final void actionPerformed(final ActionEvent event) {
				final TreePath[] paths = tree.getSelectionPaths();
				if(paths == null || paths.length < 0) {
					return;
				}
				final AlgoTreeNode selected = (AlgoTreeNode)paths[0].getLastPathComponent();
				if(selected.equals(variables) || selected.equals(beginning) || selected.equals(end)) {
					return;
				}
				clipboard.clear();
				for(final TreePath path : paths) {
					final AlgoTreeNode node = (AlgoTreeNode)path.getLastPathComponent();
					clipboard.add(node.clone());
					removeNode(node);
				}
				paste.setEnabled(true);
			}
			
		};
		cut.addActionListener(cutActionListener);
		cut.setIcon(new ImageIcon(AlgogoDesktop.class.getResource("/fr/skyost/algo/desktop/res/icons/menu_cut.png")));
		cut.setAccelerator(KeyStroke.getKeyStroke('X', ctrl));
		component.getInputMap().put(KeyStroke.getKeyStroke('X', ctrl), "cutActionListener");
		final JMenuItem copy = new JMenuItem(LanguageManager.getString("editor.menu.edit.copy"));
		final ActionListener copyActionListener = new ActionListener() {

			@Override
			public final void actionPerformed(final ActionEvent event) {
				final TreePath[] paths = tree.getSelectionPaths();
				if(paths == null || paths.length < 0) {
					return;
				}
				final AlgoTreeNode selected = (AlgoTreeNode)paths[0].getLastPathComponent();
				if(selected.equals(variables) || selected.equals(beginning) || selected.equals(end)) {
					return;
				}
				clipboard.clear();
				for(final TreePath path : paths) {
					clipboard.add(((AlgoTreeNode)path.getLastPathComponent()).clone());
				}
				paste.setEnabled(true);
			}
			
		};
		copy.addActionListener(copyActionListener);
		copy.setIcon(new ImageIcon(AlgogoDesktop.class.getResource("/fr/skyost/algo/desktop/res/icons/menu_copy.png")));
		copy.setAccelerator(KeyStroke.getKeyStroke('C', ctrl));
		component.getInputMap().put(KeyStroke.getKeyStroke('C', ctrl), "copyActionListener");
		final JMenuItem checkForUpdates = new JMenuItem(LanguageManager.getString("editor.menu.help.checkforupdates"));
		checkForUpdates.addActionListener(new ActionListener() {

			@Override
			public final void actionPerformed(final ActionEvent event) {
				new GithubUpdater("Skyost", "Algogo", AlgogoDesktop.APP_VERSION, new GithubUpdaterResultListener() {

					@Override
					public final void updaterStarted() {}

					@Override
					public final void updaterException(final Exception ex) {
						ex.printStackTrace();
						ErrorDialog.errorMessage(EditorFrame.this, ex);
					}

					@Override
					public final void updaterResponse(final String response) {}

					@Override
					public final void updaterUpdateAvailable(final String localVersion, final String remoteVersion) {
						try {
							JOptionPane.showMessageDialog(EditorFrame.this, new Object[]{new JLabelLink(String.format(LanguageManager.getString("joptionpane.updateavailable.message"), remoteVersion, AlgogoDesktop.APP_WEBSITE), new URL(AlgogoDesktop.APP_WEBSITE))}, LanguageManager.getString("joptionpane.updateavailable.title"), JOptionPane.INFORMATION_MESSAGE);
						}
						catch(final Exception ex) {
							ex.printStackTrace();
						}
					}

					@Override
					public final void updaterNoUpdate(final String localVersion, final String remoteVersion) {
						JOptionPane.showMessageDialog(EditorFrame.this, LanguageManager.getString("joptionpane.updatenotavailable.message"), LanguageManager.getString("joptionpane.updatenotavailable.title"), JOptionPane.INFORMATION_MESSAGE);
					}

				}).start();
			}

		});
		checkForUpdates.setIcon(new ImageIcon(AlgogoDesktop.class.getResource("/fr/skyost/algo/desktop/res/icons/menu_checkforupdates.png")));
		final JMenuItem about = new JMenuItem(LanguageManager.getString("editor.menu.help.about"));
		about.addActionListener(new ActionListener() {

			@Override
			public final void actionPerformed(final ActionEvent event) {
				new AboutDialog().setVisible(true);
			}

		});
		about.setIcon(new ImageIcon(AlgogoDesktop.class.getResource("/fr/skyost/algo/desktop/res/icons/menu_about.png")));
		final JMenuBar menuBar = new JMenuBar();
		final JMenu file = new JMenu(LanguageManager.getString("editor.menu.file"));
		file.add(neww);
		file.add(open);
		file.add(save);
		file.add(saveAs);
		file.addSeparator();
		file.add(export);
		file.add(print);
		file.addSeparator();
		file.add(close);
		menuBar.add(file);
		final JMenu edit = new JMenu(LanguageManager.getString("editor.menu.edit"));
		edit.add(options);
		edit.addSeparator();
		edit.add(cut);
		edit.add(copy);
		edit.add(paste);
		menuBar.add(edit);
		final JMenu help = new JMenu(LanguageManager.getString("editor.menu.help"));
		help.add(checkForUpdates);
		help.add(about);
		menuBar.add(help);
		return menuBar;
	}
	
	/**
	 * Adds a node to the editor.
	 * 
	 * @param node The editor.
	 */
	
	private final void addNode(final AlgoTreeNode node) {
		final AlgoLine line = node.getAlgoLine();
		final Instruction instruction = line.getInstruction();
		if(instruction == Instruction.CREATE_VARIABLE) {
			variables.add(node);
			Utils.reloadTree(tree, variables);
			tree.expandPath(new TreePath(variables.getPath()));
			algorithmChanged(true);
			tree.setSelectionPath(new TreePath(node.getPath()));
			return;
		}
		final AlgoTreeNode selected = (AlgoTreeNode)tree.getLastSelectedPathComponent();
		if(selected == null || selected.equals(variables) || selected.equals(beginning) || selected.equals(end)) {
			beginning.add(node);
			Utils.reloadTree(tree, beginning);
			tree.expandPath(new TreePath(beginning.getPath()));
			algorithmChanged(true);
			tree.setSelectionPath(new TreePath(node.getPath()));
			return;
		}
		if(selected.getAllowsChildren() && instruction != Instruction.ELSE) {
			selected.add(node);
			Utils.reloadTree(tree, selected);
			tree.expandPath(new TreePath(selected.getPath()));
		}
		else {
			AlgoTreeNode parent = (AlgoTreeNode)selected.getParent();
			if(parent.equals(variables)) {
				parent = beginning;
			}
			parent.insert(node, parent.getIndex(selected) + 1);
			Utils.reloadTree(tree, parent);
			tree.expandPath(new TreePath(parent.getPath()));
		}
		algorithmChanged(true);
		tree.setSelectionPath(new TreePath(node.getPath()));
	}
	
	/**
	 * Removes a node from the editor.
	 * 
	 * @param node The editor.
	 */
	
	private final void removeNode(final AlgoTreeNode node) {
		final AlgoLine line = node.getAlgoLine();
		if(line.getInstruction() == Instruction.IF && Boolean.valueOf(line.getArgs()[1])) {
			final AlgoTreeNode parent = (AlgoTreeNode)node.getParent();
			((AlgoTreeNode)parent.getChildAt(parent.getIndex(node) + 1)).removeFromParent();
		}
		node.removeFromParent();
		Utils.reloadTree(tree, node.getParent());
		algorithmChanged(true);
	}
	
	/**
	 * Resets the editor (algorithm, tree, ...).
	 */
	
	private final void resetEditor() {
		if(variables != null) {
			variables.removeAllChildren();
		}
		if(beginning != null) {
			beginning.removeAllChildren();
		}
		if(tree != null) {
			Utils.reloadTree(tree);
		}
		algoPath = null;
		algoChanged = false;
		algorithm = new Algorithm(LanguageManager.getString("editor.defaultalgorithm.untitled"), LanguageManager.getString("editor.defaultalgorithm.anonymous"));
		algorithm.addOptionsListener(this);
		this.setTitle(buildTitle());
		final DefaultMutableTreeNode root = new DefaultMutableTreeNode();
		root.add(variables = new AlgoTreeNode(algorithm.getVariables()));
		root.add(beginning = new AlgoTreeNode(algorithm.getInstructions()));
		root.add(end = new AlgoTreeNode(Keyword.END));
		tree = new JTree(root);
		tree.setShowsRootHandles(true);
		tree.setRootVisible(false);
		tree.addTreeSelectionListener(new TreeSelectionListener() {

			@Override
			public final void valueChanged(final TreeSelectionEvent event) {
				final TreePath path = event.getNewLeadSelectionPath();
				if(path == null) {
					for(final JButton button : new JButton[]{btnRemoveLine, btnEditLine, btnUp, btnDown}) {
						button.setEnabled(false);
					}
					return;
				}
				final AlgoTreeNode selected = (AlgoTreeNode)path.getLastPathComponent();
				final boolean enabled = !selected.equals(variables) && !selected.equals(beginning) && !selected.equals(end);
				for(final JButton button : new JButton[]{btnRemoveLine, btnEditLine, btnUp, btnDown}) {
					button.setEnabled(enabled);
				}
			}

		});
		final DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer)tree.getCellRenderer();
		renderer.setLeafIcon(null);
		renderer.setClosedIcon(null);
		renderer.setOpenIcon(null);
		scrollPane.setViewportView(tree);
		Utils.reloadTree(tree);
	}

	/**
	 * Builds the current title for the current algorithm.
	 * 
	 * @return The current title.
	 */

	private static final String buildTitle() {
		return buildTitle(algorithm.getTitle(), algorithm.getAuthor());
	}

	/**
	 * Builds a title for the specified title and author.
	 * 
	 * @param title The title.
	 * @param author The author.
	 * 
	 * @return A title.
	 */

	private static final String buildTitle(final String title, final String author) {
		return String.format(LanguageManager.getString("editor.title"), algoChanged ? "* " : "", title, author, AlgogoDesktop.APP_NAME, AlgogoDesktop.APP_VERSION);
	}

	/**
	 * Loads an algorithm from a file.
	 * 
	 * @param file The file.
	 */

	public final void open(final File file) {
		try {
			final String path = file.getPath();
			algorithm = Algorithm.fromJSON(Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8).get(0));
			algorithm.addOptionsListener(EditorFrame.this);
			variables.removeAllChildren();
			variables.setAlgoLine(algorithm.getVariables());
			beginning.removeAllChildren();
			beginning.setAlgoLine(algorithm.getInstructions());
			for(final AlgoLine variable : algorithm.getVariables().getChildren()) {
				variables.add(new AlgoTreeNode(variable), false);
			}
			for(final AlgoLine instruction : algorithm.getInstructions().getChildren()) {
				beginning.add(new AlgoTreeNode(instruction), false);
			}
			algoPath = path;
			algoChanged = false;
			Utils.reloadTree(tree);
			EditorFrame.this.setTitle(buildTitle());
		}
		catch(final Exception ex) {
			ex.printStackTrace();
			ErrorDialog.errorMessage(EditorFrame.this, ex);
		}
	}

	/**
	 * Saves an algorithm to a file.
	 * 
	 * @param file The file.
	 */

	public final void save(final File file) {
		try {
			String path = file.getPath();
			if(!path.endsWith(".agg")) {
				path += ".agg";
			}
			if(file.exists()) {
				file.delete();
				file.createNewFile();
			}
			Files.write(Paths.get(path), algorithm.toJSON().toString().getBytes(StandardCharsets.UTF_8));
			algoPath = path;
			algoChanged = false;
			EditorFrame.this.setTitle(buildTitle());
		}
		catch(final Exception ex) {
			ex.printStackTrace();
			ErrorDialog.errorMessage(this, ex);
		}
	}

	/**
	 * Saves an algorithm with a dialog.
	 */

	public final void saveAs() {
		final JFileChooser chooser = new JFileChooser();
		chooser.setFileFilter(new FileNameExtensionFilter(LanguageManager.getString("editor.menu.file.filter"), "agg"));
		chooser.setMultiSelectionEnabled(false);
		if(chooser.showSaveDialog(EditorFrame.this) == JFileChooser.APPROVE_OPTION) {
			save(chooser.getSelectedFile());
		}
	}

	/**
	 * Moves a node (into the tree).
	 * 
	 * @param node The node.
	 * @param step Steps (forward or backward).
	 * @param reload Reload the tree and sets the selection path.
	 * 
	 * @return <b>true</b> If the node has successfully moved.
	 * <br><b>false</b> Otherwise.
	 */

	private final boolean moveNode(final AlgoTreeNode node, final int step, final boolean reload) {
		if(step == 0) {
			return false;
		}
		final AlgoTreeNode parent = (AlgoTreeNode)node.getParent();
		final int index = parent.getIndex(node) + step;
		if(index > parent.getChildCount() - 1 || index < 0) {
			return false;
		}
		final TreePath path = tree.getSelectionPath();
		((DefaultTreeModel)tree.getModel()).insertNodeInto(node, parent, index);
		if(reload) {
			Utils.reloadTree(tree, parent);
			tree.setSelectionPath(path);
			algorithmChanged(true);
		}
		return true;
	}

	/**
	 * Must be called when the algorithm is changed.
	 * 
	 * @param setTitle <b>true</b> If you want to change the editor's title.
	 * <br><b>false</b> Otherwise.
	 */

	public final void algorithmChanged(final boolean setTitle) {
		algoChanged = true;
		if(setTitle) {
			this.setTitle(buildTitle());
		}
	}

}