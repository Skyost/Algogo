package xyz.algogo.desktop.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

import xyz.algogo.core.AlgoLine;
import xyz.algogo.core.Algorithm;
import xyz.algogo.core.Instruction;
import xyz.algogo.core.Keyword;
import xyz.algogo.core.utils.VariableHolder.VariableType;

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
	
	public static final Algorithm parse(final String title, final String author, final String text) throws ParseException {
		final Algorithm algorithm = new Algorithm(title, author);
		final AlgoLine[] parsed = parse(null, text.split(System.lineSeparator()));
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
	
	public static final AlgoLine[] parse(LinkedHashMap<String, VariableType> globalVariables, final String... lines) throws ParseException {
		if(globalVariables == null) {
			globalVariables = new LinkedHashMap<String, VariableType>();
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
				globalVariables.put(args[0].toUpperCase(), args[1].equals("0") ? VariableType.STRING : VariableType.NUMBER);
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
					throw new ParseException("\"ELSE\" must follow an \"IF\".");
				}
			}
			instructions.addChild(algoLine);
		}
		return new AlgoLine[]{variables, instructions};
	}
	
	public static final AlgoLine parseLine(final String line, final int lineNumber, final LinkedHashMap<String, VariableType> globalVariables) throws ParseException {
		final String[] parts = line.replace("\t", "").split(" ");
		final String first = data.get(parts[0].toUpperCase());
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
			if(parts.length < 4) {
				throw new ParseException("This instruction needs 3 arguments (CREATE_VARIABLE <variable> TYPE <type>).");
			}
			if(!data.get(parts[2].toUpperCase()).equals("editor.line.instruction.createvariable.type")) {
				throw new ParseException("Syntax error with \"" + parts[2] + "\". Please refer to the online help.");
			}
			final String key = data.get(parts[3].toUpperCase());
			if(key == null || (!key.equals("editor.line.instruction.createvariable.type.string") && !key.equals("editor.line.instruction.createvariable.type.number"))) {
				throw new ParseException("Syntax error with \"" + parts[3] + "\". Please refer to the online help.");
			}
			algoLine = new AlgoLine(Instruction.CREATE_VARIABLE, parts[1], key.equals("editor.line.instruction.createvariable.type.string") ? "0" : "1");
		}
		else if(first.equals("editor.line.instruction.assignvaluetovariable")) {
			if(parts.length < 3) {
				throw new ParseException("This instruction needs 2 arguments (ASSIGN_VALUE_TO_VARIABLE <variable> <value>).");
			}
			if(globalVariables.get(parts[1].toUpperCase()) == null) {
				throw new ParseException("Variable not set : \"" + parts[1] + "\".");
			}
			if(!parts[2].equals("→") && !parts[2].equals("⇒") && !parts[2].equals("⇾") && !parts[2].equals("->") && !parts[2].equals("=>") && !parts[2].equals(":=") && !parts[2].equals("=") && !parts[2].equals(":")) {
				throw new ParseException("Syntax error with \"" + parts[2] + "\". Please refer to the online help.");
			}
			algoLine = new AlgoLine(Instruction.ASSIGN_VALUE_TO_VARIABLE, parts[1], Utils.join(" ", Arrays.copyOfRange(parts, 3, parts.length)));
		}
		else if(first.equals("editor.line.instruction.showvariable")) {
			if(parts.length < 2) {
				throw new ParseException("This instruction needs 1 argument (SHOW_VARIABLE <variable> [line-break]).");
			}
			if(globalVariables.get(parts[1].toUpperCase()) == null) {
				throw new ParseException("Variable not set : \"" + parts[1] + "\".");
			}
			boolean lineBreak = true; // Line break by default.
			if(parts.length > 2 && Utils.isBoolean(parts[2])) {
				lineBreak = Boolean.valueOf(parts[2]);
			}
			algoLine = new AlgoLine(Instruction.SHOW_VARIABLE, parts[1], String.valueOf(lineBreak));
		}
		else if(first.equals("editor.line.instruction.readvariable")) {
			if(parts.length < 2) {
				throw new ParseException("This instruction needs 1 argument (READ_VARIABLE <variable>).");
			}
			if(globalVariables.get(parts[1].toUpperCase()) == null) {
				throw new ParseException("Variable not set : \"" + parts[1] + "\".");
			}
			algoLine = new AlgoLine(Instruction.READ_VARIABLE, parts[1]);
		}
		else if(first.equals("editor.line.instruction.showmessage")) {
			if(parts.length < 2) {
				throw new ParseException("This instruction needs 1 arguments (SHOW_MESSAGE <message> [line-break]).");
			}
			boolean hasBoolean = false;
			boolean lineBreak = true; // Line break by default.
			if(parts.length > 2 && Utils.isBoolean(parts[parts.length - 1])) {
				lineBreak = Boolean.valueOf(parts[parts.length - 1]);
				hasBoolean = true;
			}
			algoLine = new AlgoLine(Instruction.SHOW_MESSAGE, Utils.join(" ", Arrays.copyOfRange(parts, 1, hasBoolean ? parts.length - 1 : parts.length)), String.valueOf(lineBreak));
		}
		else if(first.equals("editor.line.instruction.if")) {
			if(parts.length < 2) {
				throw new ParseException("This instruction needs 1 argument (IF <condition>).");
			}
			boolean hasElse = false; // We correct this later.
			algoLine = new AlgoLine(Instruction.IF, Utils.join(" ", Arrays.copyOfRange(parts, 1, parts.length)), String.valueOf(hasElse));
		}
		else if(first.equals("editor.line.instruction.while")) {
			if(parts.length < 2) {
				throw new ParseException("This instruction needs 1 argument (WHILE <condition>).");
			}
			algoLine = new AlgoLine(Instruction.WHILE, Utils.join(" ", Arrays.copyOfRange(parts, 1, parts.length)));
		}
		else if(first.equals("editor.line.instruction.else")) {
			algoLine = new AlgoLine(Instruction.ELSE);
		}
		else if(first.equals("editor.line.instruction.for")) {
			if(parts.length < 6) {
				throw new ParseException("This instruction needs 5 arguments (FOR <variable> FROM <value> TO <value>).");
			}
			if(globalVariables.get(parts[1].toUpperCase()) == null) {
				throw new ParseException("Variable not set : \"" + parts[1] + "\".");
			}
			final String keyFrom = data.get(parts[2].toUpperCase());
			if(keyFrom == null || !keyFrom.equals("editor.line.instruction.for.from")) {
				throw new ParseException("Syntax error with \"" + parts[2] + "\". Please refer to the online help.");
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
				throw new ParseException("You need to add a \"TO\" argument in your \"FOR\" instruction. Please refer to the online help.");
			}
			algoLine = new AlgoLine(Instruction.FOR, parts[1], Utils.join(" ", Arrays.copyOfRange(parts, 3, toIndex)), Utils.join(" ", Arrays.copyOfRange(parts, toIndex + 1, parts.length)));
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