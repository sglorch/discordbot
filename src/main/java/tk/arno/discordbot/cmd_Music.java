package tk.arno.discordbot;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class cmd_Music {
    public cmd_Music(MessageReceivedEvent event) {
        try {
            List<String> allowedIds = Files.readAllLines(Paths.get("admins.txt"));
            if (! allowedIds.contains(event.getAuthor().getId())) {
                return;
            }
        } catch (IOException ignored) {
        }

        String[] command = event.getMessage().getContentDisplay().split(" ", 2);
        Guild guild = event.getGuild();
    }
}
