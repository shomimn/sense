package com.mnm.sense;


import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayout;
import android.view.LayoutInflater;
import android.view.View;

import com.mnm.sense.initializers.Initializer;
import com.mnm.sense.initializers.ViewInitializer;

import java.util.ArrayList;
import java.util.HashMap;

public class DynamicGrid
{

    GridLayout layout;
    public ArrayList<GridItem> items = new ArrayList<>();
    HashMap<GridItem, View> displayedItems = new HashMap<>();
    public int rowHeight = Util.dp(150);
    private int topMargin = Util.dp(5);
    private int bottomMargin = Util.dp(5);
    private int leftMargin = Util.dp(5);
    private int rightMargin = Util.dp(5);

    public DynamicGrid(GridLayout grid)
    {
        layout = grid;
    }

    public void addItem(GridItem item)
    {
        items.add(item);
    }

    public void setMargins(int left, int top, int right, int bottom)
    {
        leftMargin = left;
        topMargin = top;
        rightMargin = right;
        bottomMargin = bottom;
    }

    public void layoutItems(LayoutInflater inflater)
    {
        int row = 0;
        int col = 0;
        for (GridItem item : items)
        {
            if (displayedItems.containsKey(item))
                continue;

            CardView card = (CardView) inflater.inflate(R.layout.card_item, null);
            ViewInitializer initializer = Initializer.get(item.data.getClass());

            View view = (View) initializer.construct(layout.getContext(), item.data);

            if (view == null)
                continue;

            displayedItems.put(item, card);

            card.addView(view);
            GridLayout.LayoutParams params = layoutParamsFor(item);

            // MapFragment has priority
            if (item.priority)
                layout.addView(card, 0, params);
            else
                layout.addView(card, params);

            col = (col + item.columnSpan) % layout.getColumnCount();
            row = col == 0 ? row + item.rowSpan : row;
        }
    }

    public GridLayout.LayoutParams layoutParamsFor(GridItem item)
    {
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();

        params.height = item.rowSpan * rowHeight;
        params.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);
        GridLayout.Spec rowSpec = GridLayout.spec(GridLayout.UNDEFINED, item.rowSpan, 1);
        GridLayout.Spec columnSpec = GridLayout.spec(GridLayout.UNDEFINED, item.columnSpan, 1);
        params.rowSpec = rowSpec;
        params.columnSpec = columnSpec;

        return params;
    }

    public void removeItem(GridItem item)
    {
        View view = displayedItems.get(item);

        if (view != null)
        {
            displayedItems.remove(item);
            layout.removeViewInLayout(view);
            layout.requestLayout();
        }
    }
}
