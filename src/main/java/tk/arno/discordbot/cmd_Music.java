package tk.arno.discordbot;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.managers.AudioManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class cmd_Music {
    public cmd_Music(MessageReceivedEvent event, String[] args) {
        try {
            List<String> allowedIds = Files.readAllLines(Paths.get("admins.txt"));
            if (! allowedIds.contains(event.getAuthor().getId())) {
                return;
            }
        } catch (IOException ignored) {
        }

        Guild guild = event.getGuild();


        if (args[1].equalsIgnoreCase("join")) {
            if (! guild.getSelfMember().hasPermission(event.getTextChannel(), Permission.VOICE_CONNECT)) {
                event.getTextChannel().sendMessage("I don't have the permission to join a voice channel!").queue();
                return;
            }

            VoiceChannel connectedChannel = event.getMember().getVoiceState().getChannel();
            if (connectedChannel == null) {
                event.getTextChannel().sendMessage("You are not connected to any Voice Channel!").queue();
                return;
            }

            AudioManager audioManager = guild.getAudioManager();
            if (audioManager.isAttemptingToConnect()) {
                event.getTextChannel().sendMessage("I am already trying to connect!").queue();
            }

            audioManager.openAudioConnection(connectedChannel);
            event.getTextChannel().sendMessage("Successfully connected to your Voice Channel!").queue();
        }

        if (args[1].equalsIgnoreCase("leave")) {
            VoiceChannel connectedChannel = guild.getSelfMember().getVoiceState().getChannel();

            if (connectedChannel == null) {
                event.getTextChannel().sendMessage("I am not connected to any Voice Channel!");
                return;
            }

            event.getGuild().getAudioManager().closeAudioConnection();
            event.getChannel().sendMessage("Successfully disconnected from the Voice Channel!");
        }
    }
}
