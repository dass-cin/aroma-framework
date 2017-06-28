package aromaframework.services;

import aromaframework.components.TextNormalizer;
import aromaframework.core.domain.OntologyClass;
import aromaframework.core.domain.OntologyModel;
import aromaframework.core.domain.PropertyModel;
import br.cin.ufpe.dass.matchers.core.OntologyProperty;
import br.cin.ufpe.dass.matchers.core.requirement.restriction.ClassRestriction;
import br.cin.ufpe.dass.matchers.core.requirement.restriction.PropertyRestriction;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.rdf.model.Resource;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;

/**
 * Created by diego on 15/06/17.
 */
@Service
public class RequirementsService {

    private final TextNormalizer textNormalizer;

    public RequirementsService(TextNormalizer textNormalizer) {
        this.textNormalizer = textNormalizer;
    }

   public HashMap<String, OntologyClass> validateClassRestrictions(Set<ClassRestriction> classRestrictions, OntologyModel ontologyModel) {
        HashMap<String, OntologyClass> validatedClasses = new LinkedHashMap<>();
       classRestrictions.forEach(classRestriction -> {
           ontologyModel.getClasses().entrySet().parallelStream().forEach((c) -> {
               OntologyClass ontologyClass = c.getValue();
               switch (classRestriction.getSubject()) {
                   case NAME:
                       if (ontologyClass.getName() != null && (ontologyClass.getName().equalsIgnoreCase((String) classRestriction.getValue()) ||
                               textNormalizer.basicNormalizing(ontologyClass.getName()).equalsIgnoreCase((String) classRestriction.getValue()))) {
                           validatedClasses.put(c.getKey(), c.getValue());
                       }
                       break;
                   case ANNOTATION_LABEL:
                       if (ontologyClass.getLabel() != null && (ontologyClass.getLabel().equalsIgnoreCase((String) classRestriction.getValue()) ||
                               textNormalizer.basicNormalizing(ontologyClass.getLabel()).equalsIgnoreCase((String) classRestriction.getValue()))) {
                           validatedClasses.put(c.getKey(), c.getValue());
                       }
                       break;
                   case AXIOM_SUBCLASS_OF:
                       ontologyClass.getSubs().forEach(s -> {
                           OntologyClass sub = (OntologyClass) s;
                           if (sub.getName().equalsIgnoreCase((String) classRestriction.getValue())) {
                               validatedClasses.put(c.getKey(), c.getValue());
                           }
                       });
                       break;
                   case AXIOM_EQUIVALENT_CLASS:
                       ontologyClass.getEquiv().forEach(e -> {
                           OntologyClass eqv = (OntologyClass) e;
                           if (eqv.getName().equalsIgnoreCase((String) classRestriction.getValue())) {
                               validatedClasses.put(c.getKey(), c.getValue());
                           }
                       });
                       break;
                   case AXIOM_DISJOINT_WITH:
                       ontologyClass.getDisjointWith().forEach(d -> {
                           OntologyClass dis = (OntologyClass) d;
                           if (dis.getName().equalsIgnoreCase((String) classRestriction.getValue())) {
                               validatedClasses.put(c.getKey(), c.getValue());
                           }
                       });
                       break;
                   case ANNOTATION_COMMENT:
                       if (ontologyClass.getCommment() != null && (ontologyClass.getCommment().equalsIgnoreCase((String) classRestriction.getValue()) ||
                               ontologyClass.getCommment().contains((String) classRestriction.getValue()))) {
                           validatedClasses.put(c.getKey(), c.getValue());
                       }
                       break;
                   case ANNOTATION_IS_DEFINED_BY:
                       ontologyClass.getIsDefinedBy().forEach(d -> {
                           OntologyClass isDefinedBy = (OntologyClass) d;
                           if (isDefinedBy.getName().equalsIgnoreCase((String) classRestriction.getValue())) {
                               validatedClasses.put(c.getKey(), c.getValue());
                           }
                       });
                       break;
                   case ANNOTATION_SEE_ALSO:
                       ontologyClass.getSeeAlso().forEach(s -> {
                           OntologyClass seeAlso = (OntologyClass) s;
                           if (seeAlso.getName().equalsIgnoreCase((String) classRestriction.getValue())) {
                               validatedClasses.put(c.getKey(), c.getValue());
                           }
                       });
               }
           });
       });

        return validatedClasses;
    }


