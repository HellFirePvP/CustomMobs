package de.hellfirepvp.chat;

import de.hellfirepvp.CustomMobs;
import javax.annotation.Nullable;
import java.util.Iterator;
import org.bukkit.entity.Player;
import java.util.ArrayList;
import java.util.List;

public class ChatController
{
    private List<ChatHandle> handlers;
    
    public ChatController() {
        this.handlers = new ArrayList<ChatHandle>();
    }
    
    @Nullable
    public ChatHandle needsHandling(final Player chatter) {
        for (final ChatHandle handler : this.handlers) {
            if (handler.needsToHandle(chatter)) {
                return handler;
            }
        }
        return null;
    }
    
    public void handleChatInput(final Player player, final ChatHandle handler, final String in) {
        handler.handle(player, in);
    }
    
    public void init() {
        this.handlers.add(CustomMobs.instance.getToolController());
        this.handlers.add(CustomMobs.instance.getDropController());
    }
    
    public interface ChatHandle
    {
        boolean needsToHandle(final Player p0);
        
        void handle(final Player p0, final String p1);
    }
}
