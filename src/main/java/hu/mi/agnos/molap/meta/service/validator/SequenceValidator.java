/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hu.mi.agnos.molap.meta.service.validator;

import hu.mi.agnos.molap.meta.entity.XmlCube;
import hu.mi.agnos.molap.meta.entity.XmlHierarchy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author parisek
 */
public class SequenceValidator {

    public static XmlCube validatePartitionedFlag(XmlCube cube) {
        boolean isChange = false;
        List<XmlHierarchy> tmpHierarchies = new ArrayList<>();
        List<XmlHierarchy> sortedPartitionedHierarchyList = getSortedPartitionedHierarchyList(cube);
        List<XmlHierarchy> sortedNotPartitionedHierarchyList = getSortedNotPartitionedHierarchyList(cube);
        
        int i = 0;
        
        for (XmlHierarchy hier : sortedPartitionedHierarchyList) {
            hier.setOrder(i);
            tmpHierarchies.add(hier);
            i++;
        }
        
        for (XmlHierarchy hier : sortedNotPartitionedHierarchyList) {
            hier.setOrder(i);
            tmpHierarchies.add(hier);
            i++;
        }
        
        if (! isSorted(cube, tmpHierarchies)) {
            isChange = true;
            cube.getHierarchies().clear();
            for (XmlHierarchy hier : tmpHierarchies) {
                cube.getHierarchies().add(hier);
            }
        }
        
        return isChange ? cube : null;
    }

    private static boolean isSorted(XmlCube cube, List<XmlHierarchy> actualHierarchies) {
        List<XmlHierarchy> cubeHierarchies = cube.getHierarchies();
        if (cubeHierarchies.size() != actualHierarchies.size()) {
            return false;
        }

        for (int i = 0; i < cubeHierarchies.size(); i++) {
            if (cubeHierarchies.get(i).getUniqueName() != actualHierarchies.get(i).getUniqueName()) {
                return false;
            }
            if (cubeHierarchies.get(i).getOrder() != actualHierarchies.get(i).getOrder()) {
                return false;
            }
        }
        return true;
    }

    private static List<XmlHierarchy> getSortedPartitionedHierarchyList(XmlCube cube) {
        List<XmlHierarchy> result = new ArrayList<>();
        for (String hierName : cube.getPartitionedHierarchyList()) {
            result.add(cube.getHierarchyByName(hierName));
        }
        Collections.sort(result, (h1, h2) -> {
            return h2.getOrder() - h1.getOrder();
        });
        return result;
    }

    private static List<XmlHierarchy> getSortedNotPartitionedHierarchyList(XmlCube cube) {
        List<XmlHierarchy> result = new ArrayList<>();
        List<String> partitionedHierarchyNameList = cube.getPartitionedHierarchyList();

        for (XmlHierarchy hier : cube.getHierarchies()) {
            if (!partitionedHierarchyNameList.contains(hier.getUniqueName())) {
                if (!result.contains(hier)) {
                    result.add(hier);
                }
            }
        }
        Collections.sort(result, (h1, h2) -> {
            return h2.getOrder() - h1.getOrder();
        });
        return result;
    }

}
