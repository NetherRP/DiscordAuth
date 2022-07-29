package fr.xen0xys.discordauth.discord.events;

import fr.xen0xys.discordauth.DiscordAuth;
import fr.xen0xys.discordauth.discord.embeds.MessageEmbed;
import fr.xen0xys.discordauth.models.Commands;
import fr.xen0xys.discordauth.models.database.AccountTable;
import fr.xen0xys.discordauth.utils.StatusColor;
import fr.xen0xys.xen0lib.utils.Status;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class SlashCommandListener extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent e) {
        super.onSlashCommandInteraction(e);
        switch (e.getName()) {
            case "account" -> this.accountCommand(e);
            case "adminaccount" -> this.adminAccountCommand(e);
            case "getid" -> {
                AccountTable accountTable = DiscordAuth.getAccountTable();
                long id = accountTable.getDiscordIdFromMinecraftName(e.getOption("username").getAsString());
                if (id == -1) {
                    e.replyEmbeds(new MessageEmbed(StatusColor.Error, DiscordAuth.getLanguage().accountNotExist).build()).setEphemeral(true).complete();
                } else {
                    e.replyEmbeds(new MessageEmbed(StatusColor.OK, String.format(DiscordAuth.getLanguage().idForUsername, id)).build()).setEphemeral(true).complete();
                }
            }
            case "login" -> this.loginCommand(e);
        }
    }

    private void loginCommand(SlashCommandInteractionEvent e){
        switch (Commands.login(e.getMember().getIdLong())){
            case Success -> e.replyEmbeds(new MessageEmbed(StatusColor.OK, DiscordAuth.getLanguage().loginSuccess).build()).setEphemeral(true).complete();
            case NotExist -> e.replyEmbeds(new MessageEmbed(StatusColor.Error, DiscordAuth.getLanguage().accountNotExist).build()).setEphemeral(true).complete();
            case Error -> e.replyEmbeds(new MessageEmbed(StatusColor.Error, DiscordAuth.getLanguage().playerNotConnected).build()).setEphemeral(true).complete();
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void accountCommand(SlashCommandInteractionEvent e){
        switch (Objects.requireNonNull(e.getSubcommandName())){
            case "create":
                String minecraftName = e.getOption("username").getAsString();
                long discordId = e.getMember().getIdLong();
                String password = e.getOption("password").getAsString();
                String confirm = e.getOption("confirm").getAsString();
                switch (this.createAccount(minecraftName, discordId, password, confirm)){
                    case Success -> e.replyEmbeds(new MessageEmbed(StatusColor.OK, DiscordAuth.getLanguage().accountCreatedSuccessful).build()).setEphemeral(true).complete();
                    case Invalid -> e.replyEmbeds(new MessageEmbed(StatusColor.Error, DiscordAuth.getLanguage().invalidUsernamePassword).build()).setEphemeral(true).complete();
                    case Exist -> e.replyEmbeds(new MessageEmbed(StatusColor.Error, DiscordAuth.getLanguage().alreadyHasAccount).build()).setEphemeral(true).complete();
                    case SQLError -> e.replyEmbeds(new MessageEmbed(StatusColor.Error, DiscordAuth.getLanguage().errorOccurred).build()).setEphemeral(true).complete();
                    case Error -> e.replyEmbeds(new MessageEmbed(StatusColor.Error, DiscordAuth.getLanguage().noIdenticalPasswords).build()).setEphemeral(true).complete();
                    case Denied -> e.replyEmbeds(new MessageEmbed(StatusColor.Error, "Denied").build()).setEphemeral(true).complete();
                }
                break;
            case "manage":
                if(e.getOption("password") != null){
                    password = e.getOption("password").getAsString();
                    Status result = Commands.changePassword(e.getMember().getIdLong(), password);
                    switch (result) {
                        case Success -> e.replyEmbeds(new MessageEmbed(StatusColor.OK, DiscordAuth.getLanguage().updatedPassword).build()).setEphemeral(true).complete();
                        case Invalid -> e.replyEmbeds(new MessageEmbed(StatusColor.Error, DiscordAuth.getLanguage().invalidPassword).build()).setEphemeral(true).complete();
                        case SQLError -> e.replyEmbeds(new MessageEmbed(StatusColor.Error, DiscordAuth.getLanguage().errorOccurred).build()).setEphemeral(true).complete();
                    }
                }
                break;
            case "delete":
                e.replyEmbeds(new MessageEmbed(StatusColor.Error, DiscordAuth.getLanguage().accountDeletionConfirm).build()).addActionRow(
                        Button.primary("da_keep", DiscordAuth.getLanguage().keepAccount),
                        Button.danger("da_delete", DiscordAuth.getLanguage().deleteAccount))
                        .setEphemeral(true).complete();
                break;
        }
    }

    private void adminAccountCommand(SlashCommandInteractionEvent e){
        if(e.getMember() != null && e.getMember().getPermissions().contains(Permission.ADMINISTRATOR)){
            switch (Objects.requireNonNull(e.getSubcommandName())){
                case "create":
                    String minecraftName = e.getOption("username").getAsString();
                    long discordId = e.getOption("discordid").getAsLong();
                    String password = e.getOption("password").getAsString();
                    String confirm = e.getOption("confirm").getAsString();
                    switch (this.createAccount(minecraftName, discordId, password, confirm)){
                        case Success -> e.replyEmbeds(new MessageEmbed(StatusColor.OK, DiscordAuth.getLanguage().accountCreatedSuccessful).build()).setEphemeral(true).complete();
                        case Invalid -> e.replyEmbeds(new MessageEmbed(StatusColor.Error, DiscordAuth.getLanguage().invalidUsernamePassword).build()).setEphemeral(true).complete();
                        case Exist -> e.replyEmbeds(new MessageEmbed(StatusColor.Error, DiscordAuth.getLanguage().alreadyHasAccount).build()).setEphemeral(true).complete();
                        case SQLError -> e.replyEmbeds(new MessageEmbed(StatusColor.Error, DiscordAuth.getLanguage().errorOccurred).build()).setEphemeral(true).complete();
                        case Error -> e.replyEmbeds(new MessageEmbed(StatusColor.Error, DiscordAuth.getLanguage().invalidUsernamePassword).build()).setEphemeral(true).complete();
                    }
                    break;
                case "manage":
                    if(e.getOption("password") != null){
                        password = e.getOption("password").getAsString();
                        Status result = Commands.changePassword(e.getOption("discordid").getAsLong(), password);
                        switch (result) {
                            case Success -> e.replyEmbeds(new MessageEmbed(StatusColor.OK, DiscordAuth.getLanguage().updatedPassword).build()).setEphemeral(true).complete();
                            case Invalid -> e.replyEmbeds(new MessageEmbed(StatusColor.Error, DiscordAuth.getLanguage().invalidPassword).build()).setEphemeral(true).complete();
                            case SQLError -> e.replyEmbeds(new MessageEmbed(StatusColor.Error, DiscordAuth.getLanguage().errorOccurred).build()).setEphemeral(true).complete();
                        }
                    }
                    break;
                case "delete":
                    Status status = Commands.deleteAccount(Long.parseLong(e.getOption("discordid").getAsString()));
                    switch (status){
                        case Success -> e.replyEmbeds(new MessageEmbed(StatusColor.OK, DiscordAuth.getLanguage().accountDeleted).build()).setEphemeral(true).complete();
                        case NotExist -> e.replyEmbeds(new MessageEmbed(StatusColor.Error, DiscordAuth.getLanguage().accountNotExist).build()).setEphemeral(true).complete();
                        case SQLError -> e.replyEmbeds(new MessageEmbed(StatusColor.Error, DiscordAuth.getLanguage().errorOccurred).build()).setEphemeral(true).complete();
                    }
                    break;
            }
        }else{
            e.replyEmbeds(new MessageEmbed(StatusColor.Error, DiscordAuth.getLanguage().noPermission).build()).setEphemeral(true).complete();
        }
    }

    private Status createAccount(String minecraftName, long discordId, String password, String confirm){
        if(password.equals(confirm)) {
            return Commands.createAccount(minecraftName, discordId, password);
        }else{
            return Status.Error;
        }
    }
}
