package com.sheepsgohome.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.sheepsgohome.GameData;
import com.sheepsgohome.GameScreens;

import static com.badlogic.gdx.graphics.Texture.TextureFilter;
import static com.sheepsgohome.GameData.CAMERA_HEIGHT;
import static com.sheepsgohome.GameData.CAMERA_WIDTH;
import static com.sheepsgohome.GameData.Loc;
import static com.sheepsgohome.GameData.MUSIC_ENABLED;
import static com.sheepsgohome.GameMusic.ambient;
import static com.sheepsgohome.GameMusic.music;
import static com.sheepsgohome.GameSkins.skin;

/**
 * Created by LittleLight on 11.1.2015.
 */
public class MainMenuScreen implements Screen {

    private static final float BUTTON_WIDTH = 100;
    private Stage stage;
    private Table table;

    private TextButton buttonPlay;
    private TextButton buttonLeaderboard;
    private TextButton buttonSettings;
    private TextButton buttonExit;
    private ImageButton buttonSupport;
    private Label title;
    private Label version;

    private CheckBox musicCheckBox;

    Texture texture;
    Image bgImage;

    @Override
    public void show() {
        buttonSupport = new ImageButton(skin, "support");
        buttonPlay = new TextButton(Loc.get("play"), skin);
        buttonLeaderboard = new TextButton(Loc.get("leaderboard"), skin);
        buttonSettings = new TextButton(Loc.get("settings"), skin);
        buttonExit = new TextButton(Loc.get("exit"), skin);
        title = new Label(Loc.get("sheeps.go.home"), skin, "menuTitle");
        version = new Label(GameData.VERSION_STRING, skin);
        musicCheckBox = new CheckBox("", skin);
        musicCheckBox.setChecked(MUSIC_ENABLED);

        texture = new Texture("menu_background.png");
        bgImage = new Image(texture);


        float multiplier = 2;
        stage = new Stage(new StretchViewport(CAMERA_WIDTH * multiplier, CAMERA_HEIGHT * multiplier));

        table = new Table();
//        table.setDebug(true);


        musicCheckBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                CheckBox cbox = (CheckBox) actor;
                if (music != null && cbox != null) {
                    if (cbox.isChecked()) {
                        MUSIC_ENABLED = true;
                        if (!music.isPlaying()) {
                            music.play();
                        }
                    } else {
                        MUSIC_ENABLED = false;
                        if (music.isPlaying()) {
                            music.pause();
                        }
                    }
                    GameData.SavePreferences();
                }
            }
        });


        //click listeners
        buttonPlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                if (music != null && music.isPlaying()) {
                    music.pause();
                }

                ((Game) Gdx.app.getApplicationListener()).setScreen(GameScreens.gameplayClassicModeScreen);
            }
        });

        buttonLeaderboard.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new LeaderboardScreen());
            }
        });

        buttonSupport.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new SupportScreen());
            }
        });

        buttonSettings.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new SettingsScreen());
            }
        });

        buttonExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });


        //table
        table.setFillParent(true);

        float fontScale = (CAMERA_WIDTH * multiplier - 20) / title.getPrefWidth();
        title.setFontScale(fontScale);
        table.add(title).colspan(2).height(25).padTop(5).row();

        table.add(version).expandX().colspan(2).top().center().row();

        Table buttonsTable = new Table();
        buttonsTable.add(buttonPlay).size(BUTTON_WIDTH, BUTTON_WIDTH / 2).row();
        buttonsTable.add(buttonLeaderboard).size(BUTTON_WIDTH, BUTTON_WIDTH / 2).row();
        buttonsTable.add(buttonSettings).size(BUTTON_WIDTH, BUTTON_WIDTH / 2).row();
        buttonsTable.add(buttonExit).size(BUTTON_WIDTH, BUTTON_WIDTH / 2).row();

        table.add(buttonsTable).expandY().colspan(2).row();

        float donateSizeScale = 1.65f;

        float checkBoxSize = BUTTON_WIDTH / (donateSizeScale * 2);
        table.add(musicCheckBox).left().bottom().size(checkBoxSize, checkBoxSize).padLeft(2).padBottom(2);

        version.setFontScale(0.35f);
        table.add(buttonSupport).size(checkBoxSize, checkBoxSize)
                .bottom().right().padRight(1).padBottom(2).row();


        stage.addActor(bgImage);
        stage.addActor(table);
//        table.setDebug(true);

        Gdx.input.setInputProcessor(stage);

        bgImage.setWidth(CAMERA_WIDTH * multiplier);
        bgImage.setHeight(CAMERA_HEIGHT * multiplier);
        texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

        if (ambient != null && ambient.isPlaying()) {
            ambient.pause();
        }

        if (music != null && MUSIC_ENABLED && !music.isPlaying()) {
            music.play();
        }

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
