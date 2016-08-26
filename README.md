
# Spring App Base

Base set of tools and helpers originally designed for use within Spring MVC
applications though not restricted to this use-case.


## JDBC Tools

### SQL File Parser

`JdbcHelper.getSql` has overloaded methods to take an `InputStream` or path to
return a string of the streams contents stripping SQL like comments

- Line comments `--` are removed.
- Block comments `/* */` are removed.


### `ResultSetExtractor` factories

`JdbcHelper.singletonExtractor` and `JdbcHelper.singletonOptionalExtractor`
provide convenience methods for building new single record extractors from
a given `RowMapper`. This can be used as follows.


    private static final RowMapper<Customer> MAPPER_CUST = (rs, i)
            -> new Customer(
                    rs.getLong("ID"),
                    rs.getString("NAME"),
                    rs.getString("EMAIL"));
    private static final ResultSetExtractor<Customer> EXTRACTOR_CUST
            = singletonExtractor(MAPPER_CUST);



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


