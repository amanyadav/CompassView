package net.androidsrc.darkcompass;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;

/**
 * Created by aman on 8/7/16.
 */
public class CompassView extends View {
    String TAG = CompassView.class.getSimpleName();
    int width = 0, height = 0;
    int midX = 0, midY = 0;
    Dial dial;
    Dynamics dynamics;

    public CompassView(Context context) {
        super(context);
        init(context);
    }

    public CompassView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CompassView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private Runnable animator = new Runnable() {
        @Override
        public void run() {
            boolean needNewFrame = false;
            long now = AnimationUtils.currentAnimationTimeMillis();

            dynamics.update(now);
            if (!dynamics.isAtRest()) {
                needNewFrame = true;
            }

            if (needNewFrame) {
                postDelayed(this, 20);
            }
            invalidate();
        }
    };

    private void init(Context context) {
        dial = new Dial();
        dynamics = new Dynamics(70f, 0.90f);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        midX = width / 2;
        midY = height / 2;
        dial.setDialBoundingRectangle(0, 0, width, height);
        //dial.setRotation1(30);
        Log.d(TAG, "height is " + height / 250.0f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRGB(0, 0, 0);
        dial.draw(canvas);
    }

    public void setAngle(int rotation) {
        dial.setRotation(rotation);
    }

    private class Dial {
        float rotation = 0;
        float canvasRotationForLines = 2.5f;
        RectF dialBoundingRectangle;
        Paint smallLinesPaint, biggerLinePaint, lettersPaint, outerLettersPaint;
        int padding = 150;
        float paddingFactor = 6.78f;
        float centreLineDivisionFactor = 7.0f;
        String letters[] = {"N", "E", "S", "W"};

        public Dial() {
            dialBoundingRectangle = new RectF();
            smallLinesPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            smallLinesPaint.setColor(0xc1ffffff);
            smallLinesPaint.setStyle(Paint.Style.STROKE);

            biggerLinePaint = new Paint(smallLinesPaint);

            lettersPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            lettersPaint.setColor(0xffffffff);

            outerLettersPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            outerLettersPaint.setColor(0xffffffff);
        }

        public void setRotation(int rotation) {
            long now = AnimationUtils.currentAnimationTimeMillis();
            post(animator);
            dynamics.setCurrentAngle(this.rotation, now);
            dynamics.setTargetAngle(rotation, now);
            invalidate();
        }

        public void draw(Canvas canvas) {
            rotation = dynamics.getCurrentAngle();
            //draw + lines in center
            canvas.drawLine(height / 2 - height / centreLineDivisionFactor, height / 2, height / 2 + height / centreLineDivisionFactor, height / 2, smallLinesPaint);
            canvas.drawLine(height / 2, height / 2 - height / centreLineDivisionFactor, height / 2, height / 2 + height / centreLineDivisionFactor, smallLinesPaint);

            //draw small lines
            for (float i = 0; i < 144; i++) {
                canvas.save();
                canvas.rotate((i * canvasRotationForLines) + rotation, height / 2, width / 2);
                if (i % 12 != 0) {
                    canvas.drawLine(height / paddingFactor, height / 2, height / paddingFactor + height / 30.0f, height / 2, smallLinesPaint);
                } else {
                    if (i == 0) {
                        biggerLinePaint.setColor(0xffff0000);
                        canvas.drawLine(height / paddingFactor - height / 18.f, height / 2, height / paddingFactor + height / 32.f, height / 2, biggerLinePaint);
                        biggerLinePaint.setColor(0xc1ffffff);
                    } else {
                        canvas.drawLine(height / paddingFactor - height / 45.f, height / 2, height / paddingFactor + height / 32.f, height / 2, biggerLinePaint);
                    }
                }
                canvas.restore();
            }
            //inner letters
            for (int j = 0; j < 4; j++) {
                canvas.save();
                canvas.rotate((j * 90) + rotation, height / 2, height / 2);
                canvas.rotate((-j * 90) - rotation, height / paddingFactor + height / 10f, height / 2);
                canvas.drawText(letters[j], height / paddingFactor + height / 13.f, (height / 2) + (lettersPaint.getTextSize() / 3), lettersPaint);
                canvas.restore();
            }
            //external letters
            for (int i = 0; i < 12; i++) {
                if (i == 0)
                    continue;
                canvas.save();
                float angle = (i * 30.0f);
                canvas.rotate(angle + rotation, height / 2, height / 2);
                canvas.rotate(-(angle + rotation), height / paddingFactor - height / 14.0f, (height / 2));
                canvas.drawText("" + ((int) angle), height / paddingFactor - height / 11.0f, (height / 2) + outerLettersPaint.getTextSize() / 3, outerLettersPaint);
                canvas.restore();
            }


        }

        public void setDialBoundingRectangle(int... ints) {
            dialBoundingRectangle.set(ints[0] + padding, ints[1] + padding, ints[2] - padding, ints[3] - padding);
            smallLinesPaint.setStrokeWidth(height / 300.0f);
            biggerLinePaint.setStrokeWidth(2.0f * smallLinesPaint.getStrokeWidth());
            lettersPaint.setTextSize(height / 13f);
            lettersPaint.setFakeBoldText(true);
            outerLettersPaint.setTextSize(lettersPaint.getTextSize() / 2.0f);
        }
    }
    ///////////////////////////rarely used/////////////////////////////

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int desiredWidth = 500;
        int desiredHeight = 500;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        //Measure Width
        if (widthMode == MeasureSpec.EXACTLY) {
            //Must be this size
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            width = Math.min(desiredWidth, widthSize);
        } else {
            //Be whatever you want
            width = desiredWidth;
        }

        //Measure Height
        if (heightMode == MeasureSpec.EXACTLY) {
            //Must be this size
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            height = Math.min(desiredHeight, heightSize);
        } else {
            //Be whatever you want
            height = desiredHeight;
        }

        //MUST CALL THIS
        int dimen = Math.min(width, height);
        Log.d(TAG, "setting dimension " + dimen);
        setMeasuredDimension(dimen, dimen);
    }

}
