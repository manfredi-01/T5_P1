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
import ies.torredelrey.modelo.Positions;
import ies.torredelrey.modelo.Product;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author usuario
 */
public class ProductJpaController implements Serializable {

    public ProductJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Product product) throws PreexistingEntityException, Exception {
        if (product.getPositionsCollection() == null) {
            product.setPositionsCollection(new ArrayList<Positions>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Positions> attachedPositionsCollection = new ArrayList<Positions>();
            for (Positions positionsCollectionPositionsToAttach : product.getPositionsCollection()) {
                positionsCollectionPositionsToAttach = em.getReference(positionsCollectionPositionsToAttach.getClass(), positionsCollectionPositionsToAttach.getPositionsPK());
                attachedPositionsCollection.add(positionsCollectionPositionsToAttach);
            }
            product.setPositionsCollection(attachedPositionsCollection);
            em.persist(product);
            for (Positions positionsCollectionPositions : product.getPositionsCollection()) {
                Product oldProductOfPositionsCollectionPositions = positionsCollectionPositions.getProduct();
                positionsCollectionPositions.setProduct(product);
                positionsCollectionPositions = em.merge(positionsCollectionPositions);
                if (oldProductOfPositionsCollectionPositions != null) {
                    oldProductOfPositionsCollectionPositions.getPositionsCollection().remove(positionsCollectionPositions);
                    oldProductOfPositionsCollectionPositions = em.merge(oldProductOfPositionsCollectionPositions);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findProduct(product.getId()) != null) {
                throw new PreexistingEntityException("Product " + product + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Product product) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Product persistentProduct = em.find(Product.class, product.getId());
            Collection<Positions> positionsCollectionOld = persistentProduct.getPositionsCollection();
            Collection<Positions> positionsCollectionNew = product.getPositionsCollection();
            List<String> illegalOrphanMessages = null;
            for (Positions positionsCollectionOldPositions : positionsCollectionOld) {
                if (!positionsCollectionNew.contains(positionsCollectionOldPositions)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Positions " + positionsCollectionOldPositions + " since its product field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Positions> attachedPositionsCollectionNew = new ArrayList<Positions>();
            for (Positions positionsCollectionNewPositionsToAttach : positionsCollectionNew) {
                positionsCollectionNewPositionsToAttach = em.getReference(positionsCollectionNewPositionsToAttach.getClass(), positionsCollectionNewPositionsToAttach.getPositionsPK());
                attachedPositionsCollectionNew.add(positionsCollectionNewPositionsToAttach);
            }
            positionsCollectionNew = attachedPositionsCollectionNew;
            product.setPositionsCollection(positionsCollectionNew);
            product = em.merge(product);
            for (Positions positionsCollectionNewPositions : positionsCollectionNew) {
                if (!positionsCollectionOld.contains(positionsCollectionNewPositions)) {
                    Product oldProductOfPositionsCollectionNewPositions = positionsCollectionNewPositions.getProduct();
                    positionsCollectionNewPositions.setProduct(product);
                    positionsCollectionNewPositions = em.merge(positionsCollectionNewPositions);
                    if (oldProductOfPositionsCollectionNewPositions != null && !oldProductOfPositionsCollectionNewPositions.equals(product)) {
                        oldProductOfPositionsCollectionNewPositions.getPositionsCollection().remove(positionsCollectionNewPositions);
                        oldProductOfPositionsCollectionNewPositions = em.merge(oldProductOfPositionsCollectionNewPositions);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = product.getId();
                if (findProduct(id) == null) {
                    throw new NonexistentEntityException("The product with id " + id + " no longer exists.");
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
            Product product;
            try {
                product = em.getReference(Product.class, id);
                product.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The product with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Positions> positionsCollectionOrphanCheck = product.getPositionsCollection();
            for (Positions positionsCollectionOrphanCheckPositions : positionsCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Product (" + product + ") cannot be destroyed since the Positions " + positionsCollectionOrphanCheckPositions + " in its positionsCollection field has a non-nullable product field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(product);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Product> findProductEntities() {
        return findProductEntities(true, -1, -1);
    }

    public List<Product> findProductEntities(int maxResults, int firstResult) {
        return findProductEntities(false, maxResults, firstResult);
    }

    private List<Product> findProductEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Product.class));
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

    public Product findProduct(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Product.class, id);
        } finally {
            em.close();
        }
    }

    public int getProductCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Product> rt = cq.from(Product.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
