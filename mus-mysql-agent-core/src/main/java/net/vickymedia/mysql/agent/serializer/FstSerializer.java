package net.vickymedia.mysql.agent.serializer;

import org.nustaq.serialization.FSTConfiguration;

/**
 * User: weijie.song@ttpod.com
 * Date: 15-7-8 上午11:48
 */
public class FstSerializer implements ISerializer {
    private final static FSTConfiguration configuration = FSTConfiguration.createDefaultConfiguration();

    private static ISerializer serializer;

    public <S> byte[] serializer(S obj) throws SerializerException {
        if (obj != null) {
            return configuration.asByteArray(obj);
        } else {
            return null;
        }
    }

    public <S> S deSerializer(byte[] s) throws SerializerException {
        if (s == null) {
            return null;
        }
        return (S) configuration.asObject(s);
    }

    @Override
    public <S> S deSerializer(byte[] s, Class<S> c) throws SerializerException {
        if (s == null) {
            return null;
        }
        return (S) configuration.asObject(s);
    }

    public static ISerializer getInstance() {
        if (serializer == null) {
            serializer = new FstSerializer();
        }
        return serializer;
    }
}
