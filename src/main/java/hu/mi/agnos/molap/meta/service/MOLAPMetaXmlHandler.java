/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.mi.agnos.molap.meta.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import hu.mi.agnos.molap.meta.entity.XmlCube;
import hu.mi.agnos.molap.meta.entity.XmlMeasure;
import hu.mi.agnos.molap.meta.exception.InvalidPostfixExpressionException;
import hu.mi.agnos.molap.meta.exception.XmlHierarchyNameNotUniqueException;
import hu.mi.agnos.molap.meta.exception.XmlMeasureNameNotUniqueException;
import hu.mi.agnos.molap.meta.service.validator.HierarchyNameValidator;
import hu.mi.agnos.molap.meta.service.validator.LevelOrderValidator;
import hu.mi.agnos.molap.meta.service.validator.MeasureNameValidator;
import hu.mi.agnos.molap.meta.service.validator.PartitionedFlagValidator;
import hu.mi.agnos.molap.meta.service.validator.SequenceValidator;
import hu.mi.agnos.molap.meta.util.PostfixToInfixConverter;

import java.io.File;
import java.io.IOException;
import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.slf4j.Logger;
import org.xml.sax.SAXException;
import org.slf4j.LoggerFactory;

/**
 *
 * @author parisek
 */
public class MOLAPMetaXmlHandler {

    private final Logger logger;

    public MOLAPMetaXmlHandler() {
        super();
        logger = LoggerFactory.getLogger(MOLAPMetaXmlHandler.class);
    }

    private boolean validateXML(String path) {
        try {
            Source xmlFile = new StreamSource(new File(path));
            SchemaFactory schemaFactory = SchemaFactory
                    .newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = schemaFactory
                    .newSchema(getClass().getResource("/cube.xsd"));
            Validator validator = schema.newValidator();
            validator.validate(xmlFile);
            return true;
        } catch (SAXException | IOException e) {
            logger.error(e.getMessage());
            return false;
        }
    }

    public XmlCube validateXmlCube(String path, XmlCube cube) throws XmlHierarchyNameNotUniqueException, XmlMeasureNameNotUniqueException, IOException, InvalidPostfixExpressionException {
        boolean isChanged = false;
        String validationResult = HierarchyNameValidator.isValid(cube.getHierarchies());
        if (!validationResult.equals("")) {
            throw new XmlHierarchyNameNotUniqueException(validationResult);
        }

        validationResult = MeasureNameValidator.isValid(cube.getMeasures());
        if (!validationResult.equals("")) {
            throw new XmlMeasureNameNotUniqueException(validationResult);
        }

        XmlCube validatedCube = LevelOrderValidator.validateLevelOrder(cube);
        if (validatedCube != null) {
            isChanged = true;
            cube = validatedCube;
        }

        validatedCube = PartitionedFlagValidator.validatePartitionedFlag(cube);
        if (validatedCube != null) {
            isChanged = true;
            cube = validatedCube;
        }

        validatedCube = SequenceValidator.validatePartitionedFlag(cube);
        if (validatedCube != null) {
            isChanged = true;
            cube = validatedCube;
        }

        if (isChanged) {
            setXMLCube(path, cube);
        }
        return cube;
    }

    public XmlCube getXMLCube(String path) throws XmlHierarchyNameNotUniqueException, XmlMeasureNameNotUniqueException, IOException, InvalidPostfixExpressionException {
        XmlCube xmlCube = null;

        if (validateXML(path)) {
            XmlMapper xmlMapper = new XmlMapper();
            xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
            xmlMapper.configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true);
           
            XmlCube tmpCube = xmlMapper.readValue(new File(path), XmlCube.class);
            xmlCube = validateXmlCube(path, tmpCube);
        }
        return xmlCube;
    }

    private XmlCube deepCopyWithInfixFormula(XmlCube origin) throws JsonProcessingException, InvalidPostfixExpressionException {
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
        xmlMapper.configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true);

        //deepcopy
        XmlCube clone = xmlMapper.readValue(xmlMapper.writeValueAsString(origin), XmlCube.class);

        for (XmlMeasure originMeasure : origin.getMeasures()) {
            String formula = originMeasure.getCalculatedFormula();
            formula = originMeasure.isCalculatedMeasure()
                    ? PostfixToInfixConverter.convert(formula)
                    : formula;
            clone.modifyCalculatedFormula(originMeasure.getUniqueName(), formula);
        }
        
        return clone;
    }

    public void setXMLCube(String path, XmlCube xmlCube) throws JsonProcessingException, IOException, InvalidPostfixExpressionException {
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
        xmlMapper.configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true);

        XmlCube clone =  deepCopyWithInfixFormula(xmlCube);
        xmlMapper.writeValue(new File(path), clone);
    }

}
