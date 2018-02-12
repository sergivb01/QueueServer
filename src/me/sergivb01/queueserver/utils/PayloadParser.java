package me.sergivb01.queueserver.utils;

import org.bson.Document;

public class PayloadParser {

	public static void parse(String str){
		//System.out.println("[DEBUG 1] " + str);
		Document document = Document.parse(str);
		//System.out.println("[DEBUG 2] " + document.toJson());
		switch (document.getString("type")){
			case "pause": {
				String server = document.getString("server");
				Cache.getQueueByName(server).setRunning(!Cache.getQueueByName(server).isRunning());
				PayloadUtils.sendPlayerMessage("server", "&9Queue &f" + server + " &9is now " + (Cache.getQueueByName(server).isRunning() ? "&arunning" : "&cpaused") + "&9!");
				break;
			}
			case "addplayer": {
				String player = document.getString("player");
				String server = document.getString("server");
				int prio = document.getInteger("priority");

				if(!Cache.getQueueByName(server).getPlayers().contains(player)){
					Cache.getQueueByName(server).addPlayer(player, prio);
					System.out.println("added " + player + " to " + server + " with " + prio + " of priotity");
					PayloadUtils.sendPlayerMessage(player, "&9You have been added to &f" + server + " &9with a priority of &f" + prio);
				}else{
					PayloadUtils.sendPlayerMessage(player, "&cYou already are in a queue.");
				}
				break;
			}
			case "removeplayer": {
				String player = document.getString("player");
				if(Cache.players.containsKey(player)) {
					Cache.players.get(player).removePlayer(player);
					PayloadUtils.sendPlayerMessage(player, "&9You have been removed from the queue.");
					System.out.println("removed " + player + " from queues");
				}else{
					PayloadUtils.sendPlayerMessage(player, "&cYou are not in a queue.");
				}
				break;
			}
		}


	}

}
