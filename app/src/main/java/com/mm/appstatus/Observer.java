package com.mm.appstatus;

import android.content.Context;

public interface Observer {
    void update(final String value, Context context);
}