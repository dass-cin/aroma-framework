package aromaframework.core.domain;

import org.apache.jena.rdf.model.Resource;

import java.util.LinkedHashMap;

/**
 * Created by diego on 06/06/17.
 */
public class OntologyModel {

    private LinkedHashMap<String, OntologyClass> classes;
    private LinkedHashMap<String, PropertyModel> properties;

    Resource thing, nothing;

    public OntologyModel() {
    }

    public LinkedHashMap<String, OntologyClass> getClasses() {
        return classes;
    }

    public void setClasses(LinkedHashMap<String, OntologyClass> classes) {
        this.classes = classes;
    }

    public LinkedHashMap<String, PropertyModel> getProperties() {
        return properties;
    }

    public void setProperties(LinkedHashMap<String, PropertyModel> properties) {
        this.properties = properties;
    }

    public Resource getNothing() {
        return nothing;
    }

    public void setNothing(Resource nothing) {
        this.nothing = nothing;
    }

    public Resource getThing() {
        return thing;
    }

    public void setThing(Resource thing) {
        this.thing = thing;
    }
}
