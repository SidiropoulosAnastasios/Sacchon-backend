package gr.codehub.team2.Sacchon.repository.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JpaUtil {

        private static final String PERSISTENCE_UNIT_NAME = "Sacchon";
        private static EntityManagerFactory factory;

        public static EntityManagerFactory getEntityManagerFactory() {
            if (factory == null) {
                factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
            }
            return factory;
        }

        public static EntityManager getEntityManager() {
            return getEntityManagerFactory().createEntityManager();
        }


        public static void shutdown() {
            if (factory != null) {
                factory.close();
            }
        }


    public static EntityManager getEntityManager(EntityManager em) {
        return em;
    }
}
