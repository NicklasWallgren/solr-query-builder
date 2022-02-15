module dev.nicklasw.solr.query.builder {
    requires static lombok;
    requires static com.github.spotbugs.annotations;
    requires org.apache.commons.lang3;
    exports dev.nicklasw.solr.query.builder;
    exports dev.nicklasw.solr.query.builder.fields;
}