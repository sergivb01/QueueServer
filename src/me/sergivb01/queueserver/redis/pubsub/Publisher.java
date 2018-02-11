package me.sergivb01.queueserver.redis.pubsub;

import lombok.Getter;
import me.sergivb01.queueserver.utils.Config;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class Publisher {
	@Getter private JedisPool pool;

	public Publisher(){
		pool = new JedisPool(new JedisPoolConfig(), "localhost", 6379,  3000, Config.REDIS_PASSWORD);
	}

	public void write(final String message) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.auth(Config.REDIS_PASSWORD);
			jedis.publish(Config.REDIS_CHANNEL, message);
			pool.returnResource(jedis);
		}
		finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}


}
