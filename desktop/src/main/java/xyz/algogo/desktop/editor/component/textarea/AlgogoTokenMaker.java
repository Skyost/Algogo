package xyz.algogo.desktop.editor.component.textarea;

import org.antlr.v4.runtime.CharStreams;
import org.fife.ui.rsyntaxtextarea.Token;
import org.fife.ui.rsyntaxtextarea.TokenImpl;
import org.fife.ui.rsyntaxtextarea.TokenMakerBase;
import xyz.algogo.desktop.antlr.AlgogoHighlighting;

import javax.swing.text.Segment;
import java.util.LinkedList;
import java.util.List;

/**
 * The Algogo token maker, used to tokenize the text area content.
 */

public class AlgogoTokenMaker extends TokenMakerBase {

	@Override
	public final Token getTokenList(final Segment text, final int initialTokenType, final int startOffset) {
		if(text == null) {
			throw new IllegalArgumentException("Text cannot be null.");
		}

		final AlgogoHighlighting lexer = new AlgogoHighlighting(CharStreams.fromString(text.toString()));
		final LinkedList<org.antlr.v4.runtime.Token> tokens = new LinkedList<>();
		while(!lexer._hitEOF) {
			tokens.add(lexer.nextToken());
		}

		return toList(text, startOffset, tokens);
	}

	/**
	 * Useful method to tokenize an input.
	 *
	 * @param text Input text.
	 * @param startOffset Start offset.
	 * @param antlrTokens Current ANTLR tokens.
	 *
	 * @return The tokenized input.
	 */

	private Token toList(final Segment text, final int startOffset, final List<org.antlr.v4.runtime.Token> antlrTokens) {
		if(antlrTokens.isEmpty()) {
			return null;
		}

		final org.antlr.v4.runtime.Token antlrToken = antlrTokens.get(0);
		final TokenImpl tokenImplementation = new TokenImpl(text, text.offset + antlrToken.getStartIndex(), text.offset + antlrToken.getStartIndex() + antlrToken.getText().length() - 1, startOffset + antlrToken.getStartIndex(), antlrToken.getType(), 0);
		tokenImplementation.setNextToken(toList(text, startOffset, antlrTokens.subList(1, antlrTokens.size())));
		return tokenImplementation;
	}

}
