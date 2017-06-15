package aromaframework.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by diego on 06/06/17.
 */
@ConfigurationProperties(prefix="application")
public class ApplicationProperties {

    private Normalizer normalizer = new Normalizer();

    private Ontologies ontologies = new Ontologies();

    public static class Normalizer {

        private String stopWordsFile = "normalizer/stopwords";

        public String getStopWordsFile() {
            return stopWordsFile;
        }

        public void setStopWordsFile(String stopWordsFile) {
            this.stopWordsFile = stopWordsFile;
        }
    }

    public static class Ontologies {

        private String mainLanguage = "en";

        private String snLanguage = "sn";

        private String part = "part_of";

        private String externalNamespaces = "http://www.w3.org/1999/02/22-rdf-syntax-ns#" + ";" +
                        "http://www.w3.org/TR/xmlschema-2/#" + ";" +
                        "http://www.w3.org/2002/07/owl#" + ";" +
                        "http://www.geneontology.org/formats/oboInOwl#" + ";" +
                        "http://www.w3.org/2000/01/rdf-schema#" + ";" +
                        "http://xmlns.com/foaf/0.1/" + ";" +
                        "http://purl.org/dc/elements/1.1/";

        public String getPart() {
            return part;
        }

        public void setPart(String part) {
            this.part = part;
        }

        public String getSnLanguage() {
            return snLanguage;
        }

        public void setSnLanguage(String snLanguage) {
            this.snLanguage = snLanguage;
        }

        public String getMainLanguage() {
            return mainLanguage;
        }

        public void setMainLanguage(String mainLanguage) {
            this.mainLanguage = mainLanguage;
        }

        public String getExternalNamespaces() {
            return externalNamespaces;
        }

        public void setExternalNamespaces(String externalNamespaces) {
            this.externalNamespaces = externalNamespaces;
        }
    }

    public Normalizer getNormalizer() {
        return normalizer;
    }

    public void setNormalizer(Normalizer normalizer) {
        this.normalizer = normalizer;
    }

    public Ontologies getOntologies() {
        return ontologies;
    }

    public void setOntologies(Ontologies ontologies) {
        this.ontologies = ontologies;
    }
}
