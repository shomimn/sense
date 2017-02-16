package com.mnm.sense.views;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.mnm.sense.R;


public class UpdateView extends LinearLayout
{
    public TextView viewTitle;
    public SeekBar intervalSlider;
    public TextView intervalValue;
    public Button cancel;
    public Button confirm;

    public UpdateView(Context context)
    {
        super(context);
        init();
    }

    public UpdateView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public UpdateView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init();
    }

    public UpdateView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void init()
    {
        inflate(getContext(), R.layout.update_card, this);

        viewTitle = (TextView) findViewById(R.id.title_textview);
        intervalSlider = (SeekBar) findViewById(R.id.interval_slider);
        intervalValue = (TextView) findViewById(R.id.interval_text);
        cancel = (Button) findViewById(R.id.cancel_change);
        confirm = (Button) findViewById(R.id.confirm_change);

        viewTitle.setTypeface(null, Typeface.BOLD);

        hideButtons();

        intervalSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b)
            {
                displayProgress(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {
                showButtons();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {
            }
        });
    }

    public void hideButtons()
    {
        cancel.setVisibility(View.INVISIBLE);
        confirm.setVisibility(View.INVISIBLE);
    }

    public void showButtons()
    {
        cancel.setVisibility(View.VISIBLE);
        confirm.setVisibility(View.VISIBLE);
    }

    public void animateSlider(int value)
    {
        ObjectAnimator anim = ObjectAnimator.ofInt(intervalSlider, "progress", value);
        anim.setDuration(500);
        anim.setInterpolator(new DecelerateInterpolator());
        anim.start();
    }

    public void displayProgress(int progress)
    {
        if (progress < 60)
        {
            intervalSlider.setProgress(60);
            return;
        }

        int mins = progress / 60;
        int hours = mins / 60;
        mins %= 60;

        intervalValue.setText(String.format("%d h : %d m", hours, mins));
    }
}
