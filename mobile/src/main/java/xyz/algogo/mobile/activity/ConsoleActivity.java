package xyz.algogo.mobile.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import xyz.algogo.core.Algorithm;
import xyz.algogo.core.evaluator.context.EvaluationContext;
import xyz.algogo.core.evaluator.context.InputListener;
import xyz.algogo.core.evaluator.context.OutputListener;
import xyz.algogo.core.statement.Statement;
import xyz.algogo.mobile.R;
import xyz.algogo.mobile.utils.Utils;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

/**
 * Represents the console activity.
 */

public class ConsoleActivity extends AppCompatActivity implements InputListener, OutputListener {

	/**
	 * The current evaluation context (allows to stop the algorithm).
	 */

	private EvaluationContext currentContext;

	@Override
	protected final void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_console);
		this.setSupportActionBar(findViewById(R.id.console_toolbar));

		final FloatingActionButton fab = this.findViewById(R.id.console_fab);
		fab.setOnClickListener(view -> {
			if(currentContext == null) {
				final Algorithm algorithm = this.getIntent().hasExtra(MainActivity.INTENT_CURRENT_ALGORITHM) ? (Algorithm)this.getIntent().getSerializableExtra(MainActivity.INTENT_CURRENT_ALGORITHM) : new Algorithm();
				if(algorithm == null) {
					return;
				}

				currentContext = new EvaluationContext(this, this);
				((EditText)this.findViewById(R.id.console_content)).setText(null);
				fab.setImageResource(R.drawable.menu_console_fab_stop);
				new Thread(() -> {
					final Exception ex = algorithm.evaluate(currentContext);
					if(ex != null) {
						output(null, System.getProperty("line.separator"));
						output(null, this.getString(R.string.console_error) + System.getProperty("line.separator"));
						output(null, ex.getMessage() + System.getProperty("line.separator"));
						output(null, Utils.fromStackTrace(ex));
					}

					this.runOnUiThread(this::setFabDefaultStyle);
					currentContext = null;
				}).start();
				return;
			}

			currentContext.setStopped(true);
			currentContext = null;
		});
		setFabDefaultStyle();

		final ActionBar actionBar = this.getSupportActionBar();
		if(actionBar == null) {
			return;
		}

		actionBar.setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public final void onSaveInstanceState(final Bundle outState) {
		if(currentContext != null) {
			currentContext.setStopped(true);
			currentContext = null;
		}

		super.onSaveInstanceState(outState);
	}

	@Override
	public final boolean onOptionsItemSelected(final MenuItem item) {
		switch(item.getItemId()) {
		case android.R.id.home:
			this.onBackPressed();
			break;
		}

		return true;
	}

	@Override
	public final Object input(final Statement source, final Object... arguments) {
		try {
			final ValueCallable callable = new ValueCallable();

			this.runOnUiThread(() -> {
				final View layout = this.getLayoutInflater().inflate(R.layout.console_dialog_prompt, null);
				new AlertDialog.Builder(ConsoleActivity.this)
						.setTitle(R.string.console_dialog_title)
						.setMessage(arguments[1] == null ? this.getString(R.string.console_dialog_message, arguments[0].toString()) : arguments[1].toString())
						.setView(layout)
						.setPositiveButton(android.R.string.ok, (dialog, selected) -> callable.setMessage(((EditText)layout.findViewById(R.id.console_dialog_input)).getText().toString()))
						.setCancelable(false)
						.create()
						.show();
			});

			final ValueCallableObject object = Executors.newFixedThreadPool(1).submit(callable).get();
			if(object.exception != null) {
				throw new Exception(object.message, object.exception);
			}

			return object.message;
		}
		catch(final Exception ex) {
			ex.printStackTrace();

			output(null, Utils.fromStackTrace(ex));
		}
		return null;
	}

	@Override
	public final void output(final Statement source, final String content) {
		final EditText console = this.findViewById(R.id.console_content);
		this.runOnUiThread(() -> console.append(content));
	}

	/**
	 * Sets the default style of the FAB (default icon).
	 */

	private void setFabDefaultStyle() {
		final FloatingActionButton fab = this.findViewById(R.id.console_fab);
		fab.setImageResource(R.drawable.menu_console_fab_run);
	}

	/**
	 * Allows to pass messages between threads.
	 */

	private class ValueCallable implements Callable<ValueCallableObject> {

		/**
		 * The held message.
		 */

		private String message;

		@Override
		public final ValueCallableObject call() {
			synchronized(this) {
				try {
					this.wait();
				}
				catch(final Exception ex) {
					return new ValueCallableObject(ex, "An exception occured while waiting for the response.");
				}
				return new ValueCallableObject(null, message);
			}
		}

		/**
		 * Sets the message to hold.
		 *
		 * @param message The message to hold.
		 */

		private void setMessage(final String message) {
			synchronized(this) {
				this.message = message;
				this.notify();
			}
		}

	}

	/**
	 * The object to return thanks to the Callable.
	 */

	private class ValueCallableObject {

		/**
		 * The held exception.
		 */

		private final Throwable exception;

		/**
		 * The held message.
		 */

		private final String message;

		/**
		 * Creates a new value callable object.
		 *
		 * @param exception The exception to hold.
		 * @param message The message to hold.
		 */

		private ValueCallableObject(final Throwable exception, final String message) {
			this.exception = exception;
			this.message = message;
		}

	}

}
