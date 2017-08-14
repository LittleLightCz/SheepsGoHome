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
import static com.sheepsgohome.GameSkins.skin;
import static com.sheepsgohome.GameData.Loc;
/**
 * Created by LittleLight on 11.1.2015.
 */
public class SettingsScreen implements Screen {

    private static final float BUTTON_WIDTH = 100;
    private static final float BUTTON_SMALL_WIDTH = 50;
    private Stage stage;
    private Table table;

    private TextButton buttonPlayer;
    private TextButton buttonControls;
    private TextButton buttonSound;
    private TextButton buttonBack;
    private Label title;

    Texture texture;
    Image bgImage;

    @Override
    public void show() {
        buttonPlayer = new TextButton(Loc.get("player"), skin);
        buttonControls = new TextButton(Loc.get("controls"), skin);
        buttonSound = new TextButton(Loc.get("sound"), skin);
        buttonBack = new TextButton(Loc.get("back"), skin);
        title = new Label(Loc.get("settings"), skin, "menuTitle");

        texture = new Texture("menu_background.png");
        bgImage = new Image(texture);


        float multiplier = 2;
        stage = new Stage(new StretchViewport(CAMERA_WIDTH * multiplier, CAMERA_HEIGHT * multiplier));

        table = new Table();
//        table.setDebug(true);

        //click listeners
        buttonPlayer.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new SettingsPlayerScreen());
            }
        });

        buttonControls.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new SettingsControlsScreen());
            }
        });

        buttonSound.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new SettingsSoundScreen());
            }
        });


        buttonBack.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(GameScreens.mainMenuScreen);
            }
        });


        //table
        table.setFillParent(true);

        title.setFontScale(GameData.SETTINGS_TITLE_FONT_SCALE);
        table.add(title).top().row();

        Table buttonsTable = new Table();

        buttonsTable.add(buttonPlayer).size(BUTTON_WIDTH, BUTTON_WIDTH / 2).row();
        buttonsTable.add(buttonControls).size(BUTTON_WIDTH, BUTTON_WIDTH / 2).row();
        buttonsTable.add(buttonSound).size(BUTTON_WIDTH, BUTTON_WIDTH / 2).row();
        buttonsTable.add(buttonBack).size(BUTTON_WIDTH, BUTTON_WIDTH / 2).row();

        table.add(buttonsTable).expandY().row();
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
