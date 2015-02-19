package org.jugtaas.services.zoefxports.db;

import java.util.Map;

/**
 * User: tiziano
 * Date: 17/11/14
 * Time: 10:41
 */
public interface Database {

    void open(String persistenceUnit);
    void open(String persistenceUnit, Map<String, String> properties);
    public <E> Manager<E> createManager(Class<E> klass);

}
