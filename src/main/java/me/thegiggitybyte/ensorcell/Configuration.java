package me.thegiggitybyte.ensorcell;

import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry;

@Config(name = "ensorcell")
public class Configuration implements ConfigData, ModMenuApi {
    @ConfigEntry.Category("Temporal")
    public boolean temporalEnabled = true;
    @ConfigEntry.Category("Temporal")
    public boolean temporalItemsInvuln = false;
    
    @ConfigEntry.Category("Ascension")
    public boolean ascensionEnabled = true;
    @ConfigEntry.Category("Ascension")
    public boolean ascensionUseTeleport = false;
    
    public static Configuration getInstance() {
        return AutoConfig.getConfigHolder(Configuration.class).getConfig();
    }
    
    @Override
    public void validatePostLoad() throws ValidationException {
        // TODO: validate int level ranges.
    }
    
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return (parent) -> AutoConfig.getConfigScreen(Configuration.class, parent).get();
    }
}