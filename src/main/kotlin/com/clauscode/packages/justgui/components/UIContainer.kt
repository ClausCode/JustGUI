package com.clauscode.packages.justgui.components

import com.clauscode.packages.justgui.exception.ComponentAlreadyExistsException
import com.clauscode.packages.justgui.exception.ComponentNotFoundException
import com.clauscode.packages.justgui.serialize.UIComponentsSerializer
import kotlinx.serialization.Serializable
import net.minestom.server.inventory.click.ClickType

@Serializable
abstract class UIContainer(): UIComponent() {
    constructor(id: String) : this() { this.id = id }

    @Serializable(with = UIComponentsSerializer::class)
    val components: MutableMap<String, UIView> = LinkedHashMap()

    @Suppress("UNCHECKED_CAST")
    fun <T: UIView>componentById(componentId: String): T {
        val component = components[componentId]
            ?: throw ComponentNotFoundException("Component with id: $componentId does not exist!")
        return component as T
    }

    fun addComponent(component: UIView) {
        val componentId = component.id
        if(components.containsKey(componentId))
            throw ComponentAlreadyExistsException("Component with id: $componentId already exist!")
        components[componentId] = component
    }

    fun removeComponent(componentId: String) {
        components.remove(componentId)
    }

    open fun action(id: String, clickType: ClickType): Boolean {
        return true
    }
}