package net.vickymedia.mysql.agent.serializer;

import com.alibaba.fastjson.JSON;

/**
 * User: weijie.song@ttpod.com
 * Date: 15-7-13 下午7:12
 */
public class FastjsonSerializer implements ISerializer {

	private static ISerializer serializer;

	public <S> byte[] serializer(S obj) throws SerializerException {
		if (obj == null) {
			return null;
		}
		return JSON.toJSONBytes(obj);
	}

	public <S> S deSerializer(byte[] s) throws SerializerException {
		if (s == null) {
			return null;
		}
		return (S) JSON.parse(s);
	}

	public static ISerializer getInstance() {
		if (serializer == null) {
			serializer = new FastjsonSerializer();
		}
		return serializer;
	}

	public <S> S deSerializer(byte[] s, Class<S> c) throws SerializerException {
		if (s == null) {
			return null;
		}
		return (S) JSON.parseObject(s, c);
	}
}
