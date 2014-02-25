package sut.game01.core.sprite;


import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import playn.core.*;
import playn.core.util.CallbackList;
import playn.core.util.Clock;
import sut.game01.core.TestScreen;

public class Kobold {



    private Sprite sprite;
    private int spriteIndex=0;
    private boolean hasLoaded=false;
    private int hp = 100;
    private int t = 0;
    private Body body;

    public void paint(Clock clock) {
        if(!hasLoaded) return;
        sprite.layer().setTranslation(body.getPosition().x / TestScreen.M_PER_PIXEL,body.getPosition().y / TestScreen.M_PER_PIXEL);
    }



    public enum State{
        IDLE,ATK,DIE
    };

    private State state = State.IDLE;

    private int e =0;
    private int offset=0;
    public Kobold(final World world,final float x_px, final float y_px){
        sprite = SpriteLoader.getSprite("images/kobold.json");
        sprite.addCallback(new CallbackList<Sprite>(){

            @Override
            public void onSuccess(Sprite result) {
                sprite.setSprite(spriteIndex);
                sprite.layer().setOrigin(sprite.width() / 2f, sprite.height() / 2f);
                sprite.layer().setTranslation(x_px,y_px);
                body = initPhysicsBody(world,TestScreen.M_PER_PIXEL*x_px,TestScreen.M_PER_PIXEL*y_px);
                hasLoaded = true;

            }

            @Override
            public void onFailure(Throwable cause) {
                PlayN.log().error("Error loading image!", cause);
            }
        });

        sprite.layer().addListener(new Pointer.Adapter(){
            @Override
            public void onPointerEnd(Pointer.Event event) {
                state = State.ATK;
                spriteIndex = -1;
                e=0;
            }
        });

    }
    public Layer layer(){
        return sprite.layer();
    }
    private Body initPhysicsBody(World world, float x, float y){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DYNAMIC;
        bodyDef.position = new Vec2(0,0);
        Body body = world.createBody(bodyDef);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(56 * TestScreen.M_PER_PIXEL / 2 , sprite.layer().height()*TestScreen.M_PER_PIXEL /2);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.2f;
        fixtureDef.friction = 0.1f;
        body.createFixture(fixtureDef);
        body.setLinearDamping(0.2f);
        body.setTransform(new Vec2(x,y),0f);
        return body;
    }

    public void update(int delta){


        if(!hasLoaded) return;
        e += delta;



        if(e > 150){
            switch (state){
                case IDLE: offset = 0;
                    break;
                case ATK: offset = 3;
                    break;
                case DIE: offset = 6;
                    if (spriteIndex == 7){
                        state = State.IDLE;
                    }
                    break;
          }
            spriteIndex = offset + ((spriteIndex + 1)%3);
            sprite.setSprite(spriteIndex);

            e=0;

    }


}

        public Body getBody(){
             return this.body;
        }
}
