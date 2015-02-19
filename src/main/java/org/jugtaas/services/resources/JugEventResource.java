package org.jugtaas.services.resources;

import org.jugtaas.services.entities.JugEvent;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;

/**
 * User: tiziano
 * Date: 19/02/15
 * Time: 15:18
 */
@Path("events")
public class JugEventResource {

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("id") Long id, @Context UriInfo uriInfo){

        // dummy entity
        JugEvent dummyEvent = new JugEvent();
        dummyEvent.setId(id);
        dummyEvent.setTitle("Dummy title - " + id);
        dummyEvent.setSubtitle("Dummy sub-title");

        UriBuilder builder = uriInfo.getAbsolutePathBuilder();
        URI uri = builder.build();
        Response response = Response
                .status(javax.ws.rs.core.Response.Status.OK)
                .link(uri, "self")
                .entity(dummyEvent)
                .build();
        return response;
    }

}
