package edu.utah.cs4962.transformation;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by zbynek on 9/20/2014.
 */
public class TransformView extends ViewGroup
{
    View _transformView = null;
    public TransformView(Context context)
    {
        super(context);
        setBackgroundColor(Color.BLUE);

        _transformView = new View(context);
        _transformView.setBackgroundColor(Color.GREEN);
        _transformView.setLayoutParams(new LayoutParams(100, 100));
        addView(_transformView);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean b, int i, int i2, int i3, int i4)
    {
        for(int childIndex=0; childIndex<getChildCount(); childIndex++)
        {
            View child = getChildAt(childIndex);
            //child.layout(200, 200, 300, 300);
            child.layout(
                    getWidth()/2 - child.getMeasuredWidth()/2,
                    getHeight()/2 - child.getMeasuredHeight()/2,
                    getWidth()/2 + child.getMeasuredWidth()/2,
                    getHeight()/2 + child.getMeasuredHeight()/2
            );
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        float x = event.getX();
        float y = event.getY();

        //_transformView.setX(x);
        //_transformView.setY(y);
        _transformView.setTranslationX(x);
        _transformView.setTranslationY(y);
//        _transformView.setTranslationX(x - _transformView.getLeft());
//        _transformView.setTranslationY(y - _transformView.getTop());

        invalidate();

        return true;
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.YELLOW);
        textPaint.setTextSize(28.0f);
        textPaint.setFakeBoldText(true);
        canvas.drawText("left: " + _transformView.getLeft(), 20, 30, textPaint);
        canvas.drawText("top: " + _transformView.getTop(), 20, 60, textPaint);
        canvas.drawText("right: " + _transformView.getRight(), 20, 90, textPaint);
        canvas.drawText("bottom: " + _transformView.getBottom(), 20, 120, textPaint);
        canvas.drawText("x: " + _transformView.getX(), 20, 150, textPaint);
        canvas.drawText("y: " + _transformView.getY(), 20, 180, textPaint);
        canvas.drawText("translationX: " + _transformView.getTranslationX(), 20, 210, textPaint);
        canvas.drawText("translationY: " + _transformView.getTranslationY(), 20, 240, textPaint);

        Paint layoutPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        layoutPaint.setColor(Color.CYAN);
        layoutPaint.setStrokeWidth(2.0f);
        layoutPaint.setStyle(Paint.Style.STROKE);
        layoutPaint.setPathEffect(new DashPathEffect(new float[] {4.0f, 8.0f, 10.0f, 12.0f}, 0.0f));
        Path layoutPath = new Path();
        layoutPath.moveTo(
                _transformView.getLeft(),
                _transformView.getTop()
        );
        layoutPath.lineTo(
                _transformView.getRight(),
                _transformView.getTop()
        );
        layoutPath.lineTo(
                _transformView.getRight(),
                _transformView.getBottom()
        );
        layoutPath.lineTo(
                _transformView.getLeft(),
                _transformView.getBottom()
        );
        layoutPath.close();
        canvas.drawPath(layoutPath, layoutPaint);

    }
}
