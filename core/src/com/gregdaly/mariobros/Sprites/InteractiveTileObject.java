package com.gregdaly.mariobros.Sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.gregdaly.mariobros.MarioBros;

/**
 * Created by gregorydaly on 3/30/16.
 */
public abstract class InteractiveTileObject {
    protected World world;
    protected TiledMap tiledMap;
    protected TiledMapTile tile;
    protected Rectangle bounds;
    protected Body body;
    protected Fixture fixture;

    public InteractiveTileObject(World world, TiledMap map, Rectangle bounds){
        this.world = world;
        this.tiledMap = map;
        this.bounds = bounds;


        BodyDef bodyDef = new BodyDef();
        FixtureDef fixtureDef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set((bounds.getX()+bounds.getWidth()/2)/ MarioBros.PPM,(bounds.getY()+bounds.getHeight()/2)/MarioBros.PPM);

        body = world.createBody(bodyDef);
        shape.setAsBox(bounds.getWidth()/2/MarioBros.PPM,bounds.getHeight()/2/MarioBros.PPM);
        fixtureDef.shape = shape;
        fixture = body.createFixture(fixtureDef);
    }
    public abstract void onHeadHit();
}
