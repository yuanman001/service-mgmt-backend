package com.ai.paas.ipaas.txs.component;

import org.I0Itec.zkclient.exception.ZkMarshallingError;
import org.I0Itec.zkclient.serialize.ZkSerializer;

import java.nio.charset.Charset;

/**
 * String Serializer
 *
 * Created by gaoht on 15/5/6.
 */
public class StringSerializer implements ZkSerializer {
    @Override
    public byte[] serialize(Object data) throws ZkMarshallingError {
        return data.toString().getBytes(Charset.defaultCharset());
    }

    @Override
    public Object deserialize(byte[] bytes) throws ZkMarshallingError {
        return new String(bytes,Charset.defaultCharset());
    }
}
