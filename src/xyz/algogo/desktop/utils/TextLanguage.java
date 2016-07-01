package xyz.algogo.desktop.utils;

import java.util.List;

import xyz.algogo.core.AlgoLine;
import xyz.algogo.core.Algorithm;
import xyz.algogo.core.Instruction;
import xyz.algogo.core.Keyword;
import xyz.algogo.core.language.AlgorithmIndentedLanguage;

public class TextLanguage extends AlgorithmIndentedLanguage {
	
	private final boolean addWarning;
	private final boolean html;
	private final boolean showLineBreaks;
	
	private final String separator;
	private final String indentation;
	
	public TextLanguage() {
		this(true);
	}
	
	public TextLanguage(final boolean addWarning) {
		this(addWarning, false);
	}
	
	public TextLanguage(final boolean addWarning, final boolean html) {
		this(addWarning, html, true);
	}
	
	public TextLanguage(final boolean addWarning, final boolean html, final boolean showLineBreaks) {
		this.addWarning = addWarning;
		this.html = html;
		this.showLineBreaks = showLineBreaks;
		this.separator = html ? "<br>" : SEPARATOR;
		this.indentation = html ? "&emsp;" : "\t";
	}

	@Override
	public final String getName() {
		return LanguageManager.getString("utils.language.text");
	}

	@Override
	public final String getExtension() {
		return html ? "html" : "txt";
	}

	@Override
	public final String translate(final Algorithm algorithm) {
		final StringBuilder builder = new StringBuilder();
		if(html) {
			builder.append("<html>" + SEPARATOR);
		}
		if(addWarning) {
			if(html) {
				builder.append("<!-- ");
			}
			builder.append("This is a beta feature : there are still some remaining bugs !");
			if(html) {
				builder.append(" -->");
			}
			builder.append(SEPARATOR + SEPARATOR);
		}
		builder.append(translate(new AlgoLine(Keyword.VARIABLES)));
		for(final AlgoLine variable : algorithm.getVariables().getChildren()) {
			builder.append(translate(variable, indentation));
		}
		builder.append(translate(new AlgoLine(Keyword.BEGINNING)));
		for(final AlgoLine instruction : algorithm.getInstructions().getChildren()) {
			builder.append(translate(instruction, indentation));
		}
		builder.append(translate(new AlgoLine(Keyword.END)));
		if(html) {
			builder.append(SEPARATOR + "</html>");
		}
		final String result = builder.toString();
		return result.substring(0, result.lastIndexOf(separator));
	}

	@Override
	public final String translate(final AlgoLine line, final String indentation) {
		if(line.isKeyword()) {
			String keyword = LanguageManager.getString("editor.line.keyword." + line.getKeyword().toString().toLowerCase());
			if(html) {
				keyword = "<strong style=\"color: " + AlgoLineUtils.KEYWORD_COLOR + "\">" + keyword + "</strong>";
			}
			return keyword + separator;
		}
		final Instruction instruction = line.getInstruction();
		final String[] args = line.getArgs();
		final StringBuilder builder = new StringBuilder(indentation);
		if(html) {
			builder.append("<span style=\"color: " + AlgoLineUtils.getLineColor(instruction) + "\"><strong>");
		}
		builder.append(LanguageManager.getString("editor.line.instruction." + instruction.toString().replace("_", "").toLowerCase()));
		if(html) {
			builder.append("</strong>");
		}
		builder.append(" ");
		switch(instruction) {
		case CREATE_VARIABLE:
			builder.append(Utils.escapeHTML(args[0]) + " ");
			if(html) {
				builder.append("<strong>");
			}
			builder.append(LanguageManager.getString("editor.line.instruction.createvariable.type"));
			if(html) {
				builder.append("</strong>");
			}
			builder.append(" " + (args[1].equals("0") ? LanguageManager.getString("editor.line.instruction.createvariable.type.string") : LanguageManager.getString("editor.line.instruction.createvariable.type.number")));
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
			builder.append(Utils.escapeHTML(args[0]));
			if(showLineBreaks) {
				builder.append(" " + args[1]);
			}
			break;
		case FOR:
			builder.append(Utils.escapeHTML(args[0]) + " " + LanguageManager.getString("editor.line.instruction.for.from") + " " + args[1] + " " + LanguageManager.getString("editor.line.instruction.for.to") + " " + args[2]);
			break;
		case ELSE:
			break;
		}
		if(html) {
			builder.append("</span>");
		}
		builder.append(separator);
		if(line.getAllowsChildren()) {
			final List<AlgoLine> children = line.getChildren();
			if(children != null && children.size() > 0) {
				for(final AlgoLine child : children) {
					builder.append(translate(child, indentation + indentation));
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