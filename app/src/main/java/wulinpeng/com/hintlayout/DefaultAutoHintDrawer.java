package wulinpeng.com.hintlayout;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * @author wulinpeng
 * @description:
 */
public class DefaultAutoHintDrawer implements IAutoHintDrawer {

    @Override
    public void draw(Rect drawBounds, float lastDrawX, float drawX, float drawY,
                       float fraction, String lastHint, String currHint, Canvas canvas, Paint paint) {
        float boundsHeight = drawBounds.bottom - drawBounds.top;
        float offsetY = boundsHeight * fraction;

        int alpha = paint.getAlpha();

        paint.setAlpha((int) (alpha * (1f - fraction)));
        // draw last hint with curr fraction
        canvas.drawText(lastHint, 0, lastHint.length(), lastDrawX, drawY - offsetY, paint);

        // reset alpha
        paint.setAlpha((int) (alpha * fraction));
        // draw curr hint with curr fraction
        canvas.drawText(currHint, 0, currHint.length(), drawX, boundsHeight + drawY - offsetY, paint);

        // reset alpha
        paint.setAlpha(alpha);
    }
}
