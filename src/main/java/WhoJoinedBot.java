import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.channel.ServerChannel;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.MessageDecoration;
import org.javacord.api.interaction.SlashCommand;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandOption;
import org.javacord.api.interaction.SlashCommandOptionType;

import java.util.List;

public class WhoJoinedBot {
    public static void main(String[] args) {
        DiscordApi api = new DiscordApiBuilder()
                .setToken(System.getenv().get("DISCORD_TOKEN"))
                .login().join();

        SlashCommand command = SlashCommand.with("setChannel", "Set the channel bot sends messages",
                        List.of(
                                SlashCommandOption.create(SlashCommandOptionType.CHANNEL, "CHANNEL", "channel", true)
                        ))
                .createGlobal(api)
                .join();

        api.addSlashCommandCreateListener(event -> {
            SlashCommandInteraction slashCommandInteraction = event.getSlashCommandInteraction();
            ServerChannel channel = slashCommandInteraction.getFirstOptionChannelValue().orElse(null);

            slashCommandInteraction.createImmediateResponder()
                    .setContent("通知チャンネルが変更されました")
                    .respond();
        });

        api.addServerVoiceChannelMemberJoinListener(event -> {
            String user = event.getUser().getNickname(event.getServer()).get();
            String channel = event.getChannel().getName();

            if (event.isMove()) return;

            new MessageBuilder()
                    .append(user, MessageDecoration.UNDERLINE)
                    .append(" が ")
                    .append(channel, MessageDecoration.BOLD)
                    .append(" に入ったよん")
                    .send(api.getTextChannelsByName(System.getenv().get("CHANNEL_NAME")).iterator().next());
        });
    }
}
