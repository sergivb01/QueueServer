package me.sergivb01.queueserver.queue;

import lombok.Getter;
import lombok.Setter;
import me.sergivb01.queueserver.utils.Cache;
import me.sergivb01.queueserver.servers.Server;

import java.util.HashMap;
import java.util.PriorityQueue;

public class QueueServer {
	@Getter private Server server;
	@Getter PriorityQueue<String> players = new PriorityQueue<>(new QueueComparator());
	@Getter private HashMap<String, Integer> priorities = new HashMap<>();
	@Getter @Setter private boolean running = true;

	public QueueServer(Server server){
		this.server = server;
	}

	public void addPlayer(String playerName, int priority){
		Cache.players.put(playerName, this);
		priorities.put(playerName, priority);
		players.add(playerName);
	}

	public void removePlayer(String playerName){
		Cache.players.remove(playerName);
		priorities.remove(playerName);
		players.remove(playerName);
	}

}
