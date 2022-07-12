package com.clauscode.packages.justgui.components

import com.clauscode.packages.justgui.UI
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.decodeFromString
import net.minestom.server.inventory.click.ClickType

@Serializable
open class UIButton(): UIView() {
    constructor(id: String) : this() { this.id = id }

    @Transient
    private var actionHandler: (clickType: ClickType) -> Boolean = { _ -> true }

    fun onAction(handler: (clickType: ClickType) -> Boolean) {
        actionHandler = handler
    }

    override fun action(slot: Int, clickType: ClickType): Boolean {
        return actionHandler.invoke(clickType)
    }

    override fun decode(json: String): UIComponent {
        return UI.json.decodeFromString<UIButton>(json)
    }
}