/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.mi.agnos.molap.meta.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author parisek
 */
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class XmlLevel {

    @Getter
    @JacksonXmlProperty(isAttribute = true)
    private String uniqueName;
    
    @Getter
    @JacksonXmlProperty(isAttribute = true)
    private int depth;
    
    @Getter
    @JacksonXmlProperty(isAttribute = true)
    private String codeColumnName;
    
    @JacksonXmlProperty(isAttribute = true)
    private String codeColumnSourceName;
    
    @Getter
    @JacksonXmlProperty(isAttribute = true)
    private String nameColumnName;

    public String getCodeColumnSourceName() {
        return codeColumnSourceName == null ? codeColumnName : codeColumnSourceName;
    }   
    
    public String getColumnListToOLAPSelectStatement(boolean isInOLAPGroupSelection) {
        String result = "";
        if (!isInOLAPGroupSelection) {
            if (!codeColumnSourceName.equals(nameColumnName)) {
                result = " 'All' as " + codeColumnSourceName + ", 'All' as " + nameColumnName;
            } else {
                result = " 'All' as " + codeColumnSourceName;
            }
        } else {
            result = this.codeColumnSourceName;
            if (!codeColumnSourceName.equals(nameColumnName)) {
                result = result + ", " + nameColumnName;
            }
        }
        return result;
    }

    public String getColumnListToOLAPGroupByStatement(boolean isInOLAPGroupSelection) {
        String result = "";
        if (isInOLAPGroupSelection) {
            result = this.codeColumnSourceName;
            if (!codeColumnSourceName.equals(nameColumnName)) {
                result = result + ", " + nameColumnName;
            }
        }
        return result;
    }

    @JsonIgnore    
    public List<String> getColumnListToOLAPBuilding() {
        List<String> result = new ArrayList<>();
        result.add(codeColumnSourceName);
        if (!codeColumnSourceName.equals(nameColumnName)) {
            result.add(nameColumnName);
        }
        return result;
    }
    
    @Override
    public String toString() {
        return "Level{" + "uniqueName=" + uniqueName + ", depth=" + depth + ", codeColumnName=" + codeColumnName + ", nameColumnName=" + nameColumnName + '}';
    }
  
}
