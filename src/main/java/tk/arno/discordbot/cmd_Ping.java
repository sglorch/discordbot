package tk.arno.discordbot;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class cmd_Ping {
    public cmd_Ping(MessageReceivedEvent event, long ping) {
        System.out.println("Executing cmd_Ping...");
        event.getChannel().sendMessage("My ping is: " + ping + "ms.").queue();
    }
}
