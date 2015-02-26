package org.jugtaas.services.zoefxports.rest;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

/**
 * User: tiziano
 * Date: 17/11/14
 * Time: 14:53
 */
public class RestHelper {

    public static Response GET(Object entity, UriInfo uriInfo){
        UriBuilder builder = uriInfo.getAbsolutePathBuilder();
        URI uri = builder.build();
        Response response = Response
                .status(Response.Status.OK)
                .link(uri, "self")
                .entity(entity)
                .build();
        return response;
    }

    public static Response GET(List entities, UriInfo uriInfo){
        UriBuilder builder = uriInfo.getAbsolutePathBuilder();
        URI uri = builder.build();
        Response response = Response
                .status(Response.Status.OK)
                .link(uri, "self")
                .entity(entities)
                .build();
        return response;
    }

    public static Response POST(Object entity, UriInfo uriInfo){
        UriBuilder builder = uriInfo.getAbsolutePathBuilder();
        URI uri = builder.build();
        Response response = Response
                .status(Response.Status.CREATED)
                .link(uri, "self")
                .entity(entity)
                .build();
        return response;
    }

    public static Response DELETE(Object entity, UriInfo uriInfo){
        Response response = Response
                .status(Response.Status.NO_CONTENT)
                .build();
        return response;
    }

    public static Response PUT(Object entity, UriInfo uriInfo){
        UriBuilder builder = uriInfo.getAbsolutePathBuilder();
        URI uri = builder.build();
        Response response = Response
                .status(Response.Status.OK)
                .link(uri, "self")
                .entity(entity)
                .build();
        return response;
    }

}
