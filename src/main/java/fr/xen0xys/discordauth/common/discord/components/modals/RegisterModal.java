package fr.xen0xys.discordauth.common.discord.components.modals;

import fr.xen0xys.discordauth.common.config.language.LangField;
import fr.xen0xys.discordauth.common.database.models.Account;
import fr.xen0xys.discordauth.common.discord.Bot;
import fr.xen0xys.discordauth.common.encryption.Encryption;
import fr.xen0xys.discordauth.waterfall.DiscordAuthProxy;
import fr.xen0xys.discordjava.DJApp;
import fr.xen0xys.discordjava.components.modal.AbstractModal;
import fr.xen0xys.discordjava.components.modal.ModalField;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class RegisterModal extends AbstractModal {

    private static final ModalField username = new ModalField("username", LangField.MODAL_USERNAME_FIELD.asText(), TextInputStyle.SHORT, LangField.MODAL_USERNAME_FIELD.asText());
    private static final ModalField usernameConfirmation = new ModalField("username_confirmation", LangField.MODAL_USERNAME_CONFIRMATION_FIELD.asText(), TextInputStyle.SHORT, LangField.MODAL_USERNAME_CONFIRMATION_FIELD.asText());
    private static final ModalField password = new ModalField("password", LangField.MODAL_PASSWORD_FIELD.asText(), TextInputStyle.SHORT, LangField.MODAL_PASSWORD_FIELD.asText());
    private static final ModalField passwordConfirmation = new ModalField("password_confirmation", LangField.MODAL_USERNAME_CONFIRMATION_FIELD.asText(), TextInputStyle.SHORT, LangField.MODAL_PASSWORD_CONFIRMATION_FIELD.asText());

    public RegisterModal() {
        super("discordauth-register", LangField.MODAL_REGISTER_TITLE.asText(), username, usernameConfirmation, password, passwordConfirmation);
    }

    @Override
    public void callback(@NotNull DJApp djApp, @NotNull ModalInteractionEvent modalInteractionEvent) {
        String username = modalInteractionEvent.getValues().get(0).getAsString();
        String usernameConfirmation = modalInteractionEvent.getValues().get(1).getAsString();
        String password = modalInteractionEvent.getValues().get(2).getAsString();
        String passwordConfirmation = modalInteractionEvent.getValues().get(3).getAsString();
        if(!username.equals(usernameConfirmation)){
            modalInteractionEvent.deferReply(true).addContent(LangField.DISCORD_USERNAMES_NOT_MATCHING.asText()).queue();
            return;
        }
        if(!password.equals(passwordConfirmation)){
            modalInteractionEvent.deferReply(true).addContent(LangField.DISCORD_PASSWORDS_NOT_MATCHING.asText()).queue();
            return;
        }
        long userId = modalInteractionEvent.getUser().getIdLong();
        if(DiscordAuthProxy.getDatabaseHandler().isAccountExists(userId)){
            modalInteractionEvent.deferReply(true).addContent(LangField.DISCORD_USER_ALREADY_REGISTERED.asText()).queue();
            return;
        }
        String hashedPassword = new Encryption().hash(password);
        boolean state = DiscordAuthProxy.getDatabaseHandler().addAccount(new Account(userId, null, username, hashedPassword, null, -1));
        if(state){
            modalInteractionEvent.deferReply(true).addContent(LangField.DISCORD_ACCOUNT_CREATED.asText()).queue();
            if(Bot.getCoreConfig().isUsernameChangingEnable())
                this.changeUsername(djApp, username, userId);
        }
        else
            modalInteractionEvent.deferReply(true).addContent(LangField.DISCORD_ACCOUNT_ALREADY_EXISTS.asText()).queue();
    }

    private void changeUsername(DJApp app, String username, long userId){
        User user = app.getJDA().retrieveUserById(userId).complete();
        if(Objects.isNull(user))
            return;
        app.getJDA().getGuilds().forEach(guild -> {
            Member member = guild.retrieveMemberById(userId).complete();
            if(Objects.isNull(member))
                return;
            try{
                guild.modifyNickname(member, username).queue();
            }catch (HierarchyException e){
                app.getLogger().warning("Cannot change nickname of @" + user.getName() + " on " + guild.getName() + " because of hierarchy");
            }
        });
    }
}
