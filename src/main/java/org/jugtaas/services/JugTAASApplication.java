package org.jugtaas.services;

import org.jugtaas.services.zoefxports.IOC;
import org.jugtaas.services.zoefxports.db.Database;
import org.jugtaas.services.zoefxports.db.JPADatabaseImpl;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashMap;
import java.util.Map;

/**
 * User: tiziano
 * Date: 19/02/15
 * Time: 15:29
 */
@ApplicationPath("/services")
public class JugTAASApplication extends Application {

    public JugTAASApplication() {

        Map<String, String> properties = new HashMap<String, String>();
        properties.put("hibernate.connection.username", System.getenv("OPENSHIFT_POSTGRESQL_DB_USERNAME"));
        properties.put("hibernate.connection.password", System.getenv("OPENSHIFT_POSTGRESQL_DB_PASSWORD"));
        properties.put("hibernate.connection.url", "jdbc:postgresql://"
                + System.getenv("OPENSHIFT_POSTGRESQL_DB_HOST") + ":"
                + System.getenv("OPENSHIFT_POSTGRESQL_DB_PORT") + "/services");

        Database db = new JPADatabaseImpl();
        db.open("jugtaasservices", properties);
        IOC.registerUtility(db, Database.class);

    }

}
