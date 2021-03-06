package org.jugtaas.services.zoefxports.rest;

import org.jugtaas.services.zoefxports.db.Database;
import org.jugtaas.services.zoefxports.db.Manager;
import org.jugtaas.services.zoefxports.IOC;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.*;

/**
 * User: tiziano
 * Date: 18/11/14
 * Time: 17:34
 */
public abstract class AbstractResource<T> implements Resource<T> {


    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("id") Long id, @Context UriInfo uriInfo){
        T entity = getManager().get(id);
        if( entity==null ){
            RestHelper.notFound(uriInfo);
        }
        return RestHelper.GET(entity, uriInfo);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@QueryParam("filterBy") String filterBy,
                        @QueryParam("orderBy") String orderBy,
                        @QueryParam("startIndex") Integer startIndex,
                        @QueryParam("size") Integer size,
                        @Context UriInfo uriInfo){

        Map<String, Object> criteria = new HashMap<String, Object>();
        List<String> orderBys = new ArrayList<String>();
        List<Boolean> reverses = new ArrayList<Boolean>();

        // filterBy
        if( filterBy != null ){

            // ex. filterBy=chiamato+eq+true:bool,numero+gt+2:int

            // split filters: +eq+|+ne+|+gt+|+ge+|+lt+|+le+
            String[] filters = filterBy.split(",");
            for( String filter: filters ){

                // split operator
                String field=null;
                String value=null;
                String type=null;
                Operator operator=null;

                if( filter.contains(" ") ) { // XXX: contains +??+ unique
                    for (Operator o : Operator.values()) {
                        String andOpAnd = " " + o.name() + " ";
                        if (filter.contains(andOpAnd)) {
                            String[] fieldAndValue = filter.split(andOpAnd);
                            if (fieldAndValue.length == 2) {
                                field = fieldAndValue[0];
                                String[] valueAndType = fieldAndValue[1].split(":");
                                value = valueAndType[0];
                                if (valueAndType.length == 2) {
                                    type = valueAndType[1];
                                } else {
                                    type = "bool";
                                }
                            }
                            operator = o;
                            break;
                        }
                    }
                } else {
                    field = filter;
                    type = "bool";
                    value = "true";
                    operator = Operator.eq;
                }

                List<Object> operatorAndValue = new ArrayList<Object>();
                operatorAndValue.add(operator);
                if( "bool".equals(type) ) {
                    if( "true".equals(value) ) {
                        operatorAndValue.add(Boolean.TRUE);
                    } else if( "false".equals(value) ){
                        operatorAndValue.add(Boolean.FALSE);
                    }
                } else if( "int".equals(type) ){
                    operatorAndValue.add(Integer.parseInt(value));
                } else if( "long".equals(type) ){
                    operatorAndValue.add(Long.parseLong(value));
                } else if( "string".equals(type) ){
                    operatorAndValue.add(value);
                } else if( "date".equals(type) ){
                    operatorAndValue.add(new Date(Long.parseLong(value)));
                }
                Integer n = 1;
                while( criteria.keySet().contains(field + "#" + n) ){
                    n++;
                }
                criteria.put(field + "#" + n, operatorAndValue);

            }

            String[] strings = filterBy.split(":");

            if( strings.length==1 ){
                // Boolean
                criteria.put(filterBy, Boolean.TRUE);
            } else if( strings.length==3 ) {
                String field = strings[0];
                String operator = strings[1];
                String value = strings[2];
                if( ":".equals(operator) ){
                    if( "true".equals(value) ){
                        criteria.put(filterBy, Boolean.TRUE);
                    } else if( "false".equals(value) ){
                        criteria.put(filterBy, Boolean.FALSE);
                    } else {
                        criteria.put(filterBy, value);
                    }
                }
            }


        }

        // orderBy
        if( orderBy != null ){
            for( String fieldAndDir: orderBy.split(",") ){
                String field;
                Boolean reverse;
                if( fieldAndDir.contains(":") ){
                    String[] split = fieldAndDir.split(":");
                    field = split[0];
                    reverse = "desc".equalsIgnoreCase(split[1]);
                } else {
                    field = fieldAndDir;
                    reverse = Boolean.FALSE;
                }
                orderBys.add(field);
                reverses.add(reverse);
            }
        }
        List<T> entities = getManager().query(criteria, orderBys, reverses, size, startIndex);
        List<String> links = new ArrayList<String>();
        String path = uriInfo.getAbsolutePath().toString();
        if( !path.endsWith("/") ) {
            path += "/";
        }
        for( T entity: entities ){
            String link = path + getManager().getId(entity).toString();
            links.add(link);
        }
        return RestHelper.GET(links, uriInfo);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(T object, @Context UriInfo uriInfo){
        T saved = getManager().save(object);
        return RestHelper.POST(saved, uriInfo);
    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") Long id, @Context UriInfo uriInfo){
        Manager<T> manager = getManager();
        T delenda = manager.get(id);
        if( delenda==null ){
            return RestHelper.notFound(uriInfo);
        }
        manager.delete(delenda);
        return RestHelper.DELETE(delenda, uriInfo);
    }

    public Manager<T> getManager() {
        return IOC.queryUtility(Database.class).createManager(getEntityClass());
    }

    @PUT
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response put(@PathParam("id") Long id, T object, @Context UriInfo uriInfo){
        Manager<T> manager = getManager();
        T entity = manager.get(id);
        if( entity==null ){
            RestHelper.notFound(uriInfo);
        }
        Object id1 = manager.getId(object);
        if( id1==null || !id1.equals(id)){
            // XXX: id mandatory in json!
            RestHelper.notFound(uriInfo);
        }
        manager.save(object);
        return RestHelper.PUT(object, uriInfo);
    }

}
