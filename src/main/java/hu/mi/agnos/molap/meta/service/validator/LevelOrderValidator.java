/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hu.mi.agnos.molap.meta.service.validator;

import hu.mi.agnos.molap.meta.entity.XmlCube;
import hu.mi.agnos.molap.meta.entity.XmlHierarchy;
import hu.mi.agnos.molap.meta.entity.XmlLevel;

/**
 *
 * @author parisek
 */
public class LevelOrderValidator {

    public static XmlCube validateLevelOrder(XmlCube cube) {
        boolean isChange = false;
        for (XmlHierarchy h : cube.getHierarchies()) {
            int i = 1;
            for (XmlLevel l : h.getLevels()) {
                int dept = l.getDepth();
                if (l.getDepth() != i) {
                    isChange = true;
                    l.setDepth(i);
                }
                i++;
            }
        }
        return isChange ? cube : null;
    }
}
