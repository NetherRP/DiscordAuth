package fr.xen0xys.discordauth.common.discord.components.buttons;

import fr.xen0xys.discordauth.common.discord.components.modals.RegisterModal;
import fr.xen0xys.discordjava.DJApp;
import fr.xen0xys.discordjava.components.buttons.AbstractStaticButton;
import fr.xen0xys.discordjava.components.buttons.enums.ButtonType;
import fr.xen0xys.discordjava.components.modal.AbstractModal;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RegisterButton extends AbstractStaticButton {

    public RegisterButton() {
        super("discordauth-register", ButtonType.Success, "Register");
    }

    @Override
    public void callback(@NotNull DJApp djApp, @NotNull ButtonInteractionEvent buttonInteractionEvent) {
        AbstractModal modal = new RegisterModal();
        modal.handle(djApp.getComponentsManager(), List.of(buttonInteractionEvent.getUser().getIdLong()));
        buttonInteractionEvent.replyModal(modal.getModal()).queue();
    }
}
