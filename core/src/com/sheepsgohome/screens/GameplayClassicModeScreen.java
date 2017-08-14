package com.sheepsgohome.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.sheepsgohome.GameData;
import com.sheepsgohome.GameObjectType;
import com.sheepsgohome.GameSkins;
import com.sheepsgohome.SteerableBody;
import com.sheepsgohome.SteerableHungryWolfBody;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;

import static com.badlogic.gdx.Gdx.gl;
import static com.badlogic.gdx.Gdx.graphics;
import static com.sheepsgohome.GameData.ALPHA_WOLF_SIZE;
import static com.sheepsgohome.GameData.ALPHA_WOLF_SPEED;
import static com.sheepsgohome.GameData.CAMERA_HEIGHT;
import static com.sheepsgohome.GameData.CAMERA_WIDTH;
import static com.sheepsgohome.GameData.HOME_SIZE;
import static com.sheepsgohome.GameData.HUNGRY_WOLF_SIZE;
import static com.sheepsgohome.GameData.HUNGRY_WOLF_SPEED;
import static com.sheepsgohome.GameData.LEVEL;
import static com.sheepsgohome.GameData.SHEEP_SIZE;
import static com.sheepsgohome.GameData.SHEEP_SPEED;
import static com.sheepsgohome.GameData.SOUND_ENABLED;
import static com.sheepsgohome.GameData.VIRTUAL_JOYSTICK;
import static com.sheepsgohome.GameData.VIRTUAL_JOYSTICK_LEFT;
import static com.sheepsgohome.GameData.VIRTUAL_JOYSTICK_NONE;
import static com.sheepsgohome.GameData.VIRTUAL_JOYSTICK_RIGHT;
import static com.sheepsgohome.GameData.WILD_WOLF_SIZE;
import static com.sheepsgohome.GameData.WILD_WOLF_SPEED;
import static com.sheepsgohome.GameMusic.ambient;
import static com.sheepsgohome.GameObjectType.eAlphaWolf;
import static com.sheepsgohome.GameObjectType.eHome;
import static com.sheepsgohome.GameObjectType.eHungryWolf;
import static com.sheepsgohome.GameObjectType.eSheep;
import static com.sheepsgohome.GameObjectType.eWall;
import static com.sheepsgohome.GameObjectType.eWildWolf;
import static com.sheepsgohome.GameTools.calculateAngle;
import static com.sheepsgohome.GameTools.setRandomMovement;
import static com.sheepsgohome.GameData.Loc;
/**
 * Created by LittleLight on 11.1.2015.
 */
public class GameplayClassicModeScreen implements Screen, ContactListener {

    enum State {
        eRunning, eGameOver_Wild,eGameOver_Hungry, eGameOver_Alpha, eNextLevel
    }

    private FPSLogger fpsLogger;

    private Touchpad touchpad;
    private boolean touchpadEnabled;

    private int wolves_count;

    private Stage stage;
    private Label levelLabel;

    private State gameState;

    private SpriteBatch batch;

    private Texture sheep_texture;
    private Texture wolf_texture;
    private Texture hungry_wolf_texture;
    private Texture alpha_wolf_texture;
    private Texture home_texture;
    private Texture background_texture;

    private Sprite wolves[];
    private Sprite sheep;
    private Sprite home;
    private Sprite background;

    //Physics
    public static World world;
    public static Box2DDebugRenderer debugRenderer;
    public static OrthographicCamera camera;

    public static SteerableBody sheep_body;
    public static SteerableBody[] wolf_bodies;
    public static Body[] walls_bodies;
    public static SteerableBody home_body;

