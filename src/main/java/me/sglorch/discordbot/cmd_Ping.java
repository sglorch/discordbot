package me.sglorch.discordbot;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import java.awt.*;

class cmd_Ping {
    cmd_Ping(MessageReceivedEvent event, long ping) {
        System.out.println("Executing cmd_Ping...");
        event.getChannel().sendMessage(
                new EmbedBuilder()
                        .appendDescription("My ping is: " + ping + "ms")
                        .setFooter("ARnoBot, developed by ARnonym123", "")
                        .setColor(Color.red)
                        .setTitle("Ping to Discord API")
                        .build())
                .queue();
    }
}
