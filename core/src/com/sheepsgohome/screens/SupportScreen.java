package com.sheepsgohome.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.sheepsgohome.GameData;
import com.sheepsgohome.GameScreens;

import static com.badlogic.gdx.graphics.Texture.TextureFilter;
import static com.sheepsgohome.GameData.CAMERA_HEIGHT;
import static com.sheepsgohome.GameData.CAMERA_WIDTH;
import static com.sheepsgohome.GameData.Loc;
import static com.sheepsgohome.GameSkins.skin;

/**
 * Created by LittleLight on 11.1.2015.
 */
public class SupportScreen implements Screen {

    private static final float BUTTON_WIDTH = 100;
    private static final float BUTTON_SMALL_WIDTH = 50;
    private Stage stage;
    private Table table;

    private TextButton buttonRate;
    private TextButton buttonShareFB;
    private TextButton buttonShareGPlus;
    private TextButton buttonTweet;

    private TextButton buttonBack;

    private Label title;

    Texture texture;
    Image bgImage;

    @Override
    public void show() {
        buttonRate = new TextButton(Loc.get("rate"), skin);
        buttonShareFB = new TextButton(Loc.get("share.FB"), skin);
        buttonShareGPlus = new TextButton(Loc.get("share.GPlus"), skin);
        buttonTweet = new TextButton(Loc.get("tweet"), skin);

        buttonBack = new TextButton(Loc.get("back"), skin);
        title = new Label(Loc.get("how.to.support"), skin, "menuTitle");

        texture = new Texture("menu_background.png");
        bgImage = new Image(texture);

        float multiplier = 2;
        stage = new Stage(new StretchViewport(CAMERA_WIDTH * multiplier, CAMERA_HEIGHT * multiplier));

        table = new Table();
//        table.setDebug(true);

        //click listeners
        buttonBack.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(GameScreens.mainMenuScreen);
            }
        });

        buttonRate.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //Gdx.net.openURI("https://play.google.com/store/apps/details?id=com.tumblr.svetylk0.sheepsgohome.android");
                GameData.functions.launchRateAppAction();
            }
        });

        buttonShareFB.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.net.openURI("https://www.facebook.com/sharer/sharer.php?u=http://play.google.com/store/apps/details?id=com.tumblr.svetylk0.sheepsgohome.android");
            }
        });

        buttonShareGPlus.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.net.openURI("https://plus.google.com/share?url=http://play.google.com/store/apps/details?id=com.tumblr.svetylk0.sheepsgohome.android");
            }
        });

        buttonTweet.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.net.openURI("https://twitter.com/share?url=http://play.google.com/store/apps/details?id=com.tumblr.svetylk0.sheepsgohome.android");
            }
        });





        //table
        table.setFillParent(true);

        title.setFontScale(GameData.SETTINGS_TITLE_FONT_SCALE);
        table.add(title).top().colspan(2).row();

        Table contentTable = new Table();
        contentTable.add(buttonRate).size(BUTTON_WIDTH, BUTTON_WIDTH / 2).row();
        contentTable.add(buttonShareFB).size(BUTTON_WIDTH, BUTTON_WIDTH / 2).row();
        contentTable.add(buttonShareGPlus).size(BUTTON_WIDTH, BUTTON_WIDTH / 2).row();
        contentTable.add(buttonTweet).size(BUTTON_WIDTH, BUTTON_WIDTH / 2).row();

        table.add(contentTable).expand().top().row();

        table.add(buttonBack).size(BUTTON_SMALL_WIDTH, BUTTON_SMALL_WIDTH / 2).bottom().center().row();

        stage.addActor(bgImage);
        stage.addActor(table);

        Gdx.input.setInputProcessor(stage);

        bgImage.setWidth(CAMERA_WIDTH * multiplier);
        bgImage.setHeight(CAMERA_HEIGHT * multiplier);
        texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
        stage.dispose();
        texture.dispose();
    }
}
