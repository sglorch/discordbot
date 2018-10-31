package tk.arno.discordbot;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.Random;

public class cmd_8Ball {
    public cmd_8Ball(MessageReceivedEvent event) {
        System.out.println("Executing cmd_8Ball...");
        String[] answers = {
                "Absolutely!",
                "Yes, sure.",
                "Of course!",
                "Maybe...",
                "This can't be said.",
                "No, simply no!",
                "Why are you even asking?!",
                "Are you kidding me?",
                "There is nothing that true."
        };
        Integer randInt = new Random().nextInt(9);
        event.getChannel().sendMessage(answers[randInt]).queue();
    }
}
