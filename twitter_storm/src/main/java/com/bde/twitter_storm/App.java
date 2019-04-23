package com.bde.twitter_storm;

import java.sql.SQLException;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.topology.TopologyBuilder;

public class App {
    public static void main(String[] args) {

        String consumerKey = args[0];
        String consumerSecret = args[1];

        String accessToken = args[2];
        String accessTokenSecret = args[3];

        Config config = new Config();
        config.setDebug(false);

        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("twitter-spout", new TwitterSpout(consumerKey, consumerSecret, accessToken, accessTokenSecret));
        // emit all emojis
        builder.setBolt("all-emoji-bolt", new EmojiBolt()).shuffleGrouping("twitter-spout");
        // add to sql db
        builder.setBolt("sql-bolt", new SQLBolt()).shuffleGrouping("all-emoji-bolt");

        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("Twitter-to-SQL", config, builder.createTopology());

        // try {
        //     //how long to run topology - set to 30 secs
        //     Thread.sleep(10000);
        // } catch (Exception e) {
        //     System.out.println("ouch");
        // }
        // //shutdown the topology
        // cluster.shutdown();
    }
}
