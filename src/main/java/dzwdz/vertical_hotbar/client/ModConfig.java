package dzwdz.vertical_hotbar.client;

import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;

@Config(name = "vertical_hotbar")
public class ModConfig implements ConfigData {
    public boolean enabled = true;
    public boolean attachToTop = false;
    public boolean flipBar = false;
    public boolean hotbarBorder = true;
    public boolean itemTooltip = true;
}