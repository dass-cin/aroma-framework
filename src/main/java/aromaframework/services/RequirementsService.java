package aromaframework.services;

import aromaframework.components.TextNormalizer;
import aromaframework.core.domain.OntologyClass;
import aromaframework.core.domain.OntologyModel;
import aromaframework.core.domain.PropertyModel;
import br.cin.ufpe.dass.matchers.core.OntologyProperty;
import br.cin.ufpe.dass.matchers.core.requirement.restriction.ClassRestriction;
import br.cin.ufpe.dass.matchers.core.requirement.restriction.PropertyRestriction;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.rdf.model.RDFNode;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by diego on 15/06/17.
 */
@Service
public class RequirementsService {

    private final TextNormalizer textNormalizer;

    public RequirementsService(TextNormalizer textNormalizer) {
        this.textNormalizer = textNormalizer;
    }

    public HashMap<String, OntologyClass> validateClassRestriction(ClassRestriction classRestriction, OntologyModel ontologyModel) {
        HashMap<String, OntologyClass> validatedClasses = new LinkedHashMap<>();
        ontologyModel.getClasses().entrySet().parallelStream().forEach((c) -> {
            OntologyClass ontologyClass = c.getValue();
            switch (classRestriction.getSubject()) {
                case NAME:
                    if (ontologyClass.getName().equals(classRestriction.getValue()) ||
                            textNormalizer.basicNormalizing(ontologyClass.getName()).equals(classRestriction.getValue())) {
                        validatedClasses.put(c.getKey(), c.getValue());
                    }
                    break;
                case ANNOTATION_LABEL:
                    if (ontologyClass.getLabel().equals(classRestriction.getValue()) ||
                            textNormalizer.basicNormalizing(ontologyClass.getLabel()).equals(classRestriction.getValue())) {
                        validatedClasses.put(c.getKey(), c.getValue());
                    }
                    break;
                case AXIOM_SUBCLASS_OF:
                    ontologyClass.getSubs().forEach(s -> {
                        OntologyClass sub = (OntologyClass) s;
                        if (sub.getName().equals(classRestriction.getValue())) {
                            validatedClasses.put(c.getKey(), c.getValue());
                        }
                    });
                    break;
                case AXIOM_EQUIVALENT_CLASS:
                    ontologyClass.getEquiv().forEach(e -> {
                        OntologyClass eqv = (OntologyClass) e;
                        if (eqv.getName().equals(classRestriction.getValue())) {
                            validatedClasses.put(c.getKey(), c.getValue());
                        }
                    });
                    break;
                case AXIOM_DISJOINT_WITH:
                    ontologyClass.getDisjointWith().forEach(d -> {
                        OntologyClass dis = (OntologyClass) d;
                        if (dis.getName().equals(classRestriction.getValue())) {
                            validatedClasses.put(c.getKey(), c.getValue());
                        }
                    });
                    break;
                case ANNOTATION_COMMENT:
                    if (ontologyClass.getCommment().equals(classRestriction.getValue()) ||
                            ontologyClass.getCommment().contains((String) classRestriction.getValue())) {
                        validatedClasses.put(c.getKey(), c.getValue());
                    }
                    break;
                case ANNOTATION_IS_DEFINED_BY:
                    ontologyClass.getIsDefinedBy().forEach(d -> {
                        OntologyClass isDefinedBy = (OntologyClass) d;
                        if (isDefinedBy.getName().equals(classRestriction.getValue())) {
                            validatedClasses.put(c.getKey(), c.getValue());
                        }
                    });
                    break;
                case ANNOTATION_SEE_ALSO:
                    ontologyClass.getSeeAlso().forEach(s -> {
                        OntologyClass seeAlso = (OntologyClass) s;
                        if (seeAlso.getName().equals(classRestriction.getValue())) {
                            validatedClasses.put(c.getKey(), c.getValue());
                        }
                    });
            }
        });

        return validatedClasses;
    }


    public HashMap<String, OntologyProperty> validatePropertyDescription(PropertyRestriction propertyRestriction, OntologyModel ontologyModel) {
        HashMap<String, OntologyProperty> validatedProperties = new LinkedHashMap<>();
        ontologyModel.getProperties().entrySet().parallelStream().forEach((p) -> {
            PropertyModel ontologyProperty = p.getValue();
            switch (propertyRestriction.getSubject()) {
               case NAME:
                   if (ontologyProperty.getName().equals(propertyRestriction.getValue())) {
                       // @TODO implementar restrições de propriedade
                   }
           }
        });
        return validatedProperties;
    }
