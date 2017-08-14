package com.sheepsgohome.dialogs;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import static com.sheepsgohome.GameData.Loc;

/**
 * Created by LittleLight on 24.3.2015.
 */
public class MessageDialog extends Dialog {

    private final float BUTTON_WIDTH = 50;
    private final float WINDOW_SIZE_MULTIPLICATOR = 1.70f;
    private Label message;
    private TextButton buttonCancel;

    private float prefHeight = 70;
    private CancelAction cancelAction;

    public MessageDialog(String message, Skin skin, String windowStyleName) {
        super("", skin, windowStyleName);
        this.message = new Label(message, skin);

//        getContentTable().setDebug(true);

        this.message.setWrap(true);
        getContentTable().add(this.message).expand().width(120).center().padTop(5).row();

        buttonCancel = new TextButton(Loc.get("cancel"), skin);
        buttonCancel.getStyle().font.setScale(0.5f);
        buttonCancel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (cancelAction != null) {
                    cancelAction.CancelPressed();
                }

                hide();
            }
        });

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

    public void addCancelButton(CancelAction action) {
        cancelAction = action;
        getContentTable().add(buttonCancel).size(BUTTON_WIDTH, BUTTON_WIDTH / 2).row();
    }

    public interface CancelAction {
        void CancelPressed();
    }
}
