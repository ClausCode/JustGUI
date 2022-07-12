package com.clauscode.packages.justgui

import kotlin.math.max
import kotlin.math.min

class UIPosition {
    var x: Int = 0
        set(value) { field = max(0, min(8, value)) }
    var y: Int = 0
        set(value) { field = max(0, min(5, value)) }

    constructor(x: Int, y: Int) {
        this.x = x ; this.y = y
    }

    fun getSlot() = x + y * 9

    companion object {
        fun bySlot(slot: Int): UIPosition {
            val x = slot % 9
            val y = (slot - x) / 9

            return UIPosition(x, y)
        }
    }
}