package me.sergivb01.queueserver.servers;

import lombok.Getter;
import lombok.Setter;
import me.sergivb01.queueserver.utils.Cache;
import me.sergivb01.queueserver.queue.QueueServer;
import me.sergivb01.queueserver.redis.RedisDatabase;
import org.bson.Document;

public class Server {
	@Getter private String serverName;
	@Getter @Setter private boolean up = false;
	@Getter @Setter private int online = -1;
	@Getter @Setter private int max = -1;
	@Getter private double[] tps = new double[3];
	@Getter @Setter private boolean whitelist = true;
	@Getter private QueueServer queueServer;

	public Server(String serverName){
		this.serverName = serverName;
		System.out.println("Created server " + serverName);
	}

	public void reqNewStatus(){
		RedisDatabase.getPublisher().write("reqserverstatus;" + serverName);
	}

	public void updateData(String payload){
		Document document = Document.parse(payload);
		this.up = document.getBoolean("up");
		this.online = document.getInteger("online");
		this.whitelist = document.getBoolean("whitelist");
		this.max = document.getInteger("maxplayers");

		setTps((Document)document.get("tps"));
	}

	private void setTps(Document document){
		tps[0] = document.getDouble("tps0");
		tps[1] = document.getDouble("tps1");
		tps[2] = document.getDouble("tps2");
	}

	public void createQueue(){
		this.queueServer = new QueueServer(this);
		Cache.queues.add(this.queueServer);
	}


}
