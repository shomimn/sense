package com.mnm.sense;

import com.ubhave.sensormanager.ESException;

public interface Continuation<T>
{
    void execute(T result) throws ESException;
}
