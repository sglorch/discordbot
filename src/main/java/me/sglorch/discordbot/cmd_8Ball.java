package me.sglorch.discordbot;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Random;

class cmd_8Ball {
    cmd_8Ball(MessageReceivedEvent event) {
        System.out.println("Executing cmd_8Ball...");
        String[] answers = {
                /*
                "Absolutely!",
                "Yes, sure.",
                "Of course!",
                "Maybe...",
                "This can't be said.",
                "No, simply no!",
                "Why are you even asking?!",
                "Are you kidding me?",
                "There is nothing that true."
                */

                //affirmative
                "It is Certain.",
                "It is decicedly so.",
                "Without a doubt.",
                "Yes - definitely.",
                "You may rely on it.",
                "As I see it, yes.",
                "Most likely.",
                "Outlook good.",
                "Yes.",
                "Signs point to yes.",

                //non-commital
                "Reply hazy, try again.",
                "Ask again later.",
                "Better not tell you now.",
                "Cannot predict now.",
                "Concentrate and ask again.",

                //negative
                "Don't count on it.",
                "My reply is no.",
                "My sources say no.",
                "Outlook not so good.",
                "Very doubtful."
        };
        int randInt = new Random().nextInt(20);
        event.getChannel().sendMessage(":8ball: " + answers[randInt]).queue();
    }
}
