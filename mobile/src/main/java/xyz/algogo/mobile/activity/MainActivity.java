package xyz.algogo.mobile.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import com.dekoservidoni.omfm.OneMoreFabMenu;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import com.rustamg.filedialogs.FileDialog;
import com.rustamg.filedialogs.OpenFileDialog;
import com.rustamg.filedialogs.SaveFileDialog;

import java.io.File;

import de.mateware.snacky.Snacky;
import xyz.algogo.core.Algorithm;
import xyz.algogo.core.evaluator.variable.VariableType;
import xyz.algogo.core.exception.ParseException;
import xyz.algogo.core.language.AlgogoLanguage;
import xyz.algogo.core.statement.Statement;
import xyz.algogo.core.statement.block.conditional.IfBlock;
import xyz.algogo.core.statement.block.loop.ForLoop;
import xyz.algogo.core.statement.block.loop.WhileLoop;
import xyz.algogo.core.statement.simple.comment.BlockComment;
import xyz.algogo.core.statement.simple.comment.LineComment;
import xyz.algogo.core.statement.simple.io.PrintStatement;
import xyz.algogo.core.statement.simple.io.PrintVariableStatement;
import xyz.algogo.core.statement.simple.io.PromptStatement;
import xyz.algogo.core.statement.simple.variable.AssignStatement;
import xyz.algogo.core.statement.simple.variable.CreateVariableStatement;
import xyz.algogo.mobile.AlgorithmMobileLineEditor;
import xyz.algogo.mobile.R;
import xyz.algogo.mobile.adapter.AlgorithmAdapter;
import xyz.algogo.mobile.utils.Utils;
import xyz.algogo.mobile.view.AlgorithmRecyclerView;

/**
 * Represents the launcher activity.
 */

public class MainActivity extends AppCompatActivity implements AlgorithmMobileLineEditor.Listener, OneMoreFabMenu.OptionsClick, FileDialog.OnFileSelectedListener {

	/**
	 * Allows to pass current adapter path.
	 */

	private static final String INTENT_ADAPTER_PATH = "adapterPath";

	/**
	 * Allows to pass current algorithm.
	 */

	static final String INTENT_CURRENT_ALGORITHM = "currentAlgorithm";

	/**
	 * Allows to pass the current algorithm path.
	 */

	private static final String INTENT_ALGORITHM_PATH = "algorithmPath";

	/**
	 * Allows to pass whether the current algorithm has changed.
	 */

	private static final String INTENT_HAS_CHANGES = "hasChanges";

	/**
	 * The current algorithm path.
	 */

	private String algorithmPath = null;

	/**
	 * Whether the current algorithm has changed.
	 */

	private boolean hasChanges = false;

	@Override
	protected final void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
		this.setContentView(R.layout.activity_main);
		this.setSupportActionBar(findViewById(R.id.main_toolbar));

		final OneMoreFabMenu fab = this.findViewById(R.id.main_fab);
		fab.setOptionsClick(this);

		algorithmPath = this.getIntent().getData() == null ? null : this.getIntent().getData().getEncodedPath();
		if(algorithmPath != null) {
			openFromCurrentPath();
		}
		if(savedInstanceState != null && savedInstanceState.containsKey(INTENT_ALGORITHM_PATH)) {
			algorithmPath = savedInstanceState.getString(INTENT_ALGORITHM_PATH);
		}

		if(savedInstanceState != null && savedInstanceState.containsKey(INTENT_HAS_CHANGES)) {
			hasChanges = savedInstanceState.getBoolean(INTENT_HAS_CHANGES);
		}

		final Algorithm algorithm;
		if(savedInstanceState != null && savedInstanceState.containsKey(INTENT_CURRENT_ALGORITHM)) {
			algorithm = (Algorithm)savedInstanceState.getSerializable(INTENT_CURRENT_ALGORITHM);
		}
		else {
			algorithm = new Algorithm(this.getString(R.string.algorithm_default_title), this.getString(R.string.algorithm_default_author));
		}

