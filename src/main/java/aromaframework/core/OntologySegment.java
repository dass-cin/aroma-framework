package aromaframework.core;

import br.cin.ufpe.dass.matchers.core.Ontology;
import br.cin.ufpe.dass.matchers.core.requirement.DataRequirement;

/**
 * Created by diego on 05/06/17.
 */
public class OntologySegment {

    Ontology ontology1, ontology2;
    
    DataRequirement dataRequirements;

    public Ontology getOntology1() {
        return ontology1;
    }

    public void setOntology1(Ontology ontology1) {
        this.ontology1 = ontology1;
    }

    public Ontology getOntology2() {
        return ontology2;
    }

    public void setOntology2(Ontology ontology2) {
        this.ontology2 = ontology2;
    }

    public DataRequirement getDataRequirements() {
        return dataRequirements;
    }

    public void setDataRequirements(DataRequirement dataRequirements) {
        this.dataRequirements = dataRequirements;
    }
}
