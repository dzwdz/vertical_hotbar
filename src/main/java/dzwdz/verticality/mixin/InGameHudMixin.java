package dzwdz.verticality.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin extends DrawableHelper {
    @Shadow private int scaledWidth;

    @Shadow private int scaledHeight;

    @Shadow protected abstract void renderHotbarItem(int i, int j, float f, PlayerEntity playerEntity, ItemStack itemStack);

    @Shadow protected abstract PlayerEntity getCameraPlayer();

    @Shadow @Final private MinecraftClient client;

    @Shadow @Final private static Identifier WIDGETS_TEX;

    public int[] verticality$getSlotPos(int i) {
        if (i == 9) return new int[]{26, 2};
        return new int[]{
            2,
            2 + i * 20
        };
    }

    @Inject(at = @At("HEAD"), cancellable = true,
            method = "Lnet/minecraft/client/gui/hud/InGameHud;renderHotbar(FLnet/minecraft/client/util/math/MatrixStack;)V")
    public void renderHotbar(float f, MatrixStack matrixStack, CallbackInfo callbackInfo) {
        PlayerEntity playerEntity = getCameraPlayer();
        if (playerEntity == null) return;

        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        client.getTextureManager().bindTexture(WIDGETS_TEX);

        for(int i = 0; i < 9; i++) {
            int[] pos = verticality$getSlotPos(i);
            drawTexture(matrixStack, pos[0], pos[1], 1+20*i, 1, 20, 20);
            if (i == playerEntity.inventory.selectedSlot)
                drawTexture(matrixStack, pos[0] - 2, pos[1] - 2, 0, 22, 24, 24);
        }

        { // selected slot overlay
            int[] pos = verticality$getSlotPos(playerEntity.inventory.selectedSlot);
            drawTexture(matrixStack, pos[0] - 2, pos[1] - 2, 0, 22, 24, 24);
        }

        { // offhand
            int[] pos = verticality$getSlotPos(9);
            drawTexture(matrixStack, pos[0] - 1, pos[1] - 2, 24, 22, 29, 24);
        }

        RenderSystem.enableRescaleNormal();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        for (int i = 0; i < 9; i++) {
            int[] pos = verticality$getSlotPos(i);
            renderHotbarItem(pos[0] + 2, pos[1] + 2, f, playerEntity, playerEntity.inventory.main.get(i));
        }

        {
            int[] pos = verticality$getSlotPos(9);
            renderHotbarItem(pos[0] + 2, pos[1] + 2, f, playerEntity, playerEntity.getOffHandStack());
        }

        RenderSystem.disableRescaleNormal();
        RenderSystem.disableBlend();

        callbackInfo.cancel();
    }

    public void verticality$drawStatusBar(MatrixStack matrixStack, int y, int u, int v, int val, int color) {
        drawTexture(matrixStack, 25, y, 16, v, 9, 9);
        drawTexture(matrixStack, 25, y, u, v, 9, 9);
        client.textRenderer.drawWithShadow(matrixStack, Integer.toString(val), 35, y+1, color);
        client.getTextureManager().bindTexture(GUI_ICONS_TEXTURE);
    }

    @Inject(at = @At("HEAD"), cancellable = true,
            method = "Lnet/minecraft/client/gui/hud/InGameHud;renderStatusBars(Lnet/minecraft/client/util/math/MatrixStack;)V")
    public void renderStatusBars(MatrixStack matrixStack, CallbackInfo callbackInfo) {
        PlayerEntity playerEntity = getCameraPlayer();
        if (playerEntity == null) return;

        int baseY = 26;
        int o = 0;

        verticality$drawStatusBar(matrixStack, baseY + o++*12, 52, 0, (int)playerEntity.getHealth(), 0xFFFFFF);
        verticality$drawStatusBar(matrixStack, baseY + o++*12, 52, 27, playerEntity.getHungerManager().getFoodLevel(), 0xFFFFFF);

        int armor = playerEntity.getArmor();
        if (armor > 0)
            verticality$drawStatusBar(matrixStack, baseY + o++*12, 34, 9, armor, 0xFFFFFF);

        int air = (playerEntity.getAir()*20)/playerEntity.getMaxAir();
        if (air < 0) air = 0;
        if (playerEntity.getAir() < playerEntity.getMaxAir())
            verticality$drawStatusBar(matrixStack, baseY + o++*12, 16, 18, air, 0xFFFFFF);

        callbackInfo.cancel();
    }
}
