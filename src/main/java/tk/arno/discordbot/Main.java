package tk.arno.discordbot;

import com.sedmelluq.discord.lavaplayer.jdaudp.NativeAudioSendFactory;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import javax.security.auth.login.LoginException;

public class Main extends ListenerAdapter {

    AudioPlayerManager playerManager;

    public Main() {
        playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioSourceManagers.registerLocalSource(playerManager);
    }

    public static void main(String[] args) {
        new Main();
        try {
            JDA jda = new JDABuilder("MzgzOTg3ODUwODcxNDM5MzYy.Dre9jg._WKs7uv9uOlZtYbR9Rz2W7uZdtc")
                    .addEventListener(new Main())
                    .setAudioSendFactory(new NativeAudioSendFactory())
                    .buildBlocking();
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

        User author = event.getAuthor();
        Message message = event.getMessage();

        String msg = message.getContentDisplay();

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


            if (msg.toLowerCase().startsWith("==ping")) {
                new cmd_Ping(event, jda.getPing());
            }

            if (msg.toLowerCase().startsWith("==time")) {
                new cmd_Time(event);
            }

            if (msg.toLowerCase().startsWith("==8ball")) {
                new cmd_8Ball(event);
            }

            if (msg.toLowerCase().startsWith("==music")) {
                if (!event.isFromType(ChannelType.PRIVATE) && !event.isFromType(ChannelType.GROUP)) {
                    String[] cmdArgs = event.getMessage().getContentRaw().split(" ", 3);
                    new cmd_Music(event, cmdArgs, playerManager);
                } else {
                    event.getChannel().sendMessage("Sorry, this command can only be used inside a server!").queue();
                }
            }
        }
    }
}
