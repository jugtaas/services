package org.jugtaas.services.resources;

import org.jugtaas.services.entities.Person;
import org.jugtaas.services.zoefxports.rest.AbstractResource;

import javax.ws.rs.Path;

/**
 * User: tiziano
 * Date: 19/02/15
 * Time: 15:18
 */
@Path("persons")
public class PersonResource extends AbstractResource<Person> {

    @Override
    public Class getEntityClass() {
        return Person.class;
    }

}