    @Override
    public void show() {
        //catch menu and back buttons
        Gdx.input.setCatchBackKey(true);
        Gdx.input.setCatchMenuKey(true);

        //FPS debug
        fpsLogger = new FPSLogger();

        //Ambient
        if (ambient != null && SOUND_ENABLED && !ambient.isPlaying()) {
            ambient.play();
        }

        //Stage
        float multiplier = 1;
        stage = new Stage(new StretchViewport(CAMERA_WIDTH * multiplier, CAMERA_HEIGHT * multiplier));

        //Touchpad
        if (VIRTUAL_JOYSTICK == VIRTUAL_JOYSTICK_NONE) {
            touchpadEnabled = false;
        } else {
            touchpadEnabled = true;
        }

        touchpad = new Touchpad(0, GameSkins.skin);
        int touchPadSize = 30;
        if (VIRTUAL_JOYSTICK == VIRTUAL_JOYSTICK_RIGHT) {
            touchpad.setBounds(CAMERA_WIDTH - touchPadSize, 0, touchPadSize, touchPadSize);
        } else if (VIRTUAL_JOYSTICK == VIRTUAL_JOYSTICK_LEFT) {
            touchpad.setBounds(0, 0, touchPadSize, touchPadSize);
        }
        touchpad.addAction(Actions.alpha(0.5f));

        levelLabel = new Label(Loc.format("level", LEVEL), GameSkins.skin, "levelTitle");
        float fontScale = (CAMERA_WIDTH * multiplier - 40) / levelLabel.getPrefWidth();
        levelLabel.setFontScale(fontScale);

        Table table = new Table();
        table.add(levelLabel);
        table.setFillParent(true);

        levelLabel.addAction(Actions.sequence(
                Actions.alpha(1),
                Actions.fadeOut(3)
        ));

        stage.addActor(table);
        if (touchpadEnabled) {
            stage.addActor(touchpad);
        }
        Gdx.input.setInputProcessor(stage);
        //-------------

        gameState = State.eRunning;

        camera = new OrthographicCamera(CAMERA_WIDTH, CAMERA_HEIGHT);
        batch = new SpriteBatch();

        SetUpLevel();

        StartWolves();
    }

    private void SetUpLevel() {
        sheep_texture = new Texture("sheep.png");
        home_texture = new Texture("home.png");
        wolf_texture = new Texture("wolf.png");
        hungry_wolf_texture = new Texture("wolf-hungry.png");
        alpha_wolf_texture = new Texture("wolf-alpha.png");

        background_texture = new Texture("grass-background.jpg");
        background_texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        background = new Sprite(background_texture);
        background.setPosition(-CAMERA_WIDTH, -CAMERA_HEIGHT);
        background.setSize(CAMERA_WIDTH * 2, CAMERA_HEIGHT * 2);

        sheep = new Sprite(sheep_texture);
        sheep.setSize(SHEEP_SIZE, SHEEP_SIZE);
        sheep.setOriginCenter();

        home = new Sprite(home_texture);
        home.setSize(HOME_SIZE, HOME_SIZE);
        home.setPosition(-home.getWidth() / 2, CAMERA_HEIGHT / 2 - home.getHeight());

        InitializePhysics();

        CreateSheepBody();
        CreateHomeBody();
        CreateWallBoundaries();

        wolves_count = CreateWolfBodies();

        wolves = new Sprite[wolves_count];
        for (int i = 0; i < wolves.length; i++) {
            GameObjectType wolfType = (GameObjectType) wolf_bodies[i].getUserData();
            switch (wolfType) {
                case eHungryWolf:
                    wolves[i] = new Sprite(hungry_wolf_texture);
                    wolves[i].setSize(HUNGRY_WOLF_SIZE, HUNGRY_WOLF_SIZE);
                    break;
                case eAlphaWolf:
                    wolves[i] = new Sprite(alpha_wolf_texture);
                    wolves[i].setSize(ALPHA_WOLF_SIZE, ALPHA_WOLF_SIZE);
                    break;
                default:
                    wolves[i] = new Sprite(wolf_texture);
                    wolves[i].setSize(WILD_WOLF_SIZE, WILD_WOLF_SIZE);
                    break;
            }
            wolves[i].setOriginCenter();
        }

    }

    public void InitializePhysics() {
        Box2D.init();

        world = new World(new Vector2(0, 0), true);
        world.setContactListener(this);

        debugRenderer = new Box2DDebugRenderer();
    }

