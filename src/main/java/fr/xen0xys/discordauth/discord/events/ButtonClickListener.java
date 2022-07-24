package fr.xen0xys.discordauth.discord.events;

import fr.xen0xys.discordauth.models.Commands;
import fr.xen0xys.xen0lib.utils.Status;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class ButtonClickListener extends ListenerAdapter {

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        super.onButtonInteraction(event);
        if(event.getComponentId().equals("da_keep")) {
            event.deferEdit().setContent("Account successful keeped").setActionRows().complete();
        }else if(event.getComponentId().equals("da_delete")){
            Status status = Commands.deleteAccount(event.getMember().getIdLong());
            switch (status){
                case NotExist -> event.deferEdit().setContent("Account not found").setActionRows().complete();
                case SQLError -> event.deferEdit().setContent("Un erreur est survenue").setActionRows().complete();
                case Success -> event.deferEdit().setContent("Account deleted").setActionRows().complete();
            }
        }
    }
}
