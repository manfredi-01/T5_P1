/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ies.torredelrey.controlador;

import ies.torredelrey.controlador.exceptions.NonexistentEntityException;
import ies.torredelrey.controlador.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import ies.torredelrey.modelo.Document;
import ies.torredelrey.modelo.Positions;
import ies.torredelrey.modelo.PositionsPK;
import ies.torredelrey.modelo.Product;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author usuario
 */
public class PositionsJpaController implements Serializable {

    public PositionsJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Positions positions) throws PreexistingEntityException, Exception {
        if (positions.getPositionsPK() == null) {
            positions.setPositionsPK(new PositionsPK());
        }
        positions.getPositionsPK().setDocumentid(positions.getDocument().getId());
        positions.getPositionsPK().setProductid(positions.getProduct().getId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Document document = positions.getDocument();
            if (document != null) {
                document = em.getReference(document.getClass(), document.getId());
                positions.setDocument(document);
            }
            Product product = positions.getProduct();
            if (product != null) {
                product = em.getReference(product.getClass(), product.getId());
                positions.setProduct(product);
            }
            em.persist(positions);
            if (document != null) {
                document.getPositionsCollection().add(positions);
                document = em.merge(document);
            }
            if (product != null) {
                product.getPositionsCollection().add(positions);
                product = em.merge(product);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPositions(positions.getPositionsPK()) != null) {
                throw new PreexistingEntityException("Positions " + positions + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Positions positions) throws NonexistentEntityException, Exception {
        positions.getPositionsPK().setDocumentid(positions.getDocument().getId());
        positions.getPositionsPK().setProductid(positions.getProduct().getId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Positions persistentPositions = em.find(Positions.class, positions.getPositionsPK());
            Document documentOld = persistentPositions.getDocument();
            Document documentNew = positions.getDocument();
            Product productOld = persistentPositions.getProduct();
            Product productNew = positions.getProduct();
            if (documentNew != null) {
                documentNew = em.getReference(documentNew.getClass(), documentNew.getId());
                positions.setDocument(documentNew);
            }
            if (productNew != null) {
                productNew = em.getReference(productNew.getClass(), productNew.getId());
                positions.setProduct(productNew);
            }
            positions = em.merge(positions);
            if (documentOld != null && !documentOld.equals(documentNew)) {
                documentOld.getPositionsCollection().remove(positions);
                documentOld = em.merge(documentOld);
            }
            if (documentNew != null && !documentNew.equals(documentOld)) {
                documentNew.getPositionsCollection().add(positions);
                documentNew = em.merge(documentNew);
            }
            if (productOld != null && !productOld.equals(productNew)) {
                productOld.getPositionsCollection().remove(positions);
                productOld = em.merge(productOld);
            }
            if (productNew != null && !productNew.equals(productOld)) {
                productNew.getPositionsCollection().add(positions);
                productNew = em.merge(productNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                PositionsPK id = positions.getPositionsPK();
                if (findPositions(id) == null) {
                    throw new NonexistentEntityException("The positions with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(PositionsPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Positions positions;
            try {
                positions = em.getReference(Positions.class, id);
                positions.getPositionsPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The positions with id " + id + " no longer exists.", enfe);
            }
            Document document = positions.getDocument();
            if (document != null) {
                document.getPositionsCollection().remove(positions);
                document = em.merge(document);
            }
            Product product = positions.getProduct();
            if (product != null) {
                product.getPositionsCollection().remove(positions);
                product = em.merge(product);
            }
            em.remove(positions);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Positions> findPositionsEntities() {
        return findPositionsEntities(true, -1, -1);
    }

    public List<Positions> findPositionsEntities(int maxResults, int firstResult) {
        return findPositionsEntities(false, maxResults, firstResult);
    }

    private List<Positions> findPositionsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Positions.class));
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

    public Positions findPositions(PositionsPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Positions.class, id);
        } finally {
            em.close();
        }
    }

    public int getPositionsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Positions> rt = cq.from(Positions.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
