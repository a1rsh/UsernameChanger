package com.Gens.plugin.utils;

import com.eu.habbo.Emulator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Extras {

 public static void load(){
    registerTexts();
    checkDatabase();
 }

    private static void registerTexts(){
        Emulator.getTexts().register("commands.keys.cmd_usernameChanger", "changeusername");
        Emulator.getTexts().register("commands.description.cmd_usernameChanger", ":changeusername (new username)");
        Emulator.getTexts().register("usernameChanger.cmd.error.notAvailable","%newname% uygun degil, ozel karakterler kullanilamaz.");
        Emulator.getTexts().register("usernameChanger.cmd.error.alreadyTaken","%newname% zaten mevcut. Baska bir isim deneyin.");
        Emulator.getTexts().register("usernameChanger.cmd.success.succesfully","Yeni adiniz : %newname%. Hayirli olsun.");
    }

    private static boolean registerPermission(String name, String options, String defaultValue, boolean defaultReturn)
    {
        try (Connection connection = Emulator.getDatabase().getDataSource().getConnection())
        {
            try (PreparedStatement statement = connection.prepareStatement("ALTER TABLE  `permissions` ADD  `" + name +"` ENUM(  " + options + " ) NOT NULL DEFAULT  '" + defaultValue + "'"))
            {
                statement.execute();
                return true;
            }
        }
        catch (SQLException e)
        {}

        return defaultReturn;
    }

    public static void checkDatabase() {
        boolean reloadPermissions = false;
        reloadPermissions = registerPermission("cmd_usernameChanger", "'0', '1', '2'", "1", reloadPermissions);
        if (reloadPermissions)
        {
            Emulator.getGameEnvironment().getPermissionsManager().reload();
        }
    }



}
