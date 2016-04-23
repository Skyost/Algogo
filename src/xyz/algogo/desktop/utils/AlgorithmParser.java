package xyz.algogo.desktop.utils;

import xyz.algogo.core.AlgoLine;
import xyz.algogo.core.Algorithm;
import xyz.algogo.core.Instruction;
import xyz.algogo.core.Keyword;

public class AlgorithmParser {
	
	public static final Algorithm parse(final String title, final String author, final String text) throws ParseException {
		final Algorithm algorithm = new Algorithm(title, author);
		parse(text.split(System.lineSeparator())); // TODO
		return algorithm;
	}
	
	public static final AlgoLine[] parse(final String... lines) throws ParseException {
		final AlgoLine variables = new AlgoLine(Keyword.VARIABLES);
		final AlgoLine instructions = new AlgoLine(Keyword.BEGINNING);
		for(int i = 0; i != lines.length; i++) {
			final String line = lines[i];
			final AlgoLine algoLine = parseLine(line);
			final Instruction instruction = algoLine.getInstruction();
			if(instruction == null) {
				continue;
			}
			if(instruction == Instruction.CREATE_VARIABLE) {
				variables.addChild(algoLine);
				continue;
			}
			if(instruction == Instruction.IF || instruction == Instruction.FOR || instruction == Instruction.WHILE) {
				final int tabs = getBeginningTabCount(line);
				for(int j = i + 1; j != lines.length; j++) {
					if(getBeginningTabCount(lines[i]) <= tabs) {
						break;
					}
					for(final AlgoLine child : parse(lines[i])[0].getChildren()) {
						algoLine.addChild(child);
					}
				}
			}
			instructions.addChild(algoLine);
		}
		return new AlgoLine[]{variables, instructions};
	}
	
	public static final AlgoLine parseLine(String line) throws ParseException {
		line = line.replace("\t", "");
		final String[] parts = line.split(" ");
		for(final Keyword keyword : Keyword.values()) {
			if(parts[0].equalsIgnoreCase(LanguageManager.getString("editor.line.keyword." + keyword.name().toLowerCase()))) {
				return new AlgoLine(keyword);
			}
		}
		// Switch not allowed with non constant expressions, sorry :(
		AlgoLine algoLine = null;
		if(parts[0].equalsIgnoreCase(LanguageManager.getString("editor.line.instruction.createvariable"))) {
			if(!parts[2].equalsIgnoreCase(LanguageManager.getString("editor.line.instruction.createvariable.type"))) {
				throw new ParseException("Syntax error with \"" + parts[2] + "\". Please refer to the online help.");
			}
			final String string = LanguageManager.getString("editor.line.instruction.createvariable.type.string");
			if(!parts[3].equalsIgnoreCase(string) || !parts[3].equalsIgnoreCase(LanguageManager.getString("editor.line.instruction.createvariable.type.number"))) {
				throw new ParseException("Syntax error with \"" + parts[3] + "\". Please refer to the online help.");
			}
			algoLine = new AlgoLine(Instruction.CREATE_VARIABLE, parts[1], parts[3].equalsIgnoreCase(string) ? "0" : "1");
		}
		else if(parts[0].equalsIgnoreCase(LanguageManager.getString("editor.line.instruction.assignvaluetovariable"))) {
			// TODO
		}
		if(algoLine == null) {
			throw new ParseException("Unknown keyword or instruction \"" + parts[0] + "\". Please refer to the online help.");
		}
		final String error = AlgoLineUtils.validate(null, algoLine.getInstruction(), algoLine.getArgs());
		if(error != null) {
			throw new ParseException(LanguageManager.getString(error) + " Please refer to the online help.");
		}
		return algoLine;
	}
	
	private static final int getBeginningTabCount(final String content) {
		int tabs;
		for(tabs = 0; content.charAt(tabs) == '\t'; tabs++) {}
		return tabs;
	}

	public static class ParseException extends Exception {
		
		private static final long serialVersionUID = 1L;

		public ParseException(final String message) {
			super(message);
		}
		
	}
	
}