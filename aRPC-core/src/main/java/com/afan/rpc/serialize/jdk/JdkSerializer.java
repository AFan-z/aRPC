package com.afan.rpc.serialize.jdk;

import com.afan.rpc.serialize.Serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @author AFan
 * @date 2021/7/3 17:06
 */
public class JdkSerializer implements Serializer {
    @Override
    public <T> T deserialize(Class<T> clazz, byte[] bytes) {
        try {
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
            return (T) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("反序列化失败", e);
        }
    }

    @Override
    public <T> byte[] serialize(T object) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(object);
            return bos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("序列化失败", e);
        }
    }
}
