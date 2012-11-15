/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tradingpf;

import java.io.Serializable;

/**
 *
 * The class item records the name, the price and the seller of this item
 * @author Zoé Bellot
 * @author Simon Cathébras
 */
public class Item implements Serializable {
    private String name;
    private Integer price;
    private String sellerName;

    /**
     * Creates a new Item
     * @param name the name of the item
     * @param price the price of the item
     * @param sellerName the name of the item's seller
     */
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
    
    @Override
    public boolean equals(Object item) {
        if (item instanceof Item) {
        return (((this.name).equals(((Item)item).name)) &&
                (this.price.equals(((Item)item).price)) &&
                ((this.sellerName).equals(((Item)item).sellerName))); 
        } 
        return false;
    }
}
