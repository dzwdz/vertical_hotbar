package dzwdz.verticality.client;

import dzwdz.verticality.Vec2i;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class EntryPoint implements ClientModInitializer {
    public static final Identifier BARS = new Identifier("verticality", "textures/gui/bars.png");

    public static ModConfig config;

    public static Vec2i OFFSET = new Vec2i(-1, 1);

    @Override
    public void onInitializeClient() {
        AutoConfig.register(ModConfig.class, JanksonConfigSerializer::new);
        config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    }

    public static Vec2i getSlotPos(int i, int width, int height) {
        Vec2i pos;
        if (i == 9)
            pos = new Vec2i(
                width - 54 - (config.hotbarBorder ? 1 : 0),
                2
            );
        else {
            if (!config.attachToTop) i = 8-i;
            pos = new Vec2i(
                width - 22,
                2 + i * 20
            );
        }

        pos = pos.add(OFFSET);
        if (!config.attachToTop) pos = pos.invertY(height - 20);
        return pos;
    }

    public static Vec2i getStatusPos(int i, int width, int height) {
        Vec2i pos = new Vec2i(
                width - 43 - (config.hotbarBorder ? 1 : 0),
                26 + i * 12
        );

        pos = pos.add(OFFSET);
        if (!config.attachToTop) pos = pos.invertY(height - 9);
        return pos;
    }

    public static Vec2i getBarPos(int width, int height) {
        Vec2i pos = new Vec2i(
                width - 30 - (config.hotbarBorder ? 1 : 0),
                1
        );

        pos = pos.add(OFFSET);
        if (!config.attachToTop) pos = pos.invertY(height - 182);
        return pos;
    }

    public static Vec2i getLevelPos(int width, int height) {
        Vec2i pos = new Vec2i(
                width - 33 - (config.hotbarBorder ? 1 : 0),
                174
        );

        pos = pos.add(OFFSET);
        if (!config.attachToTop) pos = pos.invertY(height - 8);
        return pos;
    }
}
