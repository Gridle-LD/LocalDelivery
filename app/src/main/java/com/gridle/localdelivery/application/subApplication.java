package com.gridle.localdelivery.application;

import android.app.Application;
import com.facebook.drawee.backends.pipeline.Fresco;

public class subApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
    }
}
