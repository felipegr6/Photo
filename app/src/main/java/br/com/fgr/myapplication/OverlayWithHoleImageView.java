package br.com.fgr.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

public class OverlayWithHoleImageView extends View {
  private Paint mTransparentPaint;
  private Paint mSemiBlackPaint;
  private Path mPath = new Path();

  public OverlayWithHoleImageView(Context context) {
    super(context);
    initPaints();
  }

  public OverlayWithHoleImageView(Context context, AttributeSet attrs) {
    super(context, attrs);
    initPaints();
  }

  public OverlayWithHoleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initPaints();
  }

  private void initPaints() {
    mTransparentPaint = new Paint();
    mTransparentPaint.setColor(Color.TRANSPARENT);
    mTransparentPaint.setStrokeWidth(10);

    mSemiBlackPaint = new Paint();
    mSemiBlackPaint.setColor(Color.TRANSPARENT);
    mSemiBlackPaint.setStrokeWidth(10);
  }

  @Override protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    int radius = 170;
    mPath.reset();

    mPath.addCircle(canvas.getWidth() / 4, canvas.getHeight() / 4, radius, Path.Direction.CW);
    mPath.addCircle(3 * canvas.getWidth() / 4, canvas.getHeight() / 4, radius,
        Path.Direction.CW
    );
    mPath.setFillType(Path.FillType.INVERSE_EVEN_ODD);

    canvas.drawCircle(canvas.getWidth() / 4, canvas.getHeight() / 4, radius, mTransparentPaint);

    canvas.drawPath(mPath, mSemiBlackPaint);
    canvas.clipPath(mPath);
    canvas.drawColor(Color.parseColor("#A6000000"));
  }
}
