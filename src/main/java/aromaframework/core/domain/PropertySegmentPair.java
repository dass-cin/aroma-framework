package aromaframework.core.domain;

import br.cin.ufpe.dass.matchers.core.OntologyProperty;

import java.util.Set;

/**
 * Created by diego on 07/07/17.
 */
public class PropertySegmentPair {

    private PropertyModel propertySegment1;
    private PropertyModel propertySegment2;

    public PropertySegmentPair() {
    }

    public PropertySegmentPair(PropertyModel propertySegment1, PropertyModel propertySegment2) {
        this.propertySegment1 = propertySegment1;
        this.propertySegment2 = propertySegment2;
    }

    public PropertyModel getPropertySegment1() {
        return propertySegment1;
    }

    public void setPropertySegment1(PropertyModel propertySegment1) {
        this.propertySegment1 = propertySegment1;
    }

    public PropertyModel getPropertySegment2() {
        return propertySegment2;
    }

    public void setPropertySegment2(PropertyModel propertySegment2) {
        this.propertySegment2 = propertySegment2;
    }
}
