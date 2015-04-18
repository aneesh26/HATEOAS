package edu.asu.mscs.ashastry.appealserver.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import edu.asu.mscs.ashastry.appealserver.representations.Representation;

@XmlRootElement
public class Item {
    @XmlElement(namespace = Representation.APPEALS_NAMESPACE)
    private Milk milk;
    @XmlElement(namespace = Representation.APPEALS_NAMESPACE)
    private Size size;
    @XmlElement(namespace = Representation.APPEALS_NAMESPACE)
    private Drink drink;
    
    /**
     * For JAXB :-(
     */
    Item(){}
    
    public Item(Size size, Milk milk, Drink drink) {
        this.milk = milk;
        this.size = size;
        this.drink = drink;       
    }
    
    public Milk getMilk() {
        return milk;
    }

    public Size getSize() {
        return size;
    }

    public Drink getDrink() {
        return drink;
    }
    
    public String toString() {
        return size + " " + milk + " " + drink;
    }
}