package wulinpeng.com.hintlayout;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * @author wulinpeng
 * @description: 提供EditText切换hint的动画效果，本布局需要一个EditTExt作为child，但是注意不要设置EditText的hint
 */
public class AutoHintLayout extends LinearLayout {

    private AutoHintHelper mAutoHintHelper;
    private EditText mEditText;
    private boolean mInDrawableStateChanged;

    public AutoHintLayout(Context context) {
        this(context, null);
    }

    public AutoHintLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
        mAutoHintHelper = new AutoHintHelper(this, new DefaultAutoHintDrawer());
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        if (child instanceof EditText) {
            setEditText((EditText) child);
        }
    }

    private void setEditText(EditText editText) {
        this.mEditText = editText;
        mAutoHintHelper.setHintTextColor(mEditText.getHintTextColors());
        mAutoHintHelper.setHintTextSize(mEditText.getTextSize());
        mAutoHintHelper.setTypeFace(mEditText.getTypeface());
        mAutoHintHelper.setGravity(mEditText.getGravity());

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // 输入字符变化的时候判断是否需要显示hint
                if (TextUtils.isEmpty(mEditText.getText().toString())) {
                    mAutoHintHelper.showHint(true);
                } else {
                    mAutoHintHelper.showHint(false);
                }
            }
        });
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        mAutoHintHelper.draw(canvas);
    }

    public void setHint(String text, boolean anim) {
        mAutoHintHelper.setHintText(text, anim);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (mEditText != null) {
            final Rect rect = new Rect();
            // 获取EditText在本View中的位置
            setChildRect(mEditText, rect);

            l = rect.left + mEditText.getCompoundPaddingLeft();
            r = rect.right - mEditText.getCompoundPaddingRight();

            // 提供AutoHintHelper hint的绘制区域
            mAutoHintHelper.setHintBounds(
                    l, rect.top + mEditText.getCompoundPaddingTop(),
                    r, rect.bottom - mEditText.getCompoundPaddingBottom());
        }
    }

    void setChildRect(View child, Rect out) {
        out.set(0, 0, child.getWidth(), child.getHeight());
        // 添加child在本布局中的offset到rect
        offsetDescendantRectToMyCoords(child, out);
    }

    @Override
    protected void drawableStateChanged() {

        if (mInDrawableStateChanged) {
            // Some of the calls below will update the drawable state of child views. Since we're
            // using addStatesFromChildren we can get into infinite recursion, hence we'll just
            // exit in this instance
            return;
        }

        mInDrawableStateChanged = true;
        super.drawableStateChanged();

        mAutoHintHelper.setState(getDrawableState());
        mInDrawableStateChanged = false;
    }

}
