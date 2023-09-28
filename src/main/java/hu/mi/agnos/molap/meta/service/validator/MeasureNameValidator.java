/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hu.mi.agnos.molap.meta.service.validator;

import hu.mi.agnos.molap.meta.entity.XmlMeasure;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author parisek
 */
public class MeasureNameValidator {
    
    public static String isValid (List<XmlMeasure> measures){
        String result = "";
        Set tmp=new HashSet();
        for(XmlMeasure m : measures){
            String meauserName = m.getUniqueName();
            if(tmp.contains(meauserName)){
                result = meauserName;
                break;
            }
            tmp.add(meauserName);
        }
        return result;        
    }    
}
