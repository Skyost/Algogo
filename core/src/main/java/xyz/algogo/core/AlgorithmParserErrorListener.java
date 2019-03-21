package xyz.algogo.core;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

import xyz.algogo.core.exception.ParseException;

/**
 * The default algorithm parser error listener.
 */

public class AlgorithmParserErrorListener extends BaseErrorListener {

	@Override
	public void syntaxError(final Recognizer<?, ?> recognizer, final Object offendingSymbol, final int line, final int position, final String message, final RecognitionException cause) throws ParseException {
		throw new ParseException(offendingSymbol, line, position, message, cause);
	}

}