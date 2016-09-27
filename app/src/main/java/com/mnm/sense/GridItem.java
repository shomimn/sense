package com.mnm.sense;


public class GridItem
{
    int rowSpan;
    int columnSpan;
    ViewInitializer initializer;
    Object data;

    public GridItem(int rows, int cols, ViewInitializer init, Object d)
    {
        rowSpan = rows;
        columnSpan = cols;
        initializer = init;
        data = d;
    }
}
