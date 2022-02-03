package com.Gens.plugin.events;

import com.Gens.plugin.commands.UsernameChange;
import com.Gens.plugin.utils.Extras;
import com.eu.habbo.Emulator;
import com.eu.habbo.habbohotel.commands.CommandHandler;
import com.eu.habbo.plugin.EventHandler;
import com.eu.habbo.plugin.EventListener;
import com.eu.habbo.plugin.events.emulator.EmulatorLoadedEvent;

import java.io.IOException;

public class EmulatorLoaded implements EventListener {

    @EventHandler
    public static void emuLoad(EmulatorLoadedEvent event) throws IOException {
        Extras.load();

        CommandHandler.addCommand(new UsernameChange("cmd_usernameChanger", Emulator.getTexts().getValue("commands.keys.cmd_usernameChanger").split(";")));

    }

}
