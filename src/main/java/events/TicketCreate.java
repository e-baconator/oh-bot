package events;

import main.Main;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel;
import net.dv8tion.jda.api.events.channel.ChannelCreateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class TicketCreate extends ListenerAdapter {
	@Override
	public void onThreadRevealed(ThreadRevealedEvent e) {
		System.out.println("=== THREAD REVEALED EVENT FIRED ===");
		ThreadChannel channel = e.getThread();
		System.out.println("Thread ID: " + channel.getId());
		System.out.println("Thread name: " + channel.getName());
		System.out.println("Parent ID: " + (channel.getParentChannel() != null ? channel.getParentChannel().getId() : "NULL"));
		System.out.println("Expected forum ID: " + Main.getForumChannelID());
		System.out.println("isForumChannel result: " + Utils.isForumChannel(channel));
		
		if(Utils.isForumChannel(channel)) {
			System.out.println("Creating ticket in thread: " + channel.getName());
			Utils.editPost(channel, "", "open", "");
			String message = """
			**OPEN TICKET**
			TAs: To claim this post, run `!claim` in this channel.
			OP: To close this post, run `!close` in this channel.
			""";
			Message sentMessage = channel.sendMessage(message).complete();
			sentMessage.pin().queue();
			System.out.println("Ticket created successfully!");
		}
	}
	/*
	public void onChannelCreate(ChannelCreateEvent e) {
		System.out.println("=== CHANNEL CREATE EVENT FIRED ===");
		System.out.println("Channel type: " + e.getChannel().getType());
		System.out.println("Channel ID: " + e.getChannel().getId());
		System.out.println("Is ThreadChannel? " + (e.getChannel() instanceof ThreadChannel));
		
		if(e.getChannel() instanceof ThreadChannel channel && Utils.isForumChannel(channel)) {
			Utils.editPost(channel, "", "open", "");
			String message = """
			**OPEN TICKET**
			TAs: To claim this post, run `!claim` in this channel.
			OP: To close this post, run `!close` in this channel.
			""";
			Message sentMessage = channel.sendMessage(message).complete();
			sentMessage.pin().queue();
		}
	}
	*/
}
