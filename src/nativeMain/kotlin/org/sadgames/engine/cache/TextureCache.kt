package org.sadgames.engine.cache

import org.sadgames.engine.TEXTURE_CACHE_RAM_SIZE
import org.sadgames.engine.TEXTURE_CACHE_SIZE
import org.sadgames.engine.render.gl.material.textures.AbstractTexture
import org.sadgames.engine.render.gl.material.textures.AbstractTexture.Companion.loadFrom

object TextureCache: AbstractEntityCacheManager<AbstractTexture>(TEXTURE_CACHE_SIZE, TEXTURE_CACHE_RAM_SIZE, ::loadFrom)
