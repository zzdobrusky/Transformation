package edu.utah.cs4962.transformation;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.media.Image;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by zbynek on 9/20/2014.
 */
public class TransformView extends ViewGroup
{
    //View _transformView = null;
    ImageView _transformView = null;

    ArrayList<PointF> _points = new ArrayList<PointF>();
    public TransformView(Context context)
    {
        super(context);
        setBackgroundColor(Color.BLUE);

        _transformView = new ImageView(context);
        _transformView.setImageResource(R.drawable.ic_launcher);
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

        _transformView.setX(x - _transformView.getWidth()/2);
        _transformView.setY(y - _transformView.getHeight()/2);
//        _transformView.setTranslationX(x);
//        _transformView.setTranslationY(y);
//        _transformView.setTranslationX(x - _transformView.getLeft());
//        _transformView.setTranslationY(y - _transformView.getTop());

        //_transformView.setRotation(x);

//        _transformView.setScaleX(x/getWidth() * 4.0f);
//        _transformView.setScaleY(y / getHeight() * 4.0f);

        _points.add(new PointF(x, y));

        if(event.getActionMasked() == MotionEvent.ACTION_UP)
        {
            float[] pointsX = new float[_points.size()];
            float[] pointsY = new float[_points.size()];
            for (int pointIndex=0; pointIndex < _points.size(); pointIndex++)
            {
                pointsX[pointIndex] = _points.get(pointIndex).x - _transformView.getWidth()/2;
                pointsY[pointIndex] = _points.get(pointIndex).y - _transformView.getHeight()/2;
            }
            ObjectAnimator animator = new ObjectAnimator();
            animator.setTarget(_transformView);
            //animator.setPropertyName("x");
            animator.setDuration(4000);
            animator.setValues(
                    PropertyValuesHolder.ofFloat("x", pointsX),
                    PropertyValuesHolder.ofFloat("y", pointsY)
            );
            //animator.setFloatValues(_transformView.getX(), _transformView.getX() + 400.0f);
            animator.setInterpolator(new LinearInterpolator());
            animator.start();

            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
            {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator)
                {
                    invalidate();
                }
            });
        }

        if(event.getActionMasked() == MotionEvent.ACTION_DOWN)
            _points.clear();

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
        canvas.drawText("rotation: " + _transformView.getRotation(), 20, 270, textPaint);
        canvas.drawText("scaleX: " + _transformView.getScaleX(), 20, 300, textPaint);
        canvas.drawText("scaleY: " + _transformView.getScaleY(), 20, 330, textPaint);

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

        if (_points.size() > 0)
        {
            Paint polylinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            polylinePaint.setStyle(Paint.Style.STROKE);
            polylinePaint.setStrokeWidth(2.0f);
            polylinePaint.setColor(Color.RED);
            Path polylinePath = new Path();
            polylinePath.moveTo(_points.get(0).x, _points.get(0).y);
            for (PointF point : _points)
                polylinePath.lineTo(point.x, point.y);

            canvas.drawPath(polylinePath, polylinePaint);
        }
    }
}
