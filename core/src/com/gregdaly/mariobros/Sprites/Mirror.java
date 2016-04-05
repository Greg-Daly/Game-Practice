package com.gregdaly.mariobros.Sprites;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.gregdaly.mariobros.ActionResolver;
import com.gregdaly.mariobros.MarioBros;

/**
 * Created by gregorydaly on 3/31/16.
 */
public class Mirror extends InteractiveTileObject {
    public Mirror(World world, TiledMap map, Rectangle bounds) {
        super(world, map, bounds);
        fixture.setUserData(this);
    }

    @Override
    public void onHeadHit() {
        Gdx.app.log("hit on mirror","");
//        ActionResolver.launchCamera();
        MarioBros.actionResolver.launchCamera();
    }


}
