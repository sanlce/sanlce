package sut.game01.core;

import static playn.core.PlayN.*;

import debug.DebugDrawBox2D;
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.callbacks.DebugDraw;
import org.jbox2d.collision.Manifold;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.contacts.Contact;
import playn.core.*;
import playn.core.util.Callback;
import playn.core.util.Clock;
import sut.game01.core.sprite.Kobold;
import tripleplay.game.Screen;
import tripleplay.game.ScreenStack;

public class TestScreen extends Screen {

    public static float M_PER_PIXEL = 1 / 26.666667f;
    private static int width = 24;
    private static int height = 18;
    private boolean hasLoaded=false;
    private DebugDrawBox2D debugDraw;
    private World world;
    Kobold k,k1;
    private final ScreenStack ss;
    private boolean showDebugDraw = true;

    public TestScreen(ScreenStack ss) {
        this.ss = ss;
    }

    @Override
    public void wasAdded() {
        Vec2 gravity = new Vec2(0.0f, 10.0f);
        world = new World(gravity, true);
        world.setWarmStarting(true);
        world.setAutoClearForces(true);

        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {

            }

            @Override
            public void endContact(Contact contact) {

            }

            @Override
            public void preSolve(Contact contact, Manifold manifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse contactImpulse) {

            }
        });


        super.wasAdded();



        Image bgImage = assets().getImage("images/bg.png");
        Image backImage = assets().getImage("images/play.png");
        bgImage.addCallback(new Callback<Image>() {
            @Override
            public void onSuccess(Image result) {

            }

            @Override
            public void onFailure(Throwable cause) {

            }
        });


        if(showDebugDraw){
            CanvasImage image = graphics().createImage(
                    (int) (width / TestScreen.M_PER_PIXEL),
                    (int) (height / TestScreen.M_PER_PIXEL));
            layer.add(graphics().createImageLayer(image));
            debugDraw = new DebugDrawBox2D();
            debugDraw.setCanvas(image);
            debugDraw.setFlipY(false);
            debugDraw.setStrokeAlpha(150);
            debugDraw.setFillAlpha(75);
            debugDraw.setStrokeWidth(2.0f);
            debugDraw.setFlags(DebugDraw.e_shapeBit |
                               DebugDraw.e_jointBit |
                               DebugDraw.e_aabbBit);
            debugDraw.setCamera(0, 0, 1f / TestScreen.M_PER_PIXEL);
            world.setDebugDraw(debugDraw);
        }

        Body ground = world.createBody(new BodyDef());
        PolygonShape groundShape = new PolygonShape();
        groundShape.setAsEdge(new Vec2(0, height-2), new Vec2(width, height-2));

        ground.createFixture(groundShape, 0.0f);

        ImageLayer play = graphics().createImageLayer(backImage);
        layer.add(play);

        play.addListener(new Pointer.Adapter()
        {
            @Override
            public void onPointerDrag(Pointer.Event event) {
                super.onPointerDrag(event);
            }
        });

        ImageLayer bgLayer = graphics().createImageLayer(bgImage);
        //layer.add(bgLayer);
        k1 = new Kobold(world,200f,300f);
        layer.add(k1.layer());
        k = new Kobold(world,400f,300f);
        layer.add(k.layer());
    }

    @Override
    public void update(int delta) {
        super.update(delta);
        world.step(0.033f, 10, 10);
        k.update(delta);
        k1.update(delta);

        PlayN.keyboard().setListener(new Keyboard.Adapter() {
            @Override
            public void onKeyDown(Keyboard.Event event) {

                if(event.key() == Key.A){
                    k.getBody().applyForce(new Vec2(-200f,0f),k.getBody().getPosition());
                }
                if(event.key() == Key.D){
                    k.getBody().applyForce(new Vec2(200f,0f),k.getBody().getPosition());
                }
                if(event.key() == Key.W){
                    k.getBody().applyForce(new Vec2(0f,-800f),k.getBody().getPosition());
                }
                if(event.key() == Key.S){
                    k.getBody().applyForce(new Vec2(0f,1000f),k.getBody().getPosition());
                }
            }

        });
    }
    @Override
    public void paint(Clock clock) {
        super.paint(clock);

        k.paint(clock);
        k1.paint(clock);
        if (showDebugDraw){
            debugDraw.getCanvas().clear();
            world.drawDebugData();
        }
    }
}
