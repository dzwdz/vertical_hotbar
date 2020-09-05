package dzwdz.verticality.client;

import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;

@Config(name = "verticality")
public class ModConfig implements ConfigData {
    public boolean enabled = true;
    public boolean flipBar = false;
    public boolean hotbarBorder = true;
    public boolean itemTooltip = true;
}
