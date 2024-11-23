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

package org.polyfrost.polysprint.core

import net.minecraft.client.settings.KeyBinding
import org.lwjgl.input.Mouse
import org.polyfrost.oneconfig.api.hypixel.v1.HypixelUtils
import org.polyfrost.polysprint.PolySprint.gameSettings
import org.polyfrost.polysprint.PolySprint.player
import org.polyfrost.universal.UKeyboard
import org.polyfrost.universal.UScreen

fun shouldSetSprint(keyBinding: KeyBinding): Boolean {
    return keyBinding.isKeyDown || UScreen.currentScreen == null && PolySprintConfig.enabled && PolySprintConfig.toggleSprint && PolySprintConfig.toggleSprintState && keyBinding === gameSettings.keyBindSprint
}

fun shouldSetSneak(keyBinding: KeyBinding): Boolean {
    return keyBinding.isKeyDown || UScreen.currentScreen == null && PolySprintConfig.enabled && PolySprintConfig.toggleSneak && PolySprintConfig.toggleSneakState && keyBinding === gameSettings.keyBindSneak
}

fun shouldFlyBoost(): Boolean {
    return gameSettings.keyBindSprint.isKeyDown && PolySprintConfig.enabled && PolySprintConfig.toggleFlyBoost && player!!.capabilities.isFlying && player!!.capabilities.isCreativeMode && !HypixelUtils.isHypixel()
}

fun checkKeyCode(keyCode: Int) = if (keyCode > 0) UKeyboard.isKeyDown(keyCode) else Mouse.isButtonDown(
    keyCode + 100
)