package tk.arno.discordbot;

import at.mukprojects.giphy4j.Giphy;
import at.mukprojects.giphy4j.entity.search.SearchRandom;
import at.mukprojects.giphy4j.exception.GiphyException;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class cmd_Gif {

    cmd_Gif(MessageReceivedEvent event, String[] args, String apiKey) {
        event.getChannel().sendTyping().queue();
        if (args.length == 1) {
            event.getChannel().sendMessage("Please enter a valid keyword!").queue();
        } else {
            Giphy giphy = new Giphy(apiKey);
            try {
                SearchRandom giphyData = giphy.searchRandom(args[1]);
                event.getChannel().sendMessage(giphyData.getData().getImageUrl()).queue();
                //event.getChannel().sendFile(giphyData.getData().getUrl());

            } catch (GiphyException e) {
                System.out.println(e.getMessage());
                event.getChannel().sendMessage("Something went wrong executing your command...").queue();
            }
        }


    }
}
