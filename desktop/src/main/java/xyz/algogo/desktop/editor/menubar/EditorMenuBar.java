package xyz.algogo.desktop.editor.menubar;

import xyz.algogo.desktop.editor.EditorFrame;
import xyz.algogo.desktop.editor.menubar.menu.EditEditorMenu;
import xyz.algogo.desktop.editor.menubar.menu.EditorMenu;
import xyz.algogo.desktop.editor.menubar.menu.FileEditorMenu;
import xyz.algogo.desktop.editor.menubar.menu.HelpEditorMenu;

import javax.swing.*;

public class EditorMenuBar extends JMenuBar {

	private final EditorFrame editor;

	public EditorMenuBar(final EditorFrame editor) {
		this.editor = editor;

		registerMenus();
	}

	private void registerMenus() {
		for(final EditorMenu menu : new EditorMenu[]{new FileEditorMenu(editor), new EditEditorMenu(editor), new HelpEditorMenu(editor)}) {
			this.add(menu);
		}
	}

	public final EditorFrame getEditor() {
		return editor;
	}

}