package com.mnm.sense;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayout;
import android.view.LayoutInflater;
import android.view.View;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

public class DynamicGrid
{
    GridLayout layout;
    ArrayList<GridItem> items = new ArrayList<>();

    public DynamicGrid(GridLayout grid)
    {
        layout = grid;
    }

    public void addItem(GridItem item)
    {
        items.add(item);
    }

    public void layoutItems(LayoutInflater inflater)
    {
        int row = 0;
        int col = 0;
        for (GridItem item : items)
        {
            CardView card = (CardView) inflater.inflate(R.layout.card_item, null);
            ViewInitializer initializer = Initializer.get(item.data.getClass());

            View view = (View) initializer.construct(layout.getContext(), item.data);

            if (view == null)
                continue;

            card.addView(view);

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.height = item.rowSpan * Util.dp(150);
            int margin = Util.dp(5);
            params.setMargins(margin, margin, margin, margin);
            GridLayout.Spec rowSpec = GridLayout.spec(GridLayout.UNDEFINED, item.rowSpan, 1);
            GridLayout.Spec columnSpec = GridLayout.spec(GridLayout.UNDEFINED, item.columnSpan, 1);
            params.rowSpec = rowSpec;
            params.columnSpec = columnSpec;

            layout.addView(card, params);

            col = (col + item.columnSpan) % layout.getColumnCount();
            row = col == 0 ? row + item.rowSpan : row;
        }
    }
}