    public void render(float delta) {
        gl.glClearColor(0.9f, 0.9f, 0.9f, 1);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        switch (gameState) {
            case eRunning:
                //positioning
                sheep.setPosition(
                        sheep_body.getPosition().x - SHEEP_SIZE / 2,
                        sheep_body.getPosition().y - SHEEP_SIZE / 2
                );

                sheep.setRotation((float) Math.toDegrees(sheep_body.getBody().getAngle()));

                //input
                if (touchpadEnabled) {
                    if (touchpad.isTouched()) {
                        handleTouch();
                    } else {
                        sheep_body.getBody().setLinearVelocity(0, 0);
                        sheep_body.getBody().setAngularVelocity(0);
                    }

                } else {
                    if (Gdx.input.isTouched()) {
                        handleTouch(Gdx.input.getX(), Gdx.input.getY());
                    } else {
                        sheep_body.getBody().setLinearVelocity(0, 0);
                        sheep_body.getBody().setAngularVelocity(0);
                    }
                }

                //drawing
                batch.setProjectionMatrix(camera.combined);
                batch.begin();

                background.draw(batch);

                sheep.draw(batch);

                //draw home - top center
                home.draw(batch);

                //draw wolves
                GameObjectType wolfType;
                for (int i = 0; i < wolves_count; i++) {
                    wolfType = (GameObjectType) wolf_bodies[i].getUserData();

                    switch (wolfType) {
                        case eAlphaWolf:
                            wolves[i].setPosition(
                                    wolf_bodies[i].getPosition().x - ALPHA_WOLF_SIZE / 2,
                                    wolf_bodies[i].getPosition().y - ALPHA_WOLF_SIZE / 2
                            );

                            setAlphaWolfVelocity(wolf_bodies[i].getBody());
                            break;
                        case eHungryWolf:
                            wolf_bodies[i].calculateSteeringBehaviour();
                            wolves[i].setPosition(
                                    wolf_bodies[i].getPosition().x - HUNGRY_WOLF_SIZE / 2,
                                    wolf_bodies[i].getPosition().y - HUNGRY_WOLF_SIZE / 2
                            );

                            //setHungryWolfVelocity(wolf_bodies[i].getBody());
                            break;
                        default:
                            wolves[i].setPosition(
                                    wolf_bodies[i].getPosition().x - WILD_WOLF_SIZE / 2,
                                    wolf_bodies[i].getPosition().y - WILD_WOLF_SIZE / 2
                            );
                            break;
                    }


                    Vector2 targetVelocity = wolf_bodies[i].getLinearVelocity();
                    wolves[i].setRotation(calculateAngle(targetVelocity));


                    wolves[i].draw(batch);
                }

                batch.end();

//                debugRenderer.render(world, camera.combined);
                world.step(graphics.getDeltaTime(), 6, 2);

                stage.act();
                stage.draw();

                break;

            case eGameOver_Wild:
                gameOver(GameDialogType.typeSheepFailed_Wild);
                break;
            case eGameOver_Hungry:
                gameOver(GameDialogType.typeSheepFailed_Hungry);
                break;
            case eGameOver_Alpha:
                gameOver(GameDialogType.typeSheepFailed_Alpha);
                break;
            case eNextLevel:
                nextLevel();
                break;
        }

        //fps debug
//        fpsLogger.log();

    }

    private void setHungryWolfVelocity(Body wolf) {
        Vector2 vec = sheep_body.getPosition().sub(wolf.getPosition()).nor();
        wolf.setLinearVelocity(vec.x * HUNGRY_WOLF_SPEED, vec.y * HUNGRY_WOLF_SPEED);
    }

    private void setAlphaWolfVelocity(Body wolf) {
        Vector2 vec = new Vector2(sheep_body.getPosition()).sub(wolf.getPosition()).nor();
        float alphaSpeed = ALPHA_WOLF_SPEED / Math.abs(wolf.getPosition().dst(sheep_body.getPosition()) / CAMERA_HEIGHT);
        wolf.setLinearVelocity(vec.x * alphaSpeed, vec.y * alphaSpeed);
    }

