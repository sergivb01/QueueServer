package me.sergivb01.queueserver.utils;

import org.bson.Document;

public class PayloadParser {

	public static void parse(String str){
		Document document = Document.parse(str);
		System.out.println("[DEBUG] " + document.toJson());
		switch (document.getString("type")){
			case "addplayer": {
				String player = document.getString("player");
				String server = document.getString("server");
				int prio = document.getInteger("priority");

				if(!Cache.getQueueByName(server).getPlayers().contains(player)){
					Cache.getQueueByName(server).addPlayer(player, prio);
				}
				break;
			}
			case "removeplayer": {
				String player = document.getString("player");
				if(Cache.players.containsKey(player)) {
					Cache.players.get(player).removePlayer(player);
				}
				break;
			}
			case "reqstatusof": {
				String player = document.getString("player");
				PayloadUtils.sendStatus(player);
				break;
			}*
		}


	}

}
