package com.clauscode.packages.justgui.builder

import com.clauscode.packages.justgui.UI
import kotlinx.serialization.decodeFromString
import net.minestom.server.entity.Player
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import java.io.InputStreamReader

class UIBuilder {
    inline fun <reified T: UI>createUI(viewer: Player, template: String = ""): T {
        var ui = UI.json.decodeFromString<T>("{}")
        ui.viewer = viewer

        if(template.isEmpty()) return ui

        val jsonTemplate = JSONParser().parse(
            InputStreamReader(
                UIBuilder::class.java.classLoader.getResourceAsStream(template) ?: return ui
            )
        ) as JSONObject
        ui = UI.json.decodeFromString(jsonTemplate.toJSONString())
        ui.viewer = viewer
        return ui
    }
}