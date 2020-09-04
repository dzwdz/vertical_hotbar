package dzwdz.verticality.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import dzwdz.verticality.Vec2i;
import dzwdz.verticality.client.EntryPoint;
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

import static dzwdz.verticality.client.EntryPoint.*;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin extends DrawableHelper {
    @Shadow private int scaledWidth;

    @Shadow private int scaledHeight;

    @Shadow protected abstract void renderHotbarItem(int i, int j, float f, PlayerEntity playerEntity, ItemStack itemStack);

    @Shadow protected abstract PlayerEntity getCameraPlayer();

    @Shadow @Final private MinecraftClient client;

    @Shadow @Final private static Identifier WIDGETS_TEX;

    @Inject(at = @At("HEAD"), cancellable = true,
            method = "Lnet/minecraft/client/gui/hud/InGameHud;renderHotbar(FLnet/minecraft/client/util/math/MatrixStack;)V")
    public void renderHotbar(float f, MatrixStack matrixStack, CallbackInfo callbackInfo) {
        PlayerEntity playerEntity = getCameraPlayer();
        if (playerEntity == null) return;

        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);

        if (BAR_BORDER) {
            client.getTextureManager().bindTexture(BARS);
            Vec2i pos = getSlotPos(0, scaledWidth, scaledHeight);
            drawTexture(matrixStack, pos.x - 1, pos.y - 1, 20, 0, 22, 182);
        }

        client.getTextureManager().bindTexture(WIDGETS_TEX);

        for(int i = 0; i < 9; i++) {
            Vec2i pos = getSlotPos(i, scaledWidth, scaledHeight);
            drawTexture(matrixStack, pos.x, pos.y, 1+20*i, 1, 20, 20);
            if (i == playerEntity.inventory.selectedSlot)
                drawTexture(matrixStack, pos.x - 2, pos.y - 2, 0, 22, 24, 24);
        }

        { // selected slot overlay
            Vec2i pos = getSlotPos(playerEntity.inventory.selectedSlot, scaledWidth, scaledHeight);
            drawTexture(matrixStack, pos.x - 2, pos.y - 2, 0, 22, 24, 24);
        }

        { // offhand
            Vec2i pos = getSlotPos(9, scaledWidth, scaledHeight);
            drawTexture(matrixStack, pos.x - 1, pos.y - 2, 24, 22, 29, 24);
        }

        RenderSystem.enableRescaleNormal();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        for (int i = 0; i < 9; i++) {
            Vec2i pos = getSlotPos(i, scaledWidth, scaledHeight);
            renderHotbarItem(pos.x + 2, pos.y + 2, f, playerEntity, playerEntity.inventory.main.get(i));
        }

        {
            Vec2i pos = getSlotPos(9, scaledWidth, scaledHeight);
            renderHotbarItem(pos.x + 2, pos.y + 2, f, playerEntity, playerEntity.getOffHandStack());
        }

        RenderSystem.disableRescaleNormal();
        RenderSystem.disableBlend();

        callbackInfo.cancel();
    }

    public void verticality$drawStatusBar(MatrixStack matrixStack, int i, int u, int v, int val, int color) {
        Vec2i pos = getStatusPos(i, scaledWidth, scaledHeight);

        drawTexture(matrixStack, pos.x, pos.y, 16, v, 9, 9);
        drawTexture(matrixStack, pos.x, pos.y, u, v, 9, 9);
        String s = Integer.toString(val);
        int w = client.textRenderer.getWidth(s);
        client.textRenderer.drawWithShadow(matrixStack, Integer.toString(val), pos.x - 2 - w, pos.y, color);
        client.getTextureManager().bindTexture(GUI_ICONS_TEXTURE);
    }

    @Inject(at = @At("HEAD"), cancellable = true,
            method = "Lnet/minecraft/client/gui/hud/InGameHud;renderStatusBars(Lnet/minecraft/client/util/math/MatrixStack;)V")
    public void renderStatusBars(MatrixStack matrixStack, CallbackInfo callbackInfo) {
        PlayerEntity playerEntity = getCameraPlayer();
        if (playerEntity == null) return;

        int i = 0;

        verticality$drawStatusBar(matrixStack, i++, 52, 0, (int)playerEntity.getHealth(), 0xFFFFFF);
        verticality$drawStatusBar(matrixStack, i++, 52, 27, playerEntity.getHungerManager().getFoodLevel(), 0xFFFFFF);

        int armor = playerEntity.getArmor();
        if (armor > 0)
            verticality$drawStatusBar(matrixStack, i++, 34, 9, armor, 0xFFFFFF);

        int air = (playerEntity.getAir()*20)/playerEntity.getMaxAir();
        if (air < 0) air = 0;
        if (playerEntity.getAir() < playerEntity.getMaxAir())
            verticality$drawStatusBar(matrixStack, i++, 16, 18, air, 0xFFFFFF);

        callbackInfo.cancel();
    }

    @Inject(at = @At("HEAD"), cancellable = true,
            method = "Lnet/minecraft/client/gui/hud/InGameHud;renderExperienceBar(Lnet/minecraft/client/util/math/MatrixStack;I)V")
    public void renderExperienceBar(MatrixStack matrixStack, int _x, CallbackInfo callbackInfo) {
        client.getTextureManager().bindTexture(EntryPoint.BARS);

        int nextLevelXP = client.player.getNextLevelExperience();
        if (nextLevelXP > 0) {
            int n = (int)(client.player.experienceProgress * 183f);
            Vec2i pos = getBarPos(scaledWidth, scaledHeight);
            drawTexture(matrixStack, pos.x, pos.y, 15, 0, 5, 182);
            if (n > 0) {
                if (BAR_FLIP)
                    drawTexture(matrixStack, pos.x, pos.y + 182 - n, 10, 182 - n, 5, n);
                else
                    drawTexture(matrixStack, pos.x, pos.y, 10, 0, 5, n);
            }
        }

        if (client.player.experienceLevel > 0) {
            String s = "" + this.client.player.experienceLevel;
            Vec2i pos = getLevelPos(scaledWidth, scaledHeight);
            int x = pos.x - client.textRenderer.getWidth(s);
            client.textRenderer.draw(matrixStack, s, x + 1, pos.y, 0);
            client.textRenderer.draw(matrixStack, s, x - 1, pos.y, 0);
            client.textRenderer.draw(matrixStack, s, x, pos.y + 1, 0);
            client.textRenderer.draw(matrixStack, s, x, pos.y - 1, 0);
            client.textRenderer.draw(matrixStack, s, x, pos.y, 0x80FF20);
        }

        client.getTextureManager().bindTexture(GUI_ICONS_TEXTURE);
        callbackInfo.cancel();
    }
}
