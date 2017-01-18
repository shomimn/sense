package com.mnm.sense.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.mnm.sense.R;

public class BottomBorderedLinearLayout extends LinearLayout
{
    Paint borderPaint;

    public BottomBorderedLinearLayout(Context context)
    {
        super(context);
        init();
    }

    public BottomBorderedLinearLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public BottomBorderedLinearLayout(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init();
    }

    public BottomBorderedLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init()
    {
        borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(3);
        borderPaint.setColor(getResources().getColor(R.color.colorAccent));

        setWillNotDraw(false);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        float bottom = getBottom() - getTop();
        float right = getRight() - getLeft();

        canvas.drawLine(right * 0.05f, bottom, right * 0.95f, bottom, borderPaint);
    }
}
