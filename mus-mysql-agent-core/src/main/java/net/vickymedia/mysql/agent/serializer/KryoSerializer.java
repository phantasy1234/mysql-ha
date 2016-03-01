package net.vickymedia.mysql.agent.serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

/**
 * User: weijie.song@ttpod.com
 * Date: 15/12/9 下午2:17
 */
public class KryoSerializer implements ISerializer {

	private static ISerializer serializer;

	@Override
	public <S> byte[] serializer(S obj) throws SerializerException {
		if (obj == null) {
			return null;
		}
		Kryo kryo = new Kryo();
		Output output = null;
		try {
			output = new Output(1, 4096);
			kryo.writeClassAndObject(output, obj);
			return output.toBytes();
		} finally {
			assert output != null;
			output.flush();
		}
	}

	@Override
	public <S> S deSerializer(byte[] s) throws SerializerException {
		if (s == null) {
			return null;
		}
		Kryo kryo = new Kryo();
		Input input = new Input(s);
		try {
			return (S) kryo.readClassAndObject(input);
		} finally {
			input.close();
		}
	}

	@Override
	public <S> S deSerializer(byte[] s, Class<S> c) throws SerializerException {
		if (s == null) {
			return null;
		}
		Kryo kryo = new Kryo();
		Input input = new Input(s);
		try {
			return kryo.readObject(input, c);
		} finally {
			input.close();
		}
	}

	public static ISerializer getInstance() {
		if (serializer == null) {
			serializer = new FstSerializer();
		}
		return serializer;
	}
}
