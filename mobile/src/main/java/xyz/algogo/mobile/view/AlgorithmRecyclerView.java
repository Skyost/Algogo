package xyz.algogo.mobile.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Represents an algorithm recycler view.
 */

public class AlgorithmRecyclerView extends RecyclerView {

	/**
	 * Creates a new algorithm recycler view.
	 *
	 * @param context The context.
	 */

	public AlgorithmRecyclerView(final Context context) {
		this(context, null);
	}

	/**
	 * Creates a new algorithm recycler view.
	 *
	 * @param context The context.
	 * @param attributeSet Attributes.
	 */

	public AlgorithmRecyclerView(final Context context, @Nullable final AttributeSet attributeSet) {
		this(context, attributeSet, 0);
	}

	/**
	 * Creates a new algorithm recycler view.
	 *
	 * @param context The context.
	 * @param attributeSet Attributes.
	 * @param defaultStyle The default style.
	 */

	public AlgorithmRecyclerView(final Context context, @Nullable final AttributeSet attributeSet, final int defaultStyle) {
		super(context, attributeSet, defaultStyle);

		this.setLayoutManager(new LinearLayoutManager(context));
	}

	/**
	 * Runs an enter animation.
	 */

	public final void enterAnimation() {
		enterAnimation(null);
	}

	/**
	 * Runs an enter animation.
	 *
	 * @param animationEnd Method to run whenever the animation is finished.
	 */

	public final void enterAnimation(final Runnable animationEnd) {
		animate(animationEnd, android.R.anim.slide_in_left);
	}

	/**
	 * Runs an exit animation.
	 */

	public final void exitAnimation() {
		exitAnimation(null);
	}

	/**
	 * Runs an exit animation.
	 *
	 * @param animationEnd Method to run whenever the animation is finished.
	 */

	public final void exitAnimation(final Runnable animationEnd) {
		animate(animationEnd, android.R.anim.slide_out_right);
	}

	/**
	 * Runs an animation.
	 *
	 * @param animationEnd Method to run whenever the animation is finished.
	 * @param animationId The animation ID.
	 */

	private void animate(final Runnable animationEnd, final int animationId) {
		final Animation animation = AnimationUtils.loadAnimation(this.getContext(), animationId);
		animation.setDuration(200L);

		if(animationEnd != null) {
			animation.setAnimationListener(new Animation.AnimationListener() {

				@Override
				public final void onAnimationEnd(final Animation animation) {
					animationEnd.run();
				}

				@Override
				public final void onAnimationStart(final Animation animation) {}

				@Override
				public final void onAnimationRepeat(final Animation animation) {}

			});
		}

		this.startAnimation(animation);
	}

}
