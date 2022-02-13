module dev.nicklasw.query.builder {
    requires static lombok;
    requires static com.github.spotbugs.annotations;
    requires org.apache.commons.lang3;
    exports dev.nicklasw.query.builder;
    exports dev.nicklasw.query.builder.fields;
}