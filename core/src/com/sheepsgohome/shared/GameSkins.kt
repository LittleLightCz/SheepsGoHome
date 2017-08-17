package com.sheepsgohome.shared

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.scenes.scene2d.ui.Skin

object GameSkins {
    val skin by lazy {
        val generator = FreeTypeFontGenerator(Gdx.files.internal("fonts/osifont.ttf"))
        val generatorDecor = FreeTypeFontGenerator(Gdx.files.internal("fonts/akaDylanPlain.ttf"))
        val parameter = FreeTypeFontGenerator.FreeTypeFontParameter()

        Skin().apply {
            //default
            parameter.characters += "’řšžťčůňěďŽ"
            parameter.size = 30
            add("font", generator.generateFont(parameter))

            //menu
            parameter.size = 40
            add("font_menuTitle", generatorDecor.generateFont(parameter))

            getFont("font_menuTitle").region.texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
            getFont("font").region.texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)

            addRegions(TextureAtlas(Gdx.files.internal("skins/main.pack")))
            load(Gdx.files.internal("skins/skinMain.json"))

            generator.dispose()
            generatorDecor.dispose()

            //touchpad
            getRegion("touchpadbg").texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
            getRegion("selectbox").texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)

            //music on/off checkbox
            getRegion("music_on").texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
            getRegion("music_off").texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)

            //cursor
            //        skin.getRegion("text_cursor").getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        }
    }
}
