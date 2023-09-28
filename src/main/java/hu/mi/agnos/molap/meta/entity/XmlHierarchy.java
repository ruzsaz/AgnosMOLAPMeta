/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.mi.agnos.molap.meta.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
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
public class XmlHierarchy {

    @Getter
    @JacksonXmlProperty(isAttribute = true)
    private String uniqueName;

    @Getter
    @JacksonXmlProperty(isAttribute = true)
    private int order;

    @JacksonXmlProperty(isAttribute = true)
    private boolean isPartitioned;

    @Getter
    @JacksonXmlElementWrapper(localName = "Levels")
    @JacksonXmlProperty(localName = "Level")
    private final List<XmlLevel> levels;

    public XmlHierarchy() {
        this.levels = new ArrayList();
    }

    @JsonIgnore
    public boolean isPartitioned() {
        return isPartitioned;
    }

    
    public XmlHierarchy(String uniqueName, int order, boolean isPartitioned) {
        this();
        this.uniqueName = uniqueName;
        this.order = order;
        this.isPartitioned = isPartitioned;
    }

    public void addLevel(XmlLevel entity) {
        int idx = getInsertIdxOfNewLevel(entity);
        if (idx == this.levels.size()) {
            this.levels.add(entity);
        } else {
            this.levels.add(idx, entity);
        }
    }

    /**
     * Ez visszaadja az új elem beszúrási indexét. Ezt arra használjuk, hogy a
     * hierarchiában a szintek rendezetten tárolódhassanak.
     *
     * @param entity a beszúrandó XMLLevel
     * @return az új elem beszúrásának indexe
     */
    private int getInsertIdxOfNewLevel(XmlLevel entity) {
        int result = -1;
        for (int i = 0; i < this.levels.size(); i++) {
            if (this.levels.get(i).getDepth() > entity.getDepth()) {
                result = i;
                break;
            }
        }
        if (result == -1) {
            result = this.levels.size();
        }
        return result;
    }

    @JsonIgnore
    public List<String> getColumnListToOLAPBuilding() {
        List<String> result = new ArrayList<>();
        for (XmlLevel l : this.levels) {
            for (String columnName : l.getColumnListToOLAPBuilding()) {
                if (!result.contains(columnName)) {
                    result.add(columnName);
                }
            }
        }
        return result;
    }

    @JsonIgnore
    public int getOlapSqlIterationCnt() {
        return this.levels.size();
    }

    public String getOlapSelectStatementByIteration(int iteration) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < this.levels.size(); i++) {
            XmlLevel l = this.levels.get(i);
            boolean isInOLAP = i < levels.size() - iteration - 1;
            result.append(l.getColumnListToOLAPSelectStatement(isInOLAP)).append(", ");
        }

        return result.substring(0, result.length() - 2);
    }

    public String getOlapGroupByStatementByIteration(int iteration) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < this.levels.size(); i++) {
            XmlLevel l = this.levels.get(i);
            boolean isInOLAP = i < levels.size() - iteration - 1;
            String levelSQL = l.getColumnListToOLAPGroupByStatement(isInOLAP);
            if (levelSQL != null && !levelSQL.isEmpty()) {
                result.append(levelSQL).append(", ");
            }
        }
        if (result.length() > 2) {
            return result.substring(0, result.length() - 2);
        } else {
            return "";
        }
    }

    @Override
    public String toString() {
        return "Hierarchy{" + "uniqueName=" + uniqueName + ", order=" + order + ", isPartitioned=" + isPartitioned + ", levels=" + levels + '}';
    }

}
