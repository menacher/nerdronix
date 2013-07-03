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
 * This aggregator class will calculate total amount of time all users spent
 * viewing a channel
 * 
 * @author Abraham Menacherry
 * 
 */
public class ChannelViewTimeBolt extends BaseRichBolt {

	private static final long serialVersionUID = 1L;
	private OutputCollector collector;
	private Map<Integer, Integer> channelViewTimeMap = new HashMap<>();

	@Override
	public void prepare(@SuppressWarnings("rawtypes") Map stormConf,
			TopologyContext context, OutputCollector collector) {
		this.collector = collector;
	}

	@Override
	public void execute(Tuple input) {
		Integer channelId = input.getInteger(1);
		Integer viewTime = channelViewTimeMap.get(channelId);
		if (null == viewTime) {
			viewTime = input.getInteger(2);
		} else {
			viewTime += input.getInteger(2);
		}
		channelViewTimeMap.put(channelId, viewTime);
		collector.emit(new Values(channelId, viewTime));
		collector.ack(input);
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("channelId", "viewTime"));
	}

}
