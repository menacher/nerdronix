package org.nerdronix.stormchronicle;

/**
 * A simple bean class with information on the user and channel that was
 * pressed. It also has the duration of viewing channel by user. This is the
 * seed data that is then processed to find out metrics in the various Bolt
 * implementations.
 * 
 * @author Abraham Menacherry
 * 
 */
public class TvRemote {
	
	public TvRemote(int user, int channelId, int duration) {
		super();
		this.user = user;
		this.channelId = channelId;
		this.duration = duration;
	}

	private int user;
	private int channelId;
	private int duration;

	public static TvRemote createRandomRecord()
	{
		int userId = (int)(Math.random() * 11);
		int channelId = 150 + (int)(Math.random() * ((400-150) + 1)); // channels between 150 and 400
		int duration = 1000 + (int)(Math.random() * ((5000-1000) + 1)); // between 1 and 5 seconds
		return new TvRemote(userId, channelId, duration);
	}
	
	public int getUser() {
		return user;
	}

	public void setUser(int user) {
		this.user = user;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}
	
	public int getChannelId() {
		return channelId;
	}

	public void setChannelId(int channelId) {
		this.channelId = channelId;
	}
}