package tk.arno.discordbot;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import javax.security.auth.login.LoginException;

public class Main extends ListenerAdapter {


    public static void main(String[] args) {

        try {
            JDA jda = new JDABuilder("MzgzOTg3ODUwODcxNDM5MzYy.Dre9jg._WKs7uv9uOlZtYbR9Rz2W7uZdtc")
                    .addEventListener(new Main())
                    .build();
            jda.awaitReady();
            System.out.println("Finished Building JDA!");
        } catch (LoginException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void onReady(ReadyEvent event) {

    }


    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        JDA jda = event.getJDA();
        long responseNumber = event.getResponseNumber();


        User author = event.getAuthor();
        Message message = event.getMessage();
        MessageChannel channel = event.getChannel();


        String msg = message.getContentDisplay();


        boolean bot = author.isBot();

        if (event.isFromType(ChannelType.TEXT)) {

            Guild guild = event.getGuild();
            TextChannel textChannel = event.getTextChannel();
            Member member = event.getMember();

            String name;
            if (message.isWebhookMessage()) {
                name = author.getName();
            } else {
                name = member.getEffectiveName();
            }

            System.out.printf("(%s)[%s]<%s>: %s\n", guild.getName(), textChannel.getName(), name, msg);
        } else if (event.isFromType(ChannelType.PRIVATE)) {


            PrivateChannel privateChannel = event.getPrivateChannel();

            System.out.printf("[PRIV]<%s>: %s\n", author.getName(), msg);
        }


        if (! author.isBot()) {


            if (msg.toLowerCase().startsWith("!ping")) {
                new cmd_Ping(event);
            }

            if (msg.toLowerCase().startsWith("!time")) {
                new cmd_Time(event);
            }

            if (msg.toLowerCase().startsWith("!8ball")) {
                new cmd_8Ball(event);
            }
        }
    }

    /*
    private static void cmd_ping(MessageReceivedEvent event) {
        System.out.println("Executing cmd_ping...");
        event.getChannel().sendMessage("Pong!").queue();
    }

    private static void cmd_time(MessageReceivedEvent event) {
        System.out.println("Executing cmd_time...");
        LocalDateTime timePoint = LocalDateTime.now();
        LocalDateTime truncatedTime = timePoint.truncatedTo(ChronoUnit.SECONDS);
        String time = truncatedTime.format(DateTimeFormatter.ofPattern("dd.MM.yyy - HH:mm:ss"));
        event.getChannel().sendMessage("The time and date is: " + time).queue();
    }

    private static void cmd_8ball(MessageReceivedEvent event) {
        System.out.println("Executing cmd_ping...");
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
    */
}
