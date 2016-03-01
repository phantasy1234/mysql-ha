package net.vickymedia.mysql.agent.serializer;

/**
 * User: weijie.song@ttpod.com
 * Date: 15/12/31 下午5:18
 */
public enum SerializerCode {
    FASTJSON(FastjsonSerializer.getInstance()),
    FST(FstSerializer.getInstance()),
    KRYO(KryoSerializer.getInstance());

    ISerializer serializer;

    SerializerCode(ISerializer serializer) {
        this.serializer = serializer;
    }

    public ISerializer getSerializer() {
        return serializer;
    }

}
