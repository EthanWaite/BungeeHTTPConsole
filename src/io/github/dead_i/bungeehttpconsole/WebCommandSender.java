package io.github.dead_i.bungeehttpconsole;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class WebCommandSender implements CommandSender {
    private final List<String> messages = new ArrayList<String>();
    private boolean sent = false;

    public List getMessages() {
        sent = true;
        return messages;
    }

    @Override
    public String getName() {
        return "CONSOLE";
    }

    @Override
    public void sendMessage(String s) {
        if (!sent) {
            messages.add(ChatColor.stripColor(s));
        }
    }

    @Override
    public void sendMessages(String... strings) {
        for (String s : strings) {
            sendMessage(s);
        }
    }

    @Override
    public void sendMessage(BaseComponent... baseComponents) {
        sendMessage(BaseComponent.toLegacyText(baseComponents));
    }

    @Override
    public void sendMessage(BaseComponent baseComponent) {
        sendMessage(baseComponent.toLegacyText());
    }

    @Override
    public Collection<String> getGroups() {
        return Collections.emptySet();
    }

    @Override
    public void addGroups(String... strings) {
        throw new UnsupportedOperationException("Console may not have groups");
    }

    @Override
    public void removeGroups(String... strings) {
        throw new UnsupportedOperationException("Console may not have groups");
    }

    @Override
    public boolean hasPermission(String s) {
        return true;
    }

    @Override
    public void setPermission(String s, boolean b) {
        throw new UnsupportedOperationException("Console has all permissions");
    }

    @Override
    public Collection<String> getPermissions() {
        return Collections.emptySet();
    }
}
