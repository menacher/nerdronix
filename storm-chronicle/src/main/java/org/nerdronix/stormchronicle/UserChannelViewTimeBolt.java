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
 * This bolt will aggregate the amount of time a user spent viewing on a per
 * channel basis.
 * 
 * @author Abraham Menacherry
 * 
 */
public class UserChannelViewTimeBolt extends BaseRichBolt {
	private static final long serialVersionUID = 1L;
	private OutputCollector collector;
	private Map<Integer, Map<Integer,Integer>> userChannelViewMap = new HashMap<>();

	@Override
	public void prepare(@SuppressWarnings("rawtypes") Map stormConf,
			TopologyContext context, OutputCollector collector) {
		this.collector = collector;
	}

	@Override
	public void execute(Tuple input) {
		Integer userId = input.getInteger(0);
		Integer channelId = input.getInteger(1);
		Map<Integer, Integer> channelViewTime = userChannelViewMap.get(userId);
		if (null == channelViewTime) {
			channelViewTime = new HashMap<>();
			channelViewTime.put(channelId, 0);
		}
		channelViewTime.put(channelId, channelViewTime.get(channelId) + input.getInteger(2));
		collector.emit(new Values(userId, channelViewTime.get(channelId)));
		collector.ack(input);
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("channelId", "viewTime"));
	}
}
