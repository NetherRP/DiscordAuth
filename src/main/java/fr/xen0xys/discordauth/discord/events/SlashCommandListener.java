package fr.xen0xys.discordauth.discord.events;

import fr.xen0xys.discordauth.models.Commands;
import fr.xen0xys.xen0lib.utils.Status;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class SlashCommandListener extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        super.onSlashCommandInteraction(event);
        if(event.getCommandString().startsWith("/account")){
            this.accountCommand(event);
        }else if(event.getCommandString().startsWith("/adminaccount")){
            this.adminAccountCommand(event);
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
            case "modify":
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
                    break;
                case "modify":
                    break;
                case "delete":
                    break;
            }
        }else{
            e.reply("Vous n'avez pas la permission d'utiliser cette commande").setEphemeral(true).complete();
        }

    }
}
