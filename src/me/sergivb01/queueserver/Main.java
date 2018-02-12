package me.sergivb01.queueserver;

import com.instrumentalapp.Agent;
import com.instrumentalapp.AgentOptions;
import me.sergivb01.queueserver.redis.RedisDatabase;
import me.sergivb01.queueserver.servers.Server;
import me.sergivb01.queueserver.utils.Cache;
import me.sergivb01.queueserver.utils.Config;
import me.sergivb01.queueserver.utils.PayloadUtils;

import java.util.Arrays;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class Main {

    private void run(){
        new RedisDatabase();

        Config.serverNames.forEach(s -> {
            Server srv = new Server(s);
            srv.createQueue();
            srv.getQueueServer().setRunning(false);
            Cache.servers.add(srv);

        });

	    new Timer().scheduleAtFixedRate(new TimerTask() {
		    @Override
		    public void run () {
			    Cache.servers.forEach(Server::reqNewStatus);
		    }
	    },1000, 3000);


	    Agent agent = new Agent(new AgentOptions().setApiKey("85616bb982836858100b94fd087a5311").setEnabled(true));
	    new Timer().scheduleAtFixedRate(new TimerTask() {
		    @Override
		    public void run () {
			    PayloadUtils.sendStatus();
			    Cache.queues.forEach(queueServer -> agent.gauge("mcserver.queue." + queueServer.getServer().getServerName() + ".size", queueServer.getPlayers().size()));
		    }
	    },1000, 5000);

	    new Timer().scheduleAtFixedRate(new TimerTask() {
		    @Override
		    public void run(){
			    Cache.run();
		    }

	    }, 5000, 750);



        String input = "";
        Scanner s = new Scanner(System.in);

        while(!input.equalsIgnoreCase("end")){
            input = s.nextLine();
            String[] args = input.split(" ");

            switch (args[0].toLowerCase()){
                case "status":
                    Cache.servers.forEach(server -> {
                        System.out.println("===========================================");
                        System.out.println("Status for " + server.getServerName() + ":");
                        System.out.println("Up: " + server.isUp());
                        System.out.println("Online: " + server.getOnline());
                        System.out.println("Max: " + server.getMax());
                        System.out.println("Whitelist: " + server.isWhitelist());
                        System.out.println("TPS: " + Arrays.toString(server.getTps()));
                    });
                    break;
                case "queues":
                    Cache.queues.forEach(queueServer -> {
                        System.out.println("===========================================");
                        System.out.println("Status for " + queueServer.getServer().getServerName() + ":");
                        System.out.println("Running: " + queueServer.isRunning());
                        System.out.println("Players: " + queueServer.getPlayers().toString());
                        System.out.println("Priorities: " + queueServer.getPriorities().toString());
                    });
                    break;
                case "pause": {
                    String srv = args[1];
                    boolean status = !Cache.getQueueByName(srv).isRunning();
                    Cache.getQueueByName(srv).setRunning(status);
                    System.out.println(srv + " is now " + status);
                    break;
                }
                case "addplayer": {
                    String queue = args[1];
                    String player = args[2];
                    Integer priority = Integer.parseInt(args[3]);

	                if(!Cache.players.containsKey(queue)) {
		                Cache.getQueueByName(queue).addPlayer(player, priority);
		                System.out.println("Added " + player + " to " + queue + " with priority=" + priority);
	                }
                    break;
                }
                case "removeplayer": {
                    String queue = args[1];
                    String player = args[2];
	                if(Cache.players.containsKey(queue)) {
		                Cache.players.get(player).removePlayer(player);
		                System.out.println("removed " + player + " from " + queue);
	                }
                    break;
                }
                case "clear": {
                    String queue = args[1];
                    Cache.getQueueByName(queue).getPlayers().clear();
                    Cache.getQueueByName(queue).getPriorities().clear();
                    System.out.println(queue + " was cleared!");
                    break;
                }
            }

        }

        PayloadUtils.sendBackendStatus(false);
		PayloadUtils.sendPlayerMessage("server", "&aQueue system backend is currently down. Queues have been disabled.");

        RedisDatabase.getSubscriber().getJedisPubSub().unsubscribe();
        RedisDatabase.getPublisher().getPool().destroy();

        System.out.println("Disconnected.");
        System.exit(1);
    }

    public static void main(String[] args) {
	    new Main().run();
    }

}
