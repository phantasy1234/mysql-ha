package net.vickymedia.mysql.agent.zookeeper;

import com.google.common.collect.Lists;
import net.vickymedia.mysql.agent.serializer.FastjsonSerializer;
import net.vickymedia.mysql.agent.serializer.ISerializer;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.utils.ZKPaths;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.Stat;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;

/**
 * User: weijie.song
 * Date: 16/2/3 下午7:31
 */
public class ZKClient {
	CuratorFramework client;
	ISerializer serializer = FastjsonSerializer.getInstance();

	private ConcurrentSkipListSet watchers = new ConcurrentSkipListSet();
	private static Charset charset = Charset.forName("utf-8");

	public ZKClient(CuratorFramework client) {
		this.client = client;
	}

	public void pushData(String path, Object data) {
		if (!checkIfExist(path)) {
			createPath(path, data);
		}
		setData(path, data);
	}

	public String getDataString(String path) {
		try {
			return new String(client.getData().forPath(path));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public <T> T getData(String path, Class<T> c) {
		try {
			return serializer.deSerializer(client.getData().forPath(path), c);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void pushString(String path, String data) {
		if (!checkIfExist(path)) {
			createPathString(path, data);
		}
		setString(path, data);
	}

	public <T> List<T> getChildren(String path, int page, int size, Class<T> t) {
		try {
			List<String> res = client.getChildren().forPath(path);
			int start = (page - 1) * size;
			int end = page * size;
			if (res.size() <= start) {
				return Lists.newArrayList();
			}

			return res.stream().limit(end).skip(start).map(d -> getData(d, t)).collect(Collectors.toList());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Lists.newArrayList();
	}

	public void setString(String path, String data) {
		try {
			client.setData().forPath(path, data.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setData(String path, Object data) {
		try {
			client.setData().forPath(path, serializer.serializer(data));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void createPath(String path, Object data) {
		try {
			client.create().forPath(path, serializer.serializer(data));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void createPathString(String path, String data) {
		try {
			client.create().forPath(path, data.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void createPath(String path) {
		try {
			client.create().forPath(path);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean checkIfExist(String path) {
		try {
			Stat stat = client.checkExists().forPath(path);
			return stat != null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public void push(String path, Object dbResult) {
		checkAndCreate(path);
		Field[] fields = dbResult.getClass().getDeclaredFields();
		for (Field f : fields)
			try {
				PropertyDescriptor pd = new PropertyDescriptor(f.getName(), dbResult.getClass());
				Object value = pd.getReadMethod().invoke(dbResult);
				pushString(path + "/" + f.getName(), String.valueOf(value));
			} catch (IntrospectionException | InvocationTargetException | IllegalAccessException e) {
				e.printStackTrace();
			}
	}

	public void checkAndCreate(String path) {
		if (!checkIfExist(path)) {
			createPath(path);
		}
	}

	public void register(String registeNode, final ZKWatchProcesser processer) throws Exception {
		PathChildrenCache cache = new PathChildrenCache(client, registeNode, false);
		cache.start();
		PathChildrenCacheListener plis = new PathChildrenCacheListener() {

			@Override
			public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
				switch (event.getType()) {
				case CHILD_ADDED: {
					processer.add(ZKPaths.getNodeFromPath(event.getData().getPath()));
					break;
				}

				case CHILD_UPDATED: {
					System.out.println("Node changed: " + ZKPaths.getNodeFromPath(event.getData().getPath()));
					processer.update(ZKPaths.getNodeFromPath(event.getData().getPath()));
					break;
				}

				case CHILD_REMOVED: {
					System.out.println("Node removed: " + ZKPaths.getNodeFromPath(event.getData().getPath()));
					processer.delete(ZKPaths.getNodeFromPath(event.getData().getPath()));
					break;
				}
				}
			}
		};
		//注册监听
		cache.getListenable().addListener(plis);

		//		byte[] data = "disable".getBytes();//节点值
		//
		//		CuratorWatcher watcher = new ZKWatchRegister(registeNode, data);    //创建一个register watcher
		//
		//		//		Stat stat = client.checkExists().forPath(registeNode);
		//		//		if (stat != null) {
		//		//			client.delete().forPath(registeNode);
		//		//		}
		//		//		client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
		//		//				.forPath(registeNode, data);//创建的路径和值
		//
		//		//添加到session过期监控事件中
		//		//		addReconnectionWatcher(registeNode, ZookeeperWatcherType.CREATE_ON_NO_EXITS, watcher);
		//		data = client.getData().usingWatcher(watcher).forPath(registeNode);
		//		System.out.println("get path form zk : " + registeNode + ":" + new String(data));
	}

	//	public void addReconnectionWatcher(final String path, final ZookeeperWatcherType watcherType,
	//			final CuratorWatcher watcher) {
	//		synchronized (this) {
	//			if (!watchers.contains(watcher.toString()))//不要添加重复的监听事件
	//			{
	//				watchers.add(watcher.toString());
	//				System.out.println("add new watcher " + watcher);
	//				client.getConnectionStateListenable().addListener(new ConnectionStateListener() {
	//					@Override
	//					public void stateChanged(CuratorFramework client, ConnectionState newState) {
	//						System.out.println(newState);
	//						//						if (newState == ConnectionState.LOST) {//处理session过期
	//						//							try {
	//						//								if (watcherType == ZookeeperWatcherType.EXITS) {
	//						//									client.checkExists().usingWatcher(watcher).forPath(path);
	//						//								} else if (watcherType == ZookeeperWatcherType.GET_CHILDREN) {
	//						//									client.getChildren().usingWatcher(watcher).forPath(path);
	//						//								} else if (watcherType == ZookeeperWatcherType.GET_DATA) {
	//						//									client.getData().usingWatcher(watcher).forPath(path);
	//						//								} else if (watcherType == ZookeeperWatcherType.CREATE_ON_NO_EXITS) {
	//						//									//ephemeral类型的节点session过期了，需要重新创建节点，并且注册监听事件，之后监听事件中，
	//						//									//会处理create事件，将路径值恢复到先前状态
	//						//									Stat stat = client.checkExists().usingWatcher(watcher).forPath(path);
	//						//									if (stat == null) {
	//						//										System.err.println("to create");
	//						//										client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL)
	//						//												.withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE).forPath(path);
	//						//									}
	//						//								}
	//						//							} catch (Exception e) {
	//						//								e.printStackTrace();
	//						//							}
	//						//						}
	//					}
	//				});
	//			}
	//		}
	//	}

	//	public class ZKWatch implements CuratorWatcher {
	//		private final String path;
	//
	//		public String getPath() {
	//			return path;
	//		}
	//
	//		public ZKWatch(String path) {
	//			this.path = path;
	//		}
	//
	//		@Override
	//		public void process(WatchedEvent event) throws Exception {
	//			System.out.println(event.getType());
	//			if (event.getType() == Watcher.Event.EventType.NodeDataChanged) {
	//				byte[] data = client.
	//						getData().
	//						usingWatcher(this).forPath(path);
	//				System.out.println(path + ":" + new String(data, Charset.forName("utf-8")));
	//			}
	//		}
	//	}

	//	public class ZKWatchRegister implements CuratorWatcher {
	//		private final String path;
	//		private byte[] value;
	//
	//		public String getPath() {
	//			return path;
	//		}
	//
	//		public ZKWatchRegister(String path, byte[] value) {
	//			this.path = path;
	//			this.value = value;
	//		}
	//
	//		@Override
	//		public void process(WatchedEvent event) throws Exception {
	//			System.out.println(event.getType());
	//			if (event.getType() == Watcher.Event.EventType.NodeDataChanged) {
	//				//节点数据改变了，需要记录下来，以便session过期后，能够恢复到先前的数据状态
	//				byte[] data = client.
	//						getData().
	//						usingWatcher(this).forPath(path);
	//				value = data;
	//				System.out.println(path + ":" + new String(data));
	//			} else if (event.getType() == Watcher.Event.EventType.NodeDeleted) {
	//				//节点被删除了，需要创建新的节点
	//				System.out.println(path + ":" + path + " has been deleted.");
	//				Stat stat = client.checkExists().usingWatcher(this).forPath(path);
	//				if (stat == null) {
	//					client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL)
	//							.withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE).forPath(path);
	//				}
	//			} else if (event.getType() == Watcher.Event.EventType.NodeCreated) {
	//				//节点被创建时，需要添加监听事件（创建可能是由于session过期后，curator的状态监听部分触发的）
	//				System.out.println(path + ":" + " has been created!" + "the current data is " + new String(value));
	//				client.setData().forPath(path, value);
	//				client.getData().usingWatcher(this).forPath(path);
	//			}
	//		}
	//	}
	//
	//	public enum ZookeeperWatcherType {
	//		GET_DATA, GET_CHILDREN, EXITS, CREATE_ON_NO_EXITS
	//	}
}
