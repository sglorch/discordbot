package me.sglorch.discordbot;


import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

class cmd_Time {
    cmd_Time(MessageReceivedEvent event) {
        System.out.println("Executing cmd_Time...");
        LocalDateTime timePoint = LocalDateTime.now();
        LocalDateTime truncatedTime = timePoint.truncatedTo(ChronoUnit.SECONDS);
        String time = truncatedTime.format(DateTimeFormatter.ofPattern("dd.MM.yyy - HH:mm:ss"));
        event.getChannel().sendMessage("The time and date is: " + time).queue();
    }
}
