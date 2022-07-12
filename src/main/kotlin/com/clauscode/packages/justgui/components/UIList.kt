package com.clauscode.packages.justgui.components

import com.clauscode.packages.justgui.UI
import com.clauscode.packages.justgui.UIPosition
import com.clauscode.packages.justgui.serialize.UIPositionSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.decodeFromString
import net.minestom.server.inventory.Inventory
import net.minestom.server.inventory.click.ClickType
import net.minestom.server.item.ItemStack
import net.minestom.server.tag.Tag
import kotlin.math.ceil

@Serializable
open class UIList() : UIContainer() {
    constructor(id: String) : this() {
        this.id = id
    }

    @Serializable(with = UIPositionSerializer::class)
    var topLeftPosition: UIPosition = UIPosition.bySlot(0)

    @Serializable(with = UIPositionSerializer::class)
    var bottomRightPosition: UIPosition = UIPosition.bySlot(0)

    @Transient
    private var page: Int = 0

    fun currentPage() = page

    fun pageCount(): Int {
        return ceil(components.size / pageSize().toFloat()).toInt()
    }

    fun pageSize(): Int {
        return getColumns() * getRows()
    }

    private fun getRows(): Int = bottomRightPosition.x - topLeftPosition.x + 1
    private fun getColumns(): Int = bottomRightPosition.y - topLeftPosition.y + 1

    fun nextPage() {
        if (page < pageCount() - 1) {
            page++
        }
    }

    fun prevPage() {
        if (page > 0) {
            page--
        }
    }

    override fun render(inventory: Inventory) {
        val startX = topLeftPosition.x
        val startY = topLeftPosition.y

        var componentIndex: Int = page * pageSize()

        for (y in startY until getColumns()) {
            for (x in startX until getRows()) {
                val position = UIPosition(x, y)
                if (components.size > componentIndex) {
                    val componentId = components.keys.toTypedArray()[componentIndex]
                    val component = components[componentId]!!

                    if (component.isVisible) {

                        component.position = position
                        component.render(inventory)

                        val itemStack = inventory.getItemStack(position.getSlot())
                        val newStack = itemStack.withTag(Tag.String("component_id"), "$id.$componentId")
                        inventory.setItemStack(position.getSlot(), newStack)

                    } else componentIndex--
                } else {
                    inventory.setItemStack(position.getSlot(), ItemStack.AIR)
                }
                componentIndex++
            }
        }
    }

    override fun action(id: String, clickType: ClickType): Boolean {
        val component = componentById<UIView>(id)
        return component.action(component.position.getSlot(), clickType)
    }

    override fun decode(json: String): UIComponent {
        return UI.json.decodeFromString<UIList>(json)
    }
}