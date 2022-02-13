package dev.nicklasw.query.builder;

import static dev.nicklasw.query.builder.Criteria.where;
import static org.junit.jupiter.api.Assertions.*;

import dev.nicklasw.query.builder.parser.QueryParser;
import org.junit.jupiter.api.Test;

class CriteriaTest {

    // TODO, to be implemented in next release

    @Test
    void test() {
        final Criteria criteria = Criteria.where("title").expression("a title")
            .and(where("quantity").between(0, 100).and(where("episodeTitle").fuzzy("a title").boost(1.5f)))
            .or(where("title").regexp(".*title.*")).must()
            .connect()
            .or(where("title").expression("should not match").mustNot());

        var expected = "+(title:a title AND (quantity:[0 TO 100] AND episodeTitle:\"a title\"~^1.5) OR title:/.*title.*/) OR -title:should not match";

        assertEquals(expected, QueryParser.createQueryFor(criteria));
    }



}