package tk.arno.discordbot;

import com.sedmelluq.discord.lavaplayer.jdaudp.NativeAudioSendFactory;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.PlayerLibrary;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.JDAInfo;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import io.sentry.Sentry;


import org.sqlite.SQLiteJDBCLoader;

import javax.security.auth.login.LoginException;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

import static java.lang.System.currentTimeMillis;

public class Main extends ListenerAdapter {

    private static final String VERSION = "1.3-SNAPSHOT";
    private static final Long startTime = currentTimeMillis();
    private static int executedCmds = 0;
    public static Logger LOG = LoggerFactory.getLogger("ARnoBot");
    private static String GIPHY_API_KEY = "";
    private static String BOT_TOKEN = "";


    private AudioPlayerManager playerManager;

    public Main() {
      Sentry.init("https://6b12b999a58e403ab346f59f2646c517@sentry.io/1478624");
        try {
            GIPHY_API_KEY = new String(Files.readAllBytes(Paths.get("giphy.txt"))).trim();
            BOT_TOKEN = new String(Files.readAllBytes(Paths.get("token.txt"))).trim();
        } catch (IOException e) {
            System.out.println("Something went wrong reading your configurations!");
            //Sentry.capture(e);
        }
        playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioSourceManagers.registerLocalSource(playerManager);
    }

    public static void main(String[] args) {
        new Main();
        try {
            JDA jda = new JDABuilder(BOT_TOKEN)
                    .addEventListener(new Main())
                    .setAudioSendFactory(new NativeAudioSendFactory())
                    .build();
            jda.awaitReady();
            System.out.println("Finished Building JDA!");
        } catch (LoginException e) {
            e.printStackTrace();
            //Sentry.capture(e);
        } catch (InterruptedException e) {
            e.printStackTrace();
            //Sentry.capture(e);
        }
    }


    @Override
    public void onReady(ReadyEvent event) {
        System.out.println("JDA is Ready!");
    }


    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        Guild guild = event.getGuild();
        if (guild.getId().equals("453564611283779585")) {
            event.getMember().getUser().openPrivateChannel().queue();
        }
    }

/*
    @Override
    public void onPrivateChannelCreate(PrivateChannelCreateEvent event) {
        if (!event.getUser().equals(event.getJDA().getSelfUser())) {
            event.getChannel().sendMessage("Hello " + event.getUser().getAsMention()).queue();
        }
    }
*/

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
                executedCmds++;
            }

            if (msg.toLowerCase().startsWith("==time")) {
                new cmd_Time(event);
                executedCmds++;
            }

            if (msg.toLowerCase().startsWith("==8ball")) {
                new cmd_8Ball(event);
                executedCmds++;
            }

            if (msg.toLowerCase().startsWith("==music")) {
                executedCmds++;
                if (! event.isFromType(ChannelType.PRIVATE) && ! event.isFromType(ChannelType.GROUP)) {
                    String[] cmdArgs = event.getMessage().getContentRaw().split(" ", 3);
                    new cmd_Music(event, cmdArgs, playerManager);
                } else {
                    event.getChannel().sendMessage("Sorry, this command can only be used inside a server!").queue();
                }
            }
            if (msg.toLowerCase().startsWith("==version")) {
                printBanner(event);
                executedCmds++;
            }

            if (msg.toLowerCase().startsWith("==stats")) {
                printStats(event);
                executedCmds++;
            }

            if (msg.toLowerCase().startsWith("==gif")) {
                String[] cmdArgs = event.getMessage().getContentRaw().split(" ", 3);
                new cmd_Gif(event, cmdArgs, GIPHY_API_KEY);
            }
        }
    }

    private static void printBanner(MessageReceivedEvent event) {
        String os = System.getProperty("os.name");
        String arch = System.getProperty("os.arch");
        event.getChannel().sendMessage(new EmbedBuilder()
                .appendDescription("ARnoBot v" + VERSION + "\n")
                .appendDescription(" | JDA " + JDAInfo.VERSION + "\n")
                .appendDescription(" | Lavaplayer " + PlayerLibrary.VERSION + "\n")
                .appendDescription(" | SQLite " + SQLiteJDBCLoader.getVersion() + "\n")
                .appendDescription(" | " + System.getProperty("sun.arch.data.model") + "-bit JVM" + "\n")
                .appendDescription(" | " + os + " " + arch + "\n")
                .setFooter("ARnoBot, developed by ARnonym123", "https://i.imgur.com/GQ3LdW5.jpg")
                .setColor(Color.red)
                .setAuthor("Versions:")
                .build()
        ).queue();
    }

    private void printStats(MessageReceivedEvent event) {
        Long runtime = System.currentTimeMillis() - startTime;

        event.getChannel().sendMessage(new EmbedBuilder()
                .setFooter("ARnoBot, developed by ARnonym123", "https://i.imgur.com/GQ3LdW5.jpg")
                .setColor(Color.red)
                .setAuthor("Stats since last restart:")
                .appendDescription("Executed commands: " + executedCmds)
                .appendDescription("Uptime: " + String.format("%02d h, %02d min, %02d sec", TimeUnit.MILLISECONDS.toHours(runtime), TimeUnit.MILLISECONDS.toMinutes(runtime), TimeUnit.MILLISECONDS.toSeconds(runtime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(runtime))))
                .build()
        ).queue();
    }

  /*
      JDA jda = ...

      jda.getTextChannelById(...)
      jda.getPrivateChannelById(...)
      jda.getUserById(...).openPrivateChannel().queue(channel -> ...)
      jda.getGuildById(...).getTextChannelById(...)
      jda.getGuildById(...).getTextChannels()
      jda.getGuildById(...).getMemberById(...).getUser().openPrivateChannel().queue(channel -> ...)
    */

}
