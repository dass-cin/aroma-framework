package aromaframework.core.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.List;

/**
 * Created by diego on 06/06/17.
 */
public class PropertyModel {

    private String id;

    private String name;

    private String URI;

    private String label;

    private Set comments = new HashSet();

    private String type;

    private Set ranges = new HashSet();

    private Set domains = new HashSet();

    private String nameSpace;

    private List subProperties = new ArrayList();

    private List superProperties = new ArrayList();

    private List equivalentProperties = new ArrayList();

    private Set inverseOf = new HashSet();

    private boolean transitive;

    private boolean functional;

    private boolean dataTypeProperty;

    private boolean objectProperty;

    private boolean symmetricProperty;

    private boolean inverseFunctionalProperty;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Set getComments() {
        return comments;
    }

    public void setComments(Set comments) {
        this.comments = comments;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Set getRanges() {
        return ranges;
    }

    public void setRanges(Set ranges) {
        this.ranges = ranges;
    }

    public Set getDomains() {
        return domains;
    }

    public void setDomains(Set domains) {
        this.domains = domains;
    }

    public String getNameSpace() {
        return nameSpace;
    }

    public void setNameSpace(String nameSpace) {
        this.nameSpace = nameSpace;
    }

    public String getURI() {
        return URI;
    }

    public void setURI(String URI) {
        this.URI = URI;
    }

    public List getSubProperties() {
        return subProperties;
    }

    public void setSubProperties(List subProperties) {
        this.subProperties = subProperties;
    }

    public List getSuperProperties() {
        return superProperties;
    }

    public void setSuperProperties(List superProperties) {
        this.superProperties = superProperties;
    }

    public List getEquivalentProperties() {
        return equivalentProperties;
    }

    public void setEquivalentProperties(List equivalentProperties) {
        this.equivalentProperties = equivalentProperties;
    }

    public boolean isTransitive() {
        return transitive;
    }

    public void setTransitive(boolean transitive) {
        this.transitive = transitive;
    }

    public boolean isFunctional() {
        return functional;
    }

    public void setFunctional(boolean functional) {
        this.functional = functional;
    }

    public boolean isDataTypeProperty() {
        return dataTypeProperty;
    }

    public void setDataTypeProperty(boolean dataTypeProperty) {
        this.dataTypeProperty = dataTypeProperty;
    }

    public boolean isObjectProperty() {
        return objectProperty;
    }

    public void setObjectProperty(boolean objectProperty) {
        this.objectProperty = objectProperty;
    }

    public boolean isSymmetricProperty() {
        return symmetricProperty;
    }

    public void setSymmetricProperty(boolean symmetricProperty) {
        this.symmetricProperty = symmetricProperty;
    }

    public boolean isInverseFunctionalProperty() {
        return inverseFunctionalProperty;
    }

    public void setInverseFunctionalProperty(boolean inverseFunctionalProperty) {
        this.inverseFunctionalProperty = inverseFunctionalProperty;
    }

    public Set getInverseOf() {
        return inverseOf;
    }

    public void setInverseOf(Set inverseOf) {
        this.inverseOf = inverseOf;
    }
}
