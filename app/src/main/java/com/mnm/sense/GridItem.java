package com.mnm.sense;


public class GridItem
{
    public int columnSpan;
    ViewInitializer initializer;
    Object data;

    public GridItem(int span, ViewInitializer init, Object d)
    {
        columnSpan = span;
        initializer = init;
        data = d;
    }
}
