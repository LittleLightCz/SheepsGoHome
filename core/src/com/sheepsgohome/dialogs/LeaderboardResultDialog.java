package com.sheepsgohome.dialogs;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.sheepsgohome.screens.SettingsPlayerScreen;

import static com.sheepsgohome.GameData.Loc;

/**
 * Created by LittleLight on 24.3.2015.
 */
public class LeaderboardResultDialog extends Dialog {

    private final float BUTTON_WIDTH = 50;
    private final float WINDOW_SIZE_MULTIPLICATOR = 1.70f;
    private TextButton buttonOk;
    private Label title;
    private Label rank;

    private float prefHeight = 70;
    private SettingsPlayerScreen screen = null;

    public LeaderboardResultDialog(int rank, Skin skin, String windowStyleName) {
        super("", skin, windowStyleName);
        this.rank = new Label(String.valueOf(rank)+".", skin, "menuTitle");
        title = new Label(Loc.get("your.position.is"), skin);
        buttonOk = new TextButton(Loc.get("ok"), skin);

//        getContentTable().setDebug(true);

        title.setFontScale(0.40f);
        getContentTable().add(title).center().padTop(10).row();
        this.rank.setFontScale(0.5f);
        getContentTable().add(this.rank).center().row();

        buttonOk.getStyle().font.setScale(0.5f);
        buttonOk.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hide();
                if (screen != null) {
                    ((Game) Gdx.app.getApplicationListener()).setScreen(screen);
                }

            }
        });

        getContentTable().add(buttonOk).size(BUTTON_WIDTH, BUTTON_WIDTH / 2).bottom().padBottom(10).row();

        initialize();
    }

    private void initialize() {
        setModal(true);
        setMovable(false);
        setResizable(false);
    }

    @Override
    protected void result(Object object) {
        this.hide();
    }


    @Override
    public float getPrefWidth() {
        return 90 * WINDOW_SIZE_MULTIPLICATOR;
    }

    @Override
    public float getPrefHeight() {
        return prefHeight * WINDOW_SIZE_MULTIPLICATOR;
    }

    public void setPrefHeight(float height) {
        prefHeight = height;
    }

    public void setRedirectScreen(SettingsPlayerScreen screen) {
        this.screen = screen;
    }
}
