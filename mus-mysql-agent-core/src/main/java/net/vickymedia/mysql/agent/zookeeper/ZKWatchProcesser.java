package net.vickymedia.mysql.agent.zookeeper;

/**
 * User: weijie.song
 * Date: 16/2/14 下午6:11
 */
public interface ZKWatchProcesser {
	public void add(String node);

	public void update(String node);

	public void delete(String node);
}
