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
public class Character extends Sprite {
    public enum State {DOWN, UP, STANDING, RUNNING };
    public State currentState;
    public State previousState;
    public World world;
    public Body b2Body;
    private TextureRegion standingDown;
    private TextureRegion standingUp;
    private TextureRegion standingSide;
    private Animation walkToSide;
    private Animation walkUp;
    private Animation walkDown;
    private boolean runningRight;
    private float stateTimer;
    private String directionFacing = "UP";

    public Character(World world, PlayScreen playScreen){
        super(playScreen.getTextureAtlas().findRegion("little_mario"));
        this.world = world;
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer =0;
        runningRight = false;

        Array<TextureRegion> frames = new Array<TextureRegion>();
        for (int i = 6; i < 9; i++){
            frames.add(new TextureRegion(getTexture(),i*23,0,23,40));
        }
        walkToSide = new Animation(0.1f,frames);
        frames.clear();

        for (int i = 4; i < 6; i++){
            frames.add(new TextureRegion(getTexture(),i*23,0,23,40));
        }
        walkUp = new Animation(0.1f,frames);
        frames.clear();

        for (int i = 1; i < 3; i++){
            frames.add(new TextureRegion(getTexture(),i*23,0,23,40));
        }
        walkDown = new Animation(0.1f,frames);

        defineMario();
        standingDown = new TextureRegion(getTexture(),0,0,23,40);
        standingUp = new TextureRegion(getTexture(),23*3,0,23,40);
        standingSide = new TextureRegion(getTexture(),23*6,0,23,40);
        setBounds(0,0,23/ MarioBros.PPM,40/MarioBros.PPM);
        setRegion(standingDown);
    }

    public void update(float dt){
        setPosition(b2Body.getPosition().x-getWidth()/2,b2Body.getPosition().y-getHeight()/2);
        setRegion(getFrame(dt));
    }

    public TextureRegion getFrame(float dt){
        currentState = getState();
        TextureRegion wayToStand;

        if( directionFacing.equals("DOWN")){
            wayToStand = standingDown;
        } else if (directionFacing.equals("UP")){
            wayToStand = standingUp;
        } else {
            wayToStand = standingSide;
        }

        TextureRegion region;
        switch (currentState){
            case UP:
                region = walkUp.getKeyFrame(stateTimer,true);
                directionFacing = "UP";
                break;
            case RUNNING:
                region = walkToSide.getKeyFrame(stateTimer,true);
                directionFacing = "SIDE";
                break;
            case DOWN:
                region = walkDown.getKeyFrame(stateTimer,true);
                directionFacing = "DOWN";
                break;
            case STANDING:
                default:
                        region = wayToStand;
                    break;
        }

        //if mario is running left and the texture isnt facing left... flip it.
        if((b2Body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()){
            region.flip(true, false);
            runningRight = false;
        }

        //if mario is running right and the texture isnt facing right... flip it.
        else if((b2Body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()){
            region.flip(true, false);
            runningRight = true;
        }

        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;
        return region;
    }

    public State getState(){
        if(b2Body.getLinearVelocity().y > 0 || (b2Body.getLinearVelocity().y < 0 && previousState == State.UP))
            return State.UP;
        else if(b2Body.getLinearVelocity().y < 0)
            return State.DOWN;
        else if(b2Body.getLinearVelocity().x != 0)
            return State.RUNNING;
        else
            return  State.STANDING;
    }

    public void defineMario(){
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(32/ MarioBros.PPM,32/MarioBros.PPM);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        b2Body = world.createBody(bodyDef);

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
