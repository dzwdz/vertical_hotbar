package dzwdz.verticality.client;

import dzwdz.verticality.Vec2i;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class EntryPoint implements ClientModInitializer {
    @Override
    public void onInitializeClient() {

    }

    public static Vec2i getSlotPos(int i, int width, int height) {
        if (i == 9) return new Vec2i(width - 46, 2);
        return new Vec2i(
                width - 22,
                2 + i * 20
        );
    }

    public static Vec2i getStatusPos(int i, int width, int height) {
        return new Vec2i(
                width - 34,
                26 + i * 12
        );
    }
}
