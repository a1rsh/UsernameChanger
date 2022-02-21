package com.Gens.plugin.commands;

import com.eu.habbo.Emulator;
import com.eu.habbo.habbohotel.commands.Command;
import com.eu.habbo.habbohotel.gameclients.GameClient;
import com.eu.habbo.habbohotel.rooms.Room;
import com.eu.habbo.habbohotel.users.Habbo;
import com.eu.habbo.messages.ServerMessage;
import com.eu.habbo.messages.outgoing.rooms.ForwardToRoomComposer;
import com.eu.habbo.messages.outgoing.rooms.users.RoomUserDataComposer;
import com.eu.habbo.messages.outgoing.users.UserDataComposer;

import java.sql.*;


public class UsernameChange extends Command {
    public UsernameChange(String permission, String[] keys) {
        super(permission, keys);
    }

    @Override
    public boolean handle(GameClient gc, String[] prm) throws Exception {
        Habbo user = gc.getHabbo();
        if(prm.length == 2){
            String exp = "[a-zA-Z0-9]+";
            String newUsername = prm[1];
            if(newUsername.matches(exp)){
                try (Connection connection = Emulator.getDatabase().getDataSource().getConnection()){
                    PreparedStatement controlUsername = connection.prepareStatement("SELECT * FROM users WHERE username = ?");
                    controlUsername.setString(1,newUsername);

                    ResultSet rs = controlUsername.executeQuery();
                    int i = 0;
                    while(rs.next()) {
                        i++;
                    }
                    if (i < 1) {

                        user.whisper(Emulator.getTexts().getValue("usernameChanger.cmd.success.succesfully").replace("%newname%",newUsername));
                        user.getHabboInfo().setUsername(newUsername);
                        gc.sendResponse(new UserDataComposer(user));
                        user.getHabboInfo().getCurrentRoom().sendComposer(new RoomUserDataComposer(user).compose());

                        Emulator.getThreading().run(() -> {
                            Room room = gc.getHabbo().getHabboInfo().getCurrentRoom();
                            if (room != null) {
                                room = Emulator.getGameEnvironment().getRoomManager().loadRoom(room.getId());
                                ServerMessage message = new ForwardToRoomComposer(room.getId()).compose();
                                user.getClient().sendResponse(message);

                                for (Room userRoom : Emulator.getGameEnvironment().getRoomManager().getRoomsForHabbo(user)) {
                                    userRoom.setOwnerName(newUsername);
                                    userRoom.setNeedsUpdate(true);
                                    userRoom.save();
                                    Emulator.getGameEnvironment().getRoomManager().unloadRoom(userRoom);
                                }

                            }
                        }, 1000);

                        PreparedStatement updateUser = connection.prepareStatement("UPDATE users SET username = ? WHERE id = ?");
                        updateUser.setString(1,newUsername);
                        updateUser.setInt(2,user.getHabboInfo().getId());
                        updateUser.executeUpdate();

                        PreparedStatement updateRooms = connection.prepareStatement("UPDATE rooms SET owner_name = ? WHERE owner_id = ?");
                        updateRooms.setString(1,newUsername);
                        updateRooms.setInt(2,user.getHabboInfo().getId());
                        updateRooms.executeUpdate();

                    } else {
                        user.whisper(Emulator.getTexts().getValue("usernameChanger.cmd.error.alreadyTaken").replace("%newname%",newUsername));
                    }
                } catch(SQLException e){
                    System.out.println(e);
                }

            } else {
                user.whisper(Emulator.getTexts().getValue("usernameChanger.cmd.error.notAvailable").replace("%newname%",newUsername));
            }

        }
        return true;
    }
}
