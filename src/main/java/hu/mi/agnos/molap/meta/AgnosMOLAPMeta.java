/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.mi.agnos.molap.meta;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializationFeature;
import hu.mi.agnos.molap.meta.entity.XmlCube;
import hu.mi.agnos.molap.meta.exception.XmlHierarchyNameNotUniqueException;
import hu.mi.agnos.molap.meta.exception.XmlMeasureNameNotUniqueException;
import hu.mi.agnos.molap.meta.service.MOLAPMetaXmlHandler;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import hu.mi.agnos.molap.meta.entity.XmlMeasure;
import hu.mi.agnos.molap.meta.exception.InvalidPostfixExpressionException;
import hu.mi.agnos.molap.meta.util.PostfixToInfixConverter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author parisek
 */
public class AgnosMOLAPMeta {

    /**
     * @param args the command line arguments
     * @throws hu.mi.agnos.molap.meta.exception.XmlHierarchyNameNotUniqueException
     * @throws hu.mi.agnos.molap.meta.exception.XmlMeasureNameNotUniqueException
     * @throws com.fasterxml.jackson.core.JsonProcessingException
     * @throws hu.mi.agnos.molap.meta.exception.InvalidPostfixExpressionException
     */
    public static void main(String[] args) throws XmlHierarchyNameNotUniqueException, XmlMeasureNameNotUniqueException, JsonProcessingException, IOException, InvalidPostfixExpressionException {

        MOLAPMetaXmlHandler handler = new MOLAPMetaXmlHandler();
        XmlCube cube = handler.getXMLCube("/home/parisek/AGNOS_HOME/AgnosCubeBuilder/Meta/HB_AMI_MSSQL.cube.xml");
        System.out.println(cube.toString());

    }

}
