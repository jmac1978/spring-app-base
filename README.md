
# Spring App Base

Base set of tools and helpers originally designed for use within Spring MVC
applications though not restricted to this use-case.


## A Note on Dependencies

Spring App Base intentionally uses `provided` scope for most dependencies, the
only transient dependency included is [apache commons-lang](https://commons.apache.org/proper/commons-lang/)
as it's almost used everywhere.

This strategy allows you to control your dependencies. For example you may
choose to use the spring MVC helpers but not wish to include spring-security.

For each component required dependencies will be listed which must be included at
runtime.


## JDBC Tools

### SQL File Parser

`JdbcHelper.getSql` has overloaded methods to take an `InputStream` or path to
return a string of the streams contents stripping SQL like comments

- Line comments `--` are removed.
- Block comments `/* */` are removed.

#### Usage

```java
// Import the static method getSql.
import static com.drunkendev.jdbc.JdbcHelper.getSql;

// Get SQL from a file on the filesystem.
// A path object overload is also available as of 1.1.
String sql = getSql("script.sql");

// A good use case is storing your SQL resources in the same packages as a
// service class.
String sql = getSql(App.class.getResourceAsStream("script.sql"));
```


### `ResultSetExtractor` factories

`JdbcHelper.singletonExtractor` and `JdbcHelper.singletonOptionalExtractor`
provide convenience methods for building new single record extractors from
a given `RowMapper`. This can be used as follows.

```java
private static final RowMapper<Customer> MAPPER_CUST = (rs, i)
        -> new Customer(
                rs.getLong("ID"),
                rs.getString("NAME"),
                rs.getString("EMAIL"));
private static final ResultSetExtractor<Customer> EXTRACTOR_CUST
        = singletonExtractor(MAPPER_CUST);
```


### JdbcManager



## Query Cache


## Mail Queue


## Menu Builder


## JSR-310 taglib


## App Config

- combined property sources.
- taglib


# Userlog


## Other

- IE Compatibility Filter

### FileUtils

`FileUtils` contains unchecked versions of methods from `java.nio.file.Files`
for convenience use within lambda expressions.