    public HashMap<String, PropertyModel> validatePropertyDescriptions(Set<PropertyRestriction> propertyRestrictions, OntologyModel ontologyModel) {
        HashMap<String, PropertyModel> validatedProperties = new LinkedHashMap<>();
        propertyRestrictions.forEach(propertyRestriction -> {
            ontologyModel.getProperties().entrySet().parallelStream().forEach((p) -> {
                PropertyModel ontologyProperty = p.getValue();
                switch (propertyRestriction.getSubject()) {
                    case NAME:
                        if (ontologyProperty.getName() != null &&
                                textNormalizer.advancedNormalizing(ontologyProperty.getName()).equalsIgnoreCase((String) propertyRestriction.getValue())) {
                            validatedProperties.put(p.getKey(), p.getValue());
                        }
                        break;
                    case SUB_PROPERTY_OF:
                        ontologyProperty.getSubProperties().forEach(subProperty -> {
                            OntProperty sub = (OntProperty) subProperty;
                            if (sub.getLocalName() != null && textNormalizer.advancedNormalizing(sub.getLocalName()).equalsIgnoreCase((String) propertyRestriction.getValue())) {
                                validatedProperties.put(p.getKey(), p.getValue());
                            }
                        });
                        break;
                    case EQUIVALENT_PROPERTY:
                        ontologyProperty.getEquivalentProperties().forEach(e -> {
                            OntProperty eqv = (OntProperty) e;
                            if (eqv.getLocalName() != null && textNormalizer.advancedNormalizing(eqv.getLocalName()).equalsIgnoreCase((String) propertyRestriction.getValue())) {
                                validatedProperties.put(p.getKey(), p.getValue());
                            }
                        });
                        break;
                    case TRANSITIVE_PROPERTY:
                        if (p.getValue().isTransitive()) {
                            validatedProperties.put(p.getKey(), p.getValue());
                        }
                        break;
                    case DOMAIN:
                        ontologyProperty.getDomains().forEach(domain -> {
                            Resource dom = (Resource) domain;
                            if (dom.getLocalName() != null && textNormalizer.advancedNormalizing(dom.getLocalName()).equalsIgnoreCase((String) propertyRestriction.getValue())) {
                                validatedProperties.put(p.getKey(), p.getValue());
                            }
                        });
                        break;
                    case RANGE:
                        ontologyProperty.getRanges().forEach(r -> {
                            Resource range = (Resource) r;
                            if (range.getLocalName() != null && textNormalizer.advancedNormalizing(range.getLocalName()).equalsIgnoreCase((String) propertyRestriction.getValue())) {
                                validatedProperties.put(p.getKey(), p.getValue());
                            }
                        });
                        break;
                    case FUNCTIONAL_PROPERTY:
                        if (p.getValue().isFunctional()) {
                            validatedProperties.put(p.getKey(), p.getValue());
                        }
                        break;
                    case DATATYPE_PROPERTY:
                        if (p.getValue().isDataTypeProperty()) {
                            validatedProperties.put(p.getKey(), p.getValue());
                        }
                        break;
                    case OBJECT_PROPERTY:
                        if (p.getValue().isObjectProperty()) {
                            validatedProperties.put(p.getKey(), p.getValue());
                        }
                        break;
                    case SYMMETRIC_PROPERTY:
                        if (p.getValue().isSymmetricProperty()) {
                            validatedProperties.put(p.getKey(), p.getValue());
                        }
                        break;
                    case INVERSE_FUNCTIONAL_PROPERTY:
                        if (p.getValue().isInverseFunctionalProperty()) {
                            validatedProperties.put(p.getKey(), p.getValue());
                        }
                        break;
                    case INVERSE_OF:
                        ontologyProperty.getInverseOf().forEach(i -> {
                            Resource inverseOf = (Resource) i;
                            if (inverseOf.getLocalName() != null && textNormalizer.advancedNormalizing(inverseOf.getLocalName()).equalsIgnoreCase((String) propertyRestriction.getValue())) {
                                validatedProperties.put(p.getKey(), p.getValue());
                            }
                        });
                        break;
                }
            });
        });
        return validatedProperties;
    }
}