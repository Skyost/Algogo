package xyz.algogo.desktop.utils;

import java.util.List;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import xyz.algogo.core.AlgoLine;
import xyz.algogo.core.Algorithm;
import xyz.algogo.core.Instruction;
import xyz.algogo.core.Keyword;
import xyz.algogo.desktop.AlgogoDesktop;

public class AlgorithmTree extends JTree {

	private static final long serialVersionUID = 1L;
	
	public final DefaultMutableTreeNode variables = new DefaultMutableTreeNode(new AlgorithmUserObject(new AlgoLine(Keyword.VARIABLES)));
	public final DefaultMutableTreeNode beginning = new DefaultMutableTreeNode(new AlgorithmUserObject(new AlgoLine(Keyword.BEGINNING)));
	public final DefaultMutableTreeNode end = new DefaultMutableTreeNode(new AlgorithmUserObject(new AlgoLine(Keyword.END)));
	
	public AlgorithmTree() {
		this(null);
	}
	
	public AlgorithmTree(final Algorithm algorithm) {
		super(new DefaultMutableTreeNode(AlgogoDesktop.APP_NAME));
		final DefaultMutableTreeNode root = (DefaultMutableTreeNode)this.getModel().getRoot(); // Super constructor must be the first statement.
		root.add(variables);
		root.add(beginning);
		root.add(end);
		if(algorithm != null) {
			fromAlgorithm(algorithm);
		}
	}
	
	public final void fromAlgorithm(final Algorithm algorithm) {
		variables.removeAllChildren();
		for(final AlgoLine variable : algorithm.getVariables().getChildren()) {
			variables.add(asMutableTreeNode(variable));
		}
		beginning.removeAllChildren();
		for(final AlgoLine instruction : algorithm.getInstructions().getChildren()) {
			beginning.add(asMutableTreeNode(instruction));
		}
	}
	
	public final Algorithm toAlgorithm(final String title, final String author) {
		final Algorithm algorithm = new Algorithm(title, author);
		final AlgoLine variables = algorithm.getVariables();
		for(int i = 0; i != this.variables.getChildCount(); i++) {
			variables.addChild(asAlgoLine((DefaultMutableTreeNode)this.variables.getChildAt(i)));
		}
		final AlgoLine instructions = algorithm.getInstructions();
		for(int i = 0; i != this.beginning.getChildCount(); i++) {
			instructions.addChild(asAlgoLine((DefaultMutableTreeNode)this.beginning.getChildAt(i)));
		}
		return algorithm;
	}
	
	private final DefaultMutableTreeNode asMutableTreeNode(final AlgoLine line) {
		final DefaultMutableTreeNode representation = new DefaultMutableTreeNode(new AlgorithmUserObject(line));
		final List<AlgoLine> children = line.getChildren();
		if(children != null && children.size() > 0) {
			for(final AlgoLine child : children) {
				representation.add(asMutableTreeNode(child));
			}
		}
		return representation;
	}
	
	private final AlgoLine asAlgoLine(final DefaultMutableTreeNode node) {
		AlgoLine representation = getAttachedAlgoLine(node);
		representation = representation.isKeyword() ? new AlgoLine(representation.getKeyword()) : new AlgoLine(representation.getInstruction(), representation.getArgs());
		for(int i = 0; i != node.getChildCount(); i++) {
			representation.addChild(asAlgoLine((DefaultMutableTreeNode)node.getChildAt(i)));
		}
		return representation;
	}
	
	public static final AlgoLine getAttachedAlgoLine(final DefaultMutableTreeNode node) {
		final Object userObject = node.getUserObject();
		return userObject instanceof AlgorithmUserObject ? ((AlgorithmUserObject)userObject).getAlgoLine() : null;
	}
	
	/**
	 * Increases a line in the hierarchy.
	 * 
	 * @param parent The line's parent.
	 * @param position The line's position in the hierarchy.
	 */
	
