import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.MessageDecoration;

public class WhoJoinedBot {
    public static void main(String[] args) {
        DiscordApi api = new DiscordApiBuilder()
                .setToken(System.getenv().get("DISCORD_TOKEN"))
                .login().join();

        api.addServerVoiceChannelMemberJoinListener(event -> {
            String name;
            if (event.getUser().getNickname(event.getServer()).isPresent()) {
                name = event.getUser().getNickname(event.getServer()).get();
            } else {
                name = event.getUser().getName();
            }
            String channel = event.getChannel().getName();

            if (event.isMove()) return;

            new MessageBuilder()
                    .append(name, MessageDecoration.UNDERLINE)
                    .append(" が ")
                    .append(channel, MessageDecoration.BOLD)
                    .append(" に入ったよん")
                    .send(api.getTextChannelsByName(System.getenv().get("CHANNEL_NAME")).iterator().next());
        });
    }
}
