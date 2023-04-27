package org.sadgames.engine.scene.items

import org.sadgames.engine.SceneItemType
import kotlin.system.getTimeMillis

/**
 * Created by Slava Mamchur on 03.03.2023.
 */

abstract class AbstractNode(number: Long = -1, name: String? = null, var parent: AbstractNode? = null) {
    val children: MutableMap<String, AbstractNode> = HashMap()
    abstract val type: SceneItemType

    var itemNumber = number
        set(value) {
            val number = if (value < 0) generateNumber() else value
            for (item in children.values) if (item.itemNumber >= number) item.itemNumber = item.itemNumber + 1
            field = number
        }

    var itemName = name; set(value) { field = value ?: generateName() }

    protected open fun generateName(): String = "ITEM_#_$itemNumber"
    protected open fun generateNumber(): Long = getTimeMillis()
    operator fun get(name: String?): AbstractNode? {
        val items: Iterator<AbstractNode> = children.values.iterator()
        var result = children[name]

        while (result == null && items.hasNext()) result = items.next()[name]

        return result
    }

    @Suppress("SpellCheckingInspection")
    operator fun minusAssign(name: String?) {
        val node = this[name]

        if (node != null) {
            children.remove(name)
            node.parent = null

            val inum = node.itemNumber
            for (item in children.values)
                if (item.itemNumber > inum)
                    item.itemNumber -= 1
        }
    }

    fun putChild(item: AbstractNode, name: String? = item.itemName, number: Long = children.size.toLong()) {
        item.parent?.minusAssign(item.itemName)
        item.itemName = name ?: item.itemName
        item.itemNumber = number
        item.parent = this

        children[item.itemName!!] = item
    }

    fun processTreeItems(itemHandler: (item: AbstractNode?) -> Unit, condition: (item: AbstractNode?) -> Boolean) {
        val sortedItems = ArrayList(children.values)
        sortedItems.sortWith { i1: AbstractNode, i2: AbstractNode -> (i1.itemNumber - i2.itemNumber).toInt() }

        for (item in sortedItems) {
            if (condition(item))
                itemHandler(item)
            item.processTreeItems(itemHandler, condition)
        }
    }

}