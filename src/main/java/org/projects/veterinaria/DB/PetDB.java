package org.projects.veterinaria.DB;

import jakarta.persistence.EntityManager;
import org.projects.veterinaria.entity.Pet;
import org.projects.veterinaria.util.UtilEntity;

import java.util.List;
import java.util.UUID;

public class PetDB {
    EntityManager em = UtilEntity.getEntityManager();

    public List<Pet> getAllPets () {
        return em.createQuery("SELECT P FROM pets p", Pet.class).getResultList();
    }

    public Pet getPet (String id) {
        return em.find(Pet.class, id);
    }

    public void createPet (Pet newPet) {
        newPet.setId(UUID.randomUUID().toString());
        em.getTransaction().begin();
        em.merge(newPet);
        em.getTransaction().commit();
    }

    public void updatePet (Pet pet) {
        Pet newPet = getPet(pet.getId());
        newPet.setOwnerId(pet.getOwnerId());
        newPet.setPetName(pet.getPetName());
        newPet.setType(pet.getType());
        newPet.setRace(pet.getRace());
        newPet.setColor(pet.getColor());
        newPet.setGender(pet.getGender());
        newPet.setAge(pet.getAge());

        em.getTransaction().begin();
        em.merge(newPet);
        em.getTransaction().commit();
    }

    public void deletePet (String id) {
        Pet removePet = getPet(id);

        em.getTransaction().begin();
        em.remove(removePet);
        em.getTransaction().commit();
    }
}