    @Override
    public void resize(int width, int height) {

        stage.getViewport().update(width, height, true);

        //world boundaries
        walls_bodies[0].setTransform(-CAMERA_WIDTH / 2, 0, 0);
        walls_bodies[1].setTransform(CAMERA_WIDTH / 2, 0, 0);
        walls_bodies[2].setTransform(0, CAMERA_HEIGHT / 2, (float) (Math.PI / 2f));
        walls_bodies[3].setTransform(0, -CAMERA_HEIGHT / 2, (float) (Math.PI / 2f));

        //sheep
        sheep_body.getBody().setTransform(0, -CAMERA_HEIGHT / 2f + 2, (float) (Math.PI / 2f));

        //home
        home_body.getBody().setTransform(0, (CAMERA_HEIGHT / 2f) - HOME_SIZE / 2, 0);

        //wolves
        float gap = 0.5f;
        float start_offset = 6;

        float x = start_offset + (-CAMERA_WIDTH / 2);
        float y = (CAMERA_HEIGHT / 2) - HOME_SIZE - start_offset;

        int max_wolves_in_a_row = 13;

        float angle = (float) (Math.PI * 3f / 2f);

        for (int i = 0; i < wolf_bodies.length; i++) {
            wolf_bodies[i].getBody().setTransform(x + (i % max_wolves_in_a_row) * (gap + WILD_WOLF_SIZE), y, angle);

            if (i != 0 && i % max_wolves_in_a_row == max_wolves_in_a_row - 1) {
                y -= WILD_WOLF_SIZE + gap;
            }
        }

        //center last row
        int i = wolves_count - (wolves_count % max_wolves_in_a_row);
        float w_width = (wolves_count % max_wolves_in_a_row) * (gap + WILD_WOLF_SIZE);

        x += (CAMERA_WIDTH - w_width) / 2;
        x -= start_offset / 2;

        for (; i < wolf_bodies.length; i++) {
            wolf_bodies[i].getBody().setTransform(x + (i % max_wolves_in_a_row) * (gap + WILD_WOLF_SIZE), y, angle);
        }
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        //stop ambient
        if (ambient != null && ambient.isPlaying()) {
            ambient.pause();
        }

        //release menu and back buttons
        Gdx.input.setCatchBackKey(false);
        Gdx.input.setCatchMenuKey(false);

        dispose();
    }

    @Override
    public void dispose() {
        sheep_texture.dispose();
        wolf_texture.dispose();
        hungry_wolf_texture.dispose();
        alpha_wolf_texture.dispose();
        home_texture.dispose();
        background_texture.dispose();

        batch.dispose();

        world.dispose();

        stage.dispose();
    }

    private Body CreateWildWolfBody() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(0, 0);

        CircleShape circleShape = new CircleShape();
        circleShape.setRadius((WILD_WOLF_SIZE / 2) * 0.85f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circleShape;
        fixtureDef.density = 0.1f;
        fixtureDef.friction = 0.1f;
        fixtureDef.restitution = 0.6f;

        return CreateWolfBody(eWildWolf, bodyDef, fixtureDef);
    }

    private Body CreateHungryWolfBody() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(0, 0);

        CircleShape circleShape = new CircleShape();
        circleShape.setRadius((HUNGRY_WOLF_SIZE / 2) * 0.85f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circleShape;
        fixtureDef.density = 0.2f;
        fixtureDef.friction = 0.1f;
        fixtureDef.restitution = 0.6f;

        return CreateWolfBody(eHungryWolf, bodyDef, fixtureDef);
    }

    private Body CreateAlphaWolfBody() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(0, 0);

