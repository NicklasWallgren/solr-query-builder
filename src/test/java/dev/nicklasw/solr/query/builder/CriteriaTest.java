package dev.nicklasw.solr.query.builder;

import static dev.nicklasw.solr.query.builder.Criteria.where;

import dev.nicklasw.solr.query.builder.parser.QueryParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CriteriaTest {

    @Test
    void test() {
        final Criteria criteria = Criteria.where("title").expression("a title")
            .and(where("quantity").between(0, 100).and(where("episodeTitle").fuzzy("a title").boost(1.5f)))
            .or(where("title").regexp(".*title.*")).must()
            .connect()
            .or(where("title").expression("should not match").mustNot());

        var expected = "+(title:a title AND (quantity:[0 TO 100] AND episodeTitle:\"a title\"~^1.5) OR title:/.*title.*/) OR -title:should not match";

        Assertions.assertEquals(expected, QueryParser.createQueryFor(criteria));
    }

}