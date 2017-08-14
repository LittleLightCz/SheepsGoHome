package com.sheepsgohome.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
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
import com.sheepsgohome.GameSounds;
import com.sheepsgohome.dialogs.NewBadgeDialog;

import java.util.Iterator;
import java.util.Vector;

import static com.badlogic.gdx.graphics.Texture.TextureFilter;
import static com.sheepsgohome.GameData.CAMERA_HEIGHT;
import static com.sheepsgohome.GameData.CAMERA_WIDTH;
import static com.sheepsgohome.GameData.Loc;
import static com.sheepsgohome.GameData.SOUND_ENABLED;
import static com.sheepsgohome.GameData.SOUND_VOLUME;
import static com.sheepsgohome.GameSkins.skin;

public class GameplayDialog implements Screen {


    private final int BADGES_COUNT = 12;

    private float BUTTON_WIDTH = 80;

    private GameDialogType type;

    private Stage stage;
    private Table table;

    private TextButton buttonRetry;
    private TextButton buttonNext;
    private TextButton buttonQuit;
    private Label title;

    private Texture sheep_texture;
    private Texture wolf_texture;
    private Texture texture;

    private Image imgSheep;
    private Image imgWolf;
    private Image bgImage;

    private Sound sound;

    private Vector<Texture> badges;

    public GameplayDialog(GameDialogType type) {
        float multiplier = 2;
        stage = new Stage(new StretchViewport(CAMERA_WIDTH * multiplier, CAMERA_HEIGHT * multiplier));
        table = new Table();

        sheep_texture = new Texture("sheep_success.png");
        sheep_texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);


        switch (type) {
            case typeSheepFailed_Alpha:
                wolf_texture = new Texture("wolf_alpha_fail.png");
                break;
            case typeSheepFailed_Hungry:
                wolf_texture = new Texture("wolf_hungry_fail.png");
                break;
            default:
                wolf_texture = new Texture("wolf_fail.png");
                break;
        }
        wolf_texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

        badges = new Vector<Texture>();
        //fill vector
        for (int i = 0; i < BADGES_COUNT; i++) {
            Texture t = new Texture("badges/badge" + (i + 1) + ".png");
            t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
            badges.add(t);
        }

        if (type != GameDialogType.typeSheepSucceeded) {
            buttonRetry = new TextButton(Loc.get("retry"), skin);
            imgWolf = new Image(wolf_texture);

            buttonRetry.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    ((Game) Gdx.app.getApplicationListener()).setScreen(GameScreens.gameplayClassicModeScreen);
                }
            });

        } else {
            buttonNext = new TextButton(Loc.get("next.level"), skin);
            imgSheep = new Image(sheep_texture);

            buttonNext.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    ((Game) Gdx.app.getApplicationListener()).setScreen(GameScreens.gameplayClassicModeScreen);
                }
            });

        }

        buttonQuit = new TextButton(Loc.get("quit"), skin);

        buttonQuit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(GameScreens.mainMenuScreen);
            }
        });

        //---------------------------------

        texture = new Texture("menu_background.png");
        texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

        bgImage = new Image(texture);
        bgImage.setWidth(CAMERA_WIDTH * multiplier);
        bgImage.setHeight(CAMERA_HEIGHT * multiplier);


        this.type = type;
        if (type != GameDialogType.typeSheepSucceeded) {
            title = new Label(Loc.get("sheep.has.been.caught"), skin, "menuTitle");
        } else {
            title = new Label(Loc.get("home.sweet.home"), skin, "menuTitle");
        }

        float fontScale = (CAMERA_WIDTH * multiplier - 20) / title.getPrefWidth();
        title.setFontScale(fontScale);
    }

    @Override
    public void show() {
        //table
//        table.setDebug(true);
        table.add(title).top().height(30).colspan(2).row();

        boolean showNewBadgeDialog = false;
        int badgeNo = 0;

        sound = null;

        if (type != GameDialogType.typeSheepSucceeded) {

            sound = GameSounds.soundWolfFailure;

            float mult = (CAMERA_WIDTH * 2) / imgWolf.getPrefWidth() * 0.95f;
            table.add(imgWolf).size(imgWolf.getWidth() * mult, imgWolf.getHeight() * mult).colspan(2).expand().row();

            table.add(buttonRetry).size(BUTTON_WIDTH, BUTTON_WIDTH / 2);
        } else {
            //has earned new badge?
            if ((badgeNo = getEarnedBadge(GameData.LEVEL)) > 0) {
                showNewBadgeDialog = true;
            }

            if (showNewBadgeDialog) {
                sound = GameSounds.soundNewBadge;
            } else {
                sound = GameSounds.soundSheepSuccess;
            }

            //display badges
            table.add(createBadgesTable(GameData.LEVEL)).expandX().colspan(2).row();

            GameData.LEVEL++;
            GameData.gamePreferences.putInteger("LEVEL", GameData.LEVEL);
            GameData.gamePreferences.flush();

            float mult = 0.30f;
            table.add(imgSheep).size(imgSheep.getWidth() * mult, imgSheep.getHeight() * mult).colspan(2).expand().row();

            table.add(buttonNext).size(BUTTON_WIDTH, BUTTON_WIDTH / 2);
        }


        table.add(buttonQuit).size(BUTTON_WIDTH, BUTTON_WIDTH / 2).row();
        table.setFillParent(true);

        stage.addActor(bgImage);
        stage.addActor(table);

        if (showNewBadgeDialog && badgeNo > 0) {
            new NewBadgeDialog(getBadgeName(badgeNo), badges.get(badgeNo - 1), skin, "dialog").show(stage);
        }

        Gdx.input.setInputProcessor(stage);

        if (sound != null && SOUND_ENABLED) {
            sound.play(SOUND_VOLUME);
        }
    }

    private String getBadgeName(int badgeNo) {
        switch (badgeNo) {
            case 1:
                return Loc.get("pasture");
            case 2:
                return Loc.get("threat.awareness");
            case 3:
                return Loc.get("alpha.defeater");
            case 4:
                return Loc.get("escapist");
            case 5:
                return Loc.get("agility");
            case 6:
                return Loc.get("tactician");
            case 7:
                return Loc.get("fearless");
            case 8:
                return Loc.get("sheeps.defiance");
            case 9:
                return Loc.get("limitless.courage");
            case 10:
                return Loc.get("ruthless.conspiracy");
            case 11:
                return Loc.get("wolf.apocalypse");
            case 12:
                return Loc.get("sheep.master");
            default:
                return "";
        }
    }

    private Table createBadgesTable(int level) {
        Table tab = new Table();

        int badgesCount = getBadgesCount(level);
        for (int i = 0; i < badgesCount; i++) {
            tab.add(new Image(badges.get(i))).size(28, 28).padRight(1);
            if (i == 5) {
                tab.row();
            }
        }

        return tab;
    }

    private int getBadgesCount(int level) {
        int ret = level / 10;
        return ret > BADGES_COUNT ? BADGES_COUNT : ret;
    }

    private int getEarnedBadge(int level) {
        if (level > 10 * BADGES_COUNT) {
            return 0;
        }

        return (level % 10) == 0 ? getBadgesCount(level) : 0;
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
        sheep_texture.dispose();
        wolf_texture.dispose();

        //dispose vector textures
        Iterator<Texture> it = badges.iterator();
        while (it.hasNext()) {
            it.next().dispose();
        }
        badges.clear();
    }
}


