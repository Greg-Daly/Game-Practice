package com.gregdaly.mariobros;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.gregdaly.mariobros.MarioBros;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
//		config.useGL30 = true;
		initialize(new MarioBros(new ActionResolverAndroid(this)), config);
	}
}
