package me.sergivb01.queueserver.utils;

import me.sergivb01.queueserver.queue.QueueServer;
import me.sergivb01.queueserver.redis.RedisDatabase;
import org.bson.Document;

import java.util.Arrays;

public class PayloadUtils {

	public static void sendPlayerMessage(String player, String message){
		Document document = new Document("type", "message")
				.append("player", player)
				.append("message", message);
		System.out.println("payload;" + player + ";" + document.toJson() + ";placeholder");
		RedisDatabase.getPublisher().write("payload;" + player + ";" + document.toJson() + ";placeholder");
	}

	public static void sendStatus(){
		Cache.servers.forEach(server -> {
			QueueServer queueServer = server.getQueueServer();
			Document document = new Document("type", "queue")
					.append("up", server.isUp())
					.append("server", server.getServerName())
					.append("online", server.getOnline())
					.append("max", server.getMax())
					.append("whitelist", server.isWhitelist())
					.append("size", queueServer.getPlayers().size())
					.append("players", Arrays.toString(queueServer.getPlayers().toArray()))
					.append("priorities", queueServer.getPriorities())
					.append("running", queueServer.isRunning());
			RedisDatabase.getPublisher().write("payload;" + server.getServerName() + ";" + document.toJson() + ";placeholder");
		});
	}

	public static void sendBackendStatus (boolean up) {
		Document document = new Document("type", "backend")
				.append("up", up);
		RedisDatabase.getPublisher().write("payload;backend;" + document.toJson() + ";placeholder");
	}
//addplayer hcf sergivb01 1
	/*private static Document getPlayerStuff(QueueServer queueServer){
		List<Document> docs = new ArrayList<>();

		for(String str : queueServer.getPriorities().keySet()){
			Document doc = new Document("player", str)
					.append("position", Arrays.asList(queueServer.getPriorities().keySet().toArray()).indexOf(str))
					.append("priority", queueServer.getPriorities().get(str));
			docs.add(doc);
		}

		return new Document("players", docs);
	}*/


}
