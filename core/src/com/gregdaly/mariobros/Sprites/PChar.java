package com.gregdaly.mariobros.Sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.gregdaly.mariobros.MarioBros;
import com.gregdaly.mariobros.Screens.PlayScreen;


/**
 * Created by gregorydaly on 3/29/16.
 */
public class PChar extends Sprite {
    public enum State { FALLING, JUMPING, STANDING, RUNNING };
    public State currentState;
    public State previousState;
//    public World world;
    public Body b2Body;
    private TextureRegion marioStanding;
    private Animation marioRun;
    private Animation marioJump;
    private boolean runningRight;
    private float stateTimer;

    public PChar(PlayScreen playScreen){
        super(playScreen.getTextureAtlas().findRegion("little_mario"));
//        this.world = world;
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer =0;
        runningRight = true;

        Array<TextureRegion> frames = new Array<TextureRegion>();
        for (int i = 1; i < 4; i++){
            frames.add(new TextureRegion(getTexture(),i*16,10,18,16));
        }
        marioRun = new Animation(0.1f,frames);
        frames.clear();

        for (int i = 4; i < 6; i++){
            frames.add(new TextureRegion(getTexture(),i*16,10,18,16));
        }
        marioJump = new Animation(0.1f,frames);

        definePChar();
        marioStanding = new TextureRegion(getTexture(),0,10,18,16);
        setBounds(0,0,16/ MarioBros.PPM,16/MarioBros.PPM);
        setRegion(marioStanding);
    }

    public void update(float dt){
        setPosition(b2Body.getPosition().x-getWidth()/2,b2Body.getPosition().y-getHeight()/2);
        setRegion(getFrame(dt));
    }

    public TextureRegion getFrame(float dt){
        currentState = getState();

        TextureRegion region;
        switch (currentState){
            case JUMPING:
                region = marioJump.getKeyFrame(stateTimer);
                break;
            case RUNNING:
                region = marioRun.getKeyFrame(stateTimer,true);
                break;
            case FALLING:
            case STANDING:
                default:
                    region = marioStanding;
                    break;
        }

        if((b2Body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()){
            region.flip(true,false);
            runningRight = false;
        }
        else if ((b2Body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()){
            region.flip(true,false);
            runningRight = true;
        }
        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;
        return region;
    }

    public State getState(){
        if(b2Body.getLinearVelocity().y > 0 || (b2Body.getLinearVelocity().y < 0 && previousState == State.JUMPING))
            return State.JUMPING;
        else if(b2Body.getLinearVelocity().y < 0)
            return State.FALLING;
        else if(b2Body.getLinearVelocity().x != 0)
            return State.RUNNING;
        else
            return  State.STANDING;
    }

    public void definePChar(){
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(32/ MarioBros.PPM,32/MarioBros.PPM);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        //b2Body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape circle = new CircleShape();
        circle.setRadius(8/MarioBros.PPM);

        fixtureDef.shape = circle;
        b2Body.createFixture(fixtureDef);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2/MarioBros.PPM, 10/MarioBros.PPM),new Vector2(2/MarioBros.PPM, 10/MarioBros.PPM));
        fixtureDef.shape=head;
        fixtureDef.isSensor = true;

        b2Body.createFixture(fixtureDef).setUserData("head");
    }
}
