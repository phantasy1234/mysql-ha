package net.vickymedia.mysql.agent.serializer;

/**
 * User: weijie.song@ttpod.com
 * Date: 15/12/9 下午2:35
 */
public interface ISerializer {
	public <S> byte[] serializer(S obj) throws SerializerException;

	public <S> S deSerializer(byte[] s) throws SerializerException;

	public <S> S deSerializer(byte[] s, Class<S> c) throws SerializerException;
}
