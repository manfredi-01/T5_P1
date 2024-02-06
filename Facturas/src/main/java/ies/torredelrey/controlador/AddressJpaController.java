/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ies.torredelrey.controlador;

import ies.torredelrey.controlador.exceptions.IllegalOrphanException;
import ies.torredelrey.controlador.exceptions.NonexistentEntityException;
import ies.torredelrey.controlador.exceptions.PreexistingEntityException;
import ies.torredelrey.modelo.Address;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import ies.torredelrey.modelo.Document;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author usuario
 */
public class AddressJpaController implements Serializable {

    public AddressJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Address address) throws PreexistingEntityException, Exception {
        if (address.getDocumentCollection() == null) {
            address.setDocumentCollection(new ArrayList<Document>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Document> attachedDocumentCollection = new ArrayList<Document>();
            for (Document documentCollectionDocumentToAttach : address.getDocumentCollection()) {
                documentCollectionDocumentToAttach = em.getReference(documentCollectionDocumentToAttach.getClass(), documentCollectionDocumentToAttach.getId());
                attachedDocumentCollection.add(documentCollectionDocumentToAttach);
            }
            address.setDocumentCollection(attachedDocumentCollection);
            em.persist(address);
            for (Document documentCollectionDocument : address.getDocumentCollection()) {
                Address oldAddressidOfDocumentCollectionDocument = documentCollectionDocument.getAddressid();
                documentCollectionDocument.setAddressid(address);
                documentCollectionDocument = em.merge(documentCollectionDocument);
                if (oldAddressidOfDocumentCollectionDocument != null) {
                    oldAddressidOfDocumentCollectionDocument.getDocumentCollection().remove(documentCollectionDocument);
                    oldAddressidOfDocumentCollectionDocument = em.merge(oldAddressidOfDocumentCollectionDocument);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findAddress(address.getId()) != null) {
                throw new PreexistingEntityException("Address " + address + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Address address) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Address persistentAddress = em.find(Address.class, address.getId());
            Collection<Document> documentCollectionOld = persistentAddress.getDocumentCollection();
            Collection<Document> documentCollectionNew = address.getDocumentCollection();
            List<String> illegalOrphanMessages = null;
            for (Document documentCollectionOldDocument : documentCollectionOld) {
                if (!documentCollectionNew.contains(documentCollectionOldDocument)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Document " + documentCollectionOldDocument + " since its addressid field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Document> attachedDocumentCollectionNew = new ArrayList<Document>();
            for (Document documentCollectionNewDocumentToAttach : documentCollectionNew) {
                documentCollectionNewDocumentToAttach = em.getReference(documentCollectionNewDocumentToAttach.getClass(), documentCollectionNewDocumentToAttach.getId());
                attachedDocumentCollectionNew.add(documentCollectionNewDocumentToAttach);
            }
            documentCollectionNew = attachedDocumentCollectionNew;
            address.setDocumentCollection(documentCollectionNew);
            address = em.merge(address);
            for (Document documentCollectionNewDocument : documentCollectionNew) {
                if (!documentCollectionOld.contains(documentCollectionNewDocument)) {
                    Address oldAddressidOfDocumentCollectionNewDocument = documentCollectionNewDocument.getAddressid();
                    documentCollectionNewDocument.setAddressid(address);
                    documentCollectionNewDocument = em.merge(documentCollectionNewDocument);
                    if (oldAddressidOfDocumentCollectionNewDocument != null && !oldAddressidOfDocumentCollectionNewDocument.equals(address)) {
                        oldAddressidOfDocumentCollectionNewDocument.getDocumentCollection().remove(documentCollectionNewDocument);
                        oldAddressidOfDocumentCollectionNewDocument = em.merge(oldAddressidOfDocumentCollectionNewDocument);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = address.getId();
                if (findAddress(id) == null) {
                    throw new NonexistentEntityException("The address with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Address address;
            try {
                address = em.getReference(Address.class, id);
                address.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The address with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Document> documentCollectionOrphanCheck = address.getDocumentCollection();
            for (Document documentCollectionOrphanCheckDocument : documentCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Address (" + address + ") cannot be destroyed since the Document " + documentCollectionOrphanCheckDocument + " in its documentCollection field has a non-nullable addressid field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(address);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Address> findAddressEntities() {
        return findAddressEntities(true, -1, -1);
    }

    public List<Address> findAddressEntities(int maxResults, int firstResult) {
        return findAddressEntities(false, maxResults, firstResult);
    }

    private List<Address> findAddressEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Address.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Address findAddress(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Address.class, id);
        } finally {
            em.close();
        }
    }

    public int getAddressCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Address> rt = cq.from(Address.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
