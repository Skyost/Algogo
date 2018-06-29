package xyz.algogo.mobile.adapter;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import xyz.algogo.core.Algorithm;
import xyz.algogo.core.evaluator.VariableType;
import xyz.algogo.core.statement.Statement;
import xyz.algogo.core.statement.block.BlockStatement;
import xyz.algogo.core.statement.block.conditional.ElseBlock;
import xyz.algogo.core.statement.block.conditional.IfBlock;
import xyz.algogo.core.statement.simple.variable.CreateVariableStatement;
import xyz.algogo.mobile.AlgorithmMobileLineEditor;
import xyz.algogo.mobile.R;
import xyz.algogo.mobile.activity.MainActivity;
import xyz.algogo.mobile.language.AlgorithmLocalization;
import xyz.algogo.mobile.view.AlgorithmRecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Represents a recycler view adapter that allows to display and control algorithms.
 */

public class AlgorithmAdapter extends RecyclerView.Adapter<AlgorithmViewHolder> implements AlgorithmMobileLineEditor.Listener {

	/**
	 * The activity this adapter belongs to.
	 */

	private final MainActivity activity;

	/**
	 * The algorithm.
	 */

	private Algorithm algorithm;

	/**
	 * The current path.
	 */

	private final Stack<Integer> path = new Stack<>();

	/**
	 * The current parent statement.
	 */

	private BlockStatement currentStatement;

	/**
	 * All currently displayed statements.
	 */

	private List<Statement> displayedStatements = new ArrayList<>();

	/**
	 * Creates a new algorithm adapter.
	 *
	 * @param activity The activity.
	 */

	public AlgorithmAdapter(final MainActivity activity) {
		this(activity, new Algorithm());
	}

	/**
	 * Creates a new algorithm adapter.
	 *
	 * @param activity The activity.
	 * @param algorithm The algorithm.
	 */

	public AlgorithmAdapter(final MainActivity activity, final Algorithm algorithm) {
		this(activity, algorithm, new Integer[0]);
	}

	/**
	 * Creates a new algorithm adapter.
	 *
	 * @param activity The activity.
	 * @param algorithm The algorithm.
	 * @param path The path.
	 */

	public AlgorithmAdapter(final MainActivity activity, final Algorithm algorithm, final Integer[] path) {
		this.activity = activity;
		for(int index : path) {
			this.path.push(index);
		}
		this.algorithm = algorithm;

		refreshCurrentStatement();
	}

	@NonNull
	@Override
	public final AlgorithmViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
		final RelativeLayout item = (RelativeLayout)LayoutInflater.from(parent.getContext()).inflate(R.layout.main_algorithm_item, parent, false);
		return new AlgorithmViewHolder(item);
	}

	@Override
	public final void onBindViewHolder(@NonNull final AlgorithmViewHolder holder, final int position) {
		final Statement statement = displayedStatements.get(position);
		holder.bind(this, position, statement);
	}

	@Override
	public final int getItemCount() {
		return displayedStatements.size();
	}

	/**
	 * Returns the activity.
	 *
	 * @return The activity.
	 */

	public final MainActivity getActivity() {
		return activity;
	}

	/**
	 * Returns the algorithm.
	 *
	 * @return The algorithm.
	 */

	public final Algorithm getAlgorithm() {
		return algorithm;
	}

	/**
	 * Sets the algorithm.
	 *
	 * @param algorithm The algorithm.
	 */

	public final void setAlgorithm(final Algorithm algorithm) {
		if(this.algorithm.equals(algorithm)) {
			return;
		}

		this.algorithm = algorithm;
		path.clear();
		refreshCurrentStatement();
	}

	/**
	 * Returns the current statement.
	 *
	 * @return The current statement.
	 */

	public final BlockStatement getCurrentStatement() {
		return currentStatement;
	}

	/**
	 * Returns all displayed statements.
	 *
	 * @return All displayed statements.
	 */

	public final Statement[] getDisplayedStatements() {
		return displayedStatements.toArray(new Statement[displayedStatements.size()]);
	}

	/**
	 * Display the statement located a level above the current one.
	 */

	public final void goUp() {
		if(path.empty()) {
			return;
		}

		path.pop();
		refreshCurrentStatement();
		activity.getAlgorithmRecyclerView().enterAnimation();
	}

	/**
	 * Expands a child.
	 *
	 * @param childIndex The child index (can be a "fake" index).
	 */

	public final void expandChild(final int childIndex) {
		final AlgorithmRecyclerView items = activity.getAlgorithmRecyclerView();
		items.exitAnimation(() -> {
			path.push(childIndex);
			refreshCurrentStatement();
			items.enterAnimation();
		});
	}

	/**
	 * Refreshes the current statement.
	 */

	public final void refreshCurrentStatement() {
		currentStatement = algorithm.getRootBlock();
		for(int index : path) {
			currentStatement = (BlockStatement)buildDisplayedStatements(currentStatement).get(index);
		}
		displayedStatements = buildDisplayedStatements(currentStatement);

		this.notifyDataSetChanged();
		refreshTitle();
	}

	/**
	 * Returns the current path.
	 *
	 * @return The current path.
	 */

	public final Integer[] getPath() {
		return path.toArray(new Integer[path.size()]);
	}

	/**
	 * Returns the "unfaked" position (the position minus all ELSE blocks above).
	 *
	 * @param position The position.
	 *
	 * @return The "unfaked" position.
	 */

	public final int getTruePosition(final int position) {
		int truePosition = position;
		for(int i = 0; i < position; i++) {
			if(displayedStatements.get(i).getStatementId() != ElseBlock.STATEMENT_ID) {
				continue;
			}

			truePosition--;
		}

		return truePosition;
	}

	/**
	 * Builds an array containing all variables identifier.
	 *
	 * @return An array containing all variables identifier.
	 */

	public final String[] buildVariablesList() {
		return buildVariablesList(null);
	}

	/**
	 * Builds an array containing all variables identifier.
	 *
	 * @type Variables type.
	 *
	 * @return An array containing all variables identifier.
	 */

	public final String[] buildVariablesList(final VariableType type) {
		final List<String> variables = new ArrayList<>();
		for(final Statement statement : algorithm.getVariablesBlock().listStatementsById(CreateVariableStatement.STATEMENT_ID)) {
			final CreateVariableStatement createVariableStatement = (CreateVariableStatement)statement;
			if(type != null && createVariableStatement.getType() != type) {
				continue;
			}
			variables.add(createVariableStatement.getIdentifier());
		}

		return variables.toArray(new String[variables.size()]);
	}

	@Override
	public final void statementEdited(final Statement statement) {
		activity.notifyChanges();
		refreshCurrentStatement();
	}

	/**
	 * Refreshes the current activity title.
	 */

	private void refreshTitle() {
		final ActionBar actionBar = activity.getSupportActionBar();
		if(actionBar == null) {
			return;
		}

		final View layout = activity.getLayoutInflater().inflate(R.layout.main_actionbar, null);
		final TextView title = layout.findViewById(R.id.main_actionbar_title);
		currentStatement.toLanguage(new AlgorithmLocalization(this, title));

		title.setTextColor(ContextCompat.getColor(activity, android.R.color.white));
		if(path.isEmpty()) {
			title.setOnClickListener(view -> showCreditsDialog());
		}

		actionBar.setCustomView(layout);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayShowTitleEnabled(false);

		final boolean notEmpty = !path.isEmpty();
		actionBar.setDisplayHomeAsUpEnabled(notEmpty);
		actionBar.setDisplayShowHomeEnabled(notEmpty);
	}

	/**
	 * Shows the credits dialog.
	 */

	private void showCreditsDialog() {
		final View layout = activity.getLayoutInflater().inflate(R.layout.main_dialog_credits, null);
		((EditText)layout.findViewById(R.id.main_dialog_credits_title)).setText(algorithm.getTitle());
		((EditText)layout.findViewById(R.id.main_dialog_credits_author)).setText(algorithm.getAuthor());

		new AlertDialog.Builder(activity)
				.setView(layout)
				.setPositiveButton(android.R.string.ok, (dialog, selected) -> {
					activity.notifyChanges();
					algorithm.setTitle(((EditText)layout.findViewById(R.id.main_dialog_credits_title)).getText().toString());
					algorithm.setAuthor(((EditText)layout.findViewById(R.id.main_dialog_credits_author)).getText().toString());
					refreshTitle();
				})
				.setNegativeButton(android.R.string.cancel, null)
				.create()
				.show();
	}

	/**
	 * Builds a list statements to display.
	 *
	 * @param parent The parent.
	 *
	 * @return A list statements to display.
	 */

	private List<Statement> buildDisplayedStatements(final BlockStatement parent) {
		final List<Statement> displayedStatements = new ArrayList<>();
		for(final Statement statement : parent.listStatements()) {
			displayedStatements.add(statement);

			if(statement.getStatementId() == IfBlock.STATEMENT_ID && ((IfBlock)statement).hasElseBlock()) {
				displayedStatements.add(((IfBlock)statement).getElseBlock());
			}
		}

		return displayedStatements;
	}

}