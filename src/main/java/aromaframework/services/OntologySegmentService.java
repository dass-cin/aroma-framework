package aromaframework.services;

import aromaframework.core.domain.OntologyClass;
import aromaframework.core.domain.OntologyModel;
import br.cin.ufpe.dass.matchers.core.requirement.DataRequirement;
import br.cin.ufpe.dass.matchers.core.requirement.restriction.operation.RequirementOperation;
import br.cin.ufpe.dass.matchers.core.requirement.restriction.subject.ClassInfo;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by diego on 14/06/17.
 */
@Service
public class OntologySegmentService {

    public void generateSegmentPairs(OntologyModel ontology1, OntologyModel ontology2, Set<DataRequirement> dataRequirements) {

        OntologyModel subOntology1 = new OntologyModel();
        subOntology1.setNothing(ontology1.getNothing());
        subOntology1.setThing(ontology1.getThing());
        subOntology1.getClasses().putAll(ontology1.getClasses().entrySet().parallelStream()
                .filter(c -> validateRequirements(dataRequirements, c.getValue()) )
                .collect(Collectors.toMap(c -> c.getKey(), c -> c.getValue())));

        //Varrer elementos e verificar se possuem o mesmo nome
        //



    }

    private boolean validateRequirements(Set<DataRequirement> dataRequirements, OntologyClass ontologyClass) {

        //@TODO fazer validacao de requisitos

        //pra cada dataRequirement vou verificar se a classe é válida
        dataRequirements.stream().anyMatch( requirement ->
                requirement.getClassRestrictions().stream().anyMatch( classRestriction -> {
                    classRestriction.getSubject().equals(ClassInfo.NAME)
                            && classRestriction.getOperation().equals(RequirementOperation.EQUAL)
                              && (classRestriction.getValue().equals(ontologyClass.getName())
                                     || ontologyClass.getEquiv().stream().))
                }));

    }



}
