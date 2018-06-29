package xyz.algogo.desktop.editor.component.tree;

import xyz.algogo.core.language.AlgogoLanguage;
import xyz.algogo.core.statement.Statement;
import xyz.algogo.desktop.dialog.ErrorDialog;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * A class that allows to transfer a single statement.
 */

public class StatementTransfer extends ArrayList<Statement> implements Transferable {

	static {
		try {
			FLAVOR = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType + ";class=" + StatementTransfer.class.getName());
		}
		catch(final Exception ex) {
			ErrorDialog.fromThrowable(ex);
		}
	}

	/**
	 * The data flavor.
	 */

	static DataFlavor FLAVOR;

	@Override
	public final DataFlavor[] getTransferDataFlavors() {
		return new DataFlavor[]{FLAVOR};
	}

	@Override
	public final boolean isDataFlavorSupported(final DataFlavor flavor) {
		return Arrays.asList(getTransferDataFlavors()).contains(flavor);
	}

	@Override
	public final Object getTransferData(final DataFlavor flavor) {
		if(flavor.equals(FLAVOR)) {
			return this;
		}

		if(flavor.equals(DataFlavor.stringFlavor)) {
			final AlgogoLanguage language = new AlgogoLanguage();
			final StringBuilder builder = new StringBuilder();
			for(final Statement statement : this) {
				builder.append(statement.toLanguage(language));
			}

			return builder.toString();
		}

		return null;
	}

}
