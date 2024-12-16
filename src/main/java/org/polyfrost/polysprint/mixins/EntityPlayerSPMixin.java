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

import com.mojang.authlib.GameProfile;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.util.MovementInput;
import net.minecraft.world.World;
import org.objectweb.asm.Opcodes;
import org.polyfrost.oneconfig.api.event.v1.EventManager;
import org.polyfrost.oneconfig.api.event.v1.events.Event;
import org.polyfrost.polysprint.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityPlayerSP.class)
public abstract class EntityPlayerSPMixin extends AbstractClientPlayer {

    @Shadow public MovementInput movementInput;

    public EntityPlayerSPMixin(World worldIn, GameProfile playerProfile) {
        super(worldIn, playerProfile);
    }

    @Redirect(
            method = "onLivingUpdate",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/settings/KeyBinding;isKeyDown()Z"
            )
    )
    private boolean setSprintState(KeyBinding keyBinding) {
        return UtilsKt.shouldSetSprint(keyBinding);
    }

    @Redirect(
            method = "onLivingUpdate",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/entity/EntityPlayerSP;onGround:Z",
                    ordinal = 0,
                    opcode = Opcodes.GETFIELD
            )
    )
    private boolean redirectWTap(EntityPlayerSP instance) {
        return !PolySprintConfig.INSTANCE.enabled || !PolySprintConfig.INSTANCE.getDisableWTapSprint();
    }

    @Inject(
            method = "onLivingUpdate",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/entity/player/PlayerCapabilities;allowFlying:Z",
                    ordinal = 1
            )
    )
    private void modifyFlightSpeed(CallbackInfo ci) {
        if (UtilsKt.shouldFlyBoost()) {
            this.capabilities.setFlySpeed(0.05F * PolySprintConfig.INSTANCE.getFlyBoostAmount());
            if (this.movementInput.sneak) {
                this.motionY -= 0.15F * PolySprintConfig.INSTANCE.getFlyBoostAmount();
            }

            if (this.movementInput.jump) {
                this.motionY += 0.15F * PolySprintConfig.INSTANCE.getFlyBoostAmount();
            }
        } else {
            this.capabilities.setFlySpeed(0.05F);
        }
    }

    @Redirect(
            method = "onLivingUpdate",
            at = @At(value = "FIELD", target = "Lnet/minecraft/entity/player/PlayerCapabilities;isFlying:Z", opcode = Opcodes.PUTFIELD)
    )
    private void onSetFlying(PlayerCapabilities instance, boolean state) {
        instance.isFlying = state;
        Event ev;
        if (state) {
            ev = FlyStart.INSTANCE;
        } else {
            ev = FlyEnd.INSTANCE;
        }

        EventManager.INSTANCE.post(ev);
    }

    @Inject(method = "setSprinting", at = @At("HEAD"))
    private void onSetSprinting(boolean state, CallbackInfo ci) {
        Event ev;
        if (state) {
            ev = SprintStart.INSTANCE;
        } else {
            ev = SprintEnd.INSTANCE;
        }

        EventManager.INSTANCE.post(ev);
    }

}
