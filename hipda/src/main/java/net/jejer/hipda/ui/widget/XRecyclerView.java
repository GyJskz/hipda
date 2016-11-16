package net.jejer.hipda.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.os.SystemClock;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

import net.jejer.hipda.ui.OnSingleClickListener;
import net.jejer.hipda.ui.adapter.BaseRvAdapter;
import net.jejer.hipda.utils.Utils;

/**
 * Created by GreenSkinMonster on 2016-11-13.
 */
public class XRecyclerView extends RecyclerView {

    private final static int SCROLL_BACK_HEADER = 0;
    private final static int SCROLL_BACK_FOOTER = 1;

    private final static int SCROLL_DURATION = 400;

    private static int PULL_LOAD_MORE_DELTA;
    private final static float OFFSET_RADIO = 1.8f;

    private float mLastY = -1;
    private boolean mDispatchEvent = true;

    private Scroller mScroller;
    private int mScrollBack;

    private XRecyclerListener mListener;
    private LinearLayoutManager mLayoutManager;
    private BaseRvAdapter mAdapter;

    private XHeaderView mHeaderView;
    private XFooterView mFooterView;

    public XRecyclerView(Context context) {
        super(context);
        initWithContext(context);
    }

    public XRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initWithContext(context);
    }

    public XRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initWithContext(context);
    }

    private void initWithContext(Context context) {
        PULL_LOAD_MORE_DELTA = Utils.dpToPx(context, 40);
        mScroller = new Scroller(context, new DecelerateInterpolator());
        mHeaderView = new XHeaderView(context);
        mFooterView = new XFooterView(context);
        mHeaderView.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (mHeaderView.getState() == XHeaderView.STATE_READY) {
                    loadPrevious();
                }
            }
        });
        mFooterView.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (mFooterView.getState() == XFooterView.STATE_READY) {
                    loadNext();
                } else if (mFooterView.getState() == XFooterView.STATE_END) {
                    atEnd();
                }
            }
        });
    }

    @Override
    public void setLayoutManager(LayoutManager layoutManager) {
        mLayoutManager = (LinearLayoutManager) layoutManager;
        super.setLayoutManager(layoutManager);
    }

    @Override
    public void setAdapter(RecyclerView.Adapter adapter) {
        mAdapter = (BaseRvAdapter) adapter;
        super.setAdapter(mAdapter);
    }

    public void setHeaderState(int state) {
        mHeaderView.setState(state);
        if (state == XHeaderView.STATE_HIDDEN) {
            mAdapter.removeHeaderView();
        } else {
            mAdapter.setHeaderView(mHeaderView);
        }
    }

    public void setFooterState(int state) {
        mFooterView.setState(state);
        if (state == XFooterView.STATE_HIDDEN) {
            mAdapter.removeFooterView();
        } else {
            mAdapter.setFooterView(mFooterView);
        }
    }

    public void setXRecyclerListener(XRecyclerListener listener) {
        mListener = listener;
    }

    private void updateHeaderHeight(float delta) {
        int topMagin = mHeaderView.getTopMargin();

        if (mHeaderView.getTopMargin() > PULL_LOAD_MORE_DELTA) {
            stopScroll();
            mDispatchEvent = false;
            loadPrevious();
            resetHeaderHeight();
        } else {
            mHeaderView.setTopMargin(topMagin + (int) delta);
        }
    }

    private void resetHeaderHeight() {
        int topMargin = mHeaderView.getTopMargin();

        if (topMargin > 0) {
            mScrollBack = SCROLL_BACK_HEADER;
            mScroller.startScroll(0, topMargin, 0, -topMargin, SCROLL_DURATION);
            invalidate();
        }
    }

    private void updateFooterHeight(float delta) {
        int bottomMargin = mFooterView.getBottomMargin();

        if (bottomMargin > PULL_LOAD_MORE_DELTA) {
            stopScroll();
            mDispatchEvent = false;
            loadNext();
            resetFooterHeight();
        } else {
            mFooterView.setBottomMargin(bottomMargin + (int) delta);
        }
    }

    private void resetFooterHeight() {
        int bottomMargin = mFooterView.getBottomMargin();

        if (bottomMargin > 0) {
            mScrollBack = SCROLL_BACK_FOOTER;
            mScroller.startScroll(0, bottomMargin, 0, -bottomMargin, SCROLL_DURATION);
            invalidate();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN)
            mDispatchEvent = true;
        if (mDispatchEvent)
            return super.dispatchTouchEvent(ev);
        else
            return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mLastY == -1) {
            mLastY = ev.getRawY();
        }

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = ev.getRawY();
                break;

            case MotionEvent.ACTION_MOVE:
                final float deltaY = ev.getRawY() - mLastY;
                mLastY = ev.getRawY();

                if (mHeaderView.getState() == XHeaderView.STATE_READY
                        && mLayoutManager.findFirstVisibleItemPosition() == 0
                        && (mHeaderView.getTopMargin() > 0 || deltaY > 0)) {
                    // the first item is showing, header has shown or pull down.
                    updateHeaderHeight(deltaY / OFFSET_RADIO);
                } else if (mFooterView.getState() == XFooterView.STATE_READY
                        && mLayoutManager.findLastVisibleItemPosition() == mAdapter.getItemCount() - 1
                        && (mFooterView.getBottomMargin() > 0 || deltaY < 0)) {
                    // last item, already pulled up or want to pull up.
                    updateFooterHeight(-deltaY / OFFSET_RADIO);
                }
                break;

            default:
                // reset
                mLastY = -1;
                if (mHeaderView.getState() == XHeaderView.STATE_READY
                        && mLayoutManager.findFirstVisibleItemPosition() == 0) {
                    resetHeaderHeight();
                } else if (mFooterView.getState() == XFooterView.STATE_READY
                        && mLayoutManager.findLastVisibleItemPosition() == mAdapter.getItemCount() - 1) {
                    resetFooterHeight();
                }
                break;
        }
        try {
            return super.onTouchEvent(ev);
        } catch (IndexOutOfBoundsException ingored) {
            // avoid random  IndexOutOfBoundsException error
        }
        return true;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        try {
            super.dispatchDraw(canvas);
        } catch (IndexOutOfBoundsException ignored) {
        }
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            if (mScrollBack == SCROLL_BACK_HEADER) {
                mHeaderView.setTopMargin(mScroller.getCurrY());
            } else {
                mFooterView.setBottomMargin(mScroller.getCurrY());
            }
            postInvalidate();
        }
        super.computeScroll();
    }

    private void loadPrevious() {
        if (null != mListener) {
            mListener.onLoadPrevious();
        }
    }

    private void loadNext() {
        if (null != mListener) {
            mListener.onLoadNext();
        }
    }

    private void atEnd() {
        if (null != mListener) {
            mListener.atEnd();
        }
    }

    public void stopScroll() {
        dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_CANCEL, 0, 0, 0));
    }

    public interface XRecyclerListener {
        void onLoadPrevious();

        void onLoadNext();

        void atEnd();
    }
}
