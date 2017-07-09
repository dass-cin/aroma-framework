package aromaframework.services;

import aromaframework.components.TextNormalizer;
import aromaframework.core.domain.*;
import br.cin.ufpe.dass.matchers.core.Ontology;
import br.cin.ufpe.dass.matchers.core.OntologyProperty;
import br.cin.ufpe.dass.matchers.core.requirement.DataRequirement;
import org.apache.jena.ontology.*;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.RDFWriter;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by diego on 14/06/17.
 */
@Service
public class OntologySegmentService {

    private final RequirementsService requirementsService;

    private final TextNormalizer normalizer;

    public OntologySegmentService(RequirementsService requirementsService, TextNormalizer normalizer) {
        this.requirementsService = requirementsService;
        this.normalizer = normalizer;
    }

    /**
     *  Generate ontology segments based on application requirements.
     * @param ontology1
     * @param ontology2
     * @param dataRequirements
     * @return a set of ontology elements related to the defined requirements
     */
    public void generateSegments(OntologyModel ontology1, OntologyModel ontology2, Set<DataRequirement> dataRequirements) {

        OntologyModel subOntology1 = new OntologyModel();
        subOntology1.setNothing(ontology1.getNothing());
        subOntology1.setThing(ontology1.getThing());

        HashMap<String, OntologyClass> ontology1ClassesSegments = new HashMap<String, OntologyClass>();
        HashMap<String, PropertyModel> ontology1PropertiesSegments = new HashMap<String, PropertyModel>();
        dataRequirements.forEach( dataRequirement -> {
            ontology1ClassesSegments.putAll(requirementsService.validateClassRestrictions(dataRequirement.getClassRestrictions(), ontology1));
            ontology1PropertiesSegments.putAll(requirementsService.validatePropertyDescriptions(dataRequirement.getPropertyRestrictions(), ontology1));
        });

        HashMap<String, OntologyClass> ontology2ClassesSegments = new HashMap<String, OntologyClass>();
        HashMap<String, PropertyModel> ontology2PropertiesSegments = new HashMap<String, PropertyModel>();
        dataRequirements.forEach( dataRequirement -> {
            ontology2ClassesSegments.putAll(requirementsService.validateClassRestrictions(dataRequirement.getClassRestrictions(), ontology2));
            ontology2PropertiesSegments.putAll(requirementsService.validatePropertyDescriptions(dataRequirement.getPropertyRestrictions(), ontology2));
        });

        Set<ClassSegmentPair> classPairs = new HashSet<>();

        ontology1ClassesSegments.entrySet().forEach(segment1 ->  {
            ontology2ClassesSegments.entrySet().forEach(segment2 -> {
                if (normalizer.advancedNormalizing(segment1.getValue().getName()).equals(normalizer.advancedNormalizing(segment2.getValue().getName()))) {
                    classPairs.add(new ClassSegmentPair(segment1.getValue(), segment2.getValue()));
                }
            });
        });

        Set<PropertySegmentPair> propertyPairs = new HashSet<>();

        ontology1PropertiesSegments.entrySet().forEach(segment1 ->  {
            ontology2PropertiesSegments.entrySet().forEach(segment2 -> {
                if (normalizer.advancedNormalizing(segment1.getValue().getName()).equals(normalizer.advancedNormalizing(segment2.getValue().getName()))) {
                    propertyPairs.add(new PropertySegmentPair(segment1.getValue(), segment2.getValue()));
                }
            });
        });

        classPairs.forEach(pair -> {
            createClassSegmentFile(pair.getClassSegmentRoot1(), pair.getClassSegmentRoot1().getName()+ ".class-segment1.owl");
            createClassSegmentFile(pair.getClassSegmentRoot2(), pair.getClassSegmentRoot2().getName()+ ".class-segment2.owl");
        });

        propertyPairs.forEach(pair -> {
            createPropertySegmentFile(pair.getPropertySegment1(), pair.getPropertySegment1().getName()+ ".property-segment1.owl");
            createPropertySegmentFile(pair.getPropertySegment2(), pair.getPropertySegment2().getName()+ ".property-segment2.owl");
        });

    }

    public void createPropertySegmentFile(PropertyModel root, String out) {

        OntModel model = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM, null);

        String ns = "http://segment.com#";
        String base = "http://segment.com";
        model.createOntology(ns);
        model.setNsPrefix("", ns);

        OntProperty partPro = model.createObjectProperty(ns + "part_of");

        RDFWriter writer = ModelFactory.createDefaultModel().getWriter("RDF/XML-ABBREV");
        writer.setProperty("showXmlDeclaration", "true");
        writer.setProperty("tab", "8");
        writer.setProperty("xmlbase", base);

        createOntProperty(model, ns, root);

        try {
            writer.write(model, new FileOutputStream(out), null);
        } catch (java.io.FileNotFoundException e) {
            e.printStackTrace();
        }

    }


    public void createClassSegmentFile(OntologyClass root, String out) {

        OntModel model = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM, null);

        String ns = "http://segment.com#";
        String base = "http://segment.com";
        model.createOntology(ns);
        model.setNsPrefix("", ns);

        OntProperty partPro = model.createObjectProperty(ns + "part_of");

        RDFWriter writer = ModelFactory.createDefaultModel().getWriter("RDF/XML-ABBREV");
        writer.setProperty("showXmlDeclaration", "true");
        writer.setProperty("tab", "8");
        writer.setProperty("xmlbase", base);

        createOntClass(model, partPro, ns, root);

        try {
            writer.write(model, new FileOutputStream(out), null);
        } catch (java.io.FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    // @TODO limitar em N níveis...
    private void createOntClass(OntModel model, OntProperty partPro, String ns, OntologyClass ontologyClass) {

        OntClass jenaClass = model.createClass(ns + ontologyClass.getId());

        if (ontologyClass.getLabel() != null) jenaClass.addLabel(ontologyClass.getLabel(), "en");

        ontologyClass.getSynonyms().forEach(s -> jenaClass.addLabel((String) s, "sn"));

        ontologyClass.getSubs().forEach(s -> {
            OntologyClass subClass = (OntologyClass)s;
            createOntClass(model, partPro, ns, subClass);
            jenaClass.addSubClass(model.getOntClass(ns + subClass.getId()));
        });

        ontologyClass.getPartOf().forEach(p -> {
            OntologyClass partOf = (OntologyClass)p;
            createOntClass(model, partPro, ns, partOf);
            jenaClass.addSubClass(model.createSomeValuesFromRestriction(null, partPro, model.getOntClass(ns + partOf.getId())));
        });

    }

    private void createOntProperty(OntModel model, String ns, PropertyModel property) {
        OntProperty jenaProperty = model.createOntProperty( ns + property.getId());

        if (property.getLabel() != null) jenaProperty.addLabel(property.getLabel(), "en");

        property.getComments().forEach(c -> {
            RDFNode comment = (RDFNode)c;
            jenaProperty.addComment(comment.asLiteral());
        });

        property.getDomains().forEach(d -> {
            jenaProperty.addDomain(((OntResource) d).asResource());
        });

        property.getRanges().forEach( r -> {
            jenaProperty.addRange(((OntResource) r).asResource());
        });

        if (property.isTransitive()) jenaProperty.convertToTransitiveProperty();

        if (property.isFunctional()) jenaProperty.convertToFunctionalProperty();

        if (property.isDataTypeProperty()) jenaProperty.convertToDatatypeProperty();

        if (property.isInverseFunctionalProperty()) jenaProperty.convertToInverseFunctionalProperty();

        if (property.isObjectProperty()) jenaProperty.convertToObjectProperty();

        if (property.isSymmetricProperty()) jenaProperty.convertToSymmetricProperty();

        property.getInverseOf().forEach(i -> {
            jenaProperty.addInverseOf((Property) i);
        });

        property.getSuperProperties().forEach(s -> {
            PropertyModel superProperty = (PropertyModel)s;
            createOntProperty(model, ns, superProperty);
            jenaProperty.addSuperProperty(model.getOntProperty("ns" + superProperty.getId()));
        });

        property.getSubProperties().forEach(s -> {
            PropertyModel subProperty = (PropertyModel)s;
            createOntProperty(model, ns, subProperty);
            jenaProperty.addSubProperty(model.getOntProperty("ns" + subProperty.getId()));
        });

        property.getEquivalentProperties().forEach(e -> {
            PropertyModel equivalentProperty = (PropertyModel)e;
            createOntProperty(model, ns, equivalentProperty);
            jenaProperty.addEquivalentProperty(model.getOntProperty("ns" + equivalentProperty.getId()));
        });

    }

}
