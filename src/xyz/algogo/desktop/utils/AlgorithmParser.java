package xyz.algogo.desktop.utils;

import java.util.HashMap;

import xyz.algogo.core.AlgoLine;
import xyz.algogo.core.Algorithm;
import xyz.algogo.core.Instruction;
import xyz.algogo.core.Keyword;

public class AlgorithmParser {
	
	private static final HashMap<String, String> data = new HashMap<String, String>();
	
	private static final HashMap<Keyword, String> keywordsWithoutAccent = new HashMap<Keyword, String>();
	private static final HashMap<Instruction, String> instructionsWithoutAccent = new HashMap<Instruction, String>();
	private static final HashMap<String, String> miscWithoutAccent = new HashMap<String, String>();
	static {
		for(final Keyword keyword : Keyword.values()) {
			final String key = "editor.line.keyword." + keyword.name().toLowerCase();
			final String value = LanguageManager.getString(key);
			final String withoutAccents = Utils.stripAccents(value);
			data.put(value, key);
			if(!value.equals(withoutAccents)) {
				keywordsWithoutAccent.put(keyword, withoutAccents);
				data.put(withoutAccents, key);
			}
		}
		for(final Instruction instruction : Instruction.values()) {
			final String key = "editor.line.instruction." + instruction.name().toLowerCase().replace("_", "");
			final String value = LanguageManager.getString(key);
			final String withoutAccents = Utils.stripAccents(value);
			data.put(value, key);
			if(!value.equals(withoutAccents)) {
				instructionsWithoutAccent.put(instruction, withoutAccents);
				data.put(withoutAccents, key);
			}
		}
		for(final String misc : new String[]{"editor.line.instruction.createvariable.type", "editor.line.instruction.createvariable.type.string", "editor.line.instruction.createvariable.type.number", "editor.line.instruction.createvariable.type.string", "editor.line.instruction.createvariable.type.number", "editor.line.instruction.for.from", "editor.line.instruction.for.to"}) {
			final String value = LanguageManager.getString(misc);
			final String withoutAccents = Utils.stripAccents(value);
			data.put(value, misc);
			if(!value.equals(withoutAccents)) {
				miscWithoutAccent.put(misc, withoutAccents);
				data.put(withoutAccents, misc);
			}
		}
	}
	
	public static final Algorithm parse(final String title, final String author, final String text) throws ParseException {
		final Algorithm algorithm = new Algorithm(title, author);
		final AlgoLine[] parsed = parse(text.split(System.lineSeparator()));
		final AlgoLine variables = algorithm.getVariables();
		for(final AlgoLine variable : parsed[0].getChildren()) {
			variables.addChild(variable);
		}
		final AlgoLine instructions = algorithm.getInstructions();
		for(final AlgoLine instruction : parsed[1].getChildren()) {
			instructions.addChild(instruction);
		}
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
		final String first = data.get(parts[0]);
		if(first == null) {
			throw new ParseException("Syntax error with \"" + parts[0] + "\" (not a keyword or instruction). Please refer to the online help.");
		}
		for(final Keyword keyword : Keyword.values()) {
			if(first.equals("editor.line.keyword." + keyword.name().toLowerCase())) {
				return new AlgoLine(keyword);
			}
		}
		// Switch not allowed with non constant expressions, sorry :(
		AlgoLine algoLine = null;
		if(first.equals("editor.line.instruction.createvariable")) {
			if(!data.get(parts[2]).equals("editor.line.instruction.createvariable.type")) {
				throw new ParseException("Syntax error with \"" + parts[2] + "\". Please refer to the online help.");
			}
			final String key = data.get(parts[3]);
			if(key == null || (!key.equals("editor.line.instruction.createvariable.type.string") && !key.equals("editor.line.instruction.createvariable.type.number"))) {
				throw new ParseException("Syntax error with \"" + parts[3] + "\". Please refer to the online help.");
			}
			algoLine = new AlgoLine(Instruction.CREATE_VARIABLE, parts[1], key.equals("editor.line.instruction.createvariable.type.string") ? "0" : "1");
		}
		else if(first.equals("editor.line.instruction.assignvaluetovariable")) {
			// TODO
		}
		if(algoLine == null) {
			throw new ParseException("Unknown instruction \"" + parts[0] + "\". Please refer to the online help.");
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
	
	public static final HashMap<Keyword, String> getKeywordsWithoutAccent() {
		return keywordsWithoutAccent;
	}
	
	public static final HashMap<Instruction, String> getKeywordsInstructionsAccent() {
		return instructionsWithoutAccent;
	}
	
	public static final HashMap<String, String> getMiscWithoutAccent() {
		return miscWithoutAccent;
	}
	
	public static class ParseException extends Exception {
		
		private static final long serialVersionUID = 1L;

		public ParseException(final String message) {
			super(message);
		}
		
	}
	
}