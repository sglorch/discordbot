package tk.arno.discordbot;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import java.awt.*;

class cmd_Ping {
    cmd_Ping(MessageReceivedEvent event, long ping) {
        System.out.println("Executing cmd_Ping...");
        event.getChannel().sendMessage(
                new EmbedBuilder()
                        .appendDescription("My ping is: " + ping + "ms.")
                        .setFooter("ARnoBot, developed by ARnonym123", "https://i.imgur.com/GQ3LdW5.jpg")
                        .setColor(Color.red)
                        .setTitle("Ping to Discord API")
                        .build())
                .queue();
    }
}
