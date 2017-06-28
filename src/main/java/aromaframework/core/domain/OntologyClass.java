package aromaframework.core.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by diego on 06/06/17.
 */
public class OntologyClass {

    private String id;
    private String name = null;
    private List synonyms = new ArrayList();
    private String URI;
    private String label;
    private String commment;
    private List partOf = new ArrayList();
    private List hasPart = new ArrayList();
    private List supers = new ArrayList();
    private List subs = new ArrayList();
    private List equiv = new ArrayList();
    private List disjointWith = new ArrayList();
    private List seeAlso = new ArrayList();
    private List isDefinedBy = new ArrayList();

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

    public List getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(List synonyms) {
        this.synonyms = synonyms;
    }

    public List getPartOf() {
        return partOf;
    }

    public void setPartOf(List partOf) {
        this.partOf = partOf;
    }

    public List getHasPart() {
        return hasPart;
    }

    public void setHasPart(List hasPart) {
        this.hasPart = hasPart;
    }

    public List getSupers() {
        return supers;
    }

    public void setSupers(List supers) {
        this.supers = supers;
    }

    public List getSubs() {
        return subs;
    }

    public void setSubs(List subs) {
        this.subs = subs;
    }

    public List getEquiv() {
        return equiv;
    }

    public void setEquiv(List equiv) {
        this.equiv = equiv;
    }

    public String getURI() {
        return URI;
    }

    public void setURI(String URI) {
        this.URI = URI;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List getDisjointWith() {
        return disjointWith;
    }

    public void setDisjointWith(List disjointWith) {
        this.disjointWith = disjointWith;
    }

    public String getCommment() {
        return commment;
    }

    public void setCommment(String commment) {
        this.commment = commment;
    }

    public List getSeeAlso() {
        return seeAlso;
    }

    public void setSeeAlso(List seeAlso) {
        this.seeAlso = seeAlso;
    }

    public List getIsDefinedBy() {
        return isDefinedBy;
    }

    public void setIsDefinedBy(List isDefinedBy) {
        this.isDefinedBy = isDefinedBy;
    }
}
