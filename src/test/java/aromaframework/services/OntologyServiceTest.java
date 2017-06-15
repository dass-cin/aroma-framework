package aromaframework.services;

import aromaframework.AromaFrameworkApplication;
import aromaframework.core.domain.OntologyModel;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.util.FileManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.net.URI;

import static junit.framework.TestCase.assertTrue;
import static org.testng.AssertJUnit.assertNotNull;

/**
 * Created by diego on 07/06/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = AromaFrameworkApplication.class)
public class OntologyServiceTest {

    @Autowired
    private OntologyService ontologyService;

    private URI sourceOntology;

    @Before
    public void prepare() {
        sourceOntology = URI.create("file:///Users/diego/ontologies/conference/cmt.owl");
    }

    @Test
    public void hasToLoadOntology() {
        OntModel ontology = ModelFactory.createOntologyModel();
        FileManager.get().readModel(ontology, sourceOntology.getPath());
        OntologyModel ontologyModel =ontologyService.loadOntologyModel(ontology);
        assertNotNull(ontologyModel);
        assertTrue(ontologyModel.getClasses().size() > 0);
        assertTrue(ontologyModel.getProperties().size() > 0);
    }



}
