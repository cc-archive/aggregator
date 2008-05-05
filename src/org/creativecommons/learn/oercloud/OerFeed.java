/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.creativecommons.learn.oercloud;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.net.URL;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import org.creativecommons.learn.aggregate.ObjectMgr;

/**
 *
 * @author nathan
 */
@Entity
@Table(name = "oer_feeds")
@NamedQueries({@NamedQuery(name = "OerFeed.findById", query = "SELECT o FROM OerFeed o WHERE o.id = :id"), @NamedQuery(name = "OerFeed.findByUrl", query = "SELECT o FROM OerFeed o WHERE o.url = :url"), @NamedQuery(name = "OerFeed.findByLastImport", query = "SELECT o FROM OerFeed o WHERE o.lastImport = :lastImport"), @NamedQuery(name = "OerFeed.findByFeedType", query = "SELECT o FROM OerFeed o WHERE o.feedType = :feedType")})
public class OerFeed implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "url")
    private String url;
    @Column(name = "last_import")
    private BigInteger lastImport;
    @Column(name = "feed_type")
    private String feedType;

    @ManyToOne()
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    public OerFeed() {
    }

    public OerFeed(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }


    public void poll() throws FeedException, IOException {
        // get the contents of the feed and emit events for each
        
        // OPML
        if (this.getFeedType().equals("OPML")) {
            throw new UnsupportedOperationException("Not yet implemented");        
            
        } else {
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(new URL(this.getUrl())));
            
            List<SyndEntry> feed_entries = feed.getEntries();
            
            for (SyndEntry entry : feed_entries) {
                
                // emit an event with the entry information
                ObjectMgr.get().updateEntry(this, entry);
                System.out.println(entry.getTitle());
            } // for each entry
        }
    } // poll
    
    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getLastImportDate() {
        return new Date(this.getLastImport().intValue() * 1000);
    }
    
    public void setLastImportDate(Date lastImportDate) {
        this.setLastImport(BigInteger.valueOf(
                BigInteger.valueOf(lastImportDate.getTime()).intValue() / 1000
                ));
    }
    
    public BigInteger getLastImport() {
        return lastImport;
    }

    public void setLastImport(BigInteger lastImport) {
        this.lastImport = lastImport;
    }

    public String getFeedType() {
        return feedType;
    }

    public void setFeedType(String feedType) {
        this.feedType = feedType;
    }

    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OerFeed)) {
            return false;
        }
        OerFeed other = (OerFeed) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "nbtest.OerFeed[id=" + id + "]";
    }

}