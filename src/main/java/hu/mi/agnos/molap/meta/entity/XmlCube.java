/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.mi.agnos.molap.meta.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author parisek
 */
@Setter
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JacksonXmlRootElement(localName = "Cube")
public class XmlCube {

    @Getter
    @JacksonXmlProperty(isAttribute = true)
    private String cubeUniqueName;

    @Getter
    @JacksonXmlProperty(localName = "SourceDBDriver")
    private String sourceDBDriver;

    @Getter
    @JacksonXmlProperty(localName = "SourceDBURL")
    private String sourceDBURL;

    @Getter
    @JacksonXmlProperty(localName = "SourceDBUser")
    private String sourceDBUser;

    @Getter
    @JacksonXmlProperty(localName = "SourceDBPassword")
    private String sourceDBPassword;

    @JacksonXmlProperty(isAttribute = true)
    private boolean isValid;

    @Getter
    @JacksonXmlElementWrapper(localName = "Measures")
    @JacksonXmlProperty(localName = "Measure")
    private List<XmlMeasure> measures;

    @Getter
    @JacksonXmlElementWrapper(localName = "Hierarchies")
    @JacksonXmlProperty(localName = "Hierarchy")
    private final List<XmlHierarchy> hierarchies;

    @Getter
    @JacksonXmlElementWrapper(localName = "Aggregations")
    @JacksonXmlProperty(localName = "Aggregation")
    private final List<XmlAggregation> aggregations;

    public XmlCube() {
        this.measures = new ArrayList<>();
        this.hierarchies = new ArrayList<>();
        this.aggregations = new ArrayList<>();
    }

    public void addHierarchy(XmlHierarchy hierarchy) {
        this.hierarchies.add(hierarchy);
    }

    public void addMeasure(XmlMeasure measure) {
        this.measures.add(measure);
    }

    public XmlHierarchy getHierarchyByName(String hierarchyName) {
        XmlHierarchy result = null;
        for (XmlHierarchy hier : this.hierarchies) {
            if (hier.getUniqueName().equals(hierarchyName)) {
                result = hier;
                break;
            }
        }
        return result;
    }

    public void addAggregation(XmlAggregation aggregation) {
        this.aggregations.add(aggregation);
    }

    public XmlAggregation getAggregationByName(String hierarchyName, String measureName) {
        XmlAggregation result = null;
        for (XmlAggregation a : this.aggregations) {
            if (a.getHierarchyName().equals(hierarchyName) && a.getMeasureName().equals(measureName)) {
                result = a;
                break;
            }
        }
        return result;
    }

    public List<XmlAggregation> getAggregationsByName(String hierarchyName) {
        List<XmlAggregation> result = new ArrayList<>();
        for (XmlAggregation a : this.aggregations) {
            if (a.getHierarchyName().equals(hierarchyName)) {
                result.add(a);
            }
        }
        return result;
    }

    public String getAggregationFunctionByName(String hierarchyName, String measureName) {
        XmlAggregation agg = getAggregationByName(hierarchyName, measureName);
        if (agg != null) {
            return agg.getAggregationFunction();
        } else {
            return "sum";
        }
    }

    @JsonIgnore
    public List<String> getPartitionedHierarchyList() {
        List<String> result = new ArrayList<>();

        for (XmlHierarchy hier : this.hierarchies) {
            if (hier.isPartitioned()) {
                if (!result.contains(hier.getUniqueName())) {
                    result.add(hier.getUniqueName());
                }
            }
        }

        for (XmlAggregation agg : this.aggregations) {
            if (!agg.getAggregationFunction().toLowerCase().equals("sum")) {
                if (!result.contains(agg.getHierarchyName())) {
                    result.add(agg.getHierarchyName());
                }
            }
        }
        return result;
    }

    @JsonIgnore
    public List<String> getDistinctHierarchyColumnList() {
        List<String> result = new ArrayList<>();
        for (XmlHierarchy hier : this.getHierarchies()) {
            for (XmlLevel level : hier.getLevels()) {
                if (!result.contains(level.getCodeColumnSourceName())) {
                    result.add(level.getCodeColumnSourceName());
                }
                if (!level.getNameColumnName().equals(level.getCodeColumnSourceName()) && !result.contains(level.getNameColumnName())) {
                    result.add(level.getNameColumnName());
                }
            }
        }
        return result;
    }

    @JsonIgnore
    public List<String> getDistinctMeasureColumnList() {
        List<String> result = new ArrayList<>();
        for (XmlMeasure measure : getMeasures()) {
            if (!result.contains(measure.getUniqueName()) && !measure.isCalculatedMeasure()) {
                result.add(measure.getUniqueName());
            }
        }
        return result;
    }

    public void modifyCalculatedFormula(String uniqueName, String calculatedFormula) {
        for (XmlMeasure m : this.measures) {
            if (m.getUniqueName().equals(uniqueName)) {
                m.changeCalculatedFormula(calculatedFormula);
            }
        }
    }

    @Override
    public String toString() {
        return "Cube{" + " SourceDBDriver=" + sourceDBDriver + ", SourceDBURL=" + sourceDBURL + ", SourceDBUser=" + sourceDBUser + ", SourceDBPassword=" + sourceDBPassword + ", measures=" + measures + ", hierarchiss=" + hierarchies + '}';
    }

}
