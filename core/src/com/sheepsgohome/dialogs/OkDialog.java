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
import com.sheepsgohome.screens.SettingsScreen;

import static com.sheepsgohome.GameData.Loc;

/**
 * Created by LittleLight on 24.3.2015.
 */
public class OkDialog extends Dialog {

    private final float BUTTON_WIDTH = 50;
    private final float WINDOW_SIZE_MULTIPLICATOR = 1.70f;
    private TextButton buttonOk;
    private Label message;

    private float prefHeight = 70;
    private SettingsPlayerScreen screen = null;

    public OkDialog(String message, Skin skin, String windowStyleName) {
        super("", skin, windowStyleName);
        this.message = new Label(message, skin);
        buttonOk = new TextButton(Loc.get("ok"), skin);

        this.message.setWrap(true);
        getContentTable().add(this.message).expand().width(120).center().padTop(5).row();

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

        getContentTable().add(buttonOk).size(BUTTON_WIDTH, BUTTON_WIDTH / 2).bottom().padBottom(5).row();

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
