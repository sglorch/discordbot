import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import sun.plugin2.message.Message;

import javax.security.auth.login.LoginException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Main extends ListenerAdapter {

    public static void main(String[] args) throws LoginException {
        JDABuilder builder = new JDABuilder(AccountType.BOT);
        String token = "MzgzOTg3ODUwODcxNDM5MzYy.Dre9jg._WKs7uv9uOlZtYbR9Rz2W7uZdtc";
        builder.setToken(token);
        builder.addEventListener(new Main());
        builder.buildAsync();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (!event.getAuthor().isBot()) {
            System.out.println("Message from " + event.getAuthor() + "in " + event.getChannel() + ": " + event.getMessage().getContentDisplay());

            if (event.getMessage().getContentRaw().toLowerCase().startsWith("!ping")) {
                cmd_ping(event);
            }

            if (event.getMessage().getContentRaw().toLowerCase().startsWith("!time")) {
                cmd_time(event);
            }
        }
    }

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
}
