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
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.sheepsgohome.GameData;
import com.sheepsgohome.leaderboard.LeaderBoardCallback;
import com.sheepsgohome.dialogs.MessageDialog;
import com.sheepsgohome.dialogs.OkDialog;
import com.sheepsgohome.leaderboard.LeaderBoardResult;

import static com.badlogic.gdx.graphics.Texture.TextureFilter;
import static com.sheepsgohome.GameData.CAMERA_HEIGHT;
import static com.sheepsgohome.GameData.CAMERA_WIDTH;
import static com.sheepsgohome.GameData.Loc;
import static com.sheepsgohome.GameSkins.skin;

/**
 * Created by LittleLight on 11.1.2015.
 */
public class SettingsPlayerScreen implements Screen, LeaderBoardCallback {

    private static final float BUTTON_WIDTH = 100;
    private static final float BUTTON_SMALL_WIDTH = 50;
    private Stage stage;
    private Table table;

    private TextButton buttonRegister;
    private TextButton buttonSave;
    private TextButton buttonBack;

    private TextField playerName;

    private Label title;
    private Label playerNameTitle;

    private MessageDialog messageDialog = null;

    Texture texture;
    Image bgImage;

    @Override
    public void show() {
        buttonRegister = new TextButton(Loc.get("register"), skin);
        buttonSave = new TextButton(Loc.get("save"), skin);
        buttonBack = new TextButton(Loc.get("back"), skin);
        title = new Label(Loc.get("player"), skin, "menuTitle");
        playerNameTitle = new Label(Loc.get("player.name"), skin, "default");

        playerName = new TextField("", skin);
        playerName.setMaxLength(16);
        playerName.setAlignment(1);
        playerName.setText(GameData.PLAYER_NAME);

        texture = new Texture("menu_background.png");
        bgImage = new Image(texture);

        float multiplier = 2;
        stage = new Stage(new StretchViewport(CAMERA_WIDTH * multiplier, CAMERA_HEIGHT * multiplier));

        table = new Table();
//        table.setDebug(true);

        //click listeners
        buttonSave.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameData.PLAYER_NAME = validatePlayerName();
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


        final SettingsPlayerScreen __this = this;
        buttonRegister.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                String nick = validatePlayerName();

                if (nick.equals("")) {
                    OkDialog dialog = new OkDialog(Loc.get("empty.player.name"), skin, "dialog");
                    dialog.setPrefHeight(60);
                    dialog.show(stage);
                } else {
                    GameData.leaderboard.register(
                            GameData.functions.getDeviceId(),
                            nick,
                            GameData.LEVEL,
                            GameData.functions.getCountryCode(),
                            __this);
                }
            }
        });


        //table
        table.setFillParent(true);

        title.setFontScale(GameData.SETTINGS_TITLE_FONT_SCALE);
        table.add(title).top().colspan(2).row();

        Table contentTable = new Table();
        playerNameTitle.setFontScale(GameData.SETTINGS_ITEM_FONT_SCALE);
        contentTable.add(playerNameTitle).expandX().row();
        contentTable.add(playerName).size(170, BUTTON_SMALL_WIDTH / 2).padTop(5).padBottom(5).row();

        float BUTTON_REGISTER_WIDTH = BUTTON_WIDTH;
        contentTable.add(buttonRegister).size(BUTTON_REGISTER_WIDTH, BUTTON_REGISTER_WIDTH / 2).row();


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

    private String validatePlayerName() {
        String nick = playerName.getText().replaceAll("[^\\w\\_\\-]", "");
        playerName.setText(nick);
        return nick;
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

    /**
     * LeaderBoard callback
     */
    private void hideMessageDialog() {
        if (messageDialog != null) {
            messageDialog.hide();
        }
    }

    @Override
    public void connecting() {
        messageDialog = new MessageDialog(Loc.get("connecting"), skin, "dialog");
        messageDialog.setPrefHeight(40);
        messageDialog.show(stage);
    }

    @Override
    public void connectionToDatabaseFailed() {
        hideMessageDialog();

        OkDialog dialog = new OkDialog(Loc.get("connection.to.database.failed"), skin, "dialog");
        dialog.setPrefHeight(60);
        dialog.show(stage);
    }

    @Override
    public void invalidData() {
        hideMessageDialog();

        OkDialog dialog = new OkDialog(Loc.get("invalid.data"), skin, "dialog");
        dialog.setPrefHeight(80);
        dialog.show(stage);
    }

    @Override
    public void nickAlreadyInUse() {
        hideMessageDialog();

        OkDialog dialog = new OkDialog(Loc.get("player.name.already.in.use"), skin, "dialog");
        dialog.setPrefHeight(90);
        dialog.show(stage);
    }


    @Override
    public void success() {
        hideMessageDialog();

        OkDialog dialog = new OkDialog(Loc.get("player.name.registration.success"), skin, "dialog");
        dialog.setPrefHeight(70);
        dialog.show(stage);

        GameData.PLAYER_NAME = playerName.getText();
        GameData.SavePreferences();

    }

    @Override
    public void failure() {
        hideMessageDialog();

        OkDialog dialog = new OkDialog(Loc.get("unknown.failure"), skin, "dialog");
        dialog.setPrefHeight(60);
        dialog.show(stage);
    }

    @Override
    public void failedToInitializeMD5() {
        hideMessageDialog();

        OkDialog dialog = new OkDialog(Loc.get("md5.init.failed"), skin, "dialog");
        dialog.setPrefHeight(95);
        dialog.show(stage);
    }

    @Override
    public void connectionFailed() {
        hideMessageDialog();

        OkDialog dialog = new OkDialog(Loc.get("connection.failed"), skin, "dialog");
        dialog.setPrefHeight(60);
        dialog.show(stage);
    }

    @Override
    public void connectionCanceled() {
        hideMessageDialog();

        OkDialog dialog = new OkDialog(Loc.get("connection.canceled"), skin, "dialog");
        dialog.setPrefHeight(85);
        dialog.show(stage);
    }

    @Override
    public void unregisteredUser() {

    }

    @Override
    public void leaderboardResult(LeaderBoardResult result) {

    }
}
