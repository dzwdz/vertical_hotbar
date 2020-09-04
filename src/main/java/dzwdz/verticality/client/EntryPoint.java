package dzwdz.verticality.client;

import dzwdz.verticality.Vec2i;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class EntryPoint implements ClientModInitializer {
    public static final Identifier BARS = new Identifier("verticality", "textures/gui/bars.png");

    public static final boolean BAR_ALTERNATE = false;
    public static final boolean BAR_FLIP = false;
    public static final boolean BAR_BORDER = true;
    public static final boolean ITEM_TOOLTIP = true;
    public static Vec2i OFFSET = new Vec2i(-1, 1);

    @Override
    public void onInitializeClient() {

    }

    public static Vec2i getSlotPos(int i, int width, int height) {
        if (i == 9) return new Vec2i(
                width - 54 - (BAR_BORDER ? 1 : 0),
                2
        ).add(OFFSET);
        return new Vec2i(
                width - (BAR_ALTERNATE ? 30 : 22),
                2 + i * 20
        ).add(OFFSET);
    }

    public static Vec2i getStatusPos(int i, int width, int height) {
        return new Vec2i(
                width - 43 - (BAR_BORDER ? 1 : 0),
                26 + i * 12
        ).add(OFFSET);
    }

    public static Vec2i getBarPos(int width, int height) {
        return new Vec2i(
                width - (BAR_ALTERNATE ? 7 : 30) - (BAR_BORDER ? 1 : 0),
                1
        ).add(OFFSET);
    }

    public static Vec2i getLevelPos(int width, int height) {
        return new Vec2i(
                width - (BAR_ALTERNATE ? 1 : 33) - (BAR_BORDER ? 1 : 0),
                (BAR_ALTERNATE ? 185 : 174)
        ).add(OFFSET);
    }
}
