package aromaframework.services;

import aromaframework.AromaFrameworkApplication;
import aromaframework.core.domain.OntologyModel;
import br.cin.ufpe.dass.matchers.core.requirement.DataRequirement;
import br.cin.ufpe.dass.matchers.core.requirement.restriction.ClassRestriction;
import br.cin.ufpe.dass.matchers.core.requirement.restriction.PropertyRestriction;
import br.cin.ufpe.dass.matchers.core.requirement.restriction.operation.RequirementOperation;
import br.cin.ufpe.dass.matchers.core.requirement.restriction.subject.ClassInfo;
import br.cin.ufpe.dass.matchers.core.requirement.restriction.subject.PropertyInfo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;

import static junit.framework.TestCase.assertTrue;
import static org.testng.AssertJUnit.assertNotNull;

/**
 * Created by diego on 07/06/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = AromaFrameworkApplication.class)
public class OntologySegmentServiceTest {

    @Autowired
    private OntologySegmentService ontologySegmentService;

    @Autowired
    private OntologyService ontologyService;

    private URI sourceOntology;

    @Before
    public void prepare() {
        sourceOntology = URI.create("file:///Users/diego/ontologies/conference/cmt.owl");
    }

    @Test
    public void hasToGenerateSegment() {

        URI sourceOntology = URI.create("file:///Users/diego/ontologies/conference/cmt.owl");
        URI targetOntology = URI.create("file:///Users/diego/ontologies/conference/Conference.owl");

        OntologyModel ontology1 = ontologyService.loadOntologyModel(sourceOntology);
        OntologyModel ontology2 = ontologyService.loadOntologyModel(targetOntology);

        Set<DataRequirement> dataRequirementSet = new HashSet<>();
        DataRequirement req1 = new DataRequirement();
        req1.addClassRestriction(new ClassRestriction(ClassInfo.NAME, RequirementOperation.EQUAL, "conference"));
        req1.addClassRestriction(new ClassRestriction(ClassInfo.NAME, RequirementOperation.EQUAL, "person"));
        req1.addClassRestriction(new ClassRestriction(ClassInfo.NAME, RequirementOperation.EQUAL, "paper"));
        req1.addClassRestriction(new ClassRestriction(ClassInfo.NAME, RequirementOperation.EQUAL, "author"));
        req1.addClassRestriction(new ClassRestriction(ClassInfo.NAME, RequirementOperation.EQUAL, "document"));
        req1.addPropertyRestriction(new PropertyRestriction(PropertyInfo.NAME, RequirementOperation.EQUAL, "decision"));
        req1.addPropertyRestriction(new PropertyRestriction(PropertyInfo.NAME, RequirementOperation.EQUAL, "published"));
        req1.addPropertyRestriction(new PropertyRestriction(PropertyInfo.NAME, RequirementOperation.EQUAL, "author"));
        dataRequirementSet.add(req1);

        ontologySegmentService.generateSegments(ontology1, ontology2, dataRequirementSet);

    }


}
