package me.sergivb01.queueserver.queue;

import me.sergivb01.queueserver.utils.Cache;

import java.util.Comparator;

public class QueueComparator implements Comparator<String>{

	@Override
	public int compare (String x, String y) {
		if (Cache.players.get(x).getPriorities().get(x) < Cache.players.get(y).getPriorities().get(y)){
			return -1;
		}

		if (Cache.players.get(x).getPriorities().get(x) > Cache.players.get(y).getPriorities().get(y)){
			return 1;
		}

		return 0;
	}
}
