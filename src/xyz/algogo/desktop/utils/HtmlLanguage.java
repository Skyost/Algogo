package xyz.algogo.desktop.utils;

import java.util.List;

import xyz.algogo.core.AlgoLine;
import xyz.algogo.core.Algorithm;
import xyz.algogo.core.Instruction;
import xyz.algogo.core.Keyword;
import xyz.algogo.core.language.AlgorithmIndentedLanguage;
import xyz.algogo.desktop.AlgogoDesktop;

public class HtmlLanguage extends AlgorithmIndentedLanguage {
	
	private final boolean addWarning;
	private final boolean showLineBreaks;
	
	public HtmlLanguage() {
		this(true);
	}
	
	public HtmlLanguage(final boolean addWarning) {
		this(addWarning, false);
	}
	
	public HtmlLanguage(final boolean addWarning, final boolean showLineBreaks) {
		this.addWarning = addWarning;
		this.showLineBreaks = showLineBreaks;
	}
	
	@Override
	public final String getName() {
		return LanguageManager.getString("utils.language.html");
	}

	@Override
	public final String getExtension() {
		return "html";
	}

	@Override
	public final String translate(final Algorithm algorithm) {
		final StringBuilder builder = new StringBuilder();
		builder.append("<html>" + SEPARATOR);
		builder.append("\t<head>" + SEPARATOR + "\t\t<meta charset=\"utf-8\"/>" + SEPARATOR);
		builder.append("\t\t<title>" + LanguageManager.getString("editor.title", "", algorithm.getTitle(), algorithm.getAuthor(), AlgogoDesktop.APP_NAME, AlgogoDesktop.APP_VERSION) + "</title>" + SEPARATOR);
		builder.append("\t<body>" + SEPARATOR);
		if(addWarning) {
			builder.append("\t<!-- This is a beta feature : there are still some remaining bugs ! -->" + SEPARATOR + SEPARATOR);
		}
		builder.append(translate(new AlgoLine(Keyword.VARIABLES)));
		for(final AlgoLine variable : algorithm.getVariables().getChildren()) {
			builder.append(translate(variable, "&emsp;"));
		}
		builder.append(translate(new AlgoLine(Keyword.BEGINNING)));
		for(final AlgoLine instruction : algorithm.getInstructions().getChildren()) {
			builder.append(translate(instruction, "&emsp;"));
		}
		builder.append(translate(new AlgoLine(Keyword.END)));
		builder.append(SEPARATOR + "\t</body>" + SEPARATOR + "</html>");
		final String result = builder.toString();
		final int lastSeparator = result.lastIndexOf("<br>");
		return result.substring(0, lastSeparator) + result.substring(lastSeparator + "<br>".length());
	}

	@Override
	public final String translate(final AlgoLine line, final String indentation) {
		if(line.isKeyword()) {
			return "<strong style=\"color: " + AlgoLineUtils.KEYWORD_COLOR + "\">" + LanguageManager.getString("editor.line.keyword." + line.getKeyword().toString().toLowerCase()) + "</strong><br>" + SEPARATOR;
		}
		final Instruction instruction = line.getInstruction();
		final String[] args = line.getArgs();
		final StringBuilder builder = new StringBuilder(indentation);
		builder.append("<span style=\"color: " + AlgoLineUtils.getLineColor(instruction) + "\"><strong>");
		builder.append(LanguageManager.getString("editor.line.instruction." + instruction.toString().replace("_", "").toLowerCase()));
		builder.append("</strong> ");
		switch(instruction) {
		case CREATE_VARIABLE:
			builder.append(Utils.escapeHTML(args[0]) + " <strong>" + LanguageManager.getString("editor.line.instruction.createvariable.type") + "</strong> ");
			builder.append(args[1].equals("0") ? LanguageManager.getString("editor.line.instruction.createvariable.type.string") : LanguageManager.getString("editor.line.instruction.createvariable.type.number"));
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
			builder.append(Utils.escapeHTML(args[0]) + " <strong>" + LanguageManager.getString("editor.line.instruction.for.from") + "</strong> " + args[1] + " <strong>" + LanguageManager.getString("editor.line.instruction.for.to") + "</strong> " + args[2]);
			break;
		case ELSE:
			break;
		}
		builder.append("</span><br>" + SEPARATOR);
		if(line.getAllowsChildren()) {
			final List<AlgoLine> children = line.getChildren();
			if(children != null && children.size() > 0) {
				for(final AlgoLine child : children) {
					builder.append(translate(child, indentation + "&emsp;"));
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