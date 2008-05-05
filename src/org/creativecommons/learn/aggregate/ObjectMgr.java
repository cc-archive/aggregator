/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.creativecommons.learn.aggregate;

import org.creativecommons.learn.aggregate.handlers.TripleStore;
import org.creativecommons.learn.aggregate.handlers.OerCloud;
import com.sun.syndication.feed.synd.SyndEntry;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import org.creativecommons.learn.oercloud.OerFeed;

/**
 *
 * @author nathan
 */
public class ObjectMgr {
    
    private static ObjectMgr mgr = new ObjectMgr();
    private OerCloud oercloud = new OerCloud();
    private TripleStore triplestore = new TripleStore();
    
    private EntityManagerFactory emf;
    private EntityManager em;

    private ObjectMgr() {
        emf = Persistence.createEntityManagerFactory("OerCloudPU");
        em = emf.createEntityManager();
    }
    
    public static ObjectMgr get() {
        return mgr;
    } // get
    
    public EntityManager getEm() {
        return em;
    }
    
    List<OerFeed> getAllFeeds() {
        Query q = em.createQuery("select f from OerFeed f");
        return q.getResultList();
    }

    // Registration/Dispatch methods    
    public void updateEntry(OerFeed feed, SyndEntry entry) {
        // XXX: Right now this just serves as a single point of dispatch
        // for our handlers (oer cloud, triple store, etc).  In the future
        // it should probably be significantly less tightly coupled
        this.oercloud.updateEntry(feed, entry);
        this.triplestore.updateEntry(feed, entry);
        
    } // updateEntry

} // ObjectMgr
