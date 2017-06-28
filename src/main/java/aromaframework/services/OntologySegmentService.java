package aromaframework.services;

import aromaframework.core.domain.OntologyClass;
import aromaframework.core.domain.OntologyModel;
import aromaframework.core.domain.OntologySegments;
import aromaframework.core.domain.PropertyModel;
import br.cin.ufpe.dass.matchers.core.OntologyProperty;
import br.cin.ufpe.dass.matchers.core.requirement.DataRequirement;
import br.cin.ufpe.dass.matchers.core.requirement.restriction.operation.RequirementOperation;
import br.cin.ufpe.dass.matchers.core.requirement.restriction.subject.ClassInfo;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by diego on 14/06/17.
 */
@Service
public class OntologySegmentService {

    private final RequirementsService requirementsService;

    public OntologySegmentService(RequirementsService requirementsService) {
        this.requirementsService = requirementsService;
    }

    /**
     *  Generate ontology segments based on application requirements.
     * @param ontology1
     * @param ontology2
     * @param dataRequirements
     * @return a set of ontology elements related to the defined requirements
     */
    public OntologySegments generateSegments(OntologyModel ontology1, OntologyModel ontology2, Set<DataRequirement> dataRequirements) {

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

        return new OntologySegments(ontology1ClassesSegments, ontology2ClassesSegments, ontology1PropertiesSegments, ontology2PropertiesSegments);

    }

}
