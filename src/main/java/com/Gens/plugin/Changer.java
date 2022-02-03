package com.Gens.plugin;

import com.Gens.plugin.events.EmulatorLoaded;
import com.eu.habbo.Emulator;
import com.eu.habbo.habbohotel.users.Habbo;
import com.eu.habbo.plugin.HabboPlugin;

public class Changer extends HabboPlugin {

    @Override
    public void onEnable() throws Exception {
        Emulator.getPluginManager().registerEvents(this, new EmulatorLoaded());
    }

    @Override
    public void onDisable() throws Exception {

    }

    @Override
    public boolean hasPermission(Habbo habbo, String s) {
        return false;
    }
}
