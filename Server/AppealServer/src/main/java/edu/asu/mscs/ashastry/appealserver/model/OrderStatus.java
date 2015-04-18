package edu.asu.mscs.ashastry.appealserver.model;

import javax.xml.bind.annotation.XmlEnumValue;


public enum OrderStatus {
    @XmlEnumValue(value="unpaid")
    UNPAID,
    @XmlEnumValue(value="preparing")
    PREPARING, 
    @XmlEnumValue(value="ready")
    READY, 
    @XmlEnumValue(value="taken")
    TAKEN
}
