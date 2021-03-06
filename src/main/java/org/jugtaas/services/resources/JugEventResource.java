package org.jugtaas.services.resources;

import org.jugtaas.services.entities.JugEvent;
import org.jugtaas.services.zoefxports.rest.AbstractResource;

import javax.ws.rs.*;

/**
 * User: tiziano
 * Date: 19/02/15
 * Time: 15:18
 */
@Path("events")
public class JugEventResource  extends AbstractResource<JugEvent> {

    @Override
    public Class getEntityClass() {
        return JugEvent.class;
    }

}
