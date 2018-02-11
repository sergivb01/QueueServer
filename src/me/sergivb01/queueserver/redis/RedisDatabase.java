package me.sergivb01.queueserver.redis;

import lombok.Getter;
import me.sergivb01.queueserver.redis.pubsub.Publisher;
import me.sergivb01.queueserver.redis.pubsub.Subscriber;

public class RedisDatabase {
	@Getter public static Publisher publisher;
	@Getter public static Subscriber subscriber;

	public RedisDatabase(){
		init();
	}

	private void init() {
		publisher = new Publisher();
		subscriber = new Subscriber();
	}


}
