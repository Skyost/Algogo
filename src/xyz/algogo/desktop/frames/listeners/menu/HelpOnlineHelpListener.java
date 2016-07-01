package xyz.algogo.desktop.frames.listeners.menu;

import java.awt.event.ActionEvent;
import java.net.URL;
import xyz.algogo.desktop.dialogs.ErrorDialog;
import xyz.algogo.desktop.frames.EditorFrame;
import xyz.algogo.desktop.frames.listeners.AlgorithmEditorActionListener;
import xyz.algogo.desktop.utils.JLabelLink;

public class HelpOnlineHelpListener extends AlgorithmEditorActionListener {
	
	public HelpOnlineHelpListener(final EditorFrame editor) {
		super(editor);
	}

	@Override
	public final void actionPerformed(final ActionEvent event, final EditorFrame editor) {
		try {
			JLabelLink.openBrowser(new URL("https://github.com/Skyost/Algogo/wiki"));
		}
		catch(final Exception ex) {
			ErrorDialog.errorMessage(editor, ex);
		}
	}

}