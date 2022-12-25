package one.devos.nautical.teabridge.discord;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.minecraft.server.MinecraftServer;
import one.devos.nautical.teabridge.Config;

public class ChannelListener extends ListenerAdapter {
    public static ChannelListener INSTANCE = new ChannelListener();

    private MinecraftServer server;

    public void setServer(MinecraftServer server) {
        this.server = server;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        var author = event.getAuthor();
        if (!event.isFromGuild() || !event.getChannel().getId().equals(Config.INSTANCE.discord.channel) || author.isBot()) return;

        var playerList = server.getPlayerList();
        if (playerList != null) playerList.broadcastSystemMessage(FormattingUtils.formatMessage(event.getMessage()), false);
    }
}
