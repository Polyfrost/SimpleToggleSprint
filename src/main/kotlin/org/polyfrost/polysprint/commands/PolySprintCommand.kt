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
package org.polyfrost.polysprint.commands

import org.polyfrost.oneconfig.api.commands.v1.factories.annotated.Command
import org.polyfrost.polysprint.core.PolySprintConfig

@Command("polysprint", "sts", "togglesprint", "togglesneak", "simpletogglesprint")
class PolySprintCommand {

    @Command
    fun execCommand() {
        TODO("Not yet implemented")
        // PolySprintConfig.openGui()
    }
}