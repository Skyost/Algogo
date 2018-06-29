package xyz.algogo.desktop.editor.export;

import xyz.algogo.core.statement.Statement;
import xyz.algogo.core.statement.block.BlockStatement;
import xyz.algogo.core.statement.block.conditional.IfBlock;
import xyz.algogo.core.statement.block.root.AlgorithmRootBlock;
import xyz.algogo.core.statement.simple.comment.BlockComment;
import xyz.algogo.desktop.AlgogoDesktop;
import xyz.algogo.desktop.AppLanguage;
import xyz.algogo.desktop.dialog.ErrorDialog;
import xyz.algogo.desktop.editor.EditorFrame;
import xyz.algogo.desktop.editor.component.tree.AlgorithmLocalization;
import xyz.algogo.desktop.editor.component.tree.AlgorithmTreeModel;
import xyz.algogo.desktop.editor.export.languages.LanguageExportTarget;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 * The HTML export target.
 */

public class HTMLExportTarget extends LanguageExportTarget {

	/**
	 * Creates a new HTML export target.
	 *
	 * @param editor The editor.
	 */

	public HTMLExportTarget(final EditorFrame editor) {
		super(editor, new AlgorithmLocalization(editor.getAppLanguage()));
	}

	@Override
	public final void export(final File file) {
		try {
			final EditorFrame editor = this.getEditor();
			final AppLanguage appLanguage = editor.getAppLanguage();

			final StringBuilder builder = new StringBuilder();
			builder.append("<!DOCTYPE html>").append(LINE_SEPARATOR);
			builder.append("<html lang=\"" + appLanguage.getCurrentLanguageCode() + "\">").append(LINE_SEPARATOR);
			builder.append("<meta charset=\"utf-8\">").append(LINE_SEPARATOR);

			builder.append("<title>");
			builder.append(appLanguage.getString("editor.title", editor.getCredits().getTitle(), "", editor.getCredits().getAuthor(), AlgogoDesktop.APP_NAME, AlgogoDesktop.APP_VERSION));
			builder.append("</title>").append(LINE_SEPARATOR);

			builder.append("</html>").append(LINE_SEPARATOR);
			builder.append("<body style=\"font-family: Helvetica;\">").append(LINE_SEPARATOR);
			builder.append(modelToHTML());
			builder.append("</body>").append(LINE_SEPARATOR);
			builder.append("</html>");

			Files.write(file.toPath(), builder.toString().getBytes(StandardCharsets.UTF_8));
		}
		catch(final Exception ex) {
			ErrorDialog.fromThrowable(ex, this.getEditor());
		}
	}

	/**
	 * Converts the current model's algorithm to HTML.
	 *
	 * @return The HTML content.
	 */

	public final String modelToHTML() {
		final AlgorithmTreeModel model = this.getEditor().getMainPane().getAlgorithmTreeModel();
		return algorithmRootBlockToHTML((AlgorithmRootBlock)model.getRoot().getUserObject());
	}

	/**
	 * Converts an algorithm root block to HTML.
	 *
	 * @param statement The algorithm root block.
	 *
	 * @return The HTML content.
	 */

	public final String algorithmRootBlockToHTML(final AlgorithmRootBlock statement) {
		final StringBuilder builder = new StringBuilder();
		for(final Statement child : statement.listStatements()) {
			builder.append(toHTML(child));
		}
		return builder.toString();
	}

	/**
	 * Converts a statement to HTML.
	 *
	 * @param statement The statement.
	 *
	 * @return The HTML content.
	 */

	public String toHTML(final Statement statement) {
		return toHTML(statement, "");
	}

	/**
	 * Converts an algorithm to HTML.
	 *
	 * @param statement The statement.
	 * @param indentation Current indentation.
	 *
	 * @return The HTML content.
	 */

	private String toHTML(final Statement statement, String indentation) {
		final StringBuilder builder = new StringBuilder();
		builder.append(indentation);

		if(statement instanceof BlockComment) {
			builder.append(statement.toLanguage(this.getLanguage()).replace("<br>\t", "<br>" + indentation));
		}
		else {
			builder.append(statement.toLanguage(this.getLanguage()));
		}

		builder.append("<br>" + LINE_SEPARATOR);

		if(statement instanceof BlockStatement) {
			final BlockStatement block = (BlockStatement)statement;

			indentation += "&emsp;";
			for(final Statement child : block.listStatements()) {
				builder.append(toHTML(child, indentation));
			}

			if(statement.getStatementId() == IfBlock.STATEMENT_ID && ((IfBlock)block).hasElseBlock()) {
				builder.append(toHTML(((IfBlock)block).getElseBlock(), indentation.replaceFirst("&emsp;", "")));
			}
		}

		return builder.toString();
	}

}