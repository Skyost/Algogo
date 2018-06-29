package xyz.algogo.desktop.editor.history.command.tree;

import xyz.algogo.desktop.editor.EditorFrame;
import xyz.algogo.desktop.editor.component.tree.AlgorithmTreeNode;
import xyz.algogo.desktop.editor.history.command.EditorCommand;

/**
 * Represents an "down node" command.
 */

public class DownNodeCommand extends EditorCommand {

	/**
	 * The node to down.
	 */

	private AlgorithmTreeNode node;

	/**
	 * Creates a down node command.
	 *
	 * @param editor The editor.
	 * @param node The node.
	 */

	public DownNodeCommand(final EditorFrame editor, final AlgorithmTreeNode node) {
		super(editor);

		this.node = node;
	}

	@Override
	public final void execute() {
		node.down(this.getEditor().getMainPane().getAlgorithmTree());
	}

	@Override
	public final void unExecute() {
		node.up(this.getEditor().getMainPane().getAlgorithmTree());
	}

	@Override
	public final boolean canUnExecute() {
		return true;
	}

}