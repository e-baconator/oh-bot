package events;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.ForumChannel;
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CommandListener extends ListenerAdapter {
	@SuppressWarnings("DataFlowIssue")
	public void onMessageReceived(MessageReceivedEvent e) {
		if(!e.getAuthor().isBot() && e.getMessage().getContentRaw().startsWith("!")) {
			String[] message = e.getMessage().getContentRaw().toLowerCase().substring(1).split(" ");
			if(e.getChannel() instanceof ThreadChannel threadChannel && threadChannel.getParentChannel().getId().equals("1414699886124728370")) {
				ForumChannel parent = threadChannel.getParentChannel().asForumChannel();
				switch(message[0]) {
					case "claim" -> {
						if(Utils.isStaff(e.getMember())) {
							if(threadChannel.getAppliedTags().contains(Utils.getTagByName("open", parent))) {
								// apply correct tag + rename channel to include the TA name
								String nickname = e.getMember().getNickname();
								if(nickname == null) {
									nickname = e.getMember().getUser().getEffectiveName();
								}
								Utils.removeAndAddTag(threadChannel, "open", "claimed", "[TA: " + nickname + "] " + threadChannel.getName());

								// edit bot's message
								Message botMessage = threadChannel.retrievePinnedMessages().complete().getFirst();
								String newMessage = """
										**CLAIMED TICKET**
										<@%s> has claimed this ticket.
										
										TAs: To unclaim this ticket, run `!unclaim` in this channel.
										TAs and OP: To close this ticket, run `!close` in this channel.
										
										%s
										""".formatted(e.getAuthor().getId(), e.getAuthor().getId());
								botMessage.editMessage(newMessage).queue();

								// feedback message
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
							if(threadChannel.getAppliedTags().contains(Utils.getTagByName("claimed", parent))) {
								// check if the person who claimed the ticket is the person who is running the command
								Message botMessage = threadChannel.retrievePinnedMessages().complete().getFirst();
								String[] botMessageSplit = threadChannel.retrievePinnedMessages().complete().getFirst().getContentRaw().split("\n");
								if(botMessageSplit[botMessageSplit.length - 1].equals(e.getAuthor().getId()) || Utils.isAdmin(e.getMember())) {
									// apply correct tag + remove TA name
									String currentName = threadChannel.getName();
									String newName = currentName.replaceFirst("^\\[TA: [^]]+] ", "");
									Utils.removeAndAddTag(threadChannel, "claimed", "open", newName);

									// edit bot's message
									String newMessage = """
											**OPEN TICKET**
											TAs: To claim this post, run `!claim` in this channel.
											OP: To close this post, run `!close` in this channel.
											""";
									botMessage.editMessage(newMessage).queue();

									// feedback message
									e.getMessage().reply("You have successfully unclaimed this ticket!").mentionRepliedUser(false).queue();
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
						if(Utils.isStaff(e.getMember()) || Utils.isOP(threadChannel, e.getMember())) {
							if(!threadChannel.getAppliedTags().contains(Utils.getTagByName("closed", parent))) {
								// check if the person who claimed the ticket is the person who is running the command, or the original poster
								Message botMessage = threadChannel.retrievePinnedMessages().complete().getFirst();
								String[] botMessageSplit = threadChannel.retrievePinnedMessages().complete().getFirst().getContentRaw().split("\n");
								if(botMessageSplit[botMessageSplit.length - 1].equals(e.getAuthor().getId()) || Utils.isAdmin(e.getMember()) || Utils.isOP(threadChannel, e.getMember())) {
									// apply correct tags + remove TA name
									String currentName = threadChannel.getName();
									String newName = currentName.replaceFirst("^\\[TA: [^]]+]", "");
									newName = "[CLOSED] " + newName;

									if(threadChannel.getAppliedTags().contains(Utils.getTagByName("open", parent))) {
										Utils.removeAndAddTag(threadChannel, "open", "closed", newName);
									} else {
										Utils.removeAndAddTag(threadChannel, "claimed", "closed", newName);
									}


									// edit bot's message
									String newMessage = """
											**CLOSED TICKET**
											<@%s> has completed this ticket.
											
											OP: To reopen this ticket, run `!reopen` in this channel.
											""".formatted(e.getAuthor().getId());
									botMessage.editMessage(newMessage).queue();

									// feedback message
									e.getMessage().reply("You have successfully closed this ticket!").mentionRepliedUser(false).queue();
								} else {
									e.getMessage().reply("You are not the person who claimed this ticket, or the original poster!").mentionRepliedUser(false).queue();
								}
							} else {
								e.getMessage().reply("This ticket is already closed!").mentionRepliedUser(false).queue();
							}
						} else {
							e.getMessage().reply(":no_entry: **403 FORBIDDEN** :no_entry:\nYou do not have permission to run this command.").mentionRepliedUser(false).queue();
						}
					}
					case "reopen" -> {
						if(Utils.isAdmin(e.getMember()) || Utils.isOP(threadChannel, e.getMember())) {
							if(threadChannel.getAppliedTags().contains(Utils.getTagByName("closed", parent))) {
								// apply correct tags + remove CLOSED name
								String currentName = threadChannel.getName();
								String newName = currentName.replaceFirst("^\\[CLOSED] ", "");
								Utils.removeAndAddTag(threadChannel, "closed", "open", newName);

								// edit bot's message
								Message botMessage = threadChannel.retrievePinnedMessages().complete().getFirst();
								String newMessage = """
										**OPEN TICKET**
										TAs: To claim this post, run `!claim` in this channel.
										OP: To close this post, run `!close` in this channel.
										""";
								botMessage.editMessage(newMessage).queue();

								// feedback message
								e.getMessage().reply("You have successfully reopened this ticket!").mentionRepliedUser(false).queue();
							} else {
								e.getMessage().reply("This ticket is not closed!").mentionRepliedUser(false).queue();
							}
						} else {
							e.getMessage().reply("You are not the original poster and cannot reopen this ticket.").mentionRepliedUser(false).queue();
						}
					}
				}
			}
			if(message[0].equals("help")) {
				String messageToSend = """
						# HOW TO USE THIS BOT
						**Office Hours Tickets**
						Head over to <#1414699886124728370> and create a post.  A TA will be with you shortly.
						
						**Commands**
						`!claim` - TAs only.  Claim a ticket as yours.  Only one TA may claim a ticket at a time.
						`!unclaim` - TAs only.  Unclaim a ticket if you cannot solve a problem.  Only the person who claimed the ticket may unclaim it.
						`!close` - TAs and OP only.  Close a ticket (aka mark it as solved).  Only the person who claimed the ticket or the original poster may close it.
						`!reopen` - OP only.  If you need additional help related to the topic, run this command.  Only the original poster may reopen a ticket.
						
						**NOTE**: All Instructors and Mods can use all commands in any ticket.
						""";
				e.getChannel().sendMessage(messageToSend).queue();
			}
		}
	}
}