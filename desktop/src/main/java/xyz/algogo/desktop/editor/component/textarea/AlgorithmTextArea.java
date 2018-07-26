package xyz.algogo.desktop.editor.component.textarea;

import org.fife.ui.autocomplete.AutoCompletion;
import org.fife.ui.autocomplete.BasicCompletion;
import org.fife.ui.autocomplete.CompletionProvider;
import org.fife.ui.autocomplete.DefaultCompletionProvider;
import org.fife.ui.rsyntaxtextarea.AbstractTokenMakerFactory;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.TokenMakerFactory;

import java.awt.datatransfer.ClipboardOwner;

import javax.swing.JOptionPane;

import xyz.algogo.core.Algorithm;
import xyz.algogo.core.exception.ParseException;
import xyz.algogo.core.language.AlgogoLanguage;
import xyz.algogo.desktop.AppLanguage;
import xyz.algogo.desktop.antlr.AlgogoHighlighting;
import xyz.algogo.desktop.dialog.ErrorDialog;
import xyz.algogo.desktop.editor.EditorFrame;
import xyz.algogo.desktop.editor.component.EditorMainComponent;

/**
 * A text area that syntax highlights algorithm content.
 */

public class AlgorithmTextArea extends RSyntaxTextArea implements EditorMainComponent {

	static {
		final AbstractTokenMakerFactory factory = (AbstractTokenMakerFactory)TokenMakerFactory.getDefaultInstance();
		factory.putMapping("text/algogo", "xyz.algogo.desktop.editor.component.textarea.AlgogoTokenMaker");
	}

	/**
	 * The editor.
	 */

	private EditorFrame editor;

	/**
	 * Creates a new algorithm text area.
	 *
	 * @param editor The editor.
	 */

	public AlgorithmTextArea(final EditorFrame editor) {
		this.editor = editor;
		this.setSyntaxEditingStyle("text/algogo");
		this.setSyntaxScheme(new AlgogoSyntaxScheme(this.getFont()));
		this.getDocument().addUndoableEditListener(new AlgorithmUndoableEditListener(this));

		final CompletionProvider provider = createCompletionProvider();

		final AutoCompletion autoCompletion = new AutoCompletion(provider);
		autoCompletion.setAutoActivationEnabled(true);
		autoCompletion.setAutoCompleteSingleChoices(false);
		autoCompletion.setAutoActivationDelay(0);
		autoCompletion.install(this);
	}

	@Override
	public final void newAlgorithm() {
		this.setText(new Algorithm().toLanguage(new AlgogoLanguage()));
	}

	@Override
	public final String getContent() {
		return this.getText();
	}

	@Override
	public final void open(final String content) {
		this.setText(content);
	}

	@Override
	public final void deleteSelection() {
		this.replaceSelection(null);
	}

	@Override
	public final void copySelection(final ClipboardOwner clipboardOwner) {
		this.copy();
	}

	@Override
	public final void pasteClipboard() {
		this.paste();
	}

	@Override
	public final Algorithm toAlgorithm() {
		try {
			final Algorithm algorithm = Algorithm.parse(this.getText());
			editor.getCredits().apply(algorithm);

			return algorithm;
		}
		catch(final ParseException ex) {
			final AppLanguage language = editor.getAppLanguage();
			JOptionPane.showMessageDialog(editor, language.getString("editor.dialog.parseError.message", ex.getLine(), ex.getErrorMessage()), language.getString("editor.dialog.parseError.title"), JOptionPane.ERROR_MESSAGE);
		}
		catch(final Exception ex) {
			ErrorDialog.fromThrowable(ex, editor);
		}

		return null;
	}

	@Override
	public final void undoLastAction() {
		editor.getEditorHistory().undo();
	}

	@Override
	public final void redoLastAction() {
		editor.getEditorHistory().redo();
	}

	/**
	 * Returns the editor.
	 *
	 * @return The editor.
	 */

	public final EditorFrame getEditor() {
		return editor;
	}

	/**
	 * Sets the editor.
	 *
	 * @param editor The editor.
	 */

	public final void setEditor(final EditorFrame editor) {
		this.editor = editor;
	}

	/**
	 * Creates a completion provider object.
	 *
	 * @return The completion provider object.
	 */

	private CompletionProvider createCompletionProvider() {
		final DefaultCompletionProvider provider = new DefaultCompletionProvider();

		for(final int type : new int[]{
				AlgogoHighlighting.VARIABLES,
				AlgogoHighlighting.BEGINNING,
				AlgogoHighlighting.END,
				AlgogoHighlighting.ASSIGN,
				AlgogoHighlighting.TYPE_STRING,
				AlgogoHighlighting.TYPE_NUMBER,
				AlgogoHighlighting.IF,
				AlgogoHighlighting.THEN,
				AlgogoHighlighting.ELSE,
				AlgogoHighlighting.WHILE,
				AlgogoHighlighting.DO,
				AlgogoHighlighting.FOR,
				AlgogoHighlighting.FROM,
				AlgogoHighlighting.TO,
				AlgogoHighlighting.PRINT,
				AlgogoHighlighting.PRINT_VARIABLE,
				AlgogoHighlighting.PROMPT,
				AlgogoHighlighting.NO_LINE_BREAK
		}) {
			final String name = AlgogoHighlighting.VOCABULARY.getLiteralName(type);
			provider.addCompletion(new BasicCompletion(provider, name.substring(1, name.length() - 1)));
		}

		provider.setAutoActivationRules(true, null);
		return provider;
	}

}