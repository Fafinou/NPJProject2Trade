/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tradingpf;

import java.io.Serializable;

/**
 *
 * @author fingolfin
 */
public class Wish implements Serializable{
    private TraderItf follower;
    private String objectName;
    private Integer objectPrice;

    public Wish(TraderItf follower, String objectName, Integer objectPrice) {
        this.follower = follower;
        this.objectName = objectName;
        this.objectPrice = objectPrice;
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
    
}
