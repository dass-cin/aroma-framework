package aromaframework.core.domain;

import java.util.Set;

/**
 * Created by diego on 07/07/17.
 */
public class ClassSegmentPair {

    private OntologyClass classSegmentRoot1;
    private OntologyClass classSegmentRoot2;

    public ClassSegmentPair() {
    }

    public ClassSegmentPair(OntologyClass classSegmentRoot1, OntologyClass classSegmentRoot2) {
        this.classSegmentRoot1 = classSegmentRoot1;
        this.classSegmentRoot2 = classSegmentRoot2;
    }

    public OntologyClass getClassSegmentRoot1() {
        return classSegmentRoot1;
    }

    public void setClassSegmentRoot1(OntologyClass classSegmentRoot1) {
        this.classSegmentRoot1 = classSegmentRoot1;
    }

    public OntologyClass getClassSegmentRoot2() {
        return classSegmentRoot2;
    }

    public void setClassSegmentRoot2(OntologyClass classSegmentRoot2) {
        this.classSegmentRoot2 = classSegmentRoot2;
    }
}
