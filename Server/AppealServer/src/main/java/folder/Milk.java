package edu.asu.mscs.ashastry.appealserver.model;

import javax.xml.bind.annotation.XmlEnumValue;

public enum Milk {
    @XmlEnumValue(value="whole")
    WHOLE,
    @XmlEnumValue(value="skim")
    SKIM,
    @XmlEnumValue(value="semi")
    SEMI,
    @XmlEnumValue(value="none")
    NONE
}