        CircleShape circleShape = new CircleShape();
        circleShape.setRadius((ALPHA_WOLF_SIZE / 2) * 0.85f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circleShape;
        fixtureDef.density = 0.3f;
        fixtureDef.friction = 0.1f;
        fixtureDef.restitution = 0.6f;

        return CreateWolfBody(eAlphaWolf, bodyDef, fixtureDef);
    }

    private Body CreateWolfBody(GameObjectType wolfType, BodyDef bodyDef, FixtureDef fixtureDef) {

        Body body = world.createBody(bodyDef);
        body.setUserData(wolfType);
        body.createFixture(fixtureDef);

        fixtureDef.shape.dispose();

        return body;
    }


    private int CreateWolfBodies() {

        WolvesData data = getWolvesData(LEVEL);

        int count = data.getWolvesCount();

        wolf_bodies = new SteerableBody[count];
        ArrayList<SteerableBody> wolf_bodies_list = new ArrayList<SteerableBody>();

        for (int i = 0; i < data.WildWolves; i++) {
            wolf_bodies_list.add(new SteerableBody(CreateWildWolfBody()));
        }

        for (int i = 0; i < data.HungryWolves; i++) {
            wolf_bodies_list.add(new SteerableHungryWolfBody(CreateHungryWolfBody(), sheep_body, home_body));
        }

        for (int i = 0; i < data.AlphaWolves; i++) {
            wolf_bodies_list.add(new SteerableBody(CreateAlphaWolfBody()));
        }

        Collections.shuffle(wolf_bodies_list);
        wolf_bodies_list.toArray(wolf_bodies);

        return count;
    }

    private WolvesData getWolvesData(int level) {
        WolvesData data = new WolvesData();

        data.WildWolves = (((level - 1) % 5) + 1) * 5;

        if (level < 6) {
            data.HungryWolves = 0;
        } else {
            data.HungryWolves = (((level - 1 - 5) / 5) % 4) + 1;
        }

        if (level < 26) {
            data.AlphaWolves = 0;
        } else {
            data.AlphaWolves = (level - 1 - 5) / 20;
        }

        return data;
    }

    private void CreateWallBoundaries() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(0, 0);
        bodyDef.type = BodyDef.BodyType.StaticBody;

