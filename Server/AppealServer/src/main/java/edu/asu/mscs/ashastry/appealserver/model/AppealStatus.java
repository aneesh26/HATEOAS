package edu.asu.mscs.ashastry.appealserver.model;

import javax.xml.bind.annotation.XmlEnumValue;


public enum AppealStatus {
    @XmlEnumValue(value="start")
    START,
    @XmlEnumValue(value="pending")
    PENDING, 
    @XmlEnumValue(value="completed")
    COMPLETED, 
    @XmlEnumValue(value="withdrawn")
    WITHDRAWN
}
