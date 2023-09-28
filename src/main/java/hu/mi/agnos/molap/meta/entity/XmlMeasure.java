/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.mi.agnos.molap.meta.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import hu.mi.agnos.molap.meta.util.InfixToPostfixConverter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author parisek
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class XmlMeasure {

    @Setter
    @JacksonXmlProperty(isAttribute = true)
    private String uniqueName;
    
    @JacksonXmlProperty(isAttribute = true)
    private String calculatedFormula;
    
    public void setCalculatedFormula(String calculatedFormula) {
        this.calculatedFormula = InfixToPostfixConverter.convert(calculatedFormula);
    }     
    
     public void changeCalculatedFormula(String calculatedFormula) {
        this.calculatedFormula = calculatedFormula;
    }     
    
    @JsonIgnore    
    public boolean isCalculatedMeasure(){
        return this.calculatedFormula != null;
    }
    
    
    @Override
    public String toString() {
        return "Measure{" + "uniqueName=" + uniqueName + ", calculatedFormula=" + calculatedFormula + '}';
    }

}