	public static final boolean up(final DefaultMutableTreeNode parent, final int position) {
		if(position - 1 < 0) {
			return false;
		}
		final DefaultMutableTreeNode selected = (DefaultMutableTreeNode)parent.getChildAt(position);
		final DefaultMutableTreeNode above = (DefaultMutableTreeNode)parent.getChildAt(position - 1);
		final AlgoLine selectedAlgoLine = getAttachedAlgoLine(selected);
		if(selectedAlgoLine.getInstruction() == Instruction.ELSE) {
			if(position - 2 < 0) {
				return false;
			}
			final DefaultMutableTreeNode aboveAbove = (DefaultMutableTreeNode)parent.getChildAt(position - 2);
			if(getAttachedAlgoLine(aboveAbove).getInstruction() == Instruction.ELSE) {
				parent.insert((DefaultMutableTreeNode)parent.getChildAt(position - 3), position - 1);
				parent.insert(above, position - 3);
				parent.insert(selected, position - 2);
			}
			else {
				parent.insert(selected, position - 1);
				parent.insert(above, position - 2);
			}
			parent.insert(aboveAbove, position);
		}
		else if(getAttachedAlgoLine(above).getInstruction() == Instruction.ELSE) {
			final DefaultMutableTreeNode iff = (DefaultMutableTreeNode)parent.getChildAt(position - 2);
			parent.insert(selected, position - 2);
			if(selectedAlgoLine.getInstruction() == Instruction.IF && Boolean.valueOf(selectedAlgoLine.getArgs()[1])) {
				parent.insert(iff, position);
				parent.insert((DefaultMutableTreeNode)parent.getChildAt(position + 1), position - 1);
				parent.insert(above, position + 1);
			}
			else {
				parent.insert(iff, position - 1);
				parent.insert(above, position);
			}
		}
		else {
			parent.insert(selected, position - 1);
			if(selectedAlgoLine.getInstruction() == Instruction.IF && Boolean.valueOf(selectedAlgoLine.getArgs()[1])) {
				parent.insert((DefaultMutableTreeNode)parent.getChildAt(position + 1), position);
				parent.insert(above, position + 1);
			}
			else {
				parent.insert(above, position);
			}
		}
		return true;
	}
	
	/**
	 * Decreases a line in the hierarchy.
	 * 
	 * @param parent The line's parent.
	 * @param position The line's position in the hierarchy.
	 */
	
	public static final boolean down(final DefaultMutableTreeNode parent, final int position) {
		if(position + 1 >= parent.getChildCount()) {
			return false;
		}
		final DefaultMutableTreeNode selected = (DefaultMutableTreeNode)parent.getChildAt(position);
		final DefaultMutableTreeNode below = (DefaultMutableTreeNode)parent.getChildAt(position + 1);
		if(getAttachedAlgoLine(selected).getInstruction() == Instruction.ELSE) {
			if(position - 1 < 0) {
				return false;
			}
			final DefaultMutableTreeNode above = (DefaultMutableTreeNode)parent.getChildAt(position - 1);
			parent.insert(below, position - 1);
			if(ifFollowedByElse(below)) {
				parent.insert(above, position + 1);
				parent.insert((DefaultMutableTreeNode)parent.getChildAt(position + 2), position);
				parent.insert(selected, position + 2);
			}
			else {
				parent.insert(above, position);
				parent.insert(selected, position + 1);
			}
		}
		else if(ifFollowedByElse(selected)) {
			if(position + 2 >= parent.getChildCount()) {
				return false;
			}
			final DefaultMutableTreeNode belowBelow = (DefaultMutableTreeNode)parent.getChildAt(position + 2);
			parent.insert(belowBelow, position);
			if(ifFollowedByElse(belowBelow)) {
				parent.insert(selected, position + 2);
				parent.insert((DefaultMutableTreeNode)parent.getChildAt(position + 3), position + 1);
				parent.insert(below, position + 3);
			}
			else {
				parent.insert(selected, position + 1);
				parent.insert(below, position + 2);
			}
		}
		else {
			parent.insert(below, position);
			if(ifFollowedByElse(below)) {
				parent.insert((DefaultMutableTreeNode)parent.getChildAt(position + 2), position + 1);
				parent.insert(selected, position + 2);
			}
			else {
				parent.insert(selected, position + 1);
			}
		}
		return true;
	}
	
	/**
	 * Checks if the line is an IF and must be followed by an ELSE.
	 * 
	 * @param iff The line.
	 * 
	 * @return <b>true</b> If the IF must be followed by an ELSE.
	 * <b>false</b> Otherwise.
	 */
	
	public static final boolean ifFollowedByElse(final DefaultMutableTreeNode iff) {
		final AlgoLine ifLine = getAttachedAlgoLine(iff);
		return ifLine.getInstruction() == Instruction.IF && Boolean.valueOf(ifLine.getArgs()[1]);
	}
	
	public static class AlgorithmUserObject implements Cloneable {
		
		private final AlgoLine line;
		
		public AlgorithmUserObject(final AlgoLine line) {
			this.line = line;
		}
		
		@Override
		public final String toString() {
			return line.isKeyword() ? AlgoLineUtils.getLine(line.getKeyword()) : AlgoLineUtils.getLine(line.getInstruction(), line.getArgs());
		}
		
		public final AlgoLine getAlgoLine() {
			return line;
		}
		
		@Override
		public final AlgorithmUserObject clone() {
			return new AlgorithmUserObject(line.clone());
		}
		
	}
	
}