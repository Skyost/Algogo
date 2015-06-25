package fr.skyost.algo.desktop.utils;

import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

import fr.skyost.algo.core.AlgoLine;
import fr.skyost.algo.core.Instruction;
import fr.skyost.algo.core.Keyword;

/**
 * Used to handle algo lines and tree nodes.
 *
 * @author Skyost.
 */

public class AlgoTreeNode extends DefaultMutableTreeNode implements Cloneable {

	private static final long serialVersionUID = 1L;

	private AlgoLine line;
	
	/**
	 * Creates an AlgoTreeNode instance for the specified instruction.
	 * 
	 * @param instruction The instruction.
	 * @param args The arguments.
	 */

	public AlgoTreeNode(final Instruction instruction, final String... args) {
		this.line = new AlgoLine(instruction, args);
	}
	
	/**
	 * Creates an AlgoTreeNode instance for the specified keyword.
	 * 
	 * @param keyword The keyword.
	 */

	public AlgoTreeNode(final Keyword keyword) {
		this.line = new AlgoLine(keyword);
	}
	
	/**
	 * Creates an AlgoTreeNode instance for the specified algo line.
	 * 
	 * @param line The algo line.
	 */

	public AlgoTreeNode(final AlgoLine line) {
		super(line.toString());
		this.line = line;
		final List<AlgoLine> children = line.getChildren();
		if(children != null && children.size() != 0) {
			for(final AlgoLine child : children) {
				add(new AlgoTreeNode(child), false);
			}
		}
	}
	
	/**
	 * Gets the holded algo line.
	 * 
	 * @return The line.
	 */

	public final AlgoLine getAlgoLine() {
		return line;
	}
	
	/**
	 * Sets the algo line.
	 * 
	 * @param line The line.
	 */

	public final void setAlgoLine(final AlgoLine line) {
		this.line = line;
	}
	
	/**
	 * Adds a child to this node.
	 * 
	 * @param child The child.
	 */

	@Override
	public final void add(final MutableTreeNode child) {
		add(child, true);
	}

	/**
	 * Adds a child to this node.
	 * 
	 * @param child The child.
	 * @param addChildToLine If the child should be added to the algo line too.
	 */
	
	public final void add(final MutableTreeNode child, final boolean addChildToLine) {
		if(!(child instanceof AlgoTreeNode)) {
			throw new IllegalArgumentException("This node is not an AlgoTreeNode !");
		}
		insert(child, child.getParent() == this ? getChildCount() - 1 : getChildCount(), addChildToLine);
	}
	
	/**
	 * Inserts a child in this node.
	 * 
	 * @param child The child.
	 * @param childIndex The index.
	 */

	@Override
	public final void insert(final MutableTreeNode child, final int childIndex) {
		insert(child, childIndex, true);
	}
	
	/**
	 * Inserts a child in this node.
	 * 
	 * @param child The child.
	 * @param childIndex The index.
	 * @param insertChildToLine If the child should be added to the algo line too.
	 */

	public final void insert(final MutableTreeNode child, final int childIndex, final boolean insertChildToLine) {
		if(!(child instanceof AlgoTreeNode)) {
			throw new IllegalArgumentException("This node is not an AlgoTreeNode !");
		}
		super.insert(child, childIndex);
		if(insertChildToLine && line != null) {
			line.insertChild(((AlgoTreeNode)child).line, childIndex);
		}
	}
	
	/**
	 * Removes a child from this node.
	 * 
	 * @param child The child.
	 */

	@Override
	public final void remove(final MutableTreeNode child) {
		remove(this.getIndex(child), true);
	}
	
	/**
	 * Removes a child from this node.
	 * 
	 * @param child The child.
	 * @param removeFromLine If the child should be removed from the algo line too.
	 */
	
	public final void remove(final MutableTreeNode child, final boolean removeFromLine) {
		remove(this.getIndex(child), removeFromLine);
	}
	
	/**
	 * Removes a child from this node.
	 * 
	 * @param index The child's index.
	 * @param removeFromLine If the child should be removed from the algo line too.
	 */

	@Override
	public final void remove(final int index) {
		remove(index, true);
	}
	
	/**
	 * Removes a child from this node.
	 * 
	 * @param index The child's index.
	 * @param removeFromLine If the child should be removed from the algo line too.
	 */
	
	public final void remove(final int index, final boolean removeFromLine) {
		final TreeNode child = this.getChildAt(index);
		if(!(child instanceof AlgoTreeNode)) {
			throw new IllegalArgumentException("This node is not an AlgoTreeNode !");
		}
		super.remove(index);
		if(removeFromLine && line != null) {
			line.removeChild(((AlgoTreeNode)child).line);
		}
	}
	
