package xyz.algogo.desktop.frames;

import java.awt.Color;
import java.awt.Container;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.KeyStroke;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;

import org.fife.ui.rsyntaxtextarea.AbstractTokenMakerFactory;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxScheme;
import org.fife.ui.rsyntaxtextarea.Token;
import org.fife.ui.rsyntaxtextarea.TokenMakerFactory;

import com.wordpress.tips4java.TextLineNumber;

import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import xyz.algogo.core.AlgoLine;
import xyz.algogo.core.Algorithm;
import xyz.algogo.core.Instruction;
import xyz.algogo.core.formats.AlgorithmFileFormat;
import xyz.algogo.core.formats.AlgorithmFileFormat.InvalidAlgorithmVersionException;
import xyz.algogo.core.language.AlgorithmLanguage;
import xyz.algogo.desktop.AlgogoDesktop;
import xyz.algogo.desktop.AppSettings;
import xyz.algogo.desktop.dialogs.ErrorDialog;
import xyz.algogo.desktop.frames.listeners.button.*;
import xyz.algogo.desktop.frames.listeners.menu.*;
import xyz.algogo.desktop.dialogs.AddLineDialog.AlgoLineListener;
import xyz.algogo.desktop.utils.AlgoLineUtils;
import xyz.algogo.desktop.utils.AlgorithmParser;
import xyz.algogo.desktop.utils.AlgorithmParser.ParseException;
import xyz.algogo.desktop.utils.AlgorithmTree;
import xyz.algogo.desktop.utils.AlgorithmTree.AlgorithmUserObject;
import xyz.algogo.desktop.utils.GithubUpdater;
import xyz.algogo.desktop.utils.JLabelLink;
import xyz.algogo.desktop.utils.LanguageManager;
import xyz.algogo.desktop.utils.SizedStack;
import xyz.algogo.desktop.utils.TextLanguage;
import xyz.algogo.desktop.utils.Utils;
import xyz.algogo.desktop.utils.GithubUpdater.GithubUpdaterResultListener;
import xyz.algogo.desktop.utils.HtmlLanguage;

import javax.swing.JMenuBar;

public class EditorFrame extends JFrame implements AlgoLineListener {

	private static final long serialVersionUID = 1L;

	private final SizedStack<Algorithm> algorithms = new SizedStack<Algorithm>(AlgogoDesktop.getSettings().undoHistory);
	private Algorithm algorithm;

	protected String algoPath;
	private boolean algoChanged;
	private boolean freeEditMode;

	private final AlgorithmTree tree = new AlgorithmTree();
	private final RSyntaxTextArea textArea = new RSyntaxTextArea();

	private final List<AlgoLine> clipboard = new ArrayList<AlgoLine>();

	private final JScrollPane scrollPane = new JScrollPane();
	
	private final JMenu recents = new JMenu(LanguageManager.getString("editor.menu.file.recents"));
	private final JMenuItem undo = new JMenuItem(LanguageManager.getString("editor.menu.edit.undo"));
	
	private final JButton btnAddLine = new JButton(LanguageManager.getString("editor.button.addline"));
	private final JButton btnRemoveLine = new JButton(LanguageManager.getString("editor.button.removelines"));
	private final JButton btnEditLine = new JButton(LanguageManager.getString("editor.button.editline"));
	private final JButton btnUp = new JButton(LanguageManager.getString("editor.button.up"));
	private final JButton btnDown = new JButton(LanguageManager.getString("editor.button.down"));
	private final JButton btnTest = new JButton(LanguageManager.getString("editor.button.test"));
	
	private final JMenuBar editorMenu = createEditorMenuBar();
	private final JMenuBar textAreaMenu = createTextAreaMenuBar();

