package xyz.algogo.desktop.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

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
			data.put(value.toUpperCase(), key);
			if(!value.equals(withoutAccents)) {
				keywordsWithoutAccent.put(keyword, withoutAccents);
				data.put(withoutAccents.toUpperCase(), key);
			}
		}
		for(final Instruction instruction : Instruction.values()) {
			final String key = "editor.line.instruction." + instruction.name().toLowerCase().replace("_", "");
			final String value = LanguageManager.getString(key);
			final String withoutAccents = Utils.stripAccents(value);
			data.put(value.toUpperCase(), key);
			if(!value.equals(withoutAccents)) {
				instructionsWithoutAccent.put(instruction, withoutAccents);
				data.put(withoutAccents.toUpperCase(), key);
			}
		}
		for(final String misc : new String[]{"editor.line.instruction.createvariable.type", "editor.line.instruction.createvariable.type.string", "editor.line.instruction.createvariable.type.number", "editor.line.instruction.createvariable.type.string", "editor.line.instruction.createvariable.type.number", "editor.line.instruction.for.from", "editor.line.instruction.for.to"}) {
			final String value = LanguageManager.getString(misc);
			final String withoutAccents = Utils.stripAccents(value);
			data.put(value.toUpperCase(), misc);
			if(!value.equals(withoutAccents)) {
				miscWithoutAccent.put(misc, withoutAccents);
				data.put(withoutAccents.toUpperCase(), misc);
			}
		}
	}
	
	private int currentLine = 0;
	
	public final Algorithm parse(final String title, final String author, final String text) throws ParseException, IOException {
		final Algorithm algorithm = new Algorithm(title, author);
		final List<String> lines = new ArrayList<String>();
		
		final BufferedReader reader = new BufferedReader(new StringReader(text));
		String line;
		while((line = reader.readLine()) != null) {
			if(line.length() == 0 || line.replace(" ", "").length() == 0 || line.replace("\t", "").length() == 0) {
				continue;
			}
			lines.add(line);
		}
		
		final AlgoLine[] parsed = parse(null, lines.toArray(new String[lines.size()]));
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
	
	public final AlgoLine[] parse(LinkedHashMap<String, Boolean> globalVariables, final String... lines) throws ParseException {
		if(globalVariables == null) {
			globalVariables = new LinkedHashMap<String, Boolean>();
		}
		final AlgoLine variables = new AlgoLine(Keyword.VARIABLES);
		final AlgoLine instructions = new AlgoLine(Keyword.BEGINNING);
		for(int i = 0; i != lines.length; i++) {
			final String line = lines[i];
			final AlgoLine algoLine = parseLine(line, i, globalVariables);
			final Instruction instruction = algoLine.getInstruction();
			if(instruction == null) {
				continue;
			}
			if(instruction == Instruction.CREATE_VARIABLE) {
				final String[] args = algoLine.getArgs();
				variables.addChild(algoLine);
				globalVariables.put(args[0].toUpperCase(), args[1].equals("0"));
				continue;
			}
			if(algoLine.getAllowsChildren()) {
				final int tabs = getBeginningTabCount(line);
				int j;
				for(j = i + 1; j != lines.length; j++) {
					if(getBeginningTabCount(lines[j]) <= tabs) {
						break;
					}
				}
				for(final AlgoLine child : parse(globalVariables, Arrays.copyOfRange(lines, i + 1, j))[1].getChildren()) {
					algoLine.addChild(child);
				}
				i = j - 1;
			}
			final int previousIndex = instructions.getChildren().size() - 1;
			if(previousIndex >= 0) {
				final AlgoLine iff = instructions.getChildAt(previousIndex);
				if(iff.getInstruction() == Instruction.IF) {
					iff.setArgs(iff.getArgs()[0], String.valueOf(instruction == Instruction.ELSE));
				}
				else if(instruction == Instruction.ELSE) {
					throw new ParseException(currentLine, "\"ELSE\" must follow an \"IF\".");
				}
			}
			instructions.addChild(algoLine);
		}
		return new AlgoLine[]{variables, instructions};
	}
	
	public final AlgoLine parseLine(final String line, final int lineNumber, final LinkedHashMap<String, Boolean> globalVariables) throws ParseException {
		currentLine++;
		final String[] parts = line.replace("\t", "").split(" ");
		final String first = data.get(parts[0].toUpperCase());
		if(first == null) {
			throw new ParseException(currentLine, "Syntax error with \"" + parts[0] + "\" (not a keyword or instruction). Please refer to the online help.");
		}
		for(final Keyword keyword : Keyword.values()) {
			if(first.equals("editor.line.keyword." + keyword.name().toLowerCase())) {
				return new AlgoLine(keyword);
			}
		}
		AlgoLine algoLine = null;
		switch(first) {
		case "editor.line.instruction.createvariable":
			if(parts.length < 4) {
				throw new ParseException(currentLine, "This instruction needs 3 arguments (CREATE_VARIABLE <variable> TYPE <type>).");
			}
			if(!data.get(parts[2].toUpperCase()).equals("editor.line.instruction.createvariable.type")) {
				throw new ParseException(currentLine, "Syntax error with \"" + parts[2] + "\". Please refer to the online help.");
			}
			final String key = data.get(parts[3].toUpperCase());
			if(key == null || (!key.equals("editor.line.instruction.createvariable.type.string") && !key.equals("editor.line.instruction.createvariable.type.number"))) {
				throw new ParseException(currentLine, "Syntax error with \"" + parts[3] + "\". Please refer to the online help.");
			}
			algoLine = new AlgoLine(Instruction.CREATE_VARIABLE, parts[1], key.equals("editor.line.instruction.createvariable.type.string") ? "0" : "1");
			break;
		case "editor.line.instruction.assignvaluetovariable":
			if(parts.length < 3) {
				throw new ParseException(currentLine, "This instruction needs 2 arguments (ASSIGN_VALUE_TO_VARIABLE <variable> <value>).");
			}
			if(globalVariables.get(parts[1].toUpperCase()) == null) {
				throw new ParseException(currentLine, "Variable not set : \"" + parts[1] + "\".");
			}
			if(!parts[2].equals("→") && !parts[2].equals("⇒") && !parts[2].equals("⇾") && !parts[2].equals("->") && !parts[2].equals("=>") && !parts[2].equals(":=") && !parts[2].equals("=") && !parts[2].equals(":")) {
				throw new ParseException(currentLine, "Syntax error with \"" + parts[2] + "\". Please refer to the online help.");
			}
			algoLine = new AlgoLine(Instruction.ASSIGN_VALUE_TO_VARIABLE, parts[1], Utils.join(" ", Arrays.copyOfRange(parts, 3, parts.length)));
			break;
		case "editor.line.instruction.showvariable":
			if(parts.length < 2) {
				throw new ParseException(currentLine, "This instruction needs 1 argument (SHOW_VARIABLE <variable> [line-break]).");
			}
			if(globalVariables.get(parts[1].toUpperCase()) == null) {
				throw new ParseException(currentLine, "Variable not set : \"" + parts[1] + "\".");
			}
			algoLine = new AlgoLine(Instruction.SHOW_VARIABLE, parts[1], String.valueOf(parts.length > 2 && Utils.isBoolean(parts[2]) ? Boolean.valueOf(parts[2]) : true)); // Line break by default.
			break;
		case "editor.line.instruction.readvariable":
			if(parts.length < 2) {
				throw new ParseException(currentLine, "This instruction needs 1 argument (READ_VARIABLE <variable>).");
			}
			if(globalVariables.get(parts[1].toUpperCase()) == null) {
				throw new ParseException(currentLine, "Variable not set : \"" + parts[1] + "\".");
			}
			algoLine = new AlgoLine(Instruction.READ_VARIABLE, parts[1]);
			break;
		case "editor.line.instruction.showmessage":
			if(parts.length < 2) {
				throw new ParseException(currentLine, "This instruction needs 1 arguments (SHOW_MESSAGE <message> [line-break]).");
			}
			boolean hasBoolean = false;
			boolean lineBreak = true; // Line break by default.
			if(parts.length > 2 && Utils.isBoolean(parts[parts.length - 1])) {
				lineBreak = Boolean.valueOf(parts[parts.length - 1]);
				hasBoolean = true;
			}
			algoLine = new AlgoLine(Instruction.SHOW_MESSAGE, Utils.join(" ", Arrays.copyOfRange(parts, 1, hasBoolean ? parts.length - 1 : parts.length)), String.valueOf(lineBreak));
			break;
		case "editor.line.instruction.if":
			if(parts.length < 2) {
				throw new ParseException(currentLine, "This instruction needs 1 argument (IF <condition>).");
			}
			boolean hasElse = false; // We correct this later.
			algoLine = new AlgoLine(Instruction.IF, Utils.join(" ", Arrays.copyOfRange(parts, 1, parts.length)), String.valueOf(hasElse));
			break;
		case "editor.line.instruction.while":
			if(parts.length < 2) {
				throw new ParseException(currentLine, "This instruction needs 1 argument (WHILE <condition>).");
			}
			algoLine = new AlgoLine(Instruction.WHILE, Utils.join(" ", Arrays.copyOfRange(parts, 1, parts.length)));
			break;
		case "editor.line.instruction.else":
			algoLine = new AlgoLine(Instruction.ELSE);
			break;
		case "editor.line.instruction.for":
			if(parts.length < 6) {
				throw new ParseException(currentLine, "This instruction needs 5 arguments (FOR <variable> FROM <value> TO <value>).");
			}
			if(globalVariables.get(parts[1].toUpperCase()) == null) {
				throw new ParseException(currentLine, "Variable not set : \"" + parts[1] + "\".");
			}
			final String keyFrom = data.get(parts[2].toUpperCase());
			if(keyFrom == null || !keyFrom.equals("editor.line.instruction.for.from")) {
				throw new ParseException(currentLine, "Syntax error with \"" + parts[2] + "\". Please refer to the online help.");
			}
			int toIndex = -1;
			for(int i = 4; i != parts.length; i++) {
				final String keyTo = data.get(parts[i].toUpperCase());
				if(keyTo == null || !keyTo.equals("editor.line.instruction.for.to")) {
					continue;
				}
				toIndex = i;
			}
			if(toIndex == -1) {
				throw new ParseException(currentLine, "You need to add a \"TO\" argument in your \"FOR\" instruction. Please refer to the online help.");
			}
			algoLine = new AlgoLine(Instruction.FOR, parts[1], Utils.join(" ", Arrays.copyOfRange(parts, 3, toIndex)), Utils.join(" ", Arrays.copyOfRange(parts, toIndex + 1, parts.length)));
			break;
		}
		if(algoLine == null) {
			throw new ParseException(currentLine, "Unknown instruction \"" + parts[0] + "\". Please refer to the online help.");
		}
		final String error = AlgoLineUtils.validate(null, algoLine.getInstruction(), algoLine.getArgs());
		if(error != null) {
			throw new ParseException(currentLine, LanguageManager.getString(error) + " Please refer to the online help.");
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
		
		private final int currentLine;

		public ParseException(final int currentLine, final String message) {
			super(message);
			this.currentLine = currentLine;
		}
		
		/**
		 * Gets the current line.
		 * 
		 * @return The current line.
		 */
		
		public final int getCurrentLine() {
			return currentLine;
		}
		
	}
	
}