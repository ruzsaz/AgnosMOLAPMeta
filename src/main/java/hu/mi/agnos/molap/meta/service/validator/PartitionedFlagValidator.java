/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hu.mi.agnos.molap.meta.service.validator;

import hu.mi.agnos.molap.meta.entity.XmlAggregation;
import hu.mi.agnos.molap.meta.entity.XmlCube;

/**
 *
 * @author parisek
 */
public class PartitionedFlagValidator {

    public static XmlCube validatePartitionedFlag(XmlCube cube) {
        boolean isChange = false;
        for (XmlAggregation aggregation : cube.getAggregations()) {
            if (!aggregation.getAggregationFunction().toLowerCase().equals("sum")) {
                String hierarchyName = aggregation.getHierarchyName();
                if (!cube.getHierarchyByName(hierarchyName).isPartitioned()) {
                    isChange = true;
                    cube.getHierarchyByName(hierarchyName).setPartitioned(true);
                }
            }
        }
        return isChange ? cube : null;
    }

}
