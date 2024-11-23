/*
 * PolySprint - Toggle sprint and sneak with a keybind.
 *  Copyright (C) 2023  Polyfrost
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.polyfrost.polysprint

import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.InputEvent
import org.polyfrost.oneconfig.api.commands.v1.CommandManager
import org.polyfrost.oneconfig.api.hud.v1.HudManager
import org.polyfrost.polysprint.commands.PolySprintCommand
import org.polyfrost.polysprint.core.PolySprintConfig
import org.polyfrost.polysprint.core.checkKeyCode
import org.polyfrost.universal.UMinecraft

@Mod(
    modid = PolySprint.MODID,
    name = PolySprint.MOD_NAME,
    version = PolySprint.VERSION,
    clientSideOnly = true,
    modLanguageAdapter = "org.polyfrost.oneconfig.utils.v1.forge.KotlinLanguageAdapter"
)
object PolySprint {

    const val MODID = "@MOD_ID@"
    const val MOD_NAME = "@MOD_NAME@"
    const val VERSION = "@MOD_VERSION@"
    val player
        get() = UMinecraft.getPlayer()
    val gameSettings
        get() = UMinecraft.getSettings()

    var sprintHeld = false
    var sneakHeld = false

    @Mod.EventHandler
    fun onInit(event: FMLInitializationEvent) {
        println("PolySprint initialized")

        PolySprintConfig
        MinecraftForge.EVENT_BUS.register(this)
    }

    @Mod.EventHandler
    fun onPostInit(event: FMLPostInitializationEvent) {
        HudManager.register(PolySprintConfig.ToggleSprintHud())
        CommandManager.registerCommand(PolySprintCommand())
    }


    @SubscribeEvent
    fun onInput(event: InputEvent) {
        if (!PolySprintConfig.enabled) return
        val sprint = gameSettings.keyBindSprint.keyCode
        val sneak = gameSettings.keyBindSneak.keyCode
        if (!PolySprintConfig.keybindToggleSprint && checkKeyCode(sprint)) {
            if (PolySprintConfig.enabled && PolySprintConfig.toggleSprint && !sprintHeld) {
                PolySprintConfig.toggleSprintState = !PolySprintConfig.toggleSprintState
                PolySprintConfig.save()
            }
            sprintHeld = true
        } else {
            sprintHeld = false
        }
        if (!PolySprintConfig.keybindToggleSneak && checkKeyCode(sneak)) {
            if (PolySprintConfig.enabled && PolySprintConfig.toggleSneak && !sneakHeld) {
                PolySprintConfig.toggleSneakState = !PolySprintConfig.toggleSneakState
                PolySprintConfig.save()
            }
            sneakHeld = true
        } else {
            sneakHeld = false
        }
    }

}