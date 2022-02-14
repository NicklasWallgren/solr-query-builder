[![Continuous Integration](https://github.com/nicklaswallgren/solr-query-builder/workflows/ci/badge.svg)](https://github.com/nicklaswallgren/solr-query-builder/actions)
[![License](https://img.shields.io/github/license/nicklaswallgren/solr-query-builder)](https://github.com/nicklaswallgren/solr-query-builder/blob/master/LICENSE)
[![Language grade: Java](https://img.shields.io/lgtm/grade/java/g/NicklasWallgren/solr-query-builder.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/NicklasWallgren/solr-query-builder/context:java)

# Solr Query Builder

An Apache Solr Query Builder for writing Solr queries. It supports parts of [The standard Query Parser](https://solr.apache.org/guide/7_2/the-standard-query-parser.html). 
More features are to be added.

There are some [examples that may be useful](./examples).

This library is heavily influenced by `spring-data-solr`, but adds additional functionality and Boolean Operators. 

## Documentation
See the project's [Javadoc](https://nicklaswallgren.github.io/solr-query-builder/).

## Installation

The artifact is available through Maven Central via Sonatype.

### Maven

```xml

<dependency>
    <groupId>dev.nicklasw</groupId>
    <artifactId>solr-query-builder</artifactId>
    <version>0.10.0</version>
</dependency>
```

### Gradle

```
implementation 'dev.nicklasw:solr-query-builder:0.0.0'
```

## Changelog

Please see the [changelog](./CHANGELOG.md) for a release history and indications on how to upgrade from one version to
another.

## Contributing

If you find any problems or have suggestions about this library, please submit an issue. Moreover, any pull request,
code review and feedback are welcome.

## Code Guide

We use GitHub Actions to make sure the codebase is consistent and continuously tested (`gradle check`). We try to keep
comments at a maximum of 120 characters of length and code at 120.


## General Usage

```java 
import static dev.nicklasw.query.builder.Criteria.where;
import dev.nicklasw.query.builder.parser.QueryParser;

final Criteria criteria = Criteria.where("title").expression("a title")
    .and(where("quantity").between(0, 100).and(where("episodeTitle").fuzzy("a title").boost(1.5f)))
    .or(where("title").regexp(".*title.*")).must()
    .connect()
    .or(where("title").expression("should not match").mustNot());
    
>>> +(title:a title AND (quantity:[0 TO 100] AND episodeTitle:"a title"~^1.5) OR title:/.*title.*/) OR -title:should not match    
```

## License

[MIT](./LICENSE)
