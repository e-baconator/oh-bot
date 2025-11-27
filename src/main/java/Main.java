import events.CommandListener;
import events.TicketCreate;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

public class Main {
	public static void main(String[] args) throws InterruptedException {
		Dotenv env = Dotenv.load();

		String token = env.get("TOKEN");

		Message.suppressContentIntentWarning();
		JDA jda = JDABuilder.createDefault(token, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MEMBERS, GatewayIntent.MESSAGE_CONTENT)
				.disableCache(CacheFlag.ACTIVITY, CacheFlag.VOICE_STATE, CacheFlag.EMOJI, CacheFlag.STICKER, CacheFlag.CLIENT_STATUS, CacheFlag.ONLINE_STATUS, CacheFlag.SCHEDULED_EVENTS)
				.setMemberCachePolicy(MemberCachePolicy.ALL)
				.addEventListeners(new CommandListener())
				.addEventListeners(new TicketCreate())
				.useSharding(0, 1)
				.build();
		jda.awaitReady();
		jda.getPresence().setStatus(OnlineStatus.DO_NOT_DISTURB);
		jda.getPresence().setActivity(Activity.customStatus("Following the design recipe!"));
		jda.getGuildById("1410988823764013117").loadMembers().onSuccess((member) -> System.out.println("Done"));
	}
}