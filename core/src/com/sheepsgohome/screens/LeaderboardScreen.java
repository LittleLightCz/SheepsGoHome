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
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.sheepsgohome.GameData;
import com.sheepsgohome.GameScreens;
import com.sheepsgohome.dialogs.LeaderboardResultDialog;
import com.sheepsgohome.dialogs.MessageDialog;
import com.sheepsgohome.dialogs.OkDialog;
import com.sheepsgohome.leaderboard.LeaderBoardCallback;
import com.sheepsgohome.leaderboard.LeaderBoardResult;
import com.sheepsgohome.leaderboard.LeaderBoardRow;

import static com.badlogic.gdx.graphics.Texture.TextureFilter;
import static com.sheepsgohome.GameData.CAMERA_HEIGHT;
import static com.sheepsgohome.GameData.CAMERA_WIDTH;
import static com.sheepsgohome.GameData.Loc;
import static com.sheepsgohome.GameData.leaderboard;
import static com.sheepsgohome.GameSkins.skin;

/**
 * Created by LittleLight on 11.1.2015.
 */
public class LeaderboardScreen implements Screen, LeaderBoardCallback, MessageDialog.CancelAction {

    private static final float BUTTON_WIDTH = 100;
    private static final float BUTTON_SMALL_WIDTH = 50;
    private Stage stage;
    private Table table;

    private TextButton buttonBack;

    private Label title;

    private MessageDialog messageDialog = null;

    Texture texture;
    Image bgImage;

    private Table contentTable;

    @Override
    public void show() {
        buttonBack = new TextButton(Loc.get("back"), skin);
        title = new Label(Loc.get("leaderboard"), skin, "menuTitle");

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


        //table
        table.setFillParent(true);

        title.setFontScale(GameData.SETTINGS_TITLE_FONT_SCALE);
        table.add(title).top().row();

        contentTable = new Table();
//        contentTable.setDebug(true);

        ScrollPane scrollPane = new ScrollPane(contentTable);
        scrollPane.scrollTo(0, 0, 0, 0, false, false);

        table.add(scrollPane).size(CAMERA_WIDTH * 2, 256).expand().top().row();

        table.add(buttonBack).size(BUTTON_SMALL_WIDTH, BUTTON_SMALL_WIDTH / 2).bottom().center().padBottom(2).row();

        stage.addActor(bgImage);
        stage.addActor(table);

        Gdx.input.setInputProcessor(stage);

        bgImage.setWidth(CAMERA_WIDTH * multiplier);
        bgImage.setHeight(CAMERA_HEIGHT * multiplier);
        texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

        //update leadeboard first
        if (GameData.PLAYER_NAME.equals("")) {
            unregisteredUser();
        } else {
            GameData.leaderboard.register(
                    GameData.functions.getDeviceId(),
                    GameData.PLAYER_NAME,
                    GameData.LEVEL,
                    GameData.functions.getCountryCode(),
                    this);
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
        messageDialog.setPrefHeight(60);
        messageDialog.addCancelButton(this);
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
        dialog.setRedirectScreen(new SettingsPlayerScreen());
        dialog.show(stage);
    }


    @Override
    public void success() {
        hideMessageDialog();

        //fetch leaderboard
        if (!leaderboard.isTerminated())
            leaderboard.fetchLeaderboard(GameData.functions.getDeviceId(), this);
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
        hideMessageDialog();

        OkDialog dialog = new OkDialog(Loc.get("unregistered.player"), skin, "dialog");
        dialog.setPrefHeight(80);
        dialog.setRedirectScreen(new SettingsPlayerScreen());
        dialog.show(stage);
    }

    @Override
    public void leaderboardResult(final LeaderBoardResult result) {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                hideMessageDialog();

                float headingHeight = 20;

                contentTable.add(getTableHeaderLabel("#")).height(headingHeight);
                contentTable.add(getTableHeaderLabel(Loc.get("player"))).height(headingHeight);
                contentTable.add(getTableHeaderLabel(Loc.get("level.heading"))).height(headingHeight).row();

                for (LeaderBoardRow row : result.leaderboard) {
                    contentTable.add(getTableRowLabel(String.valueOf(row.rank)));
                    contentTable.add(getTableRowLabel(row.nick));
                    contentTable.add(getTableRowLabel(String.valueOf(row.level))).row();
                }

                Label emptyLabel = new Label("", skin);
                contentTable.add(emptyLabel).size(20, 1);
                contentTable.add(emptyLabel).size(110, 1);
                contentTable.add(emptyLabel).size(50, 1).row();


                //restore Level if it is higher
                if (result.mypos.level > GameData.LEVEL) {
                    GameData.LEVEL = result.mypos.level;
                }

                LeaderboardResultDialog dialog = new LeaderboardResultDialog(result.mypos.rank, skin, "dialog");
                dialog.setPrefHeight(70);
                dialog.show(stage);
            }
        });
    }

    private Label getTableHeaderLabel(String text) {
        Label l = new Label(text, skin, "menuTitle");
        l.setFontScale(0.25f);
        return l;
    }

    private Label getTableRowLabel(String text) {
        Label l = new Label(text, skin);
        l.setFontScale(0.32f);
        return l;
    }

    @Override
    public void CancelPressed() {
        leaderboard.setTerminated(true);
    }
}
