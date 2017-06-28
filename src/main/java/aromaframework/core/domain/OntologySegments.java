package aromaframework.core.domain;

import java.util.HashMap;

/**
 * Created by diego on 26/06/17.
 */
public class OntologySegments {

    private HashMap<String, OntologyClass> ontology1ClassesSegments;
    private HashMap<String, OntologyClass> ontology2ClassesSegments;
    HashMap<String, PropertyModel> ontology1PropertiesSegments;
    HashMap<String, PropertyModel> ontology2PropertiesSegments;

    public OntologySegments(HashMap<String, OntologyClass> ontology1ClassesSegments, HashMap<String, OntologyClass> ontology2ClassesSegments, HashMap<String, PropertyModel> ontology1PropertiesSegments, HashMap<String, PropertyModel> ontology2PropertiesSegments) {
        this.ontology1ClassesSegments = ontology1ClassesSegments;
        this.ontology2ClassesSegments = ontology2ClassesSegments;
        this.ontology1PropertiesSegments = ontology1PropertiesSegments;
        this.ontology2PropertiesSegments = ontology2PropertiesSegments;
    }

    public HashMap<String, OntologyClass> getOntology1ClassesSegments() {
        return ontology1ClassesSegments;
    }

    public void setOntology1ClassesSegments(HashMap<String, OntologyClass> ontology1ClassesSegments) {
        this.ontology1ClassesSegments = ontology1ClassesSegments;
    }

    public HashMap<String, OntologyClass> getOntology2ClassesSegments() {
        return ontology2ClassesSegments;
    }

    public void setOntology2ClassesSegments(HashMap<String, OntologyClass> ontology2ClassesSegments) {
        this.ontology2ClassesSegments = ontology2ClassesSegments;
    }

    public HashMap<String, PropertyModel> getOntology1PropertiesSegments() {
        return ontology1PropertiesSegments;
    }

    public void setOntology1PropertiesSegments(HashMap<String, PropertyModel> ontology1PropertiesSegments) {
        this.ontology1PropertiesSegments = ontology1PropertiesSegments;
    }

    public HashMap<String, PropertyModel> getOntology2PropertiesSegments() {
        return ontology2PropertiesSegments;
    }

    public void setOntology2PropertiesSegments(HashMap<String, PropertyModel> ontology2PropertiesSegments) {
        this.ontology2PropertiesSegments = ontology2PropertiesSegments;
    }
}
