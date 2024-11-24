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

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.MovementInput;
import net.minecraft.util.MovementInputFromOptions;
import org.polyfrost.oneconfig.api.event.v1.EventManager;
import org.polyfrost.oneconfig.api.event.v1.events.Event;
import org.polyfrost.polysprint.PolySprint;
import org.polyfrost.polysprint.core.UtilsKt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MovementInputFromOptions.class)
public abstract class MovementInputFromOptionsMixin extends MovementInput {

    @Unique
    private boolean polysprint$sneaking = false;

    @Redirect(
            method = "updatePlayerMoveState",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/settings/KeyBinding;isKeyDown()Z",
                    ordinal = 5
            )
    )
    private boolean setSneakState(KeyBinding keyBinding) {
        boolean state = UtilsKt.shouldSetSneak(keyBinding);
        if (state != polysprint$sneaking) {
            polysprint$sneaking = state;
            Event ev;
            if (state) ev = PolySprint.SneakStart.INSTANCE;
            else ev = PolySprint.SneakEnd.INSTANCE;
            EventManager.INSTANCE.post(ev);
        }
        return state;
    }

}
