package dzwdz.verticality.client;

import dzwdz.verticality.Vec2i;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class EntryPoint implements ClientModInitializer {
    public static final Identifier BARS = new Identifier("verticality", "textures/gui/bars.png");

    @Override
    public void onInitializeClient() {

    }

    public static Vec2i getSlotPos(int i, int width, int height) {
        if (i == 9) return new Vec2i(width - 54, 2);
        return new Vec2i(
                width - 22,
                2 + i * 20
        );
    }

    public static Vec2i getStatusPos(int i, int width, int height) {
        return new Vec2i(
                width - 43,
                26 + i * 12
        );
    }
}
