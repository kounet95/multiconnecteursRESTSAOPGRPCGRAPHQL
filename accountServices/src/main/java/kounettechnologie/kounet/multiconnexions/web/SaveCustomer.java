
package kounettechnologie.kounet.multiconnexions.web;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;



@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "saveCustomer", propOrder = {
    "customer"
})
public class SaveCustomer {

    protected CustomerRequest customer;

    /**
     * Obtient la valeur de la propriété customer.
     * 
     * @return
     *     possible object is
     *     {@link CustomerRequest }
     *     
     */
    public CustomerRequest getCustomer() {
        return customer;
    }

    /**
     * Définit la valeur de la propriété customer.
     * 
     * @param value
     *     allowed object is
     *     {@link CustomerRequest }
     *     
     */
    public void setCustomer(CustomerRequest value) {
        this.customer = value;
    }

}