	/**
	 * Removes this node from its parent.
	 */

	@Override
	public final void removeFromParent() {
		final TreeNode node = this.getParent();
		if(!(node instanceof AlgoTreeNode)) {
			throw new IllegalArgumentException("This node is not an AlgoTreeNode !");
		}
		if(line != null) {
			((AlgoTreeNode)node).line.removeChild(line);
		}
		super.removeFromParent();
	}
	
	/**
	 * Checks if this node can have children.
	 * 
	 * @return <b>true</b> If the node can have children.
	 * <br><b>false</b> Otherwise.
	 */

	@Override
	public final boolean getAllowsChildren() {
		return line == null ? true : line.getAllowsChildren();
	}
	
	/**
	 * @deprecated This method should not be used.
	 * 
	 * @param allows If the node should allow children.
	 */

	@Override
	public final void setAllowsChildren(final boolean allows) {
		return;
	}
	
	/**
	 * Gets a String representation of this node.
	 * 
	 * @return An HTML String.
	 */
	
	@Override
	public final String toString() {
		if(line == null) {
			return this.getUserObject().toString();
		}
		final Instruction instruction = line.getInstruction();
		if(instruction == null) {
			return getLine(line.getKeyword());
		}
		else {
			return getLine(instruction, line.getArgs());
		}
	}
	
	/**
	 * Gets the String representation for the specified keyword.
	 * 
	 * @param keyword The keyword.
	 * 
	 * @return The String representation.
	 */
	
	private static final String getLine(final Keyword keyword) {
		return "<html><b style=\"color:" + getLineColor(keyword) + "\">" + LanguageManager.getString("editor.line.keyword." + keyword.toString().toLowerCase()) + "</b></html>";
	}
	
	/**
	 * Gets the String representation for the specified instruction.
	 * 
	 * @param instruction The instruction.
	 * @param args The arguments.
	 * 
	 * @return The String representation.
	 */
	
	private static final String getLine(final Instruction instruction, final String... args) {
		final StringBuilder builder = new StringBuilder("<html><span style=color:\"" + getLineColor(instruction) + "\"><b>" + LanguageManager.getString("editor.line.instruction." + instruction.toString().replace("_", "").toLowerCase()) + "</b> ");
		switch(instruction) {
		case CREATE_VARIABLE:
			builder.append(Utils.escapeHTML(args[0]) + " <b>" + LanguageManager.getString("editor.line.instruction.createvariable.type") + "</b> " + (args[1].equals("0") ? LanguageManager.getString("editor.line.instruction.createvariable.type.string") : LanguageManager.getString("editor.line.instruction.createvariable.type.number")));
			break;
		case ASSIGN_VALUE_TO_VARIABLE:
			builder.append(Utils.escapeHTML(args[0] + " → " + args[1]));
			break;
		case SHOW_VARIABLE:
		case READ_VARIABLE:
		case SHOW_MESSAGE:
		case IF:
		case WHILE:
			builder.append(Utils.escapeHTML(args[0]));
			break;
		case FOR:
			builder.append(Utils.escapeHTML(args[0]) + " <b>" + LanguageManager.getString("editor.line.instruction.for.from") + "</b> " + args[1] + " <b>" + LanguageManager.getString("editor.line.instruction.for.to") + "</b> " + args[2]);
			break;
		case ELSE:
			break;
		default:
			builder.delete(0, builder.length() - 1);
			break;
		}
		return builder.append("</span></html>").toString();
	}
	
	/**
	 * Gets the color of the specified keyword.
	 * 
	 * @param keyword The keyword.
	 * 
	 * @return The color.
	 */
	
	private static final String getLineColor(final Keyword keyword) {
		return "#D35400";
	}
	
	/**
	 * Gets the color of the specified instruction.
	 * 
	 * @param instruction The instruction.
	 * 
	 * @return The color.
	 */
	
	private static final String getLineColor(final Instruction instruction) {
		switch(instruction) {
		case CREATE_VARIABLE:
		case ASSIGN_VALUE_TO_VARIABLE:
		case SHOW_VARIABLE:
		case READ_VARIABLE:
		case SHOW_MESSAGE:
			return "#22313F";
		default:
			return "#3498DB";
		}
	}
	
	@Override
	public final AlgoTreeNode clone() {
		try {
			final AlgoTreeNode clone = (AlgoTreeNode)super.clone();
			clone.setAlgoLine(line.clone());
			return clone;
		}
		catch(final Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

}