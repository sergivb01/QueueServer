package me.sergivb01.queueserver.utils;

import me.sergivb01.queueserver.queue.QueueServer;
import me.sergivb01.queueserver.redis.RedisDatabase;
import org.bson.Document;

import java.util.Arrays;

public class PayloadUtils {

	public static void sendStatus(String player){
		Document doc = new Document("inqueue", Cache.players.containsKey(player));
		if(Cache.players.containsKey(player)){
			QueueServer queueServer = Cache.players.get(player);
			doc.append("server", queueServer.getServer().getServerName());
			doc.append("priority", queueServer.getPriorities().get(player));
			doc.append("position", Arrays.asList(queueServer.getPriorities().keySet().toArray()).indexOf(player));
		}
		RedisDatabase.getPublisher().write("statusof;" + player + ";" + doc.toJson() + ";placeholder");
	}

	public static void sendQueueStatus(QueueServer queueServer){
		Document document = new Document("size", queueServer.getPlayers().size())
				.append("running", queueServer.isRunning())
				.append("server", new Document("name", queueServer.getServer().getServerName())
						.append("online", queueServer.getServer().getOnline())
						.append("max", queueServer.getServer().getMax())
						.append("whitelist", queueServer.getServer().isWhitelist())
				);
		RedisDatabase.getPublisher().write("queuestatus;" + queueServer.getServer().getServerName() + ";" + document.toJson() + ";placeholder");
	}

}
