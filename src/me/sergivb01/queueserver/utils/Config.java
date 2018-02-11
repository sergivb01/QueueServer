package me.sergivb01.queueserver.utils;

import java.util.ArrayList;
import java.util.List;

public class Config {
	public static List<String> serverNames = new ArrayList<>();
	public static String REDIS_CHANNEL = "serverutils";
	public static String REDIS_PASSWORD = "c95668e7c556e6c096595310f33c95dd";

	static {
		serverNames.add("hcf");
		serverNames.add("kits");
		//serverNames.add("lite");
	}

}
