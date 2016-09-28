package com.mnm.sense;


public class GridItem
{
    int rowSpan;
    int columnSpan;
    Object data;

    public GridItem(int rows, int cols,Object d)
    {
        rowSpan = rows;
        columnSpan = cols;
        data = d;
    }
}
