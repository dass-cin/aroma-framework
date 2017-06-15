package aromaframework.core.domain;

import java.util.Set;

/**
 * Created by diego on 06/06/17.
 */
public class PropertyModel {

    private String id;

    private String name;

    private String URI;

    private String label;

    private Set comments;

    private String type;

    private Set ranges;

    private Set domains;

    private String nameSpace;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Set getComments() {
        return comments;
    }

    public void setComments(Set comments) {
        this.comments = comments;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Set getRanges() {
        return ranges;
    }

    public void setRanges(Set ranges) {
        this.ranges = ranges;
    }

    public Set getDomains() {
        return domains;
    }

    public void setDomains(Set domains) {
        this.domains = domains;
    }

    public String getNameSpace() {
        return nameSpace;
    }

    public void setNameSpace(String nameSpace) {
        this.nameSpace = nameSpace;
    }

    public String getURI() {
        return URI;
    }

    public void setURI(String URI) {
        this.URI = URI;
    }
}
