/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hu.mi.agnos.molap.meta.service.validator;

import hu.mi.agnos.molap.meta.entity.XmlHierarchy;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author parisek
 */
public class HierarchyNameValidator {

    public static String isValid(List<XmlHierarchy> hiererchies) {
        String result = "";
        Set tmp = new HashSet();
        for (XmlHierarchy h : hiererchies) {
            String hierarchyName = h.getUniqueName();
            if (tmp.contains(hierarchyName)) {
                result = hierarchyName;
                break;
            }
            tmp.add(hierarchyName);
        }
        return result;
    }
}
