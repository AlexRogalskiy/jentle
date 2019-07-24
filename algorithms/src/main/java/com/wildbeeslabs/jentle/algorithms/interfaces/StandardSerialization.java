package com.wildbeeslabs.jentle.algorithms.interfaces;

import com.wildbeeslabs.jentle.algorithms.exception.SerializationException;
import org.apache.commons.lang3.ClassUtils;

import java.io.*;

/**
 * The serialization implementation using standard Java serialization
 */
public class StandardSerialization implements Serialization {
    @Override
    public void serialize(Object object, OutputStream os) {
        ObjectOutputStream out = null;
        boolean isObjectStream = os instanceof ObjectOutputStream;
        try {
            out = isObjectStream ? (ObjectOutputStream) os : new ObjectOutputStream(os);
            out.writeObject(object);
        } catch (IOException ex) {
            throw new SerializationException("Failed to serialize object", ex);
        } finally {
            //Prevent close stream. Stream closed only by:
            //com.haulmont.cuba.core.sys.remoting.HttpServiceExporter,
            //com.haulmont.cuba.core.sys.remoting.ClusteredHttpInvokerRequestExecutor()
            //Only flush buffer to output stream
            if (!isObjectStream && out != null) {
                try {
                    out.flush();
                } catch (IOException ex) {
                    throw new SerializationException("Failed to serialize object", ex);
                }
            }
        }
    }

    //To work properly must itself be loaded by the application classloader (i.e. by classloader capable of loading
    //all the other application classes). For web application it means placing this class inside webapp folder.
    @Override
    public Object deserialize(InputStream is) {
        try {
            ObjectInputStream ois;
            boolean isObjectStream = is instanceof ObjectInputStream;
            if (isObjectStream) {
                ois = (ObjectInputStream) is;
            } else {
                ois = new ObjectInputStream(is) {
                    @Override
                    protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
                        return ClassUtils.getClass(StandardSerialization.class.getClassLoader(), desc.getName());
                    }
                };
            }
            return ois.readObject();
        } catch (IOException ex) {
            throw new SerializationException("Failed to deserialize object", ex);
        } catch (ClassNotFoundException ex) {
            throw new SerializationException("Failed to deserialize object type", ex);
        }
    }

    @Override
    public byte[] serialize(Object object) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        serialize(object, bos);
        return bos.toByteArray();
    }

    @Override
    public Object deserialize(byte[] bytes) {
        if (bytes == null) {
            return null;
        }

        return deserialize(new ByteArrayInputStream(bytes));
    }
}
