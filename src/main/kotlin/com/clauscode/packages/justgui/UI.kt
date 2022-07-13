package com.clauscode.packages.justgui

import com.clauscode.packages.justgui.components.UIComponent
import com.clauscode.packages.justgui.components.UIContainer
import com.clauscode.packages.justgui.exception.ComponentAlreadyExistsException
import com.clauscode.packages.justgui.exception.ComponentNotFoundException
import com.clauscode.packages.justgui.serialize.UIComponentsSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.Json
import net.minestom.server.MinecraftServer
import net.minestom.server.entity.Player
import net.minestom.server.event.EventFilter
import net.minestom.server.event.EventNode
import net.minestom.server.event.GlobalEventHandler
import net.minestom.server.event.inventory.InventoryCloseEvent
import net.minestom.server.event.inventory.InventoryPreClickEvent
import net.minestom.server.event.trait.InventoryEvent
import net.minestom.server.inventory.Inventory
import net.minestom.server.inventory.InventoryType
import net.minestom.server.tag.Tag

@Serializable
abstract class UI() {
    constructor(viewer: Player) : this() {
        this.viewer = viewer
    }

    companion object {
        @kotlin.jvm.Transient
        val json = Json { ignoreUnknownKeys = true }
    }

    @Transient
    val globalHandler: GlobalEventHandler = MinecraftServer.getGlobalEventHandler()

    @Transient
    var viewer: Player = MinecraftServer.getInstanceManager().instances.first().players.first()

    var title: String = "Example UI"
    var type: InventoryType = InventoryType.CHEST_6_ROW

    @Transient
    private lateinit var inventory: Inventory

    @Serializable(with = UIComponentsSerializer::class)
    private val components: MutableMap<String, UIComponent> = LinkedHashMap()

    @Transient
    val handler: EventNode<InventoryEvent> = EventNode.type("myEvents", EventFilter.INVENTORY)

    init {
        handler.addListener(InventoryPreClickEvent::class.java) { event -> clickInventoryEvent(event) }
        handler.addListener(InventoryCloseEvent::class.java) { event -> closeInventoryEvent(event) }
    }

    fun show() {
        onShow()
        inventory = Inventory(type, title)
        renderAll()
        globalHandler.addChild(handler)
        viewer.openInventory(inventory)
    }

    fun close() {
        onClose()
        globalHandler.removeChild(handler)
        viewer.closeInventory()
    }

    fun renderAll() {
        inventory.clear()
        for (component in components.values) {
            render(component)
        }
    }

    fun render(component: UIComponent) {
        if (component.isVisible) {
            component.render(inventory)
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : UIComponent> componentById(componentId: String): T {
        val component = components[componentId]
            ?: throw ComponentNotFoundException("Component with id: $componentId does not exist!")
        return component as T
    }

    fun addComponent(component: UIComponent) {
        val componentId = component.id
        if (components.containsKey(componentId))
            throw ComponentAlreadyExistsException("Component with id: $componentId already exist!")
        components[componentId] = component
    }

    fun removeComponent(componentId: String) {
        components.remove(componentId)
    }

    abstract fun onShow()
    abstract fun onClose()

    /* Events */
    private fun clickInventoryEvent(event: InventoryPreClickEvent) {
        if (inventory != event.inventory) return
        if (event.slot !in 0 until inventory.size) return

        event.isCancelled = true

        var componentId = event.clickedItem.getTag(Tag.String("component_id")) ?: return
        val splitId = componentId.split(".")

        if (splitId.size > 1) {
            componentId = splitId[0]
        }
        val component = componentById<UIComponent>(componentId)

        if (component.isVisible) {
            event.isCancelled = if (component is UIContainer) {
                component.action(splitId[1], event.clickType)
            } else {
                component.action(event.slot, event.clickType)
            }
        }
    }

    private fun closeInventoryEvent(event: InventoryCloseEvent) {
        if (inventory != event.inventory) return
        close()
    }
}