package xyz.algogo.desktop.utils;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import xyz.algogo.core.AlgoLine;
import xyz.algogo.desktop.dialogs.ErrorDialog;

public class AlgoLineListSelection extends ArrayList<AlgoLine> implements Transferable {
	
	private static final long serialVersionUID = 1L;
	
	private static DataFlavor algoLineListSelectionFlavor;
	static {
		try {
			algoLineListSelectionFlavor = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType + ";class=" + AlgoLineListSelection.class.getName());
		}
		catch(final ClassNotFoundException ex) {
			ErrorDialog.errorMessage(null, ex);
		}
	}
	
	@Override
	public final Object getTransferData(final DataFlavor flavor) throws UnsupportedFlavorException, IOException {
		final DataFlavor[] flavors = getTransferDataFlavors();
		if(flavor.equals(flavors[0])) {
			return this;
		}
		else if(flavor.equals(flavors[1])) {
			final StringBuilder builder = new StringBuilder();
			final TextLanguage text = new TextLanguage(false, false);
			for(final AlgoLine line : this) {
				builder.append(text.translate(line));
			}
			return builder.toString();
		}
		return null;
	}
	
	@Override
	public final DataFlavor[] getTransferDataFlavors() {
		return new DataFlavor[]{algoLineListSelectionFlavor, DataFlavor.stringFlavor};
	}
	
	@Override
	public final boolean isDataFlavorSupported(final DataFlavor flavor) {
		return Arrays.asList(getTransferDataFlavors()).contains(flavor);
	}
	
	public static DataFlavor getAlgoLineListSelectionFlavor() {
		return algoLineListSelectionFlavor;
	}
	
}