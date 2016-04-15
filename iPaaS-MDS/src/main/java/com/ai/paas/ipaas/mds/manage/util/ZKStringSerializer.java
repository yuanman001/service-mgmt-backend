package com.ai.paas.ipaas.mds.manage.util;

import java.io.UnsupportedEncodingException;

import org.I0Itec.zkclient.exception.ZkMarshallingError;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.I0Itec.zkclient.serialize.ZkSerializer;

import com.ai.paas.ipaas.PaaSConstant;

public class ZKStringSerializer implements ZkSerializer {

	@Override
	public byte[] serialize(Object data) throws ZkMarshallingError {
		try {
			return data.toString().getBytes(PaaSConstant.CHARSET_UTF8);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Object deserialize(byte[] bytes) throws ZkMarshallingError {
		try {
			return new String(bytes, PaaSConstant.CHARSET_UTF8);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {
		SerializableSerializer se = new SerializableSerializer();
		byte[] bytes = se.serialize(5);
		ZKStringSerializer zkse = new ZKStringSerializer();
		byte[] bytes1 = zkse.serialize(5);
		System.out.println(se.deserialize(bytes) + "---" + bytes1);
	}
}
