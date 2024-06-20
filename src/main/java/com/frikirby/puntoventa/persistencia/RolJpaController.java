package com.frikirby.puntoventa.persistencia;

import com.frikirby.puntoventa.logica.Rol;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.frikirby.puntoventa.logica.Usuario;
import com.frikirby.puntoventa.persistencia.exceptions.NonexistentEntityException;
import com.frikirby.puntoventa.persistencia.exceptions.PreexistingEntityException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class RolJpaController implements Serializable {

    public RolJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    public RolJpaController() {
        this.emf = Persistence.createEntityManagerFactory("puntoVentaPU");
    }    
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Rol rol) throws PreexistingEntityException, Exception {
        if (rol.getUsuarios() == null) {
            rol.setUsuarios(new ArrayList<Usuario>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Usuario> attachedUsuarios = new ArrayList<Usuario>();
            for (Usuario usuariosUsuarioToAttach : rol.getUsuarios()) {
                usuariosUsuarioToAttach = em.getReference(usuariosUsuarioToAttach.getClass(), usuariosUsuarioToAttach.getId_usuario());
                attachedUsuarios.add(usuariosUsuarioToAttach);
            }
            rol.setUsuarios(attachedUsuarios);
            em.persist(rol);
            for (Usuario usuariosUsuario : rol.getUsuarios()) {
                Rol oldMiRolOfUsuariosUsuario = usuariosUsuario.getMiRol();
                usuariosUsuario.setMiRol(rol);
                usuariosUsuario = em.merge(usuariosUsuario);
                if (oldMiRolOfUsuariosUsuario != null) {
                    oldMiRolOfUsuariosUsuario.getUsuarios().remove(usuariosUsuario);
                    oldMiRolOfUsuariosUsuario = em.merge(oldMiRolOfUsuariosUsuario);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findRol(rol.getId_rol()) != null) {
                throw new PreexistingEntityException("Rol " + rol + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Rol rol) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Rol persistentRol = em.find(Rol.class, rol.getId_rol());
            List<Usuario> usuariosOld = persistentRol.getUsuarios();
            List<Usuario> usuariosNew = rol.getUsuarios();
            List<Usuario> attachedUsuariosNew = new ArrayList<Usuario>();
            for (Usuario usuariosNewUsuarioToAttach : usuariosNew) {
                usuariosNewUsuarioToAttach = em.getReference(usuariosNewUsuarioToAttach.getClass(), usuariosNewUsuarioToAttach.getId_usuario());
                attachedUsuariosNew.add(usuariosNewUsuarioToAttach);
            }
            usuariosNew = attachedUsuariosNew;
            rol.setUsuarios(usuariosNew);
            rol = em.merge(rol);
            for (Usuario usuariosOldUsuario : usuariosOld) {
                if (!usuariosNew.contains(usuariosOldUsuario)) {
                    usuariosOldUsuario.setMiRol(null);
                    usuariosOldUsuario = em.merge(usuariosOldUsuario);
                }
            }
            for (Usuario usuariosNewUsuario : usuariosNew) {
                if (!usuariosOld.contains(usuariosNewUsuario)) {
                    Rol oldMiRolOfUsuariosNewUsuario = usuariosNewUsuario.getMiRol();
                    usuariosNewUsuario.setMiRol(rol);
                    usuariosNewUsuario = em.merge(usuariosNewUsuario);
                    if (oldMiRolOfUsuariosNewUsuario != null && !oldMiRolOfUsuariosNewUsuario.equals(rol)) {
                        oldMiRolOfUsuariosNewUsuario.getUsuarios().remove(usuariosNewUsuario);
                        oldMiRolOfUsuariosNewUsuario = em.merge(oldMiRolOfUsuariosNewUsuario);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = rol.getId_rol();
                if (findRol(id) == null) {
                    throw new NonexistentEntityException("The rol with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(int id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Rol rol;
            try {
                rol = em.getReference(Rol.class, id);
                rol.getId_rol();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The rol with id " + id + " no longer exists.", enfe);
            }
            List<Usuario> usuarios = rol.getUsuarios();
            for (Usuario usuariosUsuario : usuarios) {
                usuariosUsuario.setMiRol(null);
                usuariosUsuario = em.merge(usuariosUsuario);
            }
            em.remove(rol);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Rol> findRolEntities() {
        return findRolEntities(true, -1, -1);
    }

    public List<Rol> findRolEntities(int maxResults, int firstResult) {
        return findRolEntities(false, maxResults, firstResult);
    }

    private List<Rol> findRolEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Rol.class));
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

    public Rol findRol(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Rol.class, id);
        } finally {
            em.close();
        }
    }

    public int getRolCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Rol> rt = cq.from(Rol.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
