package com.sheepsgohome.dialogs;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import static com.sheepsgohome.GameData.Loc;


/**
 * Created by LittleLight on 13.1.2015.
 */
public class ContinueLastGameDialog extends Dialog {

    private static final float BUTTON_WIDTH = 50;
    private TextButton buttonNo;
    private TextButton buttonYes;
    private Label label;

    public ContinueLastGameDialog(String title, Skin skin, String windowStyleName) {
        super(title, skin, windowStyleName);
        label = new Label(Loc.get("continue.last.game"), skin);
        buttonYes = new TextButton(Loc.get("yes"), skin);
        buttonNo = new TextButton(Loc.get("no"), skin);

//        skin.getRegion("dialog_bg_red").getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

//        label.getStyle().font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        label.setFontScale(0.45f);
        getContentTable().add(label).padTop(15);

        buttonYes.getStyle().font.setScale(0.5f);

        getButtonTable().add(buttonYes).size(BUTTON_WIDTH, BUTTON_WIDTH / 2);
        getButtonTable().add(buttonNo).size(BUTTON_WIDTH,BUTTON_WIDTH/2);

        setObject(buttonYes,true);
        setObject(buttonNo,false);

        initialize();

//        getContentTable().setDebug(true);
//        getButtonTable().setDebug(true);

    }

    private void initialize() {
        getButtonTable().padBottom(20);
        setModal(true);
        setMovable(false);
        setResizable(false);
    }

    @Override
    protected void result(Object object) {
        boolean result = (Boolean) object;
//        if (!result) {
//            GameData.WOLVES_COUNT = 1;
//        }
//
//        ((Game) Gdx.app.getApplicationListener()).setScreen(GameScreens.gameplayClassicModeScreen);
    }


    @Override
    public float getPrefWidth() {
        // force dialog width
        return 150;
    }

    @Override
    public float getPrefHeight() {
        // force dialog height
        return 100;
    }

}
