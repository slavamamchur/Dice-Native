package org.sadgames.engine.scene.items

import kotlin.system.getTimeMillis

/**
 * Created by Slava Mamchur on 03.03.2023.
 */

abstract class AbstractNode(number: Long = -1, name: String? = null, var parent: AbstractNode? = null) {
    interface ISceneObjectsTreeHandler { fun onProcessItem(item: AbstractNode?) }
    interface ISceneObjectsCondition { fun checkCondition(item: AbstractNode?): Boolean }

    val childs: MutableMap<String, AbstractNode> = HashMap()

    var itemNumber = number
        set(value) {
            val number = if (value < 0) generateNumber() else value
            for (item in childs.values) if (item.itemNumber >= number) item.itemNumber = item.itemNumber + 1
            field = number
        }

    var itemName = name; set(value) { field = value ?: generateName() }

    protected open fun generateName(): String = "ITEM_#_$itemNumber"
    protected open fun generateNumber(): Long = getTimeMillis()
    operator fun get(name: String?): AbstractNode? {
        val items: Iterator<AbstractNode> = childs.values.iterator()
        var result = childs[name]

        while (result == null && items.hasNext()) result = items.next()[name]

        return result
    }

    operator fun minusAssign(name: String?) {
        val node = this[name]

        if (node != null) {
            childs.remove(name)
            node.parent = null

            val inum = node.itemNumber
            for (item in childs.values)
                if (item.itemNumber > inum)
                    item.itemNumber = item.itemNumber - 1
        }
    }

    fun putChild(item: AbstractNode, name: String? = item.itemName, number: Long = childs.size.toLong()) {
        item.parent?.minusAssign(item.itemName)
        item.itemName = name
        item.itemNumber = number
        item.parent = this

        childs[name!!] = item
    }

    fun proceesTreeItems(itemHandler: (item: AbstractNode?) -> Unit, condition: (item: AbstractNode?) -> Boolean) {
        if (this is IDrawableItem) {
            val sortedItems = ArrayList(childs.values)
            sortedItems.sortWith(Comparator
            { i1: AbstractNode, i2: AbstractNode -> (i1.itemNumber - i2.itemNumber).toInt() })

            for (item in sortedItems)
                if (item is IDrawableItem) {
                    if (condition(item)) itemHandler(item)
                    item.proceesTreeItems(itemHandler, condition)
                }
        }
    }

}