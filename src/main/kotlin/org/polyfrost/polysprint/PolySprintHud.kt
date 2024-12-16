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

import org.polyfrost.oneconfig.api.config.v1.annotations.Button
import org.polyfrost.oneconfig.api.config.v1.annotations.Switch
import org.polyfrost.oneconfig.api.config.v1.annotations.Text
import org.polyfrost.oneconfig.api.event.v1.eventHandler
import org.polyfrost.oneconfig.api.hud.v1.TextHud
import org.polyfrost.polysprint.PolySprint
import org.polyfrost.polyui.unit.fix

class PolySprintHud : TextHud("") {
    private var isSneaking = false
    private var isFlying = false
    private var isSprinting = false
    private var isRiding = false

    @Switch(title = "Brackets")
    private var brackets = true

    @Button(
        title = "Reset Text on ALL HUDs",
        text = "Reset"
    )
    var resetText = Runnable {
        descendingHeld = "Descending (key held)"
        descendingToggled = "Descending (toggled)"
        descending = "Descending (vanilla)"
        flying = "Flying"
        flyBoostText = "x boost"
        riding = "Riding"
        sneakHeld = "Sneaking (key held)"
        sneakToggle = "Sneaking (toggled)"
        sneak = "Sneaking (vanilla)"
        sprintHeld = "Sprinting (key held)"
        sprintToggle = "Sprinting (toggled)"
        sprint = "Sprinting (vanilla)"
    }

    @Text(
        title = "Descending Held Text",
        category = "Display",
        subcategory = "Text"
    )
    var descendingHeld = "Descending (key held)"

    @Text(
        title = "Descending Toggled Text",
        category = "Display",
        subcategory = "Text"
    )
    var descendingToggled = "Descending (toggled)"

    @Text(
        title = "Descending Text",
        category = "Display",
        subcategory = "Text"
    )
    var descending = "Descending (vanilla)"

    @Text(
        title = "Flying Text",
        category = "Display",
        subcategory = "Text"
    )
    var flying = "Flying"

    @Text(
        title = "Fly Boost Text",
        category = "Display",
        subcategory = "Text"
    )
    var flyBoostText = "x boost"

    @Text(
        title = "Riding Text",
        category = "Display",
        subcategory = "Text"
    )
    var riding = "Riding"

    @Text(
        title = "Sneak Held Text",
        category = "Display",
        subcategory = "Text"
    )
    var sneakHeld = "Sneaking (key held)"

    @Text(
        title = "Sneak Toggle Text",
        category = "Display",
        subcategory = "Text"
    )
    var sneakToggle = "Sneaking (toggled)"

    @Text(
        title = "Sneaking Text",
        category = "Display",
        subcategory = "Text"
    )
    var sneak = "Sneaking (vanilla)"

    @Text(
        title = "Sprint Held Text",
        category = "Display",
        subcategory = "Text"
    )
    var sprintHeld = "Sprinting (key held)"

    @Text(
        title = "Sprint Toggle Text",
        category = "Display",
        subcategory = "Text"
    )
    var sprintToggle = "Sprinting (toggled)"

    @Text(
        title = "Sprinting Text",
        category = "Display",
        subcategory = "Text"
    )
    var sprint = "Sprinting (vanilla)"

    override fun initialize() {
        eventHandler { _: PolySprint.SneakStart -> isSneaking = true; updateAndRecalculate() }.register()
        eventHandler { _: PolySprint.SneakEnd -> isSneaking = false; updateAndRecalculate() }.register()
        eventHandler { _: PolySprint.FlyStart -> isFlying = true; updateAndRecalculate() }.register()
        eventHandler { _: PolySprint.FlyEnd -> isFlying = false; updateAndRecalculate() }.register()
        eventHandler { _: PolySprint.RideStart -> isRiding = true; updateAndRecalculate() }.register()
        eventHandler { _: PolySprint.RideEnd -> isRiding = false; updateAndRecalculate() }.register()
        eventHandler { _: PolySprint.SprintStart -> isSprinting = true; updateAndRecalculate() }.register()
        eventHandler { _: PolySprint.SprintEnd -> isSprinting = false; updateAndRecalculate() }.register()
    }

    override fun getText(): String? {
        if (brackets) sb.append('[')
        val config = PolySprintConfig
        if (isFlying) {
            if (isSneaking) {
                if (PolySprint.sneakHeld) sb.append(descendingHeld)
                else if (config.enabled && config.toggleSprint && config.toggleSneakState) sb.append(descendingToggled)
                else sb.append(descending)
            } else {
                sb.append(flying)
                if (shouldFlyBoost()) {
                    sb.append(' ').append(config.flyBoostAmount.fix(2)).append(flyBoostText)
                }
            }
        } else if (isRiding) sb.append(riding)
        else if (isSneaking) {
            if (PolySprint.sneakHeld) sb.append(sneakHeld)
            else if (config.enabled && config.toggleSneak && config.toggleSneakState) sb.append(sneakToggle)
            else sb.append(sneak)
        } else if (isSprinting) {
            if (PolySprint.sprintHeld) sb.append(sprintHeld)
            else if (config.enabled && config.toggleSprint && config.toggleSprintState) sb.append(sprintToggle)
            else sb.append(sprint)
        }
        if (brackets) sb.append(']')
        hidden = sb.isEmpty() || (brackets && sb.length == 2)
        return null
    }

    override fun id() = "togglesprint.json"

    override fun category() = Category.PLAYER

    override fun title() = "Toggle Sprint"
}