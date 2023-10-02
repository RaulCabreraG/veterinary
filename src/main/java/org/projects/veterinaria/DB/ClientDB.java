package org.projects.veterinaria.DB;

import jakarta.persistence.EntityManager;
import org.projects.veterinaria.entity.Client;
import org.projects.veterinaria.entity.Pet;
import org.projects.veterinaria.util.UtilEntity;

import java.util.List;
import java.util.UUID;

public class ClientDB {
    EntityManager em = UtilEntity.getEntityManager();
    public List<Client> getAllClients () {
        return em.createQuery("SELECT c FROM Client c", Client.class).getResultList();
    }

    public Client getClient (String id) {
        return em.find(Client.class, id);
    }

    public void createClient (Client newClient) {
        newClient.setId(UUID.randomUUID().toString());
        em.getTransaction().begin();
        em.merge(newClient);
        em.getTransaction().commit();
    }

    public void updateClient (Client client) {
        Client clientUpdate = getClient(client.getId());
        clientUpdate.setName(client.getName());
        clientUpdate.setLastName(client.getLastName());
        clientUpdate.setAddress(client.getAddress());
        clientUpdate.setRefNumber1(client.getRefNumber1());;
        clientUpdate.setRefNumber2(client.getRefNumber2());

        em.getTransaction().begin();
        em.merge(clientUpdate);
        em.getTransaction().commit();
    }

    public void deleteClient (String id) {
        Client removeClient = getClient(id);

        em.getTransaction().begin();
        em.remove(removeClient);
        em.getTransaction().commit();
    }

    public List<Pet> getPets (String id) {
        Client ownerClient = getClient(id);
        List<Pet> pets = em.createQuery("SELECT p FROM Pet p WHERE p.ownerId = :ownerId", Pet.class)
                .setParameter("ownerId", ownerClient)
                .getResultList();
        pets.forEach(System.out::println);
        return pets;
    }
}
