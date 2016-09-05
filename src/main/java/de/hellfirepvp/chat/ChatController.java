package de.hellfirepvp.chat;

import de.hellfirepvp.CustomMobs;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is part of the CustomMobs Plugin
 * The plugin can be found at: https://www.spigotmc.org/resources/custommobs.7339
 * Class: ChatController
 * Created by HellFirePvP
 * Date: 01.09.2016 / 21:08
 */
public class ChatController {

    private List<ChatHandle> handlers = new ArrayList<>();

    @Nullable
    public ChatHandle needsHandling(Player chatter) {
        for (ChatHandle handler : handlers) {
            if(handler.needsToHandle(chatter)) return handler;
        }
        return null;
    }

    public void handleChatInput(Player player, ChatHandle handler, String in) {
        handler.handle(player, in);
    }

    public void init() {
        handlers.add(CustomMobs.instance.getToolController());
        handlers.add(CustomMobs.instance.getDropController());
    }

    public static interface ChatHandle {

        public boolean needsToHandle(Player player);

        public void handle(Player player, String input);

    }

}
