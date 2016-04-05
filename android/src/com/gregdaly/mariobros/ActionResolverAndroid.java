package com.gregdaly.mariobros;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;

/**
 * Created by gregorydaly on 4/4/16.
 */
public class ActionResolverAndroid implements ActionResolver {
    Handler uiThread;
    Context appContext;

    public ActionResolverAndroid(Context appContext) {
        uiThread = new Handler();
        this.appContext = appContext; }

    @Override
    public void launchCamera() {
        Intent intent = new Intent(appContext,FaceTrackerActivity.class);
        appContext.startActivity(intent);
    }
}
