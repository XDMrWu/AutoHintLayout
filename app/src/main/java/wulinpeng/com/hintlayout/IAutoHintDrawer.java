package wulinpeng.com.hintlayout;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * @author wulinpeng
 * @description: hint动画的具体绘制
 */
public interface IAutoHintDrawer {

    void draw(Rect drawBounds, float lastDrawX, float drawX, float drawY, float fraction, String lastHint, String currHint, Canvas canvas, Paint paint);
}
