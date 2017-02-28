package com.mnm.sense.initializers;

import android.content.Context;
import android.widget.Button;

import com.mnm.sense.R;
import com.mnm.sense.Util;
import com.mnm.sense.models.ButtonModel;
import com.mnm.sense.views.BorderlessButton;

public class ButtonInitializer extends ViewInitializer<BorderlessButton, ButtonModel>
{
    public ButtonInitializer()
    {
        super(BorderlessButton.class, ButtonModel.class);
    }

    @Override
    public void init(final Context context, final BorderlessButton view, final ButtonModel model)
    {
        view.buttonInstance.setText(model.text);
        view.buttonInstance.setOnClickListener(model.listener);
    }
}
