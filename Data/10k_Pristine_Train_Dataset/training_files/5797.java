package org.zstack.test.deployer.schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for SecurityGroupUnion complex type.
 * <p>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;complexType name="SecurityGroupUnion">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="securityGroup" type="{http://zstack.org/schema/zstack}SecurityGroupConfig" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SecurityGroupUnion", propOrder = {
        "securityGroup"
})
public class SecurityGroupUnion {

    protected List<SecurityGroupConfig> securityGroup;

    /**
     * Gets the value of the securityGroup property.
     * <p>
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the securityGroup property.
     * <p>
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSecurityGroup().add(newItem);
     * </pre>
     * <p>
     * <p>
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SecurityGroupConfig }
     */
    public List<SecurityGroupConfig> getSecurityGroup() {
        if (securityGroup == null) {
            securityGroup = new ArrayList<SecurityGroupConfig>();
        }
        return this.securityGroup;
    }

}
