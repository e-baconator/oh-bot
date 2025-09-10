package events;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel;
import net.dv8tion.jda.api.events.channel.ChannelCreateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class TicketCreate extends ListenerAdapter {
	public void onChannelCreate(ChannelCreateEvent e) {
		if(e.getChannel() instanceof ThreadChannel channel && channel.getParentChannel().getId().equals("1414699886124728370")) {
			Utils.removeAndAddTag(channel, "", "open", "");
			String message = """
			**OPEN TICKET**
			TAs: To claim this post, run `!claim` in this channel.
			OP: To close this post, run `!close` in this channel.
			""";
			Message sentMessage = channel.sendMessage(message).complete();
			sentMessage.pin().queue();
		}
	}
}