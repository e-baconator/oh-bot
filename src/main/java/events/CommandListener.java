package events;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.ForumChannel;
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CommandListener extends ListenerAdapter {
	@SuppressWarnings("DataFlowIssue")
	public void onMessageReceived(MessageReceivedEvent e) {
		if(e.getMessage().getContentRaw().startsWith("!")) {
			String[] message = e.getMessage().getContentRaw().toLowerCase().substring(1).split(" ");
			if(e.getChannel() instanceof ThreadChannel channel && channel.getParentChannel().getId().equals("1414699886124728370")) {
				ForumChannel parent = channel.getParentChannel().asForumChannel();
				switch(message[0]) {
					case "claim" -> {
						if(Utils.isStaff(e.getMember())) {
							if(channel.getAppliedTags().contains(Utils.getTagByName("open", parent))) {
								// rename channel to include the TA name
								channel.getManager().setName("[TA: " + e.getMember().getNickname() + "] " + channel.getName()).queue();

								// apply correct tag
								Utils.removeTag(channel, "open");
								Utils.addTag(channel, "claimed");

								// edit bot's message
								Message botMessage = channel.retrievePinnedMessages().complete().getFirst();
								String newMessage = """
										**CLAIMED TICKET**
										<@%s> has claimed this ticket.
										
										TAs: To unclaim this ticket, run `!unclaim` in this channel.
										TAs and OP: To close this ticket, run `!close` in this channel.
										
										%s
										""".formatted(e.getAuthor().getId(), e.getAuthor().getId());
								botMessage.editMessage(newMessage).queue();

								e.getMessage().reply("You have successfully claimed this ticket!").mentionRepliedUser(false).queue();
							} else {
								e.getMessage().reply("This ticket has already been claimed, or is closed.").mentionRepliedUser(false).queue();
							}
						} else {
							e.getMessage().reply(":no_entry: **403 FORBIDDEN** :no_entry:\nYou do not have permission to run this command.").mentionRepliedUser(false).queue();
						}
					}
					case "unclaim" -> {
						if(Utils.isStaff(e.getMember())) {
							if(channel.getAppliedTags().contains(Utils.getTagByName("claimed", parent))) {
								Message botMessage = channel.retrievePinnedMessages().complete().getFirst();
								String[] botMessageSplit = channel.retrievePinnedMessages().complete().getFirst().getContentRaw().split("\n");
								if(botMessageSplit[botMessageSplit.length - 1].equals(e.getAuthor().getId()) || Utils.isAdmin(e.getMember())) {
									Utils.removeTag(channel, "claimed");
									Utils.addTag(channel, "open");
									String newMessage = """
									**OPEN TICKET**
									TAs: To claim this post, run `!claim` in this channel.
									OP: To close this post, run `!close` in this channel.
									""";
									botMessage.editMessage(newMessage).queue();
								} else {
									e.getMessage().reply("You are not the person who claimed this ticket!").mentionRepliedUser(false).queue();
								}
							} else {
								e.getMessage().reply("This ticket is currently unclaimed or closed.").mentionRepliedUser(false).queue();
							}
						} else {
							e.getMessage().reply(":no_entry: **403 FORBIDDEN** :no_entry:\nYou do not have permission to run this command.").mentionRepliedUser(false).queue();
						}
					}
					case "close" -> {
						if(Utils.isStaff(e.getMember())) {
							if(channel.getAppliedTags().contains(Utils.getTagByName("claimed", parent))) {
								Message botMessage = channel.retrievePinnedMessages().complete().getFirst();
								String[] botMessageSplit = channel.retrievePinnedMessages().complete().getFirst().getContentRaw().split("\n");
								if(botMessageSplit[botMessageSplit.length - 1].equals(e.getAuthor().getId()) || Utils.isAdmin(e.getMember())) {
									Utils.removeTag(channel, "claimed");
									Utils.addTag(channel, "open");
									String newMessage = """
									**OPEN TICKET**
									TAs: To claim this post, run `!claim` in this channel.
									OP: To close this post, run `!close` in this channel.
									""";
									botMessage.editMessage(newMessage).queue();
								} else {
									e.getMessage().reply("You are not the person who claimed this ticket!").mentionRepliedUser(false).queue();
								}
							} else {
								e.getMessage().reply("This ticket is currently unclaimed or closed.").mentionRepliedUser(false).queue();
							}
						} else {
							e.getMessage().reply(":no_entry: **403 FORBIDDEN** :no_entry:\nYou do not have permission to run this command.").mentionRepliedUser(false).queue();
						}
					}
					case "reopen" -> {

					}
				}
			}
			if(message[0].equals("help")) {
				e.getChannel().sendMessage("The help message will be ready in 3-5 business days").queue();
				// TODO: Add help message
			}
		}
	}
}