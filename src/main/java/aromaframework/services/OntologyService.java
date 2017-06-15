package aromaframework.services;

import aromaframework.components.TextNormalizer;
import aromaframework.config.ApplicationProperties;
import aromaframework.core.domain.OntologyClass;
import aromaframework.core.domain.OntologyModel;
import aromaframework.core.domain.PropertyModel;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Resource;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by diego on 06/06/17.
 */
@Service
public class OntologyService {

    private final TextNormalizer textNormalizer;

    private final ApplicationProperties applicationProperties;

    public OntologyService(TextNormalizer textNormalizer, ApplicationProperties applicationProperties) {
        this.textNormalizer = textNormalizer;
        this.applicationProperties = applicationProperties;
    }

    public OntologyModel loadOntologyModel(OntModel ontModel) {

        OntologyModel ontologyModel = new OntologyModel();
        ontologyModel.setNothing(ontModel.getProfile().NOTHING());
        ontologyModel.setThing(ontModel.getProfile().THING());

        ontologyModel.setProperties(new LinkedHashMap<>());
        //Object Properties
        ontologyModel.getProperties().putAll(ontModel.listObjectProperties().toList().stream()
                .filter(p -> !p.equals("disjointWith") && !p.equals("sameAs") && !p.equals("differentFrom"))
                .collect(Collectors.toMap(p -> p.getLocalName(), p -> loadPropertyModel(p))));
        //Datatype properties
        ontologyModel.getProperties().putAll(ontModel.listDatatypeProperties().toList().stream()
                .collect(Collectors.toMap(p -> p.getLocalName(), p -> loadPropertyModel(p))));


        //classes
        ontologyModel.setClasses(new LinkedHashMap<>());
        ontModel.listClasses().toSet().stream()
                .filter(r -> !r.isAnon() && !r.equals(ontologyModel.getNothing())
                        && !r.equals(ontologyModel.getThing()) && !applicationProperties.getOntologies().getExternalNamespaces().contains(r.getNameSpace()))
                .map(r -> loadOntologyClass(ontologyModel, r))
                .forEachOrdered(c -> ontologyModel.getClasses().put(c.getId(), c));

        return ontologyModel;

    }

    private OntologyClass getEquivClass(OntologyModel ontologyModel, OntClass rs) {
        Optional<OntologyClass> optional = ontologyModel.getClasses().entrySet().stream()
                .filter( o -> o.getValue() != null &&
                        o.getValue().getEquiv() != null &&
                        o.getValue().getEquiv().contains(rs) )
                .map( o -> o.getValue())
                .findFirst();
        return optional.isPresent()?optional.get():null;
    }

    private OntologyClass loadOntologyClass(OntologyModel ontologyModel, Resource rs) {

        if (ontologyModel.getClasses().get(rs.getLocalName()) != null) {
            return ontologyModel.getClasses().get(rs.getLocalName());
        }

        //check whether ontologyClass already exists in hashmap
        OntologyClass ontologyClass = ((OntClass) rs).listEquivalentClasses().toSet().stream()
                .filter(o -> ontologyModel.getClasses().get(o.getLocalName()) != null)
                .map(o -> ontologyModel.getClasses().get(o.getLocalName()))
                .findFirst()
                .orElse(getEquivClass(ontologyModel, (OntClass) rs));

        if (ontologyClass == null) {
            ontologyClass = new OntologyClass();
            ontologyClass.setId(rs.getLocalName());
            ontologyClass.setURI(rs.getURI());
            ontologyClass.setCommment(((OntClass) rs).getComment(null));
            ontologyModel.getClasses().put(ontologyClass.getId(), ontologyClass);
        } else if (ontologyClass.getEquiv() != null && !ontologyClass.getEquiv().contains(rs)) {
            ontologyClass.getEquiv().add(rs);
        }

        //fill synonyms
        ontologyClass.setSynonyms(((OntClass) rs).listLabels(applicationProperties.getOntologies().getSnLanguage()).
                toSet().stream().filter(t -> t instanceof Literal)
                .map(t -> ((Literal)t).getString()).collect(Collectors.toList()));

        //fill superclasses
        ontologyClass.setSupers(((OntClass) rs).listSuperClasses(true).toSet().stream()
                .filter(c -> !c.isAnon())
                .map(c -> loadOntologyClass(ontologyModel, c)).collect(Collectors.toList()));

        //fill partof
        ontologyClass.setPartOf(((OntClass) rs).listSuperClasses(true).toSet().stream()
                .filter(c -> c.isAnon() && c.isRestriction()
                        && c.asRestriction().getOnProperty().getLocalName().equals(applicationProperties.getOntologies().getPart()))
                .map(c -> loadOntologyClass(ontologyModel, c.asRestriction().asSomeValuesFromRestriction().getSomeValuesFrom())).collect(Collectors.toList()));

        //fill disjointWith
        ontologyClass.setDisjointWith(((OntClass) rs).listDisjointWith().toSet().stream()
                .map(c -> loadOntologyClass(ontologyModel, c)).collect(Collectors.toList()));

        //fill seeAlso
        ontologyClass.setSeeAlso(((OntClass) rs).listSeeAlso().toSet().stream()
                .map(c -> loadOntologyClass(ontologyModel, c.asResource())).collect(Collectors.toList()));

        //fill isDefinedBy
        ontologyClass.setSeeAlso(((OntClass) rs).listIsDefinedBy().toSet().stream()
                .map(c -> loadOntologyClass(ontologyModel, c.asResource())).collect(Collectors.toList()));

        return ontologyClass;

    }

    private String loadClassName(String label, String localName) {
        return textNormalizer.advancedNormalizing(label!=null?label:localName.trim());
    }

    private PropertyModel loadPropertyModel(OntProperty ontProperty) {
        PropertyModel propertyModel = new PropertyModel();
        propertyModel.setId(ontProperty.getLocalName());
        propertyModel.setURI(ontProperty.getURI());
        propertyModel.setComments(ontProperty.listComments(null).toSet());
        propertyModel.setDomains(ontProperty.listDomain().toSet());
        propertyModel.setRanges(ontProperty.listRange().toSet());
        propertyModel.setLabel(ontProperty.getLabel(null)!=null?
                textNormalizer.basicNormalizing(ontProperty.getLabel(null).trim())
                :textNormalizer.basicNormalizing(ontProperty.getLocalName().trim()));
        return propertyModel;
    }

}
