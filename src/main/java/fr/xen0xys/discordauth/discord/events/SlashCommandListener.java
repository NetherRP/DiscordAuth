package fr.xen0xys.discordauth.discord.events;

import fr.xen0xys.discordauth.DiscordAuth;
import fr.xen0xys.discordauth.models.Commands;
import fr.xen0xys.discordauth.models.database.AccountTable;
import fr.xen0xys.xen0lib.utils.Status;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.DisconnectEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class SlashCommandListener extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent e) {
        super.onSlashCommandInteraction(e);
        if(e.getName().equals("account")){
            this.accountCommand(e);
        }else if(e.getName().equals("adminaccount")){
            this.adminAccountCommand(e);
        }else if(e.getName().equals("getid")){
            AccountTable accountTable = DiscordAuth.getAccountTable();
            long id = accountTable.getDiscordIdFromMinecraftName(e.getOption("username").getAsString());
            if(id == -1) {
                e.reply("No account found for this username").setEphemeral(true).complete();
            }else{
                e.reply("Account found for this username : " + id).setEphemeral(true).complete();
            }
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void accountCommand(SlashCommandInteractionEvent e){
        switch (Objects.requireNonNull(e.getSubcommandName())){
            case "create":
                String minecraftName = e.getOption("username").getAsString();
                long discordId = e.getMember().getIdLong();
                String password = e.getOption("password").getAsString();
                if(password.equals(e.getOption("confirm").getAsString())){
                    Status result = Commands.createAccount(minecraftName, discordId, password);
                    switch (result){
                        case Success -> e.reply("Account created successfully").setEphemeral(true).complete();
                        case Invalid -> e.reply("Username or password is invalid").setEphemeral(true).complete();
                        case Exist -> e.reply("Account already exist").setEphemeral(true).complete();
                        case SQLError -> e.reply("Un erreur est survenue").setEphemeral(true).complete();
                    }
                }else{
                    e.reply("Les mots de passe ne sont pas identiques").setEphemeral(true).complete();
                }
                break;
            case "manage":
                if(e.getOption("password") != null){
                    password = e.getOption("password").getAsString();
                    Status result = Commands.changePassword(e.getMember().getIdLong(), password);
                    switch (result) {
                        case Success -> e.reply("Password changed successfully").setEphemeral(true).complete();
                        case Invalid -> e.reply("Invalid password").setEphemeral(true).complete();
                        case SQLError -> e.reply("Un erreur est survenue").setEphemeral(true).complete();
                    }
                }
                break;
            case "delete":
                e.reply("Voulez vous vraiment supprimer votre compte?").addActionRow(
                        Button.primary("da_keep", "Keep account"),
                        Button.danger("da_delete", "Delete anyways"))
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
                    if(password.equals(e.getOption("confirm").getAsString())){
                        Status result = Commands.createAccount(minecraftName, discordId, password);
                        switch (result){
                            case Success -> e.reply("Account created successfully").setEphemeral(true).complete();
                            case Invalid -> e.reply("Username or password is invalid").setEphemeral(true).complete();
                            case Exist -> e.reply("Account already exist").setEphemeral(true).complete();
                            case SQLError -> e.reply("Un erreur est survenue").setEphemeral(true).complete();
                        }
                    }else{
                        e.reply("Les mots de passe ne sont pas identiques").setEphemeral(true).complete();
                    }
                    break;
                case "manage":
                    if(e.getOption("password") != null){
                        password = e.getOption("password").getAsString();
                        Status result = Commands.changePassword(e.getOption("discordid").getAsLong(), password);
                        switch (result) {
                            case Success -> e.reply("Password changed successfully").setEphemeral(true).complete();
                            case Invalid -> e.reply("Invalid password").setEphemeral(true).complete();
                            case SQLError -> e.reply("Un erreur est survenue").setEphemeral(true).complete();
                        }
                    }
                    break;
                case "delete":
                    Status status = Commands.deleteAccount(Long.parseLong(e.getOption("discordid").getAsString()));
                    switch (status){
                        case NotExist -> e.reply("Account not found").setEphemeral(true).complete();
                        case SQLError -> e.reply("Un erreur est survenue").setEphemeral(true).complete();
                        case Success -> e.reply("Account deleted").setEphemeral(true).complete();
                    }
                    break;
            }
        }else{
            e.reply("Vous n'avez pas la permission d'utiliser cette commande").setEphemeral(true).complete();
        }

    }
}
