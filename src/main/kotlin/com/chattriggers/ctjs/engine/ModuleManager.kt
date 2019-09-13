package com.chattriggers.ctjs.engine

import com.chattriggers.ctjs.Reference
import com.chattriggers.ctjs.engine.module.Module
import com.chattriggers.ctjs.minecraft.libs.FileLib
import com.chattriggers.ctjs.triggers.TriggerType
import com.chattriggers.ctjs.utils.config.Config
import com.chattriggers.ctjs.utils.console.Console
import java.io.File
import java.util.concurrent.CompletableFuture
import java.util.concurrent.atomic.AtomicInteger

object ModuleManager {
    val loaders = mutableListOf<ILoader>()
    val generalConsole = Console(null)
    var cachedModules = listOf<Module>()

    fun importModule(moduleName: String) {
        DefaultLoader.importModule(moduleName, true)
    }

    fun deleteModule(name: String): Boolean {
        if (FileLib.deleteDirectory(File(Config.modulesFolder, name))) {
            cachedModules.filter {
                return@filter it.name == name
            }
            Reference.loadCT()
            return true
        }
        return false
    }

    fun load(updateCheck: Boolean): CompletableFuture<Unit> {
        val modules = DefaultLoader.load(updateCheck)
        cachedModules = modules

        val atomicInt = AtomicInteger(0)
        val future = CompletableFuture<Unit>()

        loaders.forEach {
            it.load(modules).whenComplete { _, _ ->
                val index = atomicInt.incrementAndGet()

                if (index == loaders.size) {
                    future.complete(Unit)
                }
            }
        }

        return future
    }

    fun load(module: Module) {
        val list = mutableListOf<Module>()
        list.addAll(cachedModules)
        list.add(module)
        cachedModules = list

        loaders.forEach {
            it.loadExtra(module)
        }
    }

    fun unload() {
        loaders.forEach {
            it.clearTriggers()
        }
    }

    fun trigger(type: TriggerType, vararg arguments: Any?) {
        loaders.forEach {
            it.exec(type, *arguments)
        }
    }

    fun getConsole(language: String): Console {
        return loaders.firstOrNull {
            it.getLanguage().langName == language
        }?.console ?: generalConsole
    }
}