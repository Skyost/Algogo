package xyz.algogo.mobile.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import xyz.algogo.core.statement.Statement;
import xyz.algogo.core.statement.block.BlockStatement;
import xyz.algogo.core.statement.block.conditional.ElseBlock;
import xyz.algogo.core.statement.block.root.EndBlock;
import xyz.algogo.mobile.AlgorithmMobileLineEditor;
import xyz.algogo.mobile.R;

/**
 * Represents a view hold that holds statement contents.
 */

public class AlgorithmViewHolder extends RecyclerView.ViewHolder {

	/**
	 * Content text view.
	 */

	private final TextView content;

	/**
	 * Arrow text view.
	 */

	private final TextView arrow;

	/**
	 * Creates a new algorithm view holder.
	 *
	 * @param layout The main_algorithm_item.xml layout.
	 */

	AlgorithmViewHolder(final RelativeLayout layout) {
		super(layout);

		this.content = layout.findViewById(R.id.main_algorithm_item_content);
		this.arrow = layout.findViewById(R.id.main_algorithm_item_arrow);
	}

	/**
	 * Binds content to this view holder.
	 *
	 * @param adapter The adapter.
	 * @param position The position.
	 * @param statement The statement.
	 */

	public final void bind(final AlgorithmAdapter adapter, final int position, final Statement statement) {
		itemView.setOnClickListener(null);
		itemView.setOnLongClickListener(null);
		arrow.setVisibility(View.GONE);

		adapter.getAlgorithmLocalizationLanguage().translate(statement, content);

		if(statement instanceof BlockStatement && statement.getStatementId() != EndBlock.STATEMENT_ID) {
			itemView.setOnClickListener(listener -> adapter.expandChild(position));
			arrow.setVisibility(View.VISIBLE);
		}

		itemView.setOnLongClickListener(listener -> openPopupMenu(adapter, adapter.getCurrentStatement(), position));
	}

	/**
	 * Returns the content text view.
	 *
	 * @return The content text view.
	 */

	public final TextView getContent() {
		return content;
	}

	/**
	 * Returns the arrow text view.
	 *
	 * @return The arrow text view.
	 */

	public final TextView getArrow() {
		return arrow;
	}

	/**
	 * Opens a popup menu containing all statement operations.
	 *
	 * @param adapter The adapter.
	 * @param currentStatement The current statement.
	 * @param position The position.
	 *
	 * @return Always true.
	 */

	private boolean openPopupMenu(final AlgorithmAdapter adapter, final BlockStatement currentStatement, final int position) {
		final PopupMenu popup = new PopupMenu(adapter.getActivity(), itemView);
		popup.getMenuInflater().inflate(R.menu.popupmenu_statement, popup.getMenu());
		popup.setOnMenuItemClickListener(item -> {
			int truePosition = adapter.getTruePosition(position);
			if(adapter.getDisplayedStatements()[truePosition].getStatementId() == ElseBlock.STATEMENT_ID) {
				truePosition--;
			}

			switch(item.getItemId()) {
			case R.id.popupmenu_statement_edit:
				Statement.getStatementType(currentStatement.getStatement(truePosition), new AlgorithmMobileLineEditor(adapter, adapter.getActivity()));
				break;
			case R.id.popupmenu_statement_remove:
				currentStatement.removeStatement(truePosition);
				adapter.refreshCurrentStatement();
				break;
			case R.id.popupmenu_statement_up:
				moveStatement(adapter, currentStatement, truePosition, -1);
				break;
			case R.id.popupmenu_statement_down:
				moveStatement(adapter, currentStatement, truePosition, 1);
				break;
			}
			return true;
		});
		popup.show();

		return true;
	}

	/**
	 * Moves a statement.
	 *
	 * @param adapter The adapter.
	 * @param parent The parent.
	 * @param position The position.
	 * @param move Where to move the statement.
	 */

	private void moveStatement(final AlgorithmAdapter adapter, final BlockStatement parent, final int position, final int move) {
		final int newPosition = position + move;
		if(newPosition < 0 || newPosition >= parent.getStatementCount()) {
			return;
		}

		final Statement statement = parent.getStatement(position);
		parent.removeStatement(position);
		parent.insertStatement(statement, position + move);

		if(adapter != null) {
			adapter.refreshCurrentStatement();
		}
	}

}