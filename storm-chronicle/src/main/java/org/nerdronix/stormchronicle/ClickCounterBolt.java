package org.nerdronix.stormchronicle;

import java.util.HashMap;
import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

/**
 * This bolt counts the number of times a channel was visited by all users.
 * 
 * @author Abraham Menacherry
 * 
 */
public class ClickCounterBolt extends BaseRichBolt {

	private static final long serialVersionUID = 1L;
	private OutputCollector collector;
	private Map<Integer, Integer> channelClickCount = new HashMap<>();

	@Override
	public void prepare(@SuppressWarnings("rawtypes") Map stormConf,
			TopologyContext context, OutputCollector collector) {
		this.collector = collector;
	}

	@Override
	public void execute(Tuple input) {
		Integer channelId = input.getInteger(1);
		Integer clickCount = channelClickCount.get(channelId);
		if (null == clickCount) {
			clickCount = 1;
		} else {
			clickCount += 1;
		}
		channelClickCount.put(channelId, clickCount);
		collector.emit(new Values(channelId, clickCount));
		collector.ack(input);
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("channelId", "clickCount"));
	}

}