	public EditorFrame() {
		this.setIconImages(AlgogoDesktop.ICONS);
		this.setSize(800, 600);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setLocationRelativeTo(null);
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
				final DefaultMutableTreeNode selected = (DefaultMutableTreeNode)path.getLastPathComponent();
				final boolean enabled = !selected.equals(tree.variables) && !selected.equals(tree.beginning) && !selected.equals(tree.end);
				for(final JButton button : new JButton[]{btnRemoveLine, btnEditLine, btnUp, btnDown}) {
					button.setEnabled(enabled);
				}
				if(tree.getSelectionCount() > 1) {
					for(final JButton button : new JButton[]{btnEditLine, btnUp, btnDown}) {
						button.setEnabled(false);
					}
				}
			}

		});
		tree.addKeyListener(new KeyListener() {

			@Override
			public final void keyPressed(final KeyEvent event) {}

			@Override
			public final void keyReleased(final KeyEvent event) {}

			@Override
			public final void keyTyped(final KeyEvent event) {
				if(event.getKeyChar() == '\177') {
					btnRemoveLine.getActionListeners()[0].actionPerformed(null);
				}
			}

		});
		final DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer)tree.getCellRenderer();
		renderer.setLeafIcon(null);
		renderer.setClosedIcon(null);
		renderer.setOpenIcon(null);
		renderer.setBorderSelectionColor(Color.decode("#95A5A6"));
		renderer.setBackgroundSelectionColor(Color.decode("#BDC3C7"));
		resetEditor();
		btnAddLine.setIcon(new ImageIcon(AlgogoDesktop.class.getResource("/xyz/algogo/desktop/res/icons/btn_add.png")));
		btnRemoveLine.setIcon(new ImageIcon(AlgogoDesktop.class.getResource("/xyz/algogo/desktop/res/icons/btn_remove.png")));
		btnRemoveLine.setEnabled(false);
		btnEditLine.setIcon(new ImageIcon(AlgogoDesktop.class.getResource("/xyz/algogo/desktop/res/icons/btn_edit.png")));
		btnEditLine.setEnabled(false);
		btnUp.setIcon(new ImageIcon(AlgogoDesktop.class.getResource("/xyz/algogo/desktop/res/icons/btn_up.png")));
		btnUp.setEnabled(false);
		btnDown.setIcon(new ImageIcon(AlgogoDesktop.class.getResource("/xyz/algogo/desktop/res/icons/btn_down.png")));
		btnDown.setEnabled(false);
		btnTest.setIcon(new ImageIcon(AlgogoDesktop.class.getResource("/xyz/algogo/desktop/res/icons/btn_test.png")));
		final Container content = this.getContentPane();
		final GroupLayout groupLayout = new GroupLayout(content);
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout.createSequentialGroup().addContainerGap().addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE).addPreferredGap(ComponentPlacement.RELATED).addGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false).addComponent(btnAddLine, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 163, Short.MAX_VALUE).addComponent(btnUp, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(btnEditLine, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(btnRemoveLine, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 163, Short.MAX_VALUE).addComponent(btnDown, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 163, Short.MAX_VALUE)).addComponent(btnTest, GroupLayout.PREFERRED_SIZE, 163, GroupLayout.PREFERRED_SIZE)).addContainerGap()));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout.createSequentialGroup().addContainerGap().addGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout.createSequentialGroup().addComponent(btnAddLine).addPreferredGap(ComponentPlacement.RELATED).addComponent(btnRemoveLine).addPreferredGap(ComponentPlacement.RELATED).addComponent(btnEditLine).addGap(18).addComponent(btnUp).addPreferredGap(ComponentPlacement.RELATED).addComponent(btnDown).addPreferredGap(ComponentPlacement.RELATED, 334, Short.MAX_VALUE).addComponent(btnTest)).addComponent(scrollPane, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 508, Short.MAX_VALUE)).addGap(21)));
		content.setLayout(groupLayout);
		this.addWindowListener(new WindowAdapter() {

			@Override
			public final void windowClosing(final WindowEvent event) {
				closeEditor();
			}

		});
		btnAddLine.addActionListener(new ButtonAddLineListener(this));
		btnRemoveLine.addActionListener(new ButtonRemoveLineListener(this));
		btnEditLine.addActionListener(new ButtonEditLineListener(this));
		btnUp.addActionListener(new ButtonUpListener(this));
		btnDown.addActionListener(new ButtonDownListener(this));
		btnTest.addActionListener(new ButtonTestListener(this));
		this.setJMenuBar(editorMenu);
		setupHighlighter();
		if(!AlgogoDesktop.getSettings().updaterDoNotAutoCheckAgain) {
			new GithubUpdater(AlgogoDesktop.APP_VERSION, new DefaultGithubUpdater()).start();
		}
	}

	@Override
	public final void lineAdded(final Instruction instruction, final String... args) {
		addAlgorithmToStack();
		addNode(new AlgoLine(instruction, args));
	}

	@Override
	public final void nodeEdited(final DefaultMutableTreeNode node, final String... args) {
		final AlgoLine line = AlgorithmTree.getAttachedAlgoLine(node);
		final String[] currentArgs = line.getArgs();
		if(currentArgs == args) {
			return;
		}
		addAlgorithmToStack();
		if(line.getInstruction() == Instruction.IF) {
			if(Boolean.valueOf(currentArgs[1]) && !Boolean.valueOf(args[1])) {
				final DefaultMutableTreeNode elsee = ((DefaultMutableTreeNode)((DefaultMutableTreeNode)node.getParent()).getChildAfter(node));
				if(elsee != null && AlgorithmTree.getAttachedAlgoLine(elsee).getInstruction() == Instruction.ELSE) {
					elsee.removeFromParent();
				}
			}
			else if(!Boolean.valueOf(currentArgs[1]) && Boolean.valueOf(args[1])) {
				final DefaultMutableTreeNode parent = (DefaultMutableTreeNode)node.getParent();
				parent.insert(new DefaultMutableTreeNode(new AlgorithmUserObject(new AlgoLine(Instruction.ELSE))), parent.getIndex(node) + 1);
			}
		}
		line.setArgs(args);
		algorithmChanged(true, true, true, (DefaultMutableTreeNode)node.getParent(), new TreePath(node.getPath()));
	}

	/**
	 * Setups the syntax highlighter.
	 */
	
	public final void setupHighlighter() {
		final AbstractTokenMakerFactory tokenMaker = (AbstractTokenMakerFactory)TokenMakerFactory.getDefaultInstance();
		tokenMaker.putMapping("text/algorithm", "xyz.algogo.desktop.res.lang.highlighting." + LanguageManager.getCurrentLanguageCode());
		textArea.setSyntaxEditingStyle("text/algorithm");
		textArea.setFont(textArea.getFont().deriveFont(12f));
		
		final SyntaxScheme scheme = textArea.getSyntaxScheme();
		scheme.getStyle(Token.RESERVED_WORD).foreground = Color.decode(AlgoLineUtils.KEYWORD_COLOR);
		scheme.getStyle(Token.RESERVED_WORD_2).foreground = Color.decode(AlgoLineUtils.INSTRUCTION_COLOR_1);
		scheme.getStyle(Token.DATA_TYPE).foreground = Color.decode(AlgoLineUtils.INSTRUCTION_COLOR_2);
		
		textArea.revalidate();
	}

	/**
	 * Creates the menu bar of the editor.
	 * 
	 * @return The menu.
	 */

	private final JMenuBar createEditorMenuBar() {
		final HashMap<KeyStroke, ActionListener> listeners = new HashMap<KeyStroke, ActionListener>();
		final int ctrl = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
		
		final JMenuItem neww = new JMenuItem(LanguageManager.getString("editor.menu.file.new"));
		neww.addActionListener(new FileNewListener(this));
		neww.setIcon(new ImageIcon(AlgogoDesktop.class.getResource("/xyz/algogo/desktop/res/icons/menu_new.png")));
		neww.setAccelerator(KeyStroke.getKeyStroke('N', ctrl));
		
		final JMenuItem open = new JMenuItem(LanguageManager.getString("editor.menu.file.open"));
		open.addActionListener(new FileOpenListener(this));
		open.setIcon(new ImageIcon(AlgogoDesktop.class.getResource("/xyz/algogo/desktop/res/icons/menu_open.png")));
		open.setAccelerator(KeyStroke.getKeyStroke('O', ctrl));
		
		final JMenuItem save = new JMenuItem(LanguageManager.getString("editor.menu.file.save"));
		save.addActionListener(new FileSaveListener(this));
		save.setIcon(new ImageIcon(AlgogoDesktop.class.getResource("/xyz/algogo/desktop/res/icons/menu_save.png")));
		save.setAccelerator(KeyStroke.getKeyStroke('S', ctrl));
		
		final JMenuItem saveAs = new JMenuItem(LanguageManager.getString("editor.menu.file.saveas"));
		saveAs.addActionListener(new FileSaveAsListener(this));
		
		final JMenu export = new JMenu(LanguageManager.getString("editor.menu.file.export"));
		final List<AlgorithmLanguage> languages = new ArrayList<AlgorithmLanguage>(Arrays.asList(AlgorithmLanguage.DEFAULT_LANGUAGES));
		languages.add(new TextLanguage());
		languages.add(new HtmlLanguage());
		for(final AlgorithmLanguage language : languages) {
			final String name = language.getName();
			final JMenuItem subMenu = new JMenuItem(name);
			subMenu.addActionListener(new FileExportLanguageListener(this, language));
			export.add(subMenu);
		}
		
		final JMenuItem exportToImage = new JMenuItem(LanguageManager.getString("editor.menu.file.export.image.title"));
		exportToImage.addActionListener(new FileExportImageListener(this));
		export.setIcon(new ImageIcon(AlgogoDesktop.class.getResource("/xyz/algogo/desktop/res/icons/menu_export.png")));
		
		final JMenuItem print = new JMenuItem(LanguageManager.getString("editor.menu.file.print.title"));
		print.addActionListener(new FilePrintListener(this));
		print.setIcon(new ImageIcon(AlgogoDesktop.class.getResource("/xyz/algogo/desktop/res/icons/menu_print.png")));
		print.setAccelerator(KeyStroke.getKeyStroke('P', ctrl));
		
		final JMenuItem close = new JMenuItem(LanguageManager.getString("editor.menu.file.close"));
		close.addActionListener(new FileCloseListener(this));
		close.setIcon(new ImageIcon(AlgogoDesktop.class.getResource("/xyz/algogo/desktop/res/icons/menu_close.png")));
		close.setAccelerator(KeyStroke.getKeyStroke('Q', ctrl));
		
		final JMenuItem options = new JMenuItem(LanguageManager.getString("editor.menu.edit.options"));
		options.addActionListener(new FileOptionsListener(this));
		options.setIcon(new ImageIcon(AlgogoDesktop.class.getResource("/xyz/algogo/desktop/res/icons/menu_options.png")));
		
		final EditUndoListener undoActionListener = new EditUndoListener(this);
		undo.addActionListener(undoActionListener);
		undo.setIcon(new ImageIcon(AlgogoDesktop.class.getResource("/xyz/algogo/desktop/res/icons/menu_undo.png")));
		undo.setAccelerator(KeyStroke.getKeyStroke('Z', ctrl));
		listeners.put(KeyStroke.getKeyStroke('Z', ctrl), undoActionListener);
		
		final JMenuItem paste = new JMenuItem(LanguageManager.getString("editor.menu.edit.paste"));
		final ActionListener pasteActionListener = new EditPasteListener(this);
		paste.addActionListener(pasteActionListener);
		paste.setIcon(new ImageIcon(AlgogoDesktop.class.getResource("/xyz/algogo/desktop/res/icons/menu_paste.png")));
		paste.setAccelerator(KeyStroke.getKeyStroke('V', ctrl));
		listeners.put(KeyStroke.getKeyStroke('V', ctrl), pasteActionListener);
		paste.setEnabled(false);
		
		final JMenuItem cut = new JMenuItem(LanguageManager.getString("editor.menu.edit.cut"));
		final ActionListener cutActionListener = new EditCopyListener(this, paste, true);
		cut.addActionListener(cutActionListener);
		cut.setIcon(new ImageIcon(AlgogoDesktop.class.getResource("/xyz/algogo/desktop/res/icons/menu_cut.png")));
		cut.setAccelerator(KeyStroke.getKeyStroke('X', ctrl));
		listeners.put(KeyStroke.getKeyStroke('X', ctrl), cutActionListener);
		
		final JMenuItem copy = new JMenuItem(LanguageManager.getString("editor.menu.edit.copy"));
		final ActionListener copyActionListener = new EditCopyListener(this, paste, false);
		copy.addActionListener(copyActionListener);
		copy.setIcon(new ImageIcon(AlgogoDesktop.class.getResource("/xyz/algogo/desktop/res/icons/menu_copy.png")));
		copy.setAccelerator(KeyStroke.getKeyStroke('C', ctrl));
		listeners.put(KeyStroke.getKeyStroke('C', ctrl), copyActionListener);
		
		final JMenuItem preferences = new JMenuItem(LanguageManager.getString("editor.menu.edit.preferences"));
		preferences.addActionListener(new EditPreferencesListener(this));
		preferences.setIcon(new ImageIcon(AlgogoDesktop.class.getResource("/xyz/algogo/desktop/res/icons/menu_preferences.png")));
		
		final JMenuItem freeEdit = new JMenuItem(LanguageManager.getString("editor.menu.edit.freeedit"));
		freeEdit.addActionListener(new EditFreeEditModeListener(this, true));
		/* Emulates the JCheckBoxMenuItem (because this one takes a margin : */
		freeEdit.setIcon(new ImageIcon(AlgogoDesktop.class.getResource("/xyz/algogo/desktop/res/icons/menu_unchecked.png")));
		
		final JMenuItem checkForUpdates = new JMenuItem(LanguageManager.getString("editor.menu.help.checkforupdates"));
		checkForUpdates.addActionListener(new HelpCheckForUpdatesListener(this));
		checkForUpdates.setIcon(new ImageIcon(AlgogoDesktop.class.getResource("/xyz/algogo/desktop/res/icons/menu_checkforupdates.png")));
		
		final JMenuItem onlineHelp = new JMenuItem(LanguageManager.getString("editor.menu.help.onlinehelp"));
		final ActionListener onlineHelpListener = new HelpOnlineHelpListener(this);
		onlineHelp.addActionListener(onlineHelpListener);
		onlineHelp.setIcon(new ImageIcon(AlgogoDesktop.class.getResource("/xyz/algogo/desktop/res/icons/menu_onlinehelp.png")));
		onlineHelp.setAccelerator(KeyStroke.getKeyStroke('H', ctrl));
		listeners.put(KeyStroke.getKeyStroke('H', ctrl), onlineHelpListener);
		
		final JMenuItem about = new JMenuItem(LanguageManager.getString("editor.menu.help.about"));
		about.addActionListener(new HelpAboutListener(this));
		about.setIcon(new ImageIcon(AlgogoDesktop.class.getResource("/xyz/algogo/desktop/res/icons/menu_about.png")));
		
		final JMenuBar menuBar = new JMenuBar();
		final JMenu file = new JMenu(LanguageManager.getString("editor.menu.file"));
		file.add(neww);
		file.add(open);
		file.add(recents);
		file.add(save);
		file.add(saveAs);
		file.addSeparator();
		export.add(exportToImage);
		file.add(export);
		file.add(print);
		file.addSeparator();
		file.add(close);
		menuBar.add(file);
		final JMenu edit = new JMenu(LanguageManager.getString("editor.menu.edit"));
		edit.add(options);
		edit.addSeparator();
		edit.add(undo);
		edit.addSeparator();
		edit.add(cut);
		edit.add(copy);
		edit.add(paste);
		edit.addSeparator();
		edit.add(preferences);
		edit.addSeparator();
		edit.add(freeEdit);
		menuBar.add(edit);
		final JMenu help = new JMenu(LanguageManager.getString("editor.menu.help"));
		help.add(checkForUpdates);
		help.addSeparator();
		help.add(onlineHelp);
		help.add(about);
		menuBar.add(help);
		registerKeyListeners(listeners);
		refreshPaths();
		return menuBar;
	}
	
	private final JMenuBar createTextAreaMenuBar() {
		final HashMap<KeyStroke, ActionListener> listeners = new HashMap<KeyStroke, ActionListener>();
		final JCheckBoxMenuItem freeEdit = new JCheckBoxMenuItem(LanguageManager.getString("editor.menu.edit.freeedit"));
		freeEdit.setSelected(true);
		freeEdit.addActionListener(new ActionListener() {

			@Override
			public final void actionPerformed(final ActionEvent event) {
				freeEdit.setSelected(true);
			}

		});
		freeEdit.addActionListener(new EditFreeEditModeListener(this, false));
		final JMenuBar menuBar = new JMenuBar();
		final JMenu edit = new JMenu(LanguageManager.getString("editor.menu.edit"));
		edit.add(freeEdit);
		menuBar.add(edit);
		registerKeyListeners(listeners);
		return menuBar;
	}
	
	/**
	 * Gets the current algorithm's path on disk.
	 * 
	 * @return The current algorithm's path.
	 */
	
	public final String getAlgorithmPath() {
		return algoPath;
	}
	
	/**
	 * Gets the current algorithm.
	 * 
	 * @return The current algorithm.
	 */
	
	public final Algorithm getAlgorithm() {
		return algorithm;
	}
	
	/**
	 * Gets the tree component (even if not showed).
	 * 
	 * @return The tree component.
	 */
	
	public final AlgorithmTree getCurrentTreeComponent() {
		return tree;
	}
	
	/**
	 * Gets the code editor (even if not showed).
	 * 
	 * @return The code editor.
	 */
	
	public final RSyntaxTextArea getCurrentTextArea() {
		return textArea;
	}
	
	/**
	 * Gets the user's clipboard.
	 * 
	 * @return The user's clipboard.
	 */
	
	public final List<AlgoLine> getClipboard() {
		return clipboard;
	}
	
	/**
	 * Allows you to enable or disable the free edit mode.
	 * 
	 * @param freeEditMode <b>true</b> Will enable the free edit mode.
	 * <br><b>false</b> Will disable the free edit mode.
	 */
	
	public void setFreeEditMode(final boolean freeEditMode) {
		if(this.freeEditMode == freeEditMode) {
			return;
		}
		if(freeEditMode) {
			EditorFrame.this.setJMenuBar(textAreaMenu);
			textArea.setText(getAlgorithm().toLanguage(new TextLanguage(false)).replace("→", "->"));
			scrollPane.setViewportView(textArea);
			scrollPane.setRowHeaderView(new TextLineNumber(textArea));
			textArea.requestFocus();
			textArea.setCaretPosition(0);
			btnAddLine.setEnabled(false);
		}
		else {
			new Thread() {
				
				@Override
				public final void run() {
					final JLabel waitMessage = new JLabel(LanguageManager.getString("parser.waitdialog.message"));
					waitMessage.setHorizontalAlignment(JLabel.CENTER);
					final JDialog waitDialog = new JDialog(EditorFrame.this);
					waitDialog.setTitle(LanguageManager.getString("parser.waitdialog.title"));
					waitDialog.add(waitMessage);
					waitDialog.pack();
					waitDialog.setSize(waitDialog.getWidth() + 10, waitDialog.getHeight() + 10);
					waitDialog.setLocationRelativeTo(EditorFrame.this);
					waitDialog.setVisible(true);
					try {
						final Algorithm current = getAlgorithm();
						final Algorithm parsed = new AlgorithmParser().parse(current.getTitle(), current.getAuthor(), textArea.getText());
						if(!parsed.equals(current)) {
							addAlgorithmToStack();
							tree.fromAlgorithm(parsed);
							tree.reload();
							algoChanged = true;
							EditorFrame.this.setTitle(buildTitle());
						}
						textArea.setText(null);
						EditorFrame.this.setJMenuBar(editorMenu);
						scrollPane.setViewportView(tree);
						scrollPane.setRowHeaderView(null);
						btnAddLine.setEnabled(true);
					}
					catch(final ParseException ex) {
						waitDialog.dispose();
						JOptionPane.showMessageDialog(EditorFrame.this, LanguageManager.getString("parser.error", ex.getCurrentLine(), ex.getMessage()), LanguageManager.getString("joptionpane.error"), JOptionPane.ERROR_MESSAGE);
						return;
					}
					catch(final Exception ex) {
						waitDialog.dispose();
						ErrorDialog.errorMessage(EditorFrame.this, ex);
						return;
					}
					waitDialog.dispose();
				}
				
			}.start();
		}
		this.freeEditMode = freeEditMode;
		btnTest.setEnabled(!freeEditMode);
		EditorFrame.this.revalidate();
	}
	
	/**
	 * Attributes the specified actions to the corresponding keys.
	 * 
	 * @param listeners A map containing the actions and the keys.
	 */

	private final void registerKeyListeners(final Map<KeyStroke, ActionListener> listeners) {
		for(final Entry<KeyStroke, ActionListener> entry : listeners.entrySet()) {
			tree.getInputMap().put(entry.getKey(), entry.getValue());
		}
	}

	/**
	 * Adds a node to the editor.
	 * 
	 * @param line The line from within the node will be created.
	 * 
	 * @return The node that has changed.
	 */

	public final DefaultMutableTreeNode addNode(final AlgoLine line) {
		return addNode(line, true);
	}
	
	/**
	 * Adds a node to the editor.
	 * 
	 * @param line The line from within the node will be created.
	 * @param update If the editor should be updated.
	 * 
	 * @return The node that has changed.
	 */
	
	public final DefaultMutableTreeNode addNode(final AlgoLine line, final boolean update) {
		final DefaultMutableTreeNode node = new DefaultMutableTreeNode(new AlgorithmUserObject(line));
		final Instruction instruction = line.getInstruction();
		if(instruction == Instruction.CREATE_VARIABLE) {
			tree.variables.add(node);
			algorithmChanged(true, true, true, tree.variables, new TreePath(node.getPath()));
			return tree.variables;
		}
		final List<AlgoLine> children = line.getChildren();
		if(children != null && children.size() > 0) {
			for(final AlgoLine child : children) {
				node.add(AlgorithmTree.asMutableTreeNode(child));
			}
		}
		final DefaultMutableTreeNode selected = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
		if(selected == null || selected.equals(tree.variables) || selected.equals(tree.beginning) || selected.equals(tree.end)) {
			tree.beginning.add(node);
			if(update) {
				algorithmChanged(true, true, true, tree.beginning, new TreePath(node.getPath()));
			}
			return tree.beginning;
		}
		final DefaultMutableTreeNode changed;
		if(AlgorithmTree.getAttachedAlgoLine(selected).getAllowsChildren() && instruction != Instruction.ELSE) {
			selected.add(node);
			changed = selected;
		}
		else {
			DefaultMutableTreeNode parent = (DefaultMutableTreeNode)selected.getParent();
			if(parent.equals(tree.variables)) {
				parent = tree.beginning;
			}
			parent.insert(node, parent.getIndex(selected) + 1);
			changed = parent;
		}
		if(update) {
			algorithmChanged(true, true, true, changed, new TreePath(node.getPath()));
		}
		return changed;
	}
	
	/**
	 * Removes a node from the editor.
	 * 
	 * @param node The node.
	 */

	public final void removeNode(final DefaultMutableTreeNode node) {
		removeNode(node, true);
	}
	
	/**
	 * Removes a node from the editor.
	 * 
	 * @param node The node.
	 * @param update If the editor should be updated.
	 */
	
	public final void removeNode(final DefaultMutableTreeNode node, final boolean update) {
		final AlgoLine line = AlgorithmTree.getAttachedAlgoLine(node);
		if(line.getInstruction() == Instruction.IF && Boolean.valueOf(line.getArgs()[1])) {
			final DefaultMutableTreeNode parent = (DefaultMutableTreeNode)node.getParent();
			((DefaultMutableTreeNode)parent.getChildAt(parent.getIndex(node) + 1)).removeFromParent();
		}
		else if(line.getInstruction() == Instruction.ELSE) {
			final DefaultMutableTreeNode parent = (DefaultMutableTreeNode)node.getParent();
			if(parent != null) {
				final AlgoLine iffLine = AlgorithmTree.getAttachedAlgoLine(((DefaultMutableTreeNode)parent.getChildAt(parent.getIndex(node) - 1)));
				iffLine.setArgs(new String[]{iffLine.getArgs()[0], String.valueOf(false)});
			}
		}
		node.removeFromParent();
		if(update) {
			algorithmChanged(true, true, true, (DefaultMutableTreeNode)node.getParent());
		}
	}

	/**
	 * Resets the editor (algorithm, tree, ...).
	 */

	public final void resetEditor() {
		algoPath = null;
		algoChanged = false;
		freeEditMode = false;
		algorithms.clear();
		algorithm = new Algorithm(LanguageManager.getString("editor.defaultalgorithm.untitled"), LanguageManager.getString("editor.defaultalgorithm.anonymous"));
		this.setTitle(buildTitle());
		tree.fromAlgorithm(algorithm);
		tree.reload();
		textArea.setText(null);
		scrollPane.setViewportView(tree);
		undo.setEnabled(false);
		btnAddLine.setEnabled(true);
	}
	
	/**
	 * Checks if the user wants to save its algorithm before closing it.
	 * 
	 * @return <b>true</b> You can close the active algorithm.
	 * <br><b>false</b> You should not close it.
	 */
	
	public final boolean askToSaveIfNeeded() {
		if(!algoChanged) {
			return true;
		}
		final int result = JOptionPane.showConfirmDialog(EditorFrame.this, LanguageManager.getString("editor.closedialog", getAlgorithm().getTitle()), AlgogoDesktop.APP_NAME, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
		if(result == JOptionPane.CANCEL_OPTION) {
			return false;
		}
		else if(result == JOptionPane.YES_OPTION) {
			if(algoPath == null || !Files.isWritable(Paths.get(algoPath))) {
				saveAs();
				return true;
			}
			final int index = algoPath.lastIndexOf(".");
			save(new File(algoPath), index == -1 ? "agg" : algoPath.substring(index));
		}
		return true;
	}
	
	/**
	 * Close the editor with a confirmation.
	 */
	
	public final void closeEditor() {
		if(askToSaveIfNeeded()) {
			System.exit(0);
		}
	}

	/**
	 * Builds the current title for the current algorithm.
	 * 
	 * @return The current title.
	 */

	public final String buildTitle() {
		final Algorithm algorithm = getAlgorithm();
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

	public final String buildTitle(final String title, final String author) {
		return LanguageManager.getString("editor.title", algoChanged ? "* " : "", title, author, AlgogoDesktop.APP_NAME, AlgogoDesktop.APP_VERSION);
	}

	/**
	 * Loads an algorithm from a file.
	 * 
	 * @param file The file.
	 * 
	 * @throws Exception If an exception occurs while reading the file.
	 */

	public final void open(final File file) throws Exception {
		try {
			if(file == null) {
				throw new Exception("File can't be null.");
			}
			algorithms.clear();
			algorithm = Algorithm.loadFromFile(file);
			tree.fromAlgorithm(algorithm);
			algoPath = file.getPath();
			saveToHistory(algoPath);
			algoChanged = false;
			tree.reload();
			EditorFrame.this.setTitle(buildTitle());
		}
		catch(final InvalidAlgorithmVersionException ex) {
			JOptionPane.showMessageDialog(this, LanguageManager.getString("editor.higherversion", AlgogoDesktop.APP_NAME), LanguageManager.getString("joptionpane.error"), JOptionPane.ERROR_MESSAGE);
		}
		catch(final Exception ex) {
			throw ex;
		}
	}

	/**
	 * Saves an algorithm to a file.
	 * 
	 * @param file The file.
	 * @param extension The file extension with dot.
	 */

	public final void save(final File file, final String extension) {
		try {
			final AtomicReference<File> reference = new AtomicReference<File>(file);
			getAlgorithm().saveToFile(reference, AlgorithmFileFormat.DEFAULT_FORMATS[extension.equalsIgnoreCase(".aggc") ? 1 : 0]);
			algoPath = reference.get().getPath();
			saveToHistory(algoPath);
			algoChanged = false;
			EditorFrame.this.setTitle(buildTitle());
		}
		catch(final Exception ex) {
			ErrorDialog.errorMessage(this, ex);
		}
	}

	/**
	 * Saves an algorithm with a dialog.
	 */

	public final void saveAs() {
		try {
			final JFileChooser chooser = new JFileChooser();
			final File currentDir = Utils.getParentFolder();
			chooser.setFileFilter(new FileNameExtensionFilter(LanguageManager.getString("editor.menu.file.filter.agg"), "agg"));
			chooser.addChoosableFileFilter(new FileNameExtensionFilter(LanguageManager.getString("editor.menu.file.filter.aggc"), "aggc"));
			chooser.removeChoosableFileFilter(chooser.getAcceptAllFileFilter());
			chooser.setMultiSelectionEnabled(false);
			chooser.setCurrentDirectory(currentDir);
			chooser.setSelectedFile(algoPath == null ? new File(currentDir, getAlgorithm().getTitle()) : new File(algoPath.contains(".") ? algoPath.substring(0, algoPath.lastIndexOf(".")) : algoPath));
			if(chooser.showSaveDialog(EditorFrame.this) == JFileChooser.APPROVE_OPTION) {
				save(chooser.getSelectedFile(), "." + ((FileNameExtensionFilter)chooser.getFileFilter()).getExtensions()[0]);
			}
		}
		catch(final Exception ex) {
			ErrorDialog.errorMessage(EditorFrame.this, ex);
		}
	}
	
	/**
	 * Saves the specified path to the "recent files" menu.
	 * 
	 * @param path The path.
	 */

	private final void saveToHistory(final String path) {
		final AppSettings settings = AlgogoDesktop.getSettings();
		if(settings.recents.contains(path)) {
			settings.recents.removeAll(Collections.singleton(path));
		}
		else if(settings.recents.size() >= AppSettings.RECENTS_LIMIT) {
			settings.recents.subList(AppSettings.RECENTS_LIMIT - 1, settings.recents.size()).clear();
		}
		if(new File(path).exists()) {
			settings.recents.add(0, path);
		}
		try {
			settings.save();
		}
		catch(final Exception ex) {
			ex.printStackTrace();
		}
		refreshPaths();
	}
	
	/**
	 * Cancels the last action.
	 */
	
	public final void undo() {
		if(algorithms.size() == 0) {
			return;
		}
		algorithm = popFromStack();
		tree.fromAlgorithm(algorithm);
		tree.reload();
		algorithmChanged(true);
		if(algorithms.size() == 0) {
			undo.setEnabled(false);
		}
	}
	
	/**
	 * Adds an algorithm to the Stack (allows to undo your action).
	 */
	
	public final void addAlgorithmToStack() {
		addAlgorithmToStack(this.algorithm.copy());
	}
	
	/**
	 * Adds an algorithm to the Stack (allows to undo your action).
	 * 
	 * @param algorithm The algorithm.
	 */
	
	public final void addAlgorithmToStack(final Algorithm algorithm) {
		algorithms.push(algorithm);
		if(algorithms.size() > 0) {
			undo.setEnabled(true);
		}
	}
	
	/**
	 * Pops the algorithm on top of the stack.
	 * 
	 * @return The algorithm on top of the stack.
	 */
	
	public final Algorithm popFromStack() {
		return algorithms.pop();
	}
	
	/**
	 * Refresh the "recent files" menu.
	 */

	private final void refreshPaths() {
		final AppSettings settings = AlgogoDesktop.getSettings();
		boolean needToSave = false;
		recents.removeAll();
		for(final String lastFile : new ArrayList<String>(settings.recents)) {
			final File file = new File(lastFile);
			if(!file.exists()) {
				settings.recents.removeAll(Collections.singleton(lastFile));
				needToSave = true;
				continue;
			}
			final JMenuItem lastFileItem = new JMenuItem(lastFile);
			lastFileItem.addActionListener(new ActionListener() {

				@Override
				public final void actionPerformed(final ActionEvent event) {
					try {
						if(askToSaveIfNeeded()) {
							open(file);
						}
					}
					catch(final Exception ex) {
						ErrorDialog.errorMessage(EditorFrame.this, ex);
					}
				}

			});
			recents.add(lastFileItem);
		}
		if(needToSave) {
			try {
				settings.save();
			}
			catch(final Exception ex) {
				ex.printStackTrace();
			}
		}
		if(recents.getMenuComponentCount() > 0) {
			recents.addSeparator();
			final JMenuItem empty = new JMenuItem(LanguageManager.getString("editor.menu.file.recents.empty"));
			empty.addActionListener(new ActionListener() {

				@Override
				public final void actionPerformed(final ActionEvent event) {
					try {
						settings.recents.clear();
						settings.save();
						refreshPaths();
					}
					catch(final Exception ex) {
						ex.printStackTrace();
					}
				}

			});
			recents.add(empty);
		}
		else {
			final JMenuItem nothing = new JMenuItem(LanguageManager.getString("editor.menu.file.recents.nothing"));
			nothing.setEnabled(false);
			recents.add(nothing);
		}
	}
	
	/**
	 * Must be called when the algorithm is changed.
	 * 
	 * @param setTitle <b>true</b> If you want to change the editor's title.
	 * <br><b>false</b> Otherwise.
	 */

	public final void algorithmChanged(final boolean setTitle) {
		algorithmChanged(setTitle, false, false, null);
	}
	
	/**
	 * Must be called when the algorithm is changed.
	 * 
	 * @param setTitle <b>true</b> If you want to change the editor's title.
	 * <br><b>false</b> Otherwise.
	 * @param reloadTree <b>true</b> If you want to reload the tree.
	 * <br><b>false</b> Otherwise.
	 * @param refreshAlgorithm <b>true</b> If you want to refresh the algorithm.
	 * <br><b>false</b> Otherwise.
	 * @param node The node that will be reloaded. If null, the whole tree will be reloaded.
	 */

	public final void algorithmChanged(final boolean setTitle, final boolean reloadTree, final boolean refreshAlgorithm, final DefaultMutableTreeNode node) {
		algorithmChanged(setTitle, reloadTree, refreshAlgorithm, node, (TreePath[])null);
	}

	/**
	 * Must be called when the algorithm is changed.
	 * 
	 * @param setTitle <b>true</b> If you want to change the editor's title.
	 * <br><b>false</b> Otherwise.
	 * @param reloadTree <b>true</b> If you want to reload the tree.
	 * <br><b>false</b> Otherwise.
	 * @param refreshAlgorithm <b>true</b> If you want to refresh the algorithm.
	 * <br><b>false</b> Otherwise.
	 * @param node The node that will be reloaded. If null, the whole tree will be reloaded.
	 * @param selection Apply a selection after the tree getting reloaded. Can be null.
	 */

	public final void algorithmChanged(final boolean setTitle, final boolean reloadTree, final boolean refreshAlgorithm, final DefaultMutableTreeNode node, final TreePath... selection) {
		algoChanged = true;
		if(reloadTree) {
			tree.reload(node);
		}
		if(selection != null) {
			tree.setSelectionPaths(selection);
		}
		if(refreshAlgorithm) {
			algorithm = tree.toAlgorithm(algorithm.getTitle(), algorithm.getAuthor());
		}
		if(setTitle) {
			this.setTitle(buildTitle());
		}
	}
	
	public class DefaultGithubUpdater implements GithubUpdaterResultListener {
		
		@Override
		public void updaterStarted() {}

		@Override
		public final void updaterException(final Exception ex) {
			ErrorDialog.errorMessage(EditorFrame.this, ex);
		}

		@Override
		public void updaterResponse(final String response) {}

		@Override
		public void updaterUpdateAvailable(final String localVersion, final String remoteVersion) {
			try {
				final AppSettings settings = AlgogoDesktop.getSettings();
				final JCheckBox doNotShowItAgain = new JCheckBox(LanguageManager.getString("joptionpane.updateavailable.objects.donotautocheckagain"));
				doNotShowItAgain.setSelected(settings.updaterDoNotAutoCheckAgain);
				JOptionPane.showMessageDialog(EditorFrame.this, new Object[]{new JLabelLink(LanguageManager.getString("joptionpane.updateavailable.message", remoteVersion, AlgogoDesktop.APP_WEBSITE), new URL(AlgogoDesktop.APP_WEBSITE)), doNotShowItAgain}, LanguageManager.getString("joptionpane.updateavailable.title"), JOptionPane.INFORMATION_MESSAGE);
				final boolean value = doNotShowItAgain.isSelected();
				if(settings.updaterDoNotAutoCheckAgain != value) {
					settings.updaterDoNotAutoCheckAgain = value;
					settings.save();
				}
			}
			catch(final Exception ex) {
				ErrorDialog.errorMessage(EditorFrame.this, ex);
			}
		}

		@Override
		public void updaterNoUpdate(final String localVersion, final String remoteVersion) {}
		
	}

}