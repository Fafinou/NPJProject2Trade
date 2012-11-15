/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tradingpf;

import java.io.Serializable;

/**
 *
 * @author Zoé Bellot
 * @author Simon Cathébras
 */
public class Wish implements Serializable{
    private TraderItf follower;
    private String objectName;
    private Integer objectPrice;
    private String followerName;

    /**
     * Creates a new wish.
     * @param follower Interface of the wisher
     * @param followerName Name of the wisher
     * @param objectName Name of the object
     * @param objectPrice Price of the object
     */
    public Wish(TraderItf follower, String followerName, String objectName, Integer objectPrice) {
        this.follower = follower;
        this.objectName = objectName;
        this.objectPrice = objectPrice;
        this.followerName = followerName;
    }

    public String getFollowerName() {
        return followerName;
    }

    public void setFollowerName(String followerName) {
        this.followerName = followerName;
    }

    
    public TraderItf getFollower() {
        return follower;
    }

    public void setFollower(TraderItf follower) {
        this.follower = follower;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public Integer getObjectPrice() {
        return objectPrice;
    }

    public void setObjectPrice(Integer objectPrice) {
        this.objectPrice = objectPrice;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Wish other = (Wish) obj;
        if (this.follower != other.follower && (this.follower == null || !this.follower.equals(other.follower))) {
            return false;
        }
        if ((this.objectName == null) ? (other.objectName != null) : !this.objectName.equals(other.objectName)) {
            return false;
        }
        if (this.objectPrice != other.objectPrice && (this.objectPrice == null || !this.objectPrice.equals(other.objectPrice))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (this.follower != null ? this.follower.hashCode() : 0);
        hash = 97 * hash + (this.objectName != null ? this.objectName.hashCode() : 0);
        hash = 97 * hash + (this.objectPrice != null ? this.objectPrice.hashCode() : 0);
        return hash;
    }
    
    
    
}
