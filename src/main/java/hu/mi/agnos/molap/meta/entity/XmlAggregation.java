/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.mi.agnos.molap.meta.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author parisek
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class XmlAggregation {

    @JacksonXmlProperty(isAttribute = true)
    private String measureName;

    @JacksonXmlProperty(isAttribute = true)
    private String hierarchyName; 

    @JacksonXmlProperty(isAttribute = true)
    private String aggregationFunction;
    
    @Override
    public String toString() {
        return "XmlAggregation{" + "measureName=" + measureName + ", hierarchyName=" + hierarchyName + ", aggregationFunction=" + aggregationFunction + '}';
    }
    
    

    
}
