package xyz.algogo.desktop.editor.menubar.menu;

import org.kordamp.ikonli.materialdesign.MaterialDesign;
import org.kordamp.ikonli.swing.FontIcon;
import xyz.algogo.desktop.editor.EditorFrame;
import xyz.algogo.desktop.editor.component.EditorMainComponent;
import xyz.algogo.desktop.editor.component.EditorMainPane;
import xyz.algogo.desktop.editor.component.EditorMainPaneToggleListener;
import xyz.algogo.desktop.editor.history.EditorHistory;
import xyz.algogo.desktop.editor.history.EditorHistoryListener;
import xyz.algogo.desktop.editor.history.command.EditorCommand;
import xyz.algogo.desktop.editor.menubar.menu.listener.edit.*;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;
import java.util.ArrayList;
import java.util.List;

/**
 * The Edit editor menu.
 */

public class EditEditorMenu extends EditorMenu implements ClipboardOwner, TreeSelectionListener, EditorHistoryListener, EditorMainPaneToggleListener {

	/**
	 * The algorithm credits menu.
	 */

	private JMenuItem credits;

	/**
	 * The undo menu.
	 */

	private JMenuItem undo;

	/**
	 * The redo menu.
	 */

	private JMenuItem redo;

	/**
	 * The cut menu.
	 */

	private JMenuItem cut;

	/**
	 * The copy menu.
	 */

	private JMenuItem copy;

	/**
	 * The paste menu.
	 */

	private JMenuItem paste;

	/**
	 * Free edit mode menu.
	 */

	private JMenuItem freeEdit;

	/**
	 * Creates a new Edit menu.
	 *
	 * @param editor The editor.
	 */

	public EditEditorMenu(final EditorFrame editor) {
		super(editor, "menu.edit");

		editor.getMainPane().getAlgorithmTree().addTreeSelectionListener(this);
		editor.getEditorHistory().addListener(this);
		editor.getMainPane().addToggleListener(this);
	}

	@Override
	public final JMenuItem[] getMenuItems() {
		final EditorFrame editor = this.getEditor();
		final List<JMenuItem> items = new ArrayList<>();

		items.add(createMenu(
				"menu.edit.settings",
				new SettingsMenuListener(editor),
				FontIcon.of(MaterialDesign.MDI_SETTINGS),
				null
		));

		credits = createMenu(
				"menu.edit.credits",
				new CreditsMenuListener(editor),
				null,
				null
		);
		items.add(credits);

		items.add(null);

		undo = createMenu(
				"menu.edit.undo",
				actionListener -> editor.getEditorHistory().undo(),
				FontIcon.of(MaterialDesign.MDI_UNDO),
				createCTRLKeyStroke('Z')
		);
		undo.setEnabled(false);
		items.add(undo);

		redo = createMenu(
				"menu.edit.redo",
				actionListener -> editor.getEditorHistory().redo(),
				FontIcon.of(MaterialDesign.MDI_REDO),
				createCTRLKeyStroke('Y')
		);
		redo.setEnabled(false);
		items.add(redo);

		items.add(null);

		cut = createMenu(
				"menu.edit.cut",
				new CutMenuListener(editor, this),
				FontIcon.of(MaterialDesign.MDI_CONTENT_CUT),
				createCTRLKeyStroke('X')
		);
		cut.setEnabled(false);
		items.add(cut);

		copy = createMenu(
				"menu.edit.copy",
				new CopyMenuListener(editor, this),
				FontIcon.of(MaterialDesign.MDI_CONTENT_COPY),
				createCTRLKeyStroke('C')
		);
		copy.setEnabled(false);
		items.add(copy);

		paste = createMenu(
				"menu.edit.paste",
				new PasteMenuListener(editor),
				FontIcon.of(MaterialDesign.MDI_CONTENT_PASTE),
				createCTRLKeyStroke('V')
		);
		paste.setEnabled(false);
		items.add(paste);

		items.add(null);

		freeEdit = createMenu(
				"menu.edit.freeEdit",
				actionListener -> this.getEditor().getMainPane().toggleCurrentComponent(),
				null,
				null
		);
		items.add(freeEdit);

		return items.toArray(new JMenuItem[items.size()]);
	}

	@Override
	public final void commandExecuted(final EditorHistory history, final EditorCommand command) {
		toggleUndoRedo(history);
	}

	@Override
	public final void commandUndone(final EditorHistory history, final EditorCommand command) {
		toggleUndoRedo(history);
	}

	@Override
	public final void commandRedone(final EditorHistory history, final EditorCommand command) {
		toggleUndoRedo(history);
	}

	@Override
	public final void undoHistoryCleared(final EditorHistory history) {
		toggleUndoRedo(history);
	}

	@Override
	public final void redoHistoryCleared(final EditorHistory history) {
		toggleUndoRedo(history);
	}

	@Override
	public final void valueChanged(final TreeSelectionEvent treeSelectionEvent) {
		final EditorMainPane mainPane = this.getEditor().getMainPane();
		if(!mainPane.isShowingTree()) {
			cut.setEnabled(true);
			copy.setEnabled(true);
			return;
		}

		final boolean enable = mainPane.getAlgorithmTree().shouldEnableEditButtons();
		cut.setEnabled(enable);
		copy.setEnabled(enable);
	}

	@Override
	public final void lostOwnership(final Clipboard clipboard, final Transferable transferable) {
		paste.setEnabled(false);
	}

	@Override
	public final void onToggle(final EditorMainComponent oldComponent, final EditorMainComponent newComponent) {
		if(newComponent == this.getEditor().getMainPane().getAlgorithmTree()) {
			credits.setEnabled(true);
			freeEdit.setIcon(null);
			return;
		}

		credits.setEnabled(false);
		freeEdit.setIcon(FontIcon.of(MaterialDesign.MDI_CHECK));
	}

	/**
	 * Returns the algorithm credits menu.
	 *
	 * @return The algorithm credits menu.
	 */

	public final JMenuItem getCreditsMenuItem() {
		return credits;
	}

	/**
	 * Returns the undo menu.
	 *
	 * @return The undo menu.
	 */

	public final JMenuItem getUndoMenuItem() {
		return undo;
	}

	/**
	 * Returns the redo menu.
	 *
	 * @return The redo menu.
	 */

	public final JMenuItem getRedoMenuItem() {
		return redo;
	}

	/**
	 * Returns the cut menu.
	 *
	 * @return The cut menu.
	 */

	public final JMenuItem getCutMenuItem() {
		return cut;
	}

	/**
	 * Returns the copy menu.
	 *
	 * @return The copy menu.
	 */

	public final JMenuItem getCopyMenuItem() {
		return copy;
	}

	/**
	 * Returns the paste menu.
	 *
	 * @return The paste menu.
	 */

	public final JMenuItem getPasteMenuItem() {
		return paste;
	}

	/**
	 * Returns the free edit menu.
	 *
	 * @return The free edit menu.
	 */

	public final JMenuItem getFreeEditMenuItem() {
		return freeEdit;
	}

	/**
	 * Toggles the activation of undo and redo menus according to the history state.
	 *
	 * @param history The editor history.
	 */

	private void toggleUndoRedo(final EditorHistory history) {
		undo.setEnabled(history.canUndo());
		redo.setEnabled(history.canRedo());
	}

}