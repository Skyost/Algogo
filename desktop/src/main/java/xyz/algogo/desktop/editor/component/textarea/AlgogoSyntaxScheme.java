package xyz.algogo.desktop.editor.component.textarea;

import org.fife.ui.rsyntaxtextarea.Style;
import org.fife.ui.rsyntaxtextarea.SyntaxScheme;

import java.awt.Font;

import xyz.algogo.desktop.antlr.AlgogoHighlighting;
import xyz.algogo.desktop.editor.component.tree.AlgorithmTreeRenderer;

/**
 * Represents the Algogo language syntax scheme.
 */

public class AlgogoSyntaxScheme extends SyntaxScheme {

	/**
	 * Syntax base font.
	 */

	private Font baseFont;

	/**
	 * Creates a new Algogo syntax scheme.
	 *
	 * @param baseFont The syntax base font.
	 */

	AlgogoSyntaxScheme(final Font baseFont) {
		super(true);

		this.baseFont = baseFont;
	}

	@Override
	public final Style getStyle(final int index) {
		final Style style = new Style();
		switch(index) {
			case AlgogoHighlighting.VARIABLES:
			case AlgogoHighlighting.BEGINNING:
			case AlgogoHighlighting.END:
				style.font = baseFont.deriveFont(Font.BOLD);
				style.foreground = AlgorithmTreeRenderer.ROOT_BLOCK_STATEMENT_COLOR;
				break;
			case AlgogoHighlighting.PRINT:
			case AlgogoHighlighting.PRINT_VARIABLE:
			case AlgogoHighlighting.PROMPT:
				style.font = baseFont.deriveFont(Font.BOLD);
				style.foreground = AlgorithmTreeRenderer.SIMPLE_STATEMENT_COLOR;
				break;
			case AlgogoHighlighting.IF:
			case AlgogoHighlighting.THEN:
			case AlgogoHighlighting.ELSE:
			case AlgogoHighlighting.WHILE:
			case AlgogoHighlighting.DO:
			case AlgogoHighlighting.FOR:
			case AlgogoHighlighting.FROM:
			case AlgogoHighlighting.TO:
				style.font = baseFont.deriveFont(Font.BOLD);
				style.foreground = AlgorithmTreeRenderer.BLOCK_STATEMENT_COLOR;
				break;
			case AlgogoHighlighting.LineComment:
			case AlgogoHighlighting.BlockComment:
				style.font = baseFont.deriveFont(Font.ITALIC);
				style.foreground = AlgorithmTreeRenderer.COMMENT_COLOR;
				break;
			case AlgogoHighlighting.TYPE_NUMBER:
			case AlgogoHighlighting.TYPE_STRING:
			case AlgogoHighlighting.NO_LINE_BREAK:
				style.underline = true;
		}

		return style;
	}

	/**
	 * Returns the syntax base font.
	 *
	 * @return The syntax base font.
	 */

	public final Font getBaseFont() {
		return baseFont;
	}

	/**
	 * Sets the syntax base font.
	 *
	 * @param baseFont The base font.
	 */

	public final void setBaseFont(final Font baseFont) {
		this.baseFont = baseFont;
	}

}
