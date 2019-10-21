package me.sglorch.discordbot;

import com.sedmelluq.discord.lavaplayer.player.AudioConfiguration;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import io.sentry.Sentry;
import me.sglorch.discordbot.audio.GuildMusicManager;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.HashMap;
import java.util.Map;

class cmd_Music {
    private final Map<Long, GuildMusicManager> musicManagers;
    private MessageReceivedEvent event;
    private String[] args;
    private final AudioPlayerManager playerManager;

    cmd_Music(MessageReceivedEvent event, String[] args, AudioPlayerManager playerManager) {
        this.playerManager = playerManager;
        this.event = event;
        this.args = args;
        this.musicManagers = new HashMap<>();
        Guild guild = event.getGuild();
        /*
         * Subcommands
         */
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
        } else if (args[1].equalsIgnoreCase("leave")) {
            VoiceChannel connectedChannel = guild.getSelfMember().getVoiceState().getChannel();

            if (connectedChannel == null) {
                event.getTextChannel().sendMessage("I am not connected to any Voice Channel!").queue();
                return;
            }

            event.getGuild().getAudioManager().closeAudioConnection();
            event.getChannel().sendMessage("Successfully disconnected from the Voice Channel!").queue();
        } else if (args[1].equalsIgnoreCase("play")) {
            System.out.println("Executing music play...");
            VoiceChannel connectedChannel = guild.getSelfMember().getVoiceState().getChannel();
            VoiceChannel theirConnectedChannel = event.getMember().getVoiceState().getChannel();


            if (connectedChannel == null) {
                if (! guild.getSelfMember().hasPermission(event.getTextChannel(), Permission.VOICE_CONNECT)) {
                    event.getTextChannel().sendMessage("I don't have the permission to join a voice channel!").queue();
                    return;
                }

                if (theirConnectedChannel == null) {
                    event.getTextChannel().sendMessage("You are not connected to any Voice Channel!").queue();
                    return;
                }

                AudioManager audioManager = guild.getAudioManager();
                if (audioManager.isAttemptingToConnect()) {
                    event.getTextChannel().sendMessage("I am already trying to connect!").queue();
                }

                audioManager.openAudioConnection(theirConnectedChannel);
                audioManager.setSelfDeafened(true);
                event.getTextChannel().sendMessage("Successfully connected to your Voice Channel!").queue();
                loadAndPlay(args[2]);
            } else {
                loadAndPlay(args[2]);
            }
        } else if (args[1].equalsIgnoreCase("skip")) {
            skipTrack();
        } else if (args[1].equalsIgnoreCase("volume")) {
            setVolume(args[2]);
        } else if (args[1].equalsIgnoreCase("stop")) {
            stopTrack();
        } else if (args[1].equalsIgnoreCase("pause")) {
            if (getGuildAudioPlayer(event.getGuild()).player.isPaused()) {
                getGuildAudioPlayer(event.getGuild()).player.setPaused(false);
            } else  {
                getGuildAudioPlayer(event.getGuild()).player.setPaused(true);
            }
        }
    }


    private void loadAndPlay(final String trackUrl) {
        GuildMusicManager musicManager = getGuildAudioPlayer(event.getGuild());

        playerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                event.getChannel().sendMessage("Adding '" + track.getInfo().title + "' to the queue").queue();

                play(musicManager, track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                AudioTrack firstTrack = playlist.getSelectedTrack();

                if (firstTrack == null) {
                    firstTrack = playlist.getTracks().get(0);
                }

                event.getChannel().sendMessage("Adding to queue " + firstTrack.getInfo().title + " (first track of playlist " + playlist.getName() + ")").queue();

                play(musicManager, firstTrack);
            }

            @Override
            public void noMatches() {
                event.getChannel().sendMessage("Nothing found by '" + trackUrl + "'").queue();
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                event.getChannel().sendMessage("Could not play: " + exception.getMessage()).queue();
                Sentry.capture(exception);
            }
        });
        playerManager.getConfiguration().setOpusEncodingQuality(AudioConfiguration.OPUS_QUALITY_MAX);
        playerManager.getConfiguration().setResamplingQuality(AudioConfiguration.ResamplingQuality.HIGH);
        playerManager.setPlayerCleanupThreshold(30000);
    }

    private void play(GuildMusicManager musicManager, AudioTrack track) {
        musicManager.scheduler.queue(track);
    }

    private synchronized GuildMusicManager getGuildAudioPlayer(Guild guild) {
        long guildId = Long.parseLong(guild.getId());
        GuildMusicManager musicManager = musicManagers.get(guildId);

        if (musicManager == null) {
            musicManager = new GuildMusicManager(playerManager);
            musicManagers.put(guildId, musicManager);
        }

        guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());

        return musicManager;
    }

    private void skipTrack() {
        GuildMusicManager musicManager = getGuildAudioPlayer(event.getGuild());
        musicManager.scheduler.nextTrack();

        event.getChannel().sendMessage("Skipped to next track...").queue();
    }

    private synchronized void setVolume(String volume) {
        GuildMusicManager musicManager = getGuildAudioPlayer(event.getGuild());
        //System.out.println("Current Volume: " + musicManager.player.getVolume());
        //musicManager.getSendHandler().audioPlayer.setVolume(Integer.parseInt(volume));
        //event.getChannel().sendMessage("Set volume to " + volume + "%.");
        System.out.println("Current Volume: " + getGuildAudioPlayer(event.getGuild()).player.getVolume());
        getGuildAudioPlayer(event.getGuild()).player.setVolume(Integer.parseInt(volume));
        event.getChannel().sendMessage("Set volume to " + getGuildAudioPlayer(event.getGuild()).player.getVolume() + "%.").queue();
    }

    private void stopTrack() {
        GuildMusicManager musicManager = getGuildAudioPlayer(event.getGuild());
        musicManager.getSendHandler().audioPlayer.stopTrack();
    }
}
