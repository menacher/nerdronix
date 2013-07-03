package org.nerdronix.stormchronicle;

import java.util.concurrent.CountDownLatch;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.generated.StormTopology;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;

/**
 * The starting point Class of this example. 
 * @author Abraham Menacherry
 *
 */
public class StartStorm {

	public static final CountDownLatch LATCH = new CountDownLatch(1);

	public static void main(String[] args) throws InterruptedException {
		Config config = new Config();
		config.setDebug(true);
		config.setNumWorkers(2);
		LocalCluster cluster = new LocalCluster();
		cluster.submitTopology("test", config, createTopology());
		LATCH.await();
		System.out.println("Processing complete");
		cluster.killTopology("test");
		cluster.shutdown();
	}

	/**
	 * Sets up the topology for storm.
	 * @return
	 */
	private static StormTopology createTopology() {
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("chronicle", new ChronicleSpout());
		builder.setBolt("click-counter", new ClickCounterBolt(), 2)
				.fieldsGrouping("chronicle", new Fields("channelId"));
		builder.setBolt("view-time", new ChannelViewTimeBolt(), 2)
				.fieldsGrouping("chronicle", new Fields("channelId"));
		builder.setBolt("view-time-by-user", new UserChannelViewTimeBolt(), 2)
				.fieldsGrouping("chronicle", new Fields("userId", "channelId"));
		return builder.createTopology();
	}

}
