package snowy.snowyaddons.client.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import snowy.snowyaddons.SnowyAddons;


public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {

        return parent -> ModConfigScreen.create(parent, SnowyAddons.CONFIG);
    }
}
