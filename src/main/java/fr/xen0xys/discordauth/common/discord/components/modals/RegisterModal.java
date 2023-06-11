package fr.xen0xys.discordauth.common.discord.components.modals;

import fr.xen0xys.discordauth.common.database.models.Account;
import fr.xen0xys.discordauth.common.encryption.Encryption;
import fr.xen0xys.discordauth.waterfall.DiscordAuthProxy;
import fr.xen0xys.discordjava.DJApp;
import fr.xen0xys.discordjava.components.modal.AbstractModal;
import fr.xen0xys.discordjava.components.modal.ModalField;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import org.jetbrains.annotations.NotNull;

public class RegisterModal extends AbstractModal {

    private static final ModalField username = new ModalField("username", "Name", TextInputStyle.SHORT, "Username");
    private static final ModalField password = new ModalField("password", "Password", TextInputStyle.SHORT, "Password");
    private static final ModalField passwordConfirmation = new ModalField("password_confirmation", "Confirmation", TextInputStyle.SHORT, "Confirmation");

    public RegisterModal() {
        super("discordauth-register", "Register account", username, password, passwordConfirmation);
    }

    @Override
    public void callback(@NotNull DJApp djApp, @NotNull ModalInteractionEvent modalInteractionEvent) {
        String username = modalInteractionEvent.getValues().get(0).getAsString();
        String password = modalInteractionEvent.getValues().get(1).getAsString();
        String passwordConfirmation = modalInteractionEvent.getValues().get(2).getAsString();
        if(!password.equals(passwordConfirmation)){
            modalInteractionEvent.deferReply(true).addContent("Passwords do not match").queue();
            return;
        }
        String hashedPassword = new Encryption(djApp.getLogger()).hash(password);
        DiscordAuthProxy.getDatabaseHandler().addAccount(new Account(null, username, hashedPassword, null, -1));
        modalInteractionEvent.deferReply(true).addContent("Account created").queue();
    }
}
