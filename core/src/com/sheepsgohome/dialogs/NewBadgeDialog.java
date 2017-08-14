package com.sheepsgohome.dialogs;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import static com.sheepsgohome.GameData.Loc;
/**
 * Created by LittleLight on 13.1.2015.
 */
public class NewBadgeDialog extends Dialog {

    private final float BUTTON_WIDTH = 50;
    private final float WINDOW_SIZE_MULTIPLICATOR = 1.70f;
    private final float BADGE_SIZE = 100f;
    private TextButton buttonOk;
    private Label title;
    private Label name;

    public NewBadgeDialog(String badgeName, Texture badgeTexture, Skin skin, String windowStyleName) {
        super("", skin, windowStyleName);
        title = new Label(Loc.get("new.badge.earned"), skin);
        name = new Label(badgeName, skin);
        buttonOk = new TextButton(Loc.get("ok"), skin);

        title.setFontScale(0.45f);
        getContentTable().add(title).row();
        getContentTable().add(new Image(badgeTexture)).size(BADGE_SIZE, BADGE_SIZE).row();
        getContentTable().add(name).row();

        buttonOk.getStyle().font.setScale(0.5f);
        buttonOk.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hide();
            }
        });

        getContentTable().add(buttonOk).size(BUTTON_WIDTH, BUTTON_WIDTH / 2).row();

        initialize();

//        getContentTable().setDebug(true);
//        getButtonTable().setDebug(true);

    }

    private void initialize() {
//        getButtonTable().padBottom(20);
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
        // force dialog width
        return 90 * WINDOW_SIZE_MULTIPLICATOR;
    }

    @Override
    public float getPrefHeight() {
        // force dialog height
        return 140 * WINDOW_SIZE_MULTIPLICATOR;
    }

}
