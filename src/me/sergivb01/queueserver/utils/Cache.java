package me.sergivb01.queueserver.utils;

import me.sergivb01.queueserver.queue.QueueServer;
import me.sergivb01.queueserver.redis.RedisDatabase;
import me.sergivb01.queueserver.servers.Server;

import java.util.*;

public class Cache{
	public static HashMap<String, QueueServer> players = new HashMap<>();
	public static List<Server> servers = new ArrayList<>();
	public static List<QueueServer> queues = new ArrayList<>();

	public static Server getServerByName(String serverName){
		Optional<Server> srv = servers.stream().filter(server -> server.getServerName().equalsIgnoreCase(serverName)).findFirst();
		return srv.orElse(null);
	}

	public static QueueServer getQueueByName(String serverName){
		Optional<QueueServer> srv = queues.stream().filter(queueServer -> queueServer.getServer().getServerName().equalsIgnoreCase(serverName)).findFirst();
		return srv.orElse(null);
	}

	private static boolean canRun(QueueServer queueServer){
		/*System.out.println("=============================================");
		System.out.println(queueServer.getServer().isUp());
		System.out.println(queueServer.getServer().getOnline() + "/" + queueServer.getServer().getMax());
		System.out.println(queueServer.getServer().getServerName() + " Running: " + queueServer.isRunning() + " Size: " + queueServer.getPriorities().size());*/
		return (queueServer.isRunning() && queueServer.getPlayers().size() != 0 && queueServer.getPriorities().size() != 0 && queueServer.getServer().getOnline() < queueServer.getServer().getMax());
	}

	public static void run(){
		queues.stream().filter(Cache::canRun).forEach(queueServer -> {
			String player = queueServer.getPlayers().remove();
			System.out.println("Sent " + player + "to " + queueServer.getServer().getServerName() + ". Priority was " + queueServer.getPriorities().get(player));

			RedisDatabase.getPublisher().write("reqsend;" + player + ";" + queueServer.getServer().getServerName());

			queueServer.getPriorities().remove(player);
			players.remove(player);

		});
	}


}
