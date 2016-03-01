package net.vickymedia.mysql.agent.serializer;

/**
 * User: weijie.song@ttpod.com
 * Date: 15/12/9 下午2:40
 */
public class SerializerFactory {
    public static ISerializer getSerializer() {
        return KryoSerializer.getInstance();
    }

    public static ISerializer getSerializer(SerializerCode code) {
        return code.getSerializer();
    }
}
