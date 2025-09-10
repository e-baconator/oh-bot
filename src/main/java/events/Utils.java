package events;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.ForumChannel;
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel;
import net.dv8tion.jda.api.entities.channel.forums.ForumTag;

import java.util.ArrayList;
import java.util.List;

public class Utils {
	/**
	 * Retrieves a ForumTag by its name from the given ForumChannel.
	 *
	 * @param name the name of the tag to retrieve
	 * @param channel the ForumChannel from which to retrieve the tag
	 * @return the matching ForumTag, or null if no matching tag is found
	 */
	public static ForumTag getTagByName(String name, ForumChannel channel) {
		return channel.getAvailableTagsByName(name, true).getFirst();
	}

	public static void addTag(ThreadChannel channel, String tag) {
		ForumChannel parent = channel.getParentChannel().asForumChannel();
		List<ForumTag> appliedTags = channel.getAppliedTags();
		ArrayList<ForumTag> newAppliedTags = new ArrayList<>(appliedTags);
		newAppliedTags.add(Utils.getTagByName(tag, parent));
		channel.getManager().setAppliedTags(newAppliedTags).queue();
	}

	public static void removeTag(ThreadChannel channel, String tag) {
		ForumChannel parent = channel.getParentChannel().asForumChannel();
		List<ForumTag> appliedTags = channel.getAppliedTags();
		ArrayList<ForumTag> newAppliedTags = new ArrayList<>(appliedTags);
		newAppliedTags.remove(Utils.getTagByName(tag, parent));
		channel.getManager().setAppliedTags(newAppliedTags).queue();
	}

	public static boolean isStaff(Member member) {
		return member.getRoles().contains(member.getGuild().getRoleById("1410989464506863616")) || member.getRoles().contains(member.getGuild().getRoleById("1410989547285909634"));
	}

	public static boolean isAdmin(Member member) {
		return member.getRoles().contains(member.getGuild().getRoleById("1410989244276805764")) || member.getRoles().contains(member.getGuild().getRoleById("1410989547285909634"));
	}
}