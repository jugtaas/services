package org.jugtaas.services.zoefxports.rest.json;

import org.codehaus.jackson.map.ObjectMapper;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;

/**
 * User: tiziano
 * Date: 18/11/14
 * Time: 10:05
 */
@Provider
@Produces(MediaType.APPLICATION_JSON)
public class ListMessageBodyWriter<T> implements MessageBodyWriter<T> {
    @Override
    public boolean isWriteable(Class aClass, Type type, Annotation[] annotations, MediaType mediaType) {
        return "java.util.ArrayList".equals(aClass.getName());
    }

    @Override
    public long getSize(Object o, Class aClass, Type type, Annotation[] annotations, MediaType mediaType) {
        return 0;
    }

    @Override
    public void writeTo(Object o, Class aClass, Type type, Annotation[] annotations, MediaType mediaType, MultivaluedMap multivaluedMap, OutputStream outputStream) throws IOException, WebApplicationException {
        ObjectMapper mapper = new ObjectMapper();
        if( o instanceof List ) {
            String out = "[";
            List<T> entities = (List<T>) o;
            //outputStream.write("[".getBytes());
            Boolean first = Boolean.TRUE;
            for (T entity : entities) {
                if (first) {
                    first = Boolean.FALSE;
                } else {
                    //outputStream.write(",".getBytes());
                    out += ",";
                }
                //mapper.writeValue(outputStream, entity);
                out += mapper.writeValueAsString(entity);
            }
            //outputStream.write("]".getBytes());
            out += "]";
            outputStream.write(out.getBytes());
        } else {
            mapper.writeValue(outputStream, o);
        }
    }
}
