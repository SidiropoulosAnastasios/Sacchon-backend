package gr.codehub.team2.Sacchon.repository;

import gr.codehub.team2.Sacchon.model.*;
import gr.codehub.team2.Sacchon.repository.lib.Repository;

import javax.persistence.EntityManager;


public class ChiefDoctorRepository extends Repository<ChiefDoctor, Long> {

    private EntityManager entityManager;

    public ChiefDoctorRepository(EntityManager entityManager) {
        super(entityManager);
        this.entityManager = entityManager;
    }

    @Override
    public Class<ChiefDoctor> getEntityClass() {
        return ChiefDoctor.class;
    }

    @Override
    public String getEntityClassName() {
        return ChiefDoctor.class.getName();
    }

}
