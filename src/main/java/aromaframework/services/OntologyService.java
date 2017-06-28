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
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.util.FileManager;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.List;

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

    public OntologyModel loadOntologyModel(URI uri) {
        OntModel ontology = ModelFactory.createOntologyModel();
        FileManager.get().readModel(ontology, uri.getPath());
        return loadOntologyModel(ontology);
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
                        o.getValue().getEquiv().contains(rs) )
                .map( o -> o.getValue())
                .findFirst();
        return optional.isPresent()?optional.get():null;
    }

    private OntologyClass loadOntologyClass(OntologyModel ontologyModel, Resource rs) {

        if (rs.isAnon() || rs.equals(ontologyModel.getNothing()) || rs.equals(ontologyModel.getThing())) {
            return null;
        }

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
            ontologyClass.setId(rs.getURI());
            ontologyClass.setLabel(((OntClass) rs).getLabel(null));
            ontologyClass.setName(((OntClass) rs).getLocalName());
            ontologyClass.setURI(rs.getURI());
            ontologyClass.setCommment(((OntClass) rs).getComment(null));
            ontologyModel.getClasses().put(ontologyClass.getId(), ontologyClass);
        } else if (!ontologyClass.getEquiv().contains(rs)) {
            ontologyClass.getEquiv().add(rs);
        }

        OntologyClass finalOntologyClass = ontologyClass;

        //fill synonyms
        ontologyClass.setSynonyms(((OntClass) rs).listLabels(applicationProperties.getOntologies().getSnLanguage()).
                toSet().stream().filter(t -> t instanceof Literal)
                .filter(c -> !finalOntologyClass.getSynonyms().contains(c))
                .map(t -> ((Literal)t).getString()).collect(Collectors.toList()));

        //fill subclasses
//        ontologyClass.setSubs(((OntClass) rs).listSubClasses(true).toSet().stream()
//                .filter(c -> !c.isAnon())
//                .map(c -> (loadOntologyClass(ontologyModel, c)))
//                .collect(Collectors.toList()));

        //fill superclasses
        //preciso checar se alguma das superclasses está na lista de super classe, se tiver, considerar ela...
        ontologyClass.setSupers(((OntClass) rs).listSuperClasses(true).toSet().stream()
                .filter(c -> !c.isAnon())
                .map(c -> (loadOntologyClass(ontologyModel, c)))
                .collect(Collectors.toList()));

        //fill partof
        ontologyClass.setPartOf(((OntClass) rs).listSuperClasses(true).toSet().stream()
                .filter(c -> c.isAnon() && c.isRestriction() &&
                        finalOntologyClass.getPartOf().stream().filter( sc -> ((OntologyClass)sc).getName().equals(finalOntologyClass.getName())).count() == 0
                && c.asRestriction().getOnProperty().getLocalName().equals(applicationProperties.getOntologies().getPart()))
                .map(c -> loadOntologyClass(ontologyModel, c.asRestriction().asSomeValuesFromRestriction().getSomeValuesFrom())).collect(Collectors.toList()));

        //fill disjointWith
        ontologyClass.setDisjointWith(((OntClass) rs).listDisjointWith().toSet().stream()
                .filter(c -> c.listDisjointWith().toSet().stream().filter(d -> d.isDisjointWith(c)).collect(Collectors.toList()).size() == 0 &&
                 finalOntologyClass.getDisjointWith().stream().filter( sc -> ((OntologyClass)sc).getName().equals(finalOntologyClass.getName())).count() == 0)
                .map(c -> loadOntologyClass(ontologyModel, c)).collect(Collectors.toList()));

        //fill seeAlso
        ontologyClass.setSeeAlso(((OntClass) rs).listSeeAlso().toSet().stream()
                .filter(c -> finalOntologyClass.getSeeAlso().stream().filter( sc -> ((OntologyClass)sc).getName().equals(finalOntologyClass.getName())).count() == 0)
                .map(c -> loadOntologyClass(ontologyModel, c.asResource())).collect(Collectors.toList()));

        //fill isDefinedBy
        ontologyClass.setIsDefinedBy(((OntClass) rs).listIsDefinedBy().toSet().stream()
                .filter(c -> finalOntologyClass.getIsDefinedBy().stream().filter( sc -> ((OntologyClass)sc).getName().equals(finalOntologyClass.getName())).count() == 0)
                .map(c -> loadOntologyClass(ontologyModel, c.asResource())).collect(Collectors.toList()));

        return ontologyClass;

    }

    private String loadClassName(String label, String localName) {
        return textNormalizer.advancedNormalizing(label!=null?label:localName.trim());
    }

    private PropertyModel loadPropertyModel(OntProperty ontProperty) {
        PropertyModel propertyModel = new PropertyModel();
        propertyModel.setId(ontProperty.getLocalName());
        propertyModel.setName(ontProperty.getLocalName());
        propertyModel.setURI(ontProperty.getURI());
        propertyModel.setComments(ontProperty.listComments(null).toSet());
        propertyModel.setDomains(ontProperty.listDomain().toSet());
        propertyModel.setRanges(ontProperty.listRange().toSet());
        propertyModel.setTransitive(ontProperty.isTransitiveProperty());
        propertyModel.setFunctional(ontProperty.isFunctionalProperty());
        propertyModel.setDataTypeProperty(ontProperty.isDatatypeProperty());
        propertyModel.setObjectProperty(ontProperty.isObjectProperty());
        propertyModel.setSymmetricProperty(ontProperty.isSymmetricProperty());
        propertyModel.setInverseFunctionalProperty(ontProperty.isInverseFunctionalProperty());
        propertyModel.setInverseOf(ontProperty.listInverseOf().toSet());
        propertyModel.setLabel(ontProperty.getLabel(null)!=null?
                textNormalizer.basicNormalizing(ontProperty.getLabel(null).trim())
                :textNormalizer.basicNormalizing(ontProperty.getLocalName().trim()));
        propertyModel.setSubProperties(ontProperty.listSubProperties().toSet()
                .stream()
                .filter(p -> !propertyModel.getSubProperties().contains(p) &&
                        p.listSubProperties().toSet().stream().filter(pr -> pr.listSubProperties().toSet().contains(p)).collect(Collectors.toList()).size() == 0)
                .map(p -> loadPropertyModel(p)).collect(Collectors.toList()));
        propertyModel.setSuperProperties(ontProperty.listSuperProperties().toSet()
                .stream()
                .filter(p -> !propertyModel.getSuperProperties().contains(p) &&
                        p.listSuperProperties().toSet().stream().filter(pr -> pr.listSuperProperties().toSet().contains(p)).collect(Collectors.toList()).size() == 0)
                .map(p -> loadPropertyModel(p)).collect(Collectors.toList()));
        propertyModel.setEquivalentProperties(ontProperty.listEquivalentProperties().toSet()
                .stream().filter(p -> !propertyModel.getEquivalentProperties().contains(p)).map(p -> loadPropertyModel(p)).collect(Collectors.toList()));
        return propertyModel;
    }

}
