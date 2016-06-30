package xyz.algogo.desktop.utils;

import java.util.List;

import xyz.algogo.core.AlgoLine;
import xyz.algogo.core.Algorithm;
import xyz.algogo.core.Instruction;
import xyz.algogo.core.Keyword;
import xyz.algogo.core.language.AlgorithmIndentedLanguage;

public class TextLanguage extends AlgorithmIndentedLanguage {
	
	private final boolean addWarning;
	
	public TextLanguage() {
		this(true);
	}
	
	public TextLanguage(final boolean addWarning) {
		this.addWarning = addWarning;
	}

	@Override
	public final String getName() {
		return LanguageManager.getString("utils.language.text");
	}

	@Override
	public final String getExtension() {
		return "txt";
	}

	@Override
	public final String translate(final Algorithm algorithm) {
		final StringBuilder builder = new StringBuilder();
		if(addWarning) {
			builder.append("This is a beta feature : there are still remaining some bugs !" + SEPARATOR + SEPARATOR);
		}
		builder.append(translate(new AlgoLine(Keyword.VARIABLES)));
		for(final AlgoLine variable : algorithm.getVariables().getChildren()) {
			builder.append(translate(variable, "\t"));
		}
		builder.append(translate(new AlgoLine(Keyword.BEGINNING)));
		for(final AlgoLine instruction : algorithm.getInstructions().getChildren()) {
			builder.append(translate(instruction, "\t"));
		}
		builder.append(translate(new AlgoLine(Keyword.END)));
		final String result = builder.toString();
		return result.substring(0, result.lastIndexOf(SEPARATOR));
	}

	@Override
	public final String translate(final AlgoLine line, final String indentation) {
		if(line.isKeyword()) {
			return LanguageManager.getString("editor.line.keyword." + line.getKeyword().toString().toLowerCase()) + SEPARATOR;
		}
		final Instruction instruction = line.getInstruction();
		final String[] args = line.getArgs();
		final StringBuilder builder = new StringBuilder(indentation + LanguageManager.getString("editor.line.instruction." + instruction.toString().replace("_", "").toLowerCase()) + " ");
		switch(instruction) {
		case CREATE_VARIABLE:
			builder.append(Utils.escapeHTML(args[0]) + " " + LanguageManager.getString("editor.line.instruction.createvariable.type") + " " + (args[1].equals("0") ? LanguageManager.getString("editor.line.instruction.createvariable.type.string") : LanguageManager.getString("editor.line.instruction.createvariable.type.number")));
			break;
		case ASSIGN_VALUE_TO_VARIABLE:
			builder.append(Utils.escapeHTML(args[0]) + " → " + Utils.escapeHTML(args[1]));
			break;
		case READ_VARIABLE:
		case IF:
		case WHILE:
			builder.append(Utils.escapeHTML(args[0]));
			break;
		case SHOW_VARIABLE:
		case SHOW_MESSAGE:
			builder.append(Utils.escapeHTML(args[0]) + " " + args[1]);
			break;
		case FOR:
			builder.append(Utils.escapeHTML(args[0]) + " " + LanguageManager.getString("editor.line.instruction.for.from") + " " + args[1] + " " + LanguageManager.getString("editor.line.instruction.for.to") + " " + args[2]);
			break;
		case ELSE:
			break;
		}
		builder.append(SEPARATOR);
		if(line.getAllowsChildren()) {
			final List<AlgoLine> children = line.getChildren();
			if(children != null && children.size() > 0) {
				for(final AlgoLine child : children) {
					builder.append(translate(child, indentation + "\t"));
				}
			}
		}
		return builder.toString();
	}
	
	@Override
	public final boolean canTranslate(final Instruction instruction) {
		return true;
	}

}