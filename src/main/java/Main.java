import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;

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

            if (event.getMessage().getContentRaw().equalsIgnoreCase("!ping")) {
                cmd_ping(event);
            }
        }
    }

    private static void cmd_ping(MessageReceivedEvent event) {
        event.getChannel().sendMessage("Pong!").queue();
    }
}