        PolygonShape groundBox = new PolygonShape();
        groundBox.setAsBox(0.1f, Math.max(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

        walls_bodies = new Body[4];
        for (int i = 0; i < walls_bodies.length; i++) {
            walls_bodies[i] = world.createBody(bodyDef);
            walls_bodies[i].setUserData(eWall);
            walls_bodies[i].createFixture(groundBox, 0.0f);
        }

        groundBox.dispose();
    }

    private void CreateSheepBody() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(0, 0);

        sheep_body = new SteerableBody(world.createBody(bodyDef));

        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(SHEEP_SIZE / 2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circleShape;
        fixtureDef.density = 0.1f;
        fixtureDef.friction = 0.1f;
        fixtureDef.restitution = 0.6f;

        sheep_body.getBody().createFixture(fixtureDef);
        sheep_body.getBody().setUserData(eSheep);

        circleShape.dispose();
    }

    private void CreateHomeBody() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(0, 0);

        home_body = new SteerableBody(world.createBody(bodyDef));

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(HOME_SIZE / 2, HOME_SIZE / 2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.1f;
        fixtureDef.friction = 0.1f;
        fixtureDef.restitution = 0.6f;

        home_body.getBody().createFixture(fixtureDef);
        home_body.getBody().setUserData(eHome);

        shape.dispose();
    }

    //Touchpad touch
    private void handleTouch() {
        Vector2 vec = new Vector2(touchpad.getKnobPercentX(), touchpad.getKnobPercentY()).nor();
        vec.x *= SHEEP_SPEED;
        vec.y *= SHEEP_SPEED;

        sheep_body.getBody().setLinearVelocity(vec.x, vec.y);

        //set Angle
        sheep_body.getBody().setTransform(sheep_body.getPosition(), (float) Math.toRadians(calculateAngle(vec.x, vec.y)));
    }

    //Finger touch
    private void handleTouch(int x, int y) {

        float targetx = x;
        float targety = Gdx.graphics.getHeight() - y;

        targetx = (targetx / (float) Gdx.graphics.getWidth()) * CAMERA_WIDTH - CAMERA_WIDTH / 2f;
        targety = (targety / (float) Gdx.graphics.getHeight()) * CAMERA_HEIGHT - CAMERA_HEIGHT / 2f;

        Vector2 pos = sheep_body.getPosition();
        targetx -= pos.x;
        targety -= pos.y;

        float divider = Math.max(Math.abs(targetx), Math.abs(targety));

        targetx = (targetx / divider) * SHEEP_SPEED;
        targety = (targety / divider) * SHEEP_SPEED;

        sheep_body.getBody().setLinearVelocity(targetx, targety);

        //set Angle
        sheep_body.getBody().setTransform(pos, (float) Math.toRadians(calculateAngle(targetx, targety)));
    }

    private void StartWolves() {
        for (SteerableBody b : wolf_bodies) {
            setRandomMovement(b.getBody(), WILD_WOLF_SPEED);
        }
    }

    @Override
    public void beginContact(Contact contact) {
        Body bodyA = contact.getFixtureA().getBody();
        Body bodyB = contact.getFixtureB().getBody();

        GameObjectType typeA = (GameObjectType) bodyA.getUserData();
        GameObjectType typeB = (GameObjectType) bodyB.getUserData();

        if (typeA != null && typeB != null) {

            switch (typeA) {
                case eHome:
                    switch (typeB) {
                        case eWildWolf:
                            setRandomMovement(bodyB, GameData.WILD_WOLF_SPEED);
                            break;
                        case eSheep:
                            gameState = State.eNextLevel;
                            break;
                    }
                    break;
                case eWall:
                    if (typeB == eWildWolf) {
                        setRandomMovement(bodyB, GameData.WILD_WOLF_SPEED);
                    }
                    break;
                case eWildWolf:
                    setRandomMovement(bodyA, GameData.WILD_WOLF_SPEED);

                    if (typeB == eWildWolf) {
                        setRandomMovement(bodyB, GameData.WILD_WOLF_SPEED);
                    } else if (typeB == eSheep) {
                        gameState = State.eGameOver_Wild;
                    }
                    break;
                case eHungryWolf:
                    if (typeB == eWildWolf) {
                        setRandomMovement(bodyB, GameData.WILD_WOLF_SPEED);
                    } else if (typeB == eSheep) {
                        gameState = State.eGameOver_Hungry;
                    }
                    break;
                case eAlphaWolf:
                    if (typeB == eWildWolf) {
                        setRandomMovement(bodyB, GameData.WILD_WOLF_SPEED);
                    } else if (typeB == eSheep) {
                        gameState = State.eGameOver_Alpha;
                    }
                    break;
                case eSheep:
                    switch (typeB) {
                        case eWildWolf:
                            gameState = State.eGameOver_Wild;
                            break;
                        case eHungryWolf:
                            gameState = State.eGameOver_Hungry;
                            break;
                        case eAlphaWolf:
                            gameState = State.eGameOver_Alpha;
                            break;
                        case eHome:
                            gameState = State.eNextLevel;
                            break;
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private void gameOver(GameDialogType dialogType) {
        ((Game) Gdx.app.getApplicationListener()).setScreen(new GameplayDialog(dialogType));
    }

    private void nextLevel() {
        ((Game) Gdx.app.getApplicationListener()).setScreen(new GameplayDialog(GameDialogType.typeSheepSucceeded));
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }


    private class WolvesData {
        int WildWolves;
        int HungryWolves;
        int AlphaWolves;

        public WolvesData() {
        }

        public WolvesData(int wildWolves, int hungryWolves, int alphaWolves) {
            WildWolves = wildWolves;
            HungryWolves = hungryWolves;
            AlphaWolves = alphaWolves;
        }

        int getWolvesCount() {
            return WildWolves + HungryWolves + AlphaWolves;
        }
    }


}


