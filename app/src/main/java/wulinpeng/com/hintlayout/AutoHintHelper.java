package wulinpeng.com.hintlayout;

import android.animation.ValueAnimator;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.v4.view.GravityCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

/**
 * @author wulinpeng
 * @description:
 */
public class AutoHintHelper {

    private View mView;

    private final Rect mHintBounds = new Rect();
    private String mLastHintText = "";
    private String mHintText = "";
    private float mHintTextSize = 15;
    private ColorStateList mHintTextColor;
    private Typeface mTypeFace;
    private int mGravity = Gravity.CENTER_VERTICAL;
    private float mLastDrawX;
    private float mDrawX;
    private float mDrawY;
    private int[] state;
    private Paint mPaint = new Paint();

    private ValueAnimator mAnimator;
    private float mCurrentFraction = 0f;

    IAutoHintDrawer mAutoHintDrawer;

    private boolean mShowHint = true;

    public AutoHintHelper(View mView, IAutoHintDrawer drawer) {
        this.mView = mView;
        this.mAutoHintDrawer = drawer;
        initAnim();
    }

    private void initAnim() {
        mAnimator = new ValueAnimator();
        mAnimator.setDuration(300);
        mAnimator.setFloatValues(0f, 1f);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mCurrentFraction = animation.getAnimatedFraction();
                mView.invalidate();
            }
        });
    }

    public void setHintText(String text, boolean anim) {
        mLastHintText = mHintText;
        mHintText = text;
        if (mAnimator.isRunning()) {
            mAnimator.cancel();
        }
        calculateDrawXY();
        if (anim) {
            mCurrentFraction = 0f;
            mAnimator.start();
        } else {
            mCurrentFraction = 1f;
            mView.invalidate();
        }
    }

    public void setHintTextSize(float mHintTextSize) {
        this.mHintTextSize = mHintTextSize;
        mPaint.setTextSize(mHintTextSize);
        calculateDrawXY();
    }

    public void setHintTextColor(ColorStateList mHintTextColor) {
        this.mHintTextColor = mHintTextColor;
    }

    public void setTypeFace(Typeface mTypeFace) {
        this.mTypeFace = mTypeFace;
        mPaint.setTypeface(mTypeFace);
        calculateDrawXY();
    }

    public void setState(int[] state) {
        this.state = state;
    }

    public void setGravity(int mGravity) {
        this.mGravity = mGravity;
        calculateDrawXY();
        mView.invalidate();
    }

    void setHintBounds(int left, int top, int right, int bottom) {
        Log.d("Debug", "set bounds:" + left + " " + top + " " + right + " " + bottom);
        if (!rectEquals(mHintBounds, left, top, right, bottom)) {
            mHintBounds.set(left, top, right, bottom);
            onBoundsChanged();
        }
    }

    public void showHint(boolean showHint) {
        mShowHint = showHint;
        mView.invalidate();
    }

    private void onBoundsChanged() {
        calculateDrawXY();
        mView.invalidate();
    }

    /**
     * 根据gravity和paint的参数计算lastHint和当前hint的drawX以及drawY
     */
    private void calculateDrawXY() {

        float lastHintLength = mPaint.measureText(mLastHintText, 0, mLastHintText.length());
        float hintLength = mPaint.measureText(mHintText, 0, mHintText.length());

        switch (mGravity & GravityCompat.RELATIVE_HORIZONTAL_GRAVITY_MASK) {
            case Gravity.CENTER_HORIZONTAL:
                mLastDrawX = mHintBounds.centerX() - (lastHintLength / 2);
                mDrawX = mHintBounds.centerX() - (hintLength / 2);
                break;
            case Gravity.RIGHT:
                mLastDrawX = mHintBounds.right - lastHintLength;
                mDrawX = mHintBounds.right - hintLength;
                break;
            case Gravity.LEFT:
            default:
                mLastDrawX = mDrawX = mHintBounds.left;
                break;
        }
        switch (mGravity & Gravity.VERTICAL_GRAVITY_MASK) {
            case Gravity.BOTTOM:
                mDrawY = mHintBounds.bottom;
                break;
            case Gravity.TOP:
                mDrawY = mHintBounds.top - mPaint.ascent();
                break;
            case Gravity.CENTER_VERTICAL:
            default:
                float textHeight = mPaint.descent() - mPaint.ascent();
                float textOffset = (textHeight / 2) - mPaint.descent();
                mDrawY = mHintBounds.centerY() + textOffset;
                break;
        }
    }

    public void draw(Canvas canvas) {
        if (!mShowHint) {
            // draw empty
            return;
        }
        mPaint.setColor(state == null? mHintTextColor.getDefaultColor(): mHintTextColor.getColorForState(state, 0));
        if (mAutoHintDrawer != null) {
            mAutoHintDrawer.draw(mHintBounds, mLastDrawX, mDrawX, mDrawY, mCurrentFraction, mLastHintText, mHintText, canvas, mPaint);
        }
    }

    private static boolean rectEquals(Rect r, int left, int top, int right, int bottom) {
        return !(r.left != left || r.top != top || r.right != right || r.bottom != bottom);
    }

}
