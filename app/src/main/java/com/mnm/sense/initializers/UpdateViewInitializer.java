package com.mnm.sense.initializers;

import android.content.Context;
import android.view.View;

import com.mnm.sense.Repository;
import com.mnm.sense.models.UpdateViewModel;
import com.mnm.sense.views.UpdateView;

public class UpdateViewInitializer extends ViewInitializer<UpdateView, UpdateViewModel>
{
    public UpdateViewInitializer()
    {
        super(UpdateView.class, UpdateViewModel.class);
    }

    @Override
    public void init(Context context, final UpdateView view, UpdateViewModel model)
    {
        view.viewTitle.setText("Update Interval");
        view.intervalSlider.setMax(12 * 60 * 60);
        try
        {
            view.intervalSlider.setProgress(Repository.instance().getUpdateInterval());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        view.cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int interval = 10 * 60000;

                try
                {
                    interval = Repository.instance().getUpdateInterval();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                view.animateSlider(interval);

                view.hideButtons();
            }
        });

        view.confirm.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    Repository.instance().setUpdateInterval(view.intervalSlider.getProgress() * 1000);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                view.hideButtons();
            }
        });
    }
}
