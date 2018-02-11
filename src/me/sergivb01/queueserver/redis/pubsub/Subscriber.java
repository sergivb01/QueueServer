package me.sergivb01.queueserver.redis.pubsub;

import lombok.Getter;
import me.sergivb01.queueserver.utils.Cache;
import me.sergivb01.queueserver.utils.Config;
import me.sergivb01.queueserver.utils.PayloadParser;
import org.bson.Document;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;


public class Subscriber {
	@Getter private JedisPubSub jedisPubSub;
	private Jedis jedis;

	public Subscriber() {
		this.jedis = new Jedis("localhost", 6379,  3000);
		this.jedis.auth(Config.REDIS_PASSWORD);
		this.init();
	}

	private void init() {
		jedisPubSub = this.get();
		new Thread(() -> jedis.subscribe(jedisPubSub, Config.REDIS_CHANNEL)).start();
	}

	private JedisPubSub get() {
		return new JedisPubSub() {
			@Override
			public void onMessage(final String channel, final String message) {
				final String[] args = message.split(";");
				//System.out.println(Arrays.toString(args));
				if (args.length > 2) {
					final String command = args[0].toLowerCase();
					final String server = args[1];
					final String payload = args[2];
					switch (command) {
						case "payload":
							Document document = Document.parse(payload);
							if(document.getString("type").equalsIgnoreCase("serverstatus")){
								Cache.getServerByName(server).updateData(payload);
							}
							if(document.getString("type").equalsIgnoreCase("addplayer") || document.getString("type").equalsIgnoreCase("removeplayer")){
								PayloadParser.parse(payload);
							}
							break;
						default:
							//System.out.println("I don't know how to handle this dude! [" + message + "]");
							break;
					}
				}
			}

			public void onPMessage(final String s, final String s1, final String s2) { }
			public void onSubscribe(final String s, final int i) { }
			public void onUnsubscribe(final String s, final int i) { }
			public void onPUnsubscribe(final String s, final int i) { }
			public void onPSubscribe(final String s, final int i) {}
		};

	}


}