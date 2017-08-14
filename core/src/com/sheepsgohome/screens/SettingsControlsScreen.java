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
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.sheepsgohome.GameData;

import static com.badlogic.gdx.graphics.Texture.TextureFilter;
import static com.sheepsgohome.GameData.CAMERA_HEIGHT;
import static com.sheepsgohome.GameData.CAMERA_WIDTH;
import static com.sheepsgohome.GameData.VIRTUAL_JOYSTICK;
import static com.sheepsgohome.GameSkins.skin;
import static com.sheepsgohome.GameData.Loc;
/**
 * Created by LittleLight on 11.1.2015.
 */
public class SettingsControlsScreen implements Screen {

    private static final float BUTTON_WIDTH = 100;
    private static final float BUTTON_SMALL_WIDTH = 50;
    private Stage stage;
    private Table table;

    private TextButton buttonSave;
    private TextButton buttonBack;

    private Label title;
    private Label virtualJoystickTitle;

    Texture texture;
    Image bgImage;

    @Override
    public void show() {
        buttonSave = new TextButton(Loc.get("save"), skin);
        buttonBack = new TextButton(Loc.get("back"), skin);
        title = new Label(Loc.get("controls"), skin, "menuTitle");
        virtualJoystickTitle = new Label(Loc.get("virtual.joystick"), skin, "default");

        texture = new Texture("menu_background.png");
        bgImage = new Image(texture);

        final SelectBox<String> virtualJoystickSelectBox = new SelectBox<String>(skin);
        virtualJoystickSelectBox.setItems(Loc.get("none"), Loc.get("left"), Loc.get("right"));
        virtualJoystickSelectBox.setSelectedIndex(VIRTUAL_JOYSTICK);

        float multiplier = 2;
        stage = new Stage(new StretchViewport(CAMERA_WIDTH * multiplier, CAMERA_HEIGHT * multiplier));

        table = new Table();
//        table.setDebug(true);

        //click listeners
        buttonSave.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                VIRTUAL_JOYSTICK = virtualJoystickSelectBox.getSelectedIndex();
                GameData.SavePreferences();
                ((Game) Gdx.app.getApplicationListener()).setScreen(new SettingsScreen());
            }
        });

        buttonBack.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new SettingsScreen());
            }
        });


        //table
        table.setFillParent(true);

        title.setFontScale(GameData.SETTINGS_TITLE_FONT_SCALE);
        table.add(title).top().colspan(2).row();

        Table contentTable = new Table();
        virtualJoystickTitle.setFontScale(GameData.SETTINGS_ITEM_FONT_SCALE);
        contentTable.add(virtualJoystickTitle).expandX().padRight(2);
        contentTable.add(virtualJoystickSelectBox).expandX().width(50).height(20).row();


        table.add(contentTable).expand().colspan(2).top().row();

        table.add(buttonSave).size(BUTTON_SMALL_WIDTH, BUTTON_SMALL_WIDTH / 2).bottom().right();
        table.add(buttonBack).size(BUTTON_SMALL_WIDTH, BUTTON_SMALL_WIDTH / 2).bottom().left().row();

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
