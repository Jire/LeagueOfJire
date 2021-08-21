package com.leagueofjire.overlay.transparency
import com.badlogic.gdx.utils.IntMap

open class EnumLookUpWithDefault<T>(
    map: Map<Int, T>,
    private val defaultValue: T
) {
    //to get rid of type casting
    private val valueMap: IntMap<T> = IntMap(map.size)

    init {
        map.forEach { (k, v) -> valueMap.put(k, v) }
    }

    operator fun get(id: Int) = valueMap[id] ?: defaultValue
}