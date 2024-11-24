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

package org.polyfrost.polysprint.mixins;

import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.world.WorldSettings;
import org.polyfrost.oneconfig.api.event.v1.EventManager;
import org.polyfrost.oneconfig.api.event.v1.events.Event;
import org.polyfrost.polysprint.PolySprint;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WorldSettings.GameType.class)
public abstract class WorldSettingsMixin {
    @Redirect(method = "configurePlayerCapabilities", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/player/PlayerCapabilities;isFlying:Z"))
    private void onSetFlying(PlayerCapabilities instance, boolean state) {
        Event ev;
        if (state) ev = PolySprint.FlyStart.INSTANCE;
        else ev = PolySprint.FlyEnd.INSTANCE;
        EventManager.INSTANCE.post(ev);
    }
}
