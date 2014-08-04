package com.law.fangyuan;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Scroller;

public class SlidingLayout extends LinearLayout {

	public static final int LEFT = 0x001;
	public static final int RIGHT = 0x002;
	public static final int MIDDLE = 0x000;
	private int mCurState = MIDDLE;// 当前显示的view

	public final int MENU_border_left_Width = 120;
	public final int MENU_border_right_Width = 30;

	private Scroller mScroller;
	private LinearLayout leftLayout, rightLayout, childLayout;
	private Context context;
	private boolean fling;
	private boolean mIsBeingDragged = false;
	private int mTouchSlop;
	/**
	 * Position of the last motion event.
	 */
	private float mLastMotionX, mLastMotionY;

	/**
	 * ID of the active pointer. This is used to retain consistency during
	 * drags/flings if multiple pointers are used.
	 */
	private int mActivePointerId = INVALID_POINTER;

	/**
	 * Sentinel value for no current active pointer. Used by
	 * {@link #mActivePointerId}.
	 */
	private static final int INVALID_POINTER = -1;

	private int leftwidth = 0;
	private int rightwidth = 0;
	private int moveWidth = 0;

	public SlidingLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public SlidingLayout(Context context) {
		super(context);
		initView(context);
	}

	public Scroller getScroller() {
		return mScroller;
	}

	public void initView(Context context) {
		this.context = context;
		this.leftwidth = AMenu.dip2px(context, MENU_border_left_Width);
		this.rightwidth = AMenu.dip2px(context, MENU_border_right_Width);
		this.mScroller = new Scroller(context);
//		this.mScroller = new Scroller(context, AnimationUtils.loadInterpolator(
//				context, android.R.anim.overshoot_interpolator));

		final ViewConfiguration configuration = ViewConfiguration.get(context);
		mTouchSlop = configuration.getScaledTouchSlop();
		mCurState = MIDDLE;
	}

	public void addChildView(View child) {
		this.childLayout.addView(child);
	}

	//获取屏幕宽度
	@SuppressWarnings("deprecation")
	private int getViewWidthInPix(Context context) {
		int viewWidthInPix = -1;
		if (viewWidthInPix == -1) {
			WindowManager manager = (WindowManager) context
					.getSystemService(Context.WINDOW_SERVICE);
			viewWidthInPix = manager.getDefaultDisplay().getWidth();
		}
		return viewWidthInPix;
	}
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);

		for (int i = 0; i < getChildCount(); i++) {
			View child = getChildAt(i);
			child.layout(child.getLeft() + moveWidth, child.getTop(),
					child.getRight() + moveWidth, child.getBottom());
		}

	}

	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			scrollTo(mScroller.getCurrX(), 0);
			postInvalidate();
		}
	}
	
	//事件拦截
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		final int action = ev.getAction();
		if ((action == MotionEvent.ACTION_MOVE) && (mIsBeingDragged)) {
			return true;// 拦截不传递给child view
		}

		switch (action & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN: {
			final float x = ev.getX();
			final float y = ev.getY();
			if (!inChild((int) x, (int) y)) {
				mIsBeingDragged = false;
				break;
				// 超出边界,return false传递给子view处理
			}

			/*
			 * Remember location of down touch. ACTION_DOWN always refers to
			 * pointer index 0.
			 */
			mLastMotionX = x;
			mLastMotionY = y;
			mActivePointerId = ev.getPointerId(0);

			/*
			 * If being flinged and user touches the screen, initiate drag;
			 * otherwise don't. mScroller.isFinished should be false when being
			 * flinged.
			 */
			mIsBeingDragged = !mScroller.isFinished();
			break;
		}
		case MotionEvent.ACTION_MOVE: {
			/*
			 * mIsBeingDragged == false, otherwise the shortcut would have
			 * caught it. Check whether the user has moved far enough from his
			 * original down touch.
			 */

			/*
			 * Locally do absolute value. mLastMotionY is set to the y value of
			 * the down event.
			 */
			final int activePointerId = mActivePointerId;
			if (activePointerId == INVALID_POINTER) {
				// If we don't have a valid id, the touch down wasn't on
				// content.
				break;
			}

			final int pointerIndex = ev.findPointerIndex(activePointerId);
			final float x = ev.getX(pointerIndex);
			final float y = ev.getY(pointerIndex);
			final int xDiff = (int) Math.abs(x - mLastMotionX);
			final int yDiff = (int) Math.abs(y - mLastMotionY);
			if (xDiff > mTouchSlop && yDiff < xDiff) {
				mIsBeingDragged = true;
			}
			break;
		}
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			mIsBeingDragged = false;
			mActivePointerId = INVALID_POINTER;
			scrollToScreen();
			break;
		}
		return mIsBeingDragged;
	}

	/*
	 * 事件处理
	 * ACTION_DOWN和ACTION_UP就是单点触摸屏幕，按下去和放开的操作；
	 * ACTION_POINTER_DOWN和ACTION_POINTER_UP就是多点触摸屏幕
	 * ，当有一只手指按下去的时候，另一只手指按下和放开的动作捕捉； ACTION_MOVE就是手指在屏幕上移动的操作
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN
				&& !inChild((int) event.getX(), (int) event.getY())) {
			// Don't handle edge touches immediately -- they may actually belong
			// to one of our
			// descendants.
			return false;
		}

		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			return true; // 本VIEW消化掉

		case MotionEvent.ACTION_MOVE:
			final int activePointerIndex = event
					.findPointerIndex(mActivePointerId);

			final float x = event.getX(activePointerIndex);
			final float y = event.getY(activePointerIndex);

			final int distanceX = (int) /* Math.abs */-(x - mLastMotionX);

			// 在滑动过程中，就需要显示新的brotherView，不然显示的还是之前的brotherView，最后松开手时会突然变称新brotherView,影响体验
			if (distanceX < 0 && getScrollX() < 0 && leftLayout != null) {
				setBrotherVisibility(LEFT);
			} else if (distanceX > 0 && getScrollX() > 0 && rightLayout != null) {
				//setBrotherVisibility(RIGHT);
				return false;
			} else {
				setBrotherVisibility(MIDDLE);
			}

			scrollBy((int) distanceX, 0);

			mLastMotionX = x;
			mLastMotionY = y;
			break;

		case MotionEvent.ACTION_UP:
			mIsBeingDragged = false;
			mActivePointerId = INVALID_POINTER;
			scrollToScreen();
			break;

		default:
			return super.onTouchEvent(event);
		}
		return mIsBeingDragged;

	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		// TODO Auto-generated method stub
		super.onScrollChanged(l, t, oldl, oldt);
	}

	private void scrollToScreen() {

		int scrollDistance = 0;
		if (Math.abs(getScrollX()) > getWidth() / 2){
			scrollDistance = (getScrollX() > 0) ? getWidth() - rightwidth
					- getScrollX() : -(getWidth() - leftwidth - Math
					.abs(getScrollX()));
		}else{
			scrollDistance = -getScrollX();
		}			

		int distance = scrollDistance + getScrollX();
		if (distance > 0) return;
		if (distance > 0) {
			mCurState = RIGHT;
		} else if (distance < 0) {
			mCurState = LEFT;
		} else {
			mCurState = MIDDLE;
		}
		mScroller.startScroll(getScrollX(), 0, scrollDistance, 0,
				Math.abs(scrollDistance) * 2);
		invalidate();

	}

	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		if (Math.abs(velocityX) > ViewConfiguration.get(context)
				.getScaledMinimumFlingVelocity()) {
			fling = true;
			scrollToScreen();
		}

		return fling;
	}

	@SuppressWarnings("unused")
	private boolean inChild(int x, int y) {
		if (getChildCount() > 0) {
			final int scrollX = mScroller.getCurrX();
			final View child = getChildAt(0);

			return !(scrollX + x < 0 || scrollX + x > getWidth() || y < 0 || y > getHeight());

		}
		return false;
	}

	/**
	 * 璁剧疆褰撳墠鏄剧ず鐨剉iew
	 * 
	 * @param whichpg
	 */
	public void setPage(int whichpg) {
		int targetX = 0, moveDistance = 0;
		if (whichpg == LEFT) {
			targetX = -(getViewWidthInPix(context) - leftwidth);
			mCurState = LEFT;
		} else if (whichpg == RIGHT) {
			targetX = getViewWidthInPix(context) - rightwidth;
			mCurState = RIGHT;
		} else {
			mCurState = MIDDLE;
		}
		setBrotherVisibility(whichpg);
		moveDistance = targetX - getScrollX();
		mScroller.startScroll(getScrollX(), 0, moveDistance, 0,
				Math.abs(moveDistance) * 2);
		invalidate();
	}

	/**
	 * 杩斿洖褰撳墠鏄剧ず鐨剉iew
	 * 
	 * @return
	 */
	public int getPage() {
		return mCurState;
	}

	/**
	 * 璁剧疆BrotherView
	 * 
	 * @param left
	 * @param right
	 */
	public void setBrotherLayout(LinearLayout left, LinearLayout right) {
		this.leftLayout = left;
		this.rightLayout = right;
	}

	/**
	 * 鏍规嵁褰撳墠鐘舵�鏄剧ず鎴栭殣钘弙iew
	 * 
	 * @param state
	 */
	private void setBrotherVisibility(int state) {
		switch (state) {
		case LEFT:
			rightLayout.setVisibility(View.GONE);
			leftLayout.setVisibility(View.VISIBLE);
			break;
		case RIGHT:
			rightLayout.setVisibility(View.VISIBLE);
			leftLayout.setVisibility(View.GONE);
			break;
		case MIDDLE:
			break;
		default:
			break;
		}
	}
}
