package com.soulblade.mod.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class ModKeyBindings {

    public static final KeyMapping ABILITY_KEY = new KeyMapping(
            "key.soulblade.ability",         // translation key
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_R,                 // default: R key
            "key.categories.soulblade"       // category in controls menu
    );
}
