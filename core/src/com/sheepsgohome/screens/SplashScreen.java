package com.sheepsgohome.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.sheepsgohome.GameData;
import com.sheepsgohome.GameScreens;

import static com.badlogic.gdx.Gdx.gl;
import static com.badlogic.gdx.graphics.Texture.TextureFilter.Linear;
import static com.sheepsgohome.GameData.CAMERA_HEIGHT;
import static com.sheepsgohome.GameData.CAMERA_WIDTH;
import static com.sheepsgohome.GameData.Loc;
import static com.sheepsgohome.GameSkins.skin;
import static com.sheepsgohome.GameData.Loc;
/**
 * Created by LittleLight on 11.1.2015.
 */
public class SplashScreen implements Screen {
    private final float SHEEP_SPLASH_SIZE = 0.18f;
    private final Image splashImage;
    private final Stage stage;
    private final Texture texture;
    private final OrthographicCamera camera;
    private final Label title;
    private Texture sheep;
    private Image sheepImage;

    public SplashScreen() {
        texture = new Texture("menu_background.png");
        texture.setFilter(Linear, Linear);
        splashImage = new Image(texture);

        sheep = new Texture("sheep_success.png");
        sheep.setFilter(Linear, Linear);
        sheepImage = new Image(sheep);

        title = new Label(Loc.get("sheeps.go.home"), skin, "menuTitle");

        stage = new Stage();

        camera = new OrthographicCamera(CAMERA_WIDTH, CAMERA_HEIGHT);
    }

    @Override
    public void show() {
        splashImage.setPosition(-GameData.CAMERA_WIDTH / 2, -GameData.CAMERA_HEIGHT / 2);
        splashImage.setZIndex(10);
        splashImage.setSize(GameData.CAMERA_WIDTH, GameData.CAMERA_HEIGHT);
        splashImage.addAction(Actions.sequence(
                Actions.alpha(0),
                Actions.fadeIn(2.5f),
                Actions.delay(2.5f),
                Actions.fadeOut(1.4f)
        ));


        sheepImage.setPosition(50, -130);
        sheepImage.setZIndex(100);
        sheepImage.setSize(sheepImage.getWidth() * SHEEP_SPLASH_SIZE, sheepImage.getHeight() * SHEEP_SPLASH_SIZE);
        sheepImage.addAction(Actions.sequence(
                Actions.delay(2.5f),
                Actions.moveTo(-20, sheepImage.getY(), 0.5f),
                Actions.delay(2),
                Actions.fadeOut(1),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        ((Game) Gdx.app.getApplicationListener()).setScreen(GameScreens.mainMenuScreen);
                    }
                })
        ));

        float multiplier = 1;
        float fontScale = (CAMERA_WIDTH * multiplier - 20) / title.getPrefWidth();
        title.setFontScale(fontScale);
        title.setPosition((CAMERA_WIDTH - title.getPrefWidth()) / 2f - (CAMERA_WIDTH / 2f), 60);
        title.addAction(Actions.sequence(
                Actions.delay(2.5f),
                Actions.moveTo(title.getX(), 20, 0.5f)
        ));


        stage.addActor(splashImage);
        stage.addActor(sheepImage);
        stage.addActor(title);

        stage.getViewport().setCamera(camera);
    }

    @Override
    public void render(float delta) {
        gl.glClearColor(0, 0, 0, 1);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        stage.act();
        stage.draw();

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        texture.dispose();
        sheep.dispose();
        stage.dispose();
    }
}
