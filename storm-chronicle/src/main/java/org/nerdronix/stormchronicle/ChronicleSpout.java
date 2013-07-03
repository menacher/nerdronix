package org.nerdronix.stormchronicle;

import java.io.IOException;
import java.util.Map;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;

import com.higherfrequencytrading.chronicle.Chronicle;
import com.higherfrequencytrading.chronicle.Excerpt;
import com.higherfrequencytrading.chronicle.impl.IndexedChronicle;
import com.higherfrequencytrading.chronicle.tools.ChronicleTools;

/**
 * This spout will first create a file with random user clicks and write them to
 * a file using java chronicle library. The {@link #nextTuple()} method will
 * then read from the same file and give back the inputs to the topology.
 * 
 * @author Abraham Menacherry
 * 
 */
public class ChronicleSpout extends BaseRichSpout {

	private static final long serialVersionUID = 1L;
	private SpoutOutputCollector collector;
	public static final String TMP = System.getProperty("java.io.tmpdir");
	private transient Chronicle chronicle;
	private transient Excerpt excerpt;

	@Override
	public void open(@SuppressWarnings("rawtypes") Map conf,
			TopologyContext context, SpoutOutputCollector collector) {
		this.collector = collector;
		String basePath = TMP + "/TvRemoteClick";
		ChronicleTools.deleteOnExit(basePath);

		try {
			chronicle = new IndexedChronicle(basePath);
			excerpt = chronicle.createExcerpt();
			for (int i = 0; i < 10000; i++) {
				excerpt.startExcerpt(20);// three integers
				TvRemote click = TvRemote.createRandomRecord();
				Integer channelId = click.getChannelId();
				excerpt.writeInt(click.getUser());
				excerpt.writeInt(channelId);
				excerpt.writeInt(click.getDuration());
				excerpt.finish();
			}
			// poison.
			excerpt.startExcerpt(20);
			excerpt.writeInt(123);
			excerpt.writeInt(0);
			excerpt.writeInt(0);
			excerpt.finish();
			excerpt.toStart();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void nextTuple() {
		if (excerpt.nextIndex()) {
			int userId = excerpt.readInt();
			int channelId = excerpt.readInt();
			int duration = excerpt.readInt();
			if (channelId == 0) {
				// poison, set countdown LATCH.
				StartStorm.LATCH.countDown();
			} else {
				collector.emit(new Values(userId, channelId, duration));
			}
		} else {
			// dont busy spin, not actually required here since we have the
			// poison object.
			Utils.sleep(500);
		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("userId", "channelId", "duration"));
	}

	@Override
	public void close() {
		String basePath = TMP + "/TvRemoteClick";
		ChronicleTools.deleteOnExit(basePath);
	}
}