		final AlgorithmRecyclerView items = getAlgorithmRecyclerView();
		if(savedInstanceState != null && savedInstanceState.containsKey(INTENT_ADAPTER_PATH)) {
			items.setAdapter(new AlgorithmAdapter(this, algorithm, (Integer[])savedInstanceState.getSerializable(INTENT_ADAPTER_PATH)));
			return;
		}

		items.setAdapter(new AlgorithmAdapter(this, algorithm));
	}

	@Override
	public final void onSaveInstanceState(final Bundle outState) {
		final AlgorithmAdapter adapter = getAlgorithmAdapter();
		outState.putSerializable(INTENT_ADAPTER_PATH, adapter.getPath());
		outState.putSerializable(INTENT_CURRENT_ALGORITHM, adapter.getAlgorithm());
		outState.putString(INTENT_ALGORITHM_PATH, algorithmPath);
		outState.putBoolean(INTENT_HAS_CHANGES, hasChanges);

		super.onSaveInstanceState(outState);
	}

	@Override
	public final void onBackPressed() {
		final AlgorithmAdapter adapter = getAlgorithmAdapter();
		if(adapter.getPath().length == 0) {
			super.onBackPressed();
			return;
		}

		adapter.goUp();
	}

	@Override
	public final boolean onCreateOptionsMenu(final Menu menu) {
		this.getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public final boolean onOptionsItemSelected(final MenuItem item) {
		switch(item.getItemId()) {
		case android.R.id.home:
			this.onBackPressed();
			return true;
		case R.id.menu_main_run:
			final Intent consoleIntent = new Intent(this, ConsoleActivity.class);
			consoleIntent.putExtra(INTENT_CURRENT_ALGORITHM, getAlgorithmAdapter().getAlgorithm());
			this.startActivity(consoleIntent);
			return true;
		case R.id.menu_main_new:
			askBeforeAction(() -> getAlgorithmAdapter().setAlgorithm(new Algorithm()));
			return true;
		case R.id.menu_main_open:
			askBeforeAction(() -> openDialog(new OpenFileDialog()));
			return true;
		case R.id.menu_main_save:
			if(algorithmPath != null) {
				saveToCurrentPath();
				return true;
			}
		case R.id.menu_main_saveas:
			openDialog(new SaveFileDialog());
			return true;
		case R.id.menu_main_help:
			Utils.openURL("https://github.com/Skyost/Algogo/wiki/Algogo-Mobile", this);
			return true;
		case R.id.menu_main_about:
			new AlertDialog.Builder(this)
					.setTitle(R.string.app_name)
					.setMessage(R.string.main_dialog_about)
					.setPositiveButton(android.R.string.ok, null)
					.setNeutralButton(R.string.main_dialog_about_skyost, (dialog, selected) -> Utils.openURL("https://www.skyost.eu", this))
					.setNegativeButton(R.string.main_dialog_about_project, (dialog, selected) -> Utils.openURL("https://www.algogo.xyz", this))
					.create()
					.show();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public final void statementEdited(final Statement statement) {
		notifyChanges();

		final AlgorithmAdapter adapter = getAlgorithmAdapter();
		adapter.getCurrentStatement().addStatement(statement);
		adapter.refreshCurrentStatement();
	}

	@Override
	public final void onOptionClick(final Integer optionId) {
		final AlgorithmMobileLineEditor editor = new AlgorithmMobileLineEditor(this, this);
		switch(optionId) {
		case R.id.menu_main_fab_variables:
			createListDialog((dialog, selected) -> {
				switch(selected) {
				case 0:
					showAddDialogIfPossible(CreateVariableStatement.STATEMENT_ID, () -> editor.isCreateVariableStatement(null));
					break;
				case 1:
					showAddDialogIfPossible(AssignStatement.STATEMENT_ID, () -> editor.isAssignStatement(null), true, false);
					break;
				}
			}, R.string.addLineDialog_createVariable, R.string.addLineDialog_assignValueToVariable);
			break;
		case R.id.menu_main_fab_io:
			createListDialog((dialog, selected) -> {
				switch(selected) {
				case 0:
					showAddDialogIfPossible(PromptStatement.STATEMENT_ID, () -> editor.isPromptStatement(null), true, false);
					break;
				case 1:
					showAddDialogIfPossible(PrintVariableStatement.STATEMENT_ID, () -> editor.isPrintVariableStatement(null), true, false);
					break;
				case 2:
					showAddDialogIfPossible(PrintStatement.STATEMENT_ID, () -> editor.isPrintStatement(null));
					break;
				}
			}, R.string.addLineDialog_prompt, R.string.addLineDialog_printVariable, R.string.addLineDialog_print);
			break;
		case R.id.menu_main_fab_blocks:
			createListDialog((dialog, selected) -> {
				switch(selected) {
				case 0:
					showAddDialogIfPossible(IfBlock.STATEMENT_ID, () -> editor.isIfBlock(null));
					break;
				case 1:
					showAddDialogIfPossible(ForLoop.STATEMENT_ID, () -> editor.isForLoop(null), false, true);
					break;
				case 2:
					showAddDialogIfPossible(WhileLoop.STATEMENT_ID, () -> editor.isWhileLoop(null));
					break;
				}
			}, R.string.addLineDialog_ifElse, R.string.addLineDialog_for, R.string.addLineDialog_while);
			break;
		case R.id.menu_main_fab_comments:
			createListDialog((dialog, selected) -> {
				switch(selected) {
				case 0:
					showAddDialogIfPossible(LineComment.STATEMENT_ID, () -> editor.isLineComment(null));
					break;
				case 1:
					showAddDialogIfPossible(BlockComment.STATEMENT_ID, () -> editor.isBlockComment(null));
					break;
				}
			}, R.string.addLineDialog_singleLineComment, R.string.addLineDialog_multiLineComment);
			break;
		}
	}

	@Override
	public final void onFileSelected(final FileDialog dialog, final File file) {
		final String currentPath = algorithmPath;
		algorithmPath = file.getPath();
		final boolean result = dialog instanceof OpenFileDialog ? openFromCurrentPath() : saveToCurrentPath();
		if(result) {
			hasChanges = false;
		}
		else {
			algorithmPath = currentPath;
		}
	}

	/**
	 * Notify to the activity that the current algorithm has changed.
	 */

	public final void notifyChanges() {
		hasChanges = true;
	}

	/**
	 * Returns the algorithm recycler view.
	 *
	 * @return The algorithm recycler view.
	 */

	public final AlgorithmRecyclerView getAlgorithmRecyclerView() {
		return (AlgorithmRecyclerView)this.findViewById(R.id.main_algorithm_items);
	}

	/**
	 * Returns the algorithm adapter.
	 *
	 * @return The algorithm adapter.
	 */

	public final AlgorithmAdapter getAlgorithmAdapter() {
		return (AlgorithmAdapter)getAlgorithmRecyclerView().getAdapter();
	}

	/**
	 * Asks the user to save his changes before executing an action.
	 *
	 * @param action The action.
	 */

	private void askBeforeAction(final Runnable action) {
		if(!hasChanges) {
			action.run();
			return;
		}

		new AlertDialog.Builder(this)
				.setMessage(R.string.main_dialog_save)
				.setPositiveButton(android.R.string.ok, (dialog, selected) -> {
					saveToCurrentPath();
					action.run();
				})
				.setNeutralButton(android.R.string.cancel, null)
				.setNegativeButton(R.string.generic_dialog_no, (dialog, selected) -> action.run())
				.create()
				.show();
	}

	/**
	 * Shows the add statement dialog if possible.
	 *
	 * @param statementId The statement ID.
	 * @param method The add line dialog method.
	 */

	private void showAddDialogIfPossible(final int statementId, final Runnable method) {
		showAddDialogIfPossible(statementId, method, false, false);
	}

	/**
	 * Shows the add statement dialog if possible.
	 *
	 * @param statementId The statement ID.
	 * @param method The add line dialog method.
	 * @param needOneVariable Whether we need to check if there is more than one variable.
	 * @param needOneNumberVariable Whether we need to check if there is more than one number variable.
	 */

	private void showAddDialogIfPossible(final int statementId, final Runnable method, final boolean needOneVariable, final boolean needOneNumberVariable) {
		final AlgorithmAdapter adapter = getAlgorithmAdapter();
		if(needOneNumberVariable && adapter.buildVariablesList(VariableType.NUMBER).length <= 0) {
			Snacky.builder().setActivity(this).setText(R.string.snackbar_error_noNumberVariable).error().show();
			return;
		}

		if(needOneVariable && adapter.buildVariablesList().length <= 0) {
			Snacky.builder().setActivity(this).setText(R.string.snackbar_error_noVariable).error().show();
			return;
		}

		if(!adapter.getCurrentStatement().isValidChild(statementId)) {
			Snacky.builder().setActivity(this).setText(R.string.snackbar_error_wrongLocation).error().show();
			return;
		}

		method.run();
	}

	/**
	 * Opens the file located in the current path.
	 *
	 * @return Whether the operation is a success.
	 */

	private boolean openFromCurrentPath() {
		try {
			this.getAlgorithmAdapter().setAlgorithm(Algorithm.parse(Utils.read(new File(algorithmPath))));
			return true;
		}
		catch(final ParseException ex) {
			Snacky.builder().setActivity(this).setText(this.getString(R.string.snackbar_error_parse, ex.getLine(), ex.getErrorMessage())).error().show();
		}
		catch(final Exception ex) {
			ex.printStackTrace();
			Utils.notifyError(this, ex);
		}

		return false;
	}

	/**
	 * Saves the current algorithm to the current path.
	 *
	 * @return Whether the operation is a success.
	 */

	private boolean saveToCurrentPath() {
		try {
			Utils.write(new File(algorithmPath), getAlgorithmAdapter().getAlgorithm().toLanguage(new AlgogoLanguage()));
			Snacky.builder().setActivity(this).setText(this.getString(R.string.snackbar_message_saved, algorithmPath)).success().show();
			return true;
		}
		catch(final Exception ex) {
			ex.printStackTrace();
			Utils.notifyError(this, ex);
		}

		return false;
	}

	/**
	 * Creates a list dialog containing the specific items.
	 *
	 * @param listener On click listener.
	 * @param items The items.
	 */

	private void createListDialog(final DialogInterface.OnClickListener listener, final int... items) {
		final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item);
		for(final int item : items) {
			adapter.add(this.getString(item));
		}

		new AlertDialog.Builder(this)
				.setAdapter(adapter, listener)
				.setNegativeButton(android.R.string.cancel, null)
		.create().show();
	}

	/**
	 * Opens a file dialog.
	 *
	 * @param dialog The file dialog.
	 */

	private void openDialog(final FileDialog dialog) {
		Permissions.check(
				this,
				new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
				R.string.main_dialog_permissions_message,
				new Permissions.Options()
						.setRationaleDialogTitle(this.getString(R.string.main_dialog_permissions_title)),
				new PermissionHandler() {

					@Override
					public final void onGranted() {
						final Bundle dialogArgs = new Bundle();
						dialogArgs.putString(FileDialog.EXTENSION, ".agg2");
						dialog.setArguments(dialogArgs);
						dialog.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.AppTheme);
						dialog.show(MainActivity.this.getSupportFragmentManager(), dialog.getClass().getName());
					}

				}
		);
	}

}