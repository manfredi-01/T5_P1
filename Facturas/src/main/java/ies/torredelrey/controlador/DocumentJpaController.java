/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ies.torredelrey.controlador;

import ies.torredelrey.controlador.exceptions.IllegalOrphanException;
import ies.torredelrey.controlador.exceptions.NonexistentEntityException;
import ies.torredelrey.controlador.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import ies.torredelrey.modelo.Address;
import ies.torredelrey.modelo.Document;
import ies.torredelrey.modelo.Positions;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author usuario
 */
public class DocumentJpaController implements Serializable {

    public DocumentJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Document document) throws PreexistingEntityException, Exception {
        if (document.getPositionsCollection() == null) {
            document.setPositionsCollection(new ArrayList<Positions>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Address addressid = document.getAddressid();
            if (addressid != null) {
                addressid = em.getReference(addressid.getClass(), addressid.getId());
                document.setAddressid(addressid);
            }
            Collection<Positions> attachedPositionsCollection = new ArrayList<Positions>();
            for (Positions positionsCollectionPositionsToAttach : document.getPositionsCollection()) {
                positionsCollectionPositionsToAttach = em.getReference(positionsCollectionPositionsToAttach.getClass(), positionsCollectionPositionsToAttach.getPositionsPK());
                attachedPositionsCollection.add(positionsCollectionPositionsToAttach);
            }
            document.setPositionsCollection(attachedPositionsCollection);
            em.persist(document);
            if (addressid != null) {
                addressid.getDocumentCollection().add(document);
                addressid = em.merge(addressid);
            }
            for (Positions positionsCollectionPositions : document.getPositionsCollection()) {
                Document oldDocumentOfPositionsCollectionPositions = positionsCollectionPositions.getDocument();
                positionsCollectionPositions.setDocument(document);
                positionsCollectionPositions = em.merge(positionsCollectionPositions);
                if (oldDocumentOfPositionsCollectionPositions != null) {
                    oldDocumentOfPositionsCollectionPositions.getPositionsCollection().remove(positionsCollectionPositions);
                    oldDocumentOfPositionsCollectionPositions = em.merge(oldDocumentOfPositionsCollectionPositions);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findDocument(document.getId()) != null) {
                throw new PreexistingEntityException("Document " + document + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Document document) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Document persistentDocument = em.find(Document.class, document.getId());
            Address addressidOld = persistentDocument.getAddressid();
            Address addressidNew = document.getAddressid();
            Collection<Positions> positionsCollectionOld = persistentDocument.getPositionsCollection();
            Collection<Positions> positionsCollectionNew = document.getPositionsCollection();
            List<String> illegalOrphanMessages = null;
            for (Positions positionsCollectionOldPositions : positionsCollectionOld) {
                if (!positionsCollectionNew.contains(positionsCollectionOldPositions)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Positions " + positionsCollectionOldPositions + " since its document field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (addressidNew != null) {
                addressidNew = em.getReference(addressidNew.getClass(), addressidNew.getId());
                document.setAddressid(addressidNew);
            }
            Collection<Positions> attachedPositionsCollectionNew = new ArrayList<Positions>();
            for (Positions positionsCollectionNewPositionsToAttach : positionsCollectionNew) {
                positionsCollectionNewPositionsToAttach = em.getReference(positionsCollectionNewPositionsToAttach.getClass(), positionsCollectionNewPositionsToAttach.getPositionsPK());
                attachedPositionsCollectionNew.add(positionsCollectionNewPositionsToAttach);
            }
            positionsCollectionNew = attachedPositionsCollectionNew;
            document.setPositionsCollection(positionsCollectionNew);
            document = em.merge(document);
            if (addressidOld != null && !addressidOld.equals(addressidNew)) {
                addressidOld.getDocumentCollection().remove(document);
                addressidOld = em.merge(addressidOld);
            }
            if (addressidNew != null && !addressidNew.equals(addressidOld)) {
                addressidNew.getDocumentCollection().add(document);
                addressidNew = em.merge(addressidNew);
            }
            for (Positions positionsCollectionNewPositions : positionsCollectionNew) {
                if (!positionsCollectionOld.contains(positionsCollectionNewPositions)) {
                    Document oldDocumentOfPositionsCollectionNewPositions = positionsCollectionNewPositions.getDocument();
                    positionsCollectionNewPositions.setDocument(document);
                    positionsCollectionNewPositions = em.merge(positionsCollectionNewPositions);
                    if (oldDocumentOfPositionsCollectionNewPositions != null && !oldDocumentOfPositionsCollectionNewPositions.equals(document)) {
                        oldDocumentOfPositionsCollectionNewPositions.getPositionsCollection().remove(positionsCollectionNewPositions);
                        oldDocumentOfPositionsCollectionNewPositions = em.merge(oldDocumentOfPositionsCollectionNewPositions);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = document.getId();
                if (findDocument(id) == null) {
                    throw new NonexistentEntityException("The document with id " + id + " no longer exists.");
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
            Document document;
            try {
                document = em.getReference(Document.class, id);
                document.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The document with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Positions> positionsCollectionOrphanCheck = document.getPositionsCollection();
            for (Positions positionsCollectionOrphanCheckPositions : positionsCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Document (" + document + ") cannot be destroyed since the Positions " + positionsCollectionOrphanCheckPositions + " in its positionsCollection field has a non-nullable document field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Address addressid = document.getAddressid();
            if (addressid != null) {
                addressid.getDocumentCollection().remove(document);
                addressid = em.merge(addressid);
            }
            em.remove(document);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Document> findDocumentEntities() {
        return findDocumentEntities(true, -1, -1);
    }

    public List<Document> findDocumentEntities(int maxResults, int firstResult) {
        return findDocumentEntities(false, maxResults, firstResult);
    }

    private List<Document> findDocumentEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Document.class));
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

    public Document findDocument(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Document.class, id);
        } finally {
            em.close();
        }
    }

    public int getDocumentCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Document> rt = cq.from(Document.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
