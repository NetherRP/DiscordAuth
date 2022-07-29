package fr.xen0xys.discordauth.discord.events;

import fr.xen0xys.discordauth.discord.embeds.MessageEmbed;
import fr.xen0xys.discordauth.models.Commands;
import fr.xen0xys.discordauth.utils.StatusColor;
import fr.xen0xys.xen0lib.utils.Status;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class ButtonClickListener extends ListenerAdapter {

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        super.onButtonInteraction(event);
        if(event.getComponentId().equals("da_keep")) {
            event.deferEdit().setEmbeds(new MessageEmbed(StatusColor.OK, "Account successful keeped").build()).setActionRows().complete();
        }else if(event.getComponentId().equals("da_delete")){
            Status status = Commands.deleteAccount(event.getMember().getIdLong());
            switch (status){
                case Success -> event.deferEdit().setEmbeds(new MessageEmbed(StatusColor.OK, "Account deleted").build()).setActionRows().complete();
                case NotExist -> event.deferEdit().setEmbeds(new MessageEmbed(StatusColor.Error, "Account not found").build()).setActionRows().complete();
                case SQLError -> event.deferEdit().setEmbeds(new MessageEmbed(StatusColor.Error, "Un erreur est survenue").build()).setActionRows().complete();
            }
        }
    }
}
