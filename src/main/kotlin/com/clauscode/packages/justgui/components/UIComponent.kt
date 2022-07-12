package com.clauscode.packages.justgui.components

import kotlinx.serialization.Serializable
import net.minestom.server.inventory.Inventory
import net.minestom.server.inventory.click.ClickType

@Serializable
abstract class UIComponent() {
    constructor(id: String) : this() { this.id = id }

    var id: String = "ui_component"
    val props: MutableMap<String, String> = HashMap()
    var isVisible: Boolean = true
    abstract fun render(inventory: Inventory)
    open fun action(slot: Int, clickType: ClickType): Boolean = true

    abstract fun decode(json: String): UIComponent
}