package fr.skyost.algo.desktop.dialogs;

import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JButton;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle.ComponentPlacement;

import com.eclipsesource.json.JsonObject;

import fr.skyost.algo.desktop.AlgogoDesktop;
import fr.skyost.algo.desktop.utils.JLabelLink;
import fr.skyost.algo.desktop.utils.Utils;

public class ErrorDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	private static final String DEFAULT_LINK = "https://github.com/Skyost/Algogo/issues/new?title=Report%20an%20issue&body=**What%20were%20you%20doing%20when%20the%20error%20occurred%20%3F**%0AI%20was%20doing...%0A%0A**OS%20version%20and%20architecture%20(ex.%20Windows%208.1%20-%2064%20bits)%20%3A**%0A%20-%20%20bits%0A%0A**Java%20version%20and%20architecture%20(ex.%20Java%201.7.0_60%20-%2064%20bits)%20%3A**%0AJava%20%20-%20%20bits.%0A%0A**Stacktrace%20(the%20stacktrace%20obtained%20when%20the%20error%20occurred)%20%3A**%0A%s%0A%0A*Be%20sure%20to%20include%20any%20additional%20information%20you%20have%20!%0AThank%20you%20very%20much%20%3B)*";
	
	private final JLabel lblSubmitting = new JLabel();
	private final JLabelLink lblInfos = new JLabelLink("You can report this issue here.", new URL(DEFAULT_LINK.replace("%s", "Paste%20it%20here.")));
	private final JTextArea errorTextArea = new JTextArea();

	private ErrorDialog(final Component component, final Throwable throwable) throws MalformedURLException {
		final String error = throwable.getClass().getName();
		final StringWriter stringWriter = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(stringWriter);
		throwable.printStackTrace(printWriter);
		this.setTitle(error);
		this.setSize(600, 600);
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(AlgogoDesktop.class.getResource("/fr/skyost/algo/desktop/res/icons/app_icon.png")));
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setModalityType(ModalityType.APPLICATION_MODAL);
		this.setLocationRelativeTo(component);
		final JLabel lblMessage = new JLabel("An error occured : \"" + error + "\", here is the stacktrace :");
		lblMessage.setFont(lblMessage.getFont().deriveFont(Font.BOLD));
		errorTextArea.setText(stringWriter.toString());
		errorTextArea.setEditable(false);
		final JScrollPane scrollPane = new JScrollPane(errorTextArea);
		lblInfos.setFont(lblMessage.getFont().deriveFont(Font.ITALIC));
		lblSubmitting.setVisible(false);
		final JButton btnClose = new JButton("Close");
		btnClose.addActionListener(new ActionListener() {

			@Override
			public final void actionPerformed(final ActionEvent event) {
				ErrorDialog.this.dispose();
			}

		});
		final Container content = this.getContentPane();
		final GroupLayout groupLayout = new GroupLayout(content);
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.TRAILING).addGroup(Alignment.LEADING, groupLayout.createSequentialGroup().addContainerGap().addGroup(groupLayout.createParallelGroup(Alignment.LEADING).addComponent(scrollPane, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 564, Short.MAX_VALUE).addComponent(lblMessage, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 564, Short.MAX_VALUE).addComponent(btnClose, GroupLayout.DEFAULT_SIZE, 564, Short.MAX_VALUE).addComponent(lblInfos, GroupLayout.DEFAULT_SIZE, 564, Short.MAX_VALUE).addComponent(lblSubmitting)).addContainerGap()));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.TRAILING).addGroup(groupLayout.createSequentialGroup().addContainerGap().addComponent(lblMessage).addPreferredGap(ComponentPlacement.RELATED).addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE).addPreferredGap(ComponentPlacement.RELATED).addComponent(lblSubmitting).addPreferredGap(ComponentPlacement.RELATED).addComponent(lblInfos, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED).addComponent(btnClose).addContainerGap()));
		content.setLayout(groupLayout);
		if(!AlgogoDesktop.DEBUG) {
			new DPasteUploader().start();
		}
	}

	/**
	 * Shows an error message (no translation because it can be risky).
	 * 
	 * @param component The parent component.
	 * @param throwable The error. Will be printed.
	 * 
	 * @return The dialog.
	 */

	public static ErrorDialog errorMessage(final Component component, final Throwable throwable) {
		return errorMessage(component, throwable, true);
	}
	
	/**
	 * Shows an error message (no translation because it can be risky).
	 * 
	 * @param component The parent component.
	 * @param throwable The error.
	 * @param printStackTrace If the error should be printed.
	 * 
	 * @return The dialog.
	 */
	
	public static ErrorDialog errorMessage(final Component component, final Throwable throwable, final boolean printStackTrace) {
		try {
			if(printStackTrace) {
				throwable.printStackTrace();
			}
			final ErrorDialog dialog = new ErrorDialog(component, throwable);
			dialog.setVisible(true);
			return dialog;
		}
		catch(final Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	private class DPasteUploader extends Thread {

		@Override
		public final void run() {
			lblSubmitting.setText("Submitting to Paste.ee...");
			lblSubmitting.setIcon(new ImageIcon(AlgogoDesktop.class.getResource("/fr/skyost/algo/desktop/res/images/submitting_animation.gif")));
			lblSubmitting.setVisible(true);
			try {
				final HttpURLConnection connection = (HttpURLConnection)new URL("https://paste.ee/api").openConnection();
				connection.setRequestMethod("POST");
				connection.addRequestProperty("User-Agent", AlgogoDesktop.APP_NAME + " by " + Utils.join(' ', AlgogoDesktop.APP_AUTHORS) + " v" + AlgogoDesktop.APP_VERSION);
				connection.setDoOutput(true);
				final DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
				wr.writeBytes("key=e011d9928427c6238dcb01fd66b3006e&description=" + ErrorDialog.this.getTitle() + "&format=json&paste=" + URLEncoder.encode(errorTextArea.getText(), StandardCharsets.UTF_8.displayName()));
				wr.flush();
				wr.close();
				final String response = connection.getResponseCode() + " " + connection.getResponseMessage();
				if(!response.startsWith("2")) {
					throw new IOException("Invalid response : \"" + response + "\".");
				}
				final BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String line;
				final StringBuilder builder = new StringBuilder();
				while((line = in.readLine()) != null) {
					builder.append(line);
				}
				in.close();
				lblInfos.setLink(new URL(DEFAULT_LINK.replace("%s", JsonObject.readFrom(builder.toString()).get("paste").asObject().get("link").asString() + ".")));
			}
			catch(final Exception ex) {
				ex.printStackTrace();
			}
			lblSubmitting.setVisible(false);
		}

	}
}