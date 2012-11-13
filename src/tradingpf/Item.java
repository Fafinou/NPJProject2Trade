/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tradingpf;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author fingolfin
 */
public class Item implements Serializable {
    private String name;
    private Integer price;
    private String sellerName;

    public Item(String name, Integer price, String sellerName) {
        this.name = name;
        this.price = price;
        this.sellerName = sellerName;
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }
    
}
