# Single Thread Jackson

[![Build Status](https://travis-ci.com/appenders/appenders-jackson-st.svg?branch=main)](https://travis-ci.com/github/appenders/appenders-jackson-st)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.appenders.jackson/appenders-jackson-st/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.appenders.jackson/appenders-jackson-st/badge.svg)
[![codecov](https://codecov.io/gh/appenders/appenders-jackson-st/branch/main/graph/badge.svg?token=UHMX2NODNW)](https://codecov.io/gh/appenders/appenders-jackson-st)
[![contributions welcome](https://img.shields.io/badge/contributions-welcome-brightgreen.svg?style=flat)](https://github.com/appenders/appenders-jackson-st)

The goal of this library is to increase throughput and efficiency of Jackson FasterXML `UTF8JsonGenerator` during POJOs serialization while preserving as many [jackson-core](https://github.com/FasterXML/jackson-core) features as possible.

This goal was achieved by [SingleThreadJsonFactory](https://github.com/appenders/appenders-jackson-st/blob/main/src/main/java/org/appenders/jackson/singlethread/SingleThreadJsonFactory.java). By creating reusable instances of core components of the generator, [jackson-benchmarks](https://github.com/FasterXML/jackson-benchmarks) show consistent performance improvements in following areas:
* `Throughput` increase by at least 5%
* `Memory allocation` reduction by ~58% (80% with Afterburner)

NOTE: At the moment, it's meant to be used by ONLY ONE thread.

## Non-supported features

* Encodings other than UTF-8
* Writer-based generators
* File-based generators
* Readers (not optimized - this factory is meant to be used for writes only!)
* Closing OutputStream on `IOContext.resourceManaged=true` (`false` on `OutputStream` and `DataOutput` writes)

## Supported features

* Writes to `OutputStream`
* Writes to `DataOutput`
* Pretty printers
* Separators
* Character escapes
* All the other awesome Jackson FasterXML features! :)

## Usage

1. Build:
    ```bash
   mvn clean install
    ```

   Ensure that `com.fasterxml.jackson.core:jackson-core:2.12.0` or newer is available on your classpath - see `Dependencies` section below.


2. Create `com.fasterxml.jackson.databind.ObjectWriter`
    ```java
    ObjectWriter objectWriter = new ObjectMapper(new SingleThreadJsonFactory()).writerFor(Foo.class);
    ```

3. Serialize!
    ```java
   objectWriter.writeValue(oututStream, foo);
   ```

## Dependencies

Be aware that Jackson FasterXML Core jar MUST be provided for this library to work. By design, you can choose which jars you'd like to have on your classpath.

It's compatible with Jackson FasterXML Core v2.12 or higher.

## Notes

This factory was developed as a part of [log4j2-elasticsearch project](https://github.com/rfoltyns/log4j2-elasticsearch) and is available via configuration (pluggable and optional).