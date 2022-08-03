package com.clauscode.packages.justgui.components

import com.clauscode.packages.justgui.UI
import com.clauscode.packages.justgui.UIPosition
import com.clauscode.packages.justgui.serialize.MaterialSerializer
import com.clauscode.packages.justgui.serialize.UIPositionSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.decodeFromString
import net.kyori.adventure.text.Component
import net.minestom.server.inventory.Inventory
import net.minestom.server.item.Enchantment
import net.minestom.server.item.ItemHideFlag
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material
import net.minestom.server.tag.Tag
import kotlin.math.max
import kotlin.math.min

@Serializable
open class UIView() : UIComponent() {
    constructor(id: String) : this() {
        this.id = id
    }

    var title: String = "Example View"
    var description: List<String> = emptyList()

    @Serializable(UIPositionSerializer::class)
    var position: UIPosition = UIPosition.bySlot(0)

    @Serializable(MaterialSerializer::class)
    var icon: Material = Material.STONE
    var amount: Int = 1
        set(value) {
            field = max(0, min(127, value))
        }
    var glow: Boolean = false

    @Transient
    private var renderHandler: () -> Unit = {}

    fun onRender(handler: () -> Unit) {
        renderHandler = handler
    }

    override fun render(inventory: Inventory) {
        renderHandler.invoke()
        inventory.setItemStack(position.getSlot(), createItemStack())
    }

    private fun createItemStack() =
        ItemStack.of(icon, amount)
            .withDisplayName(Component.text(putProps(title)))
            .withLore(createLore())
            .withMeta { meta ->
                if (glow) {
                    meta.enchantment(Enchantment.UNBREAKING, 1)
                }
                meta.hideFlag(ItemHideFlag.HIDE_ENCHANTS, ItemHideFlag.HIDE_ATTRIBUTES)
            }
            .withTag(Tag.String("component_id"), id)

    private fun createLore(): MutableList<Component> {
        val lore: MutableList<Component> = ArrayList()
        for (line in description) {
            lore.add(Component.text(putProps(line)))
        }
        return lore
    }

    private fun putProps(str: String): String {
        var out = str
        for(key in props.keys) {
            out = out.replace("{$key}", props[key]!!)
        }
        return out
    }

    override fun decode(json: String): UIComponent {
        return UI.json.decodeFromString<UIView>(json)
    }
}