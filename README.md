# Single Thread Jackson

[![Build Status](https://travis-ci.com/appenders/appenders-jackson-st.svg?branch=main)](https://travis-ci.com/github/appenders/appenders-jackson-st)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.appenders.st/appenders-jackson-st/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.appenders.st/appenders-jackson-st/badge.svg)
[![codecov](https://codecov.io/gh/appenders/appenders-jackson-st/branch/main/graph/badge.svg?token=UHMX2NODNW)](https://codecov.io/gh/appenders/appenders-jackson-st)
[![contributions welcome](https://img.shields.io/badge/contributions-welcome-brightgreen.svg?style=flat)](https://github.com/appenders/appenders-jackson-st)

The goal of this library is to increase throughput and efficiency of Jackson FasterXML `UTF8JsonGenerator` during POJOs serialization while preserving as many [jackson-core](https://github.com/FasterXML/jackson-core) features as possible.

This goal was achieved by [SingleThreadJsonFactory](https://github.com/appenders/appenders-jackson-st/blob/main/src/main/java/org/appenders/jackson/singlethread/SingleThreadJsonFactory.java). By creating reusable instances of core components of the generator, [jackson-benchmarks with single-thread mapper](https://github.com/appenders/jackson-benchmarks/tree/2.12) show consistent performance improvements in following areas:
* `Throughput` increase by at least 5%
* `Memory allocation` reduction by ~58% (80% with Afterburner)

NOTE: It's meant to be used by ONLY ONE thread (at a time..).

## Benchmarks

**Isolated i7-10875H core** (isolated vs non-isolated didn't make much difference, but removing the noise is always good)

```shell
java -jar target/microbenchmarks.jar ".*JsonStdWrite.*PojoMediaItem.*" -prof gc -wi 20 -w 1 -i 20 -r 1 -f 10 -t 1
```

```
Benchmark                                                                             Mode  Cnt        Score       Error   Units
JsonStdWriteAfterburner.writePojoMediaItem                                           thrpt  200  1080925.594 ±  3189.635   ops/s
JsonStdWriteAfterburner.writePojoMediaItem:·gc.alloc.rate                            thrpt  200      297.226 ±     0.892  MB/sec
JsonStdWriteAfterburner.writePojoMediaItem:·gc.alloc.rate.norm                       thrpt  200      432.000 ±     0.001    B/op
JsonStdWriteAfterburner.writePojoMediaItem:·gc.churn.PS_Eden_Space                   thrpt  200      340.449 ±   241.829  MB/sec
JsonStdWriteAfterburner.writePojoMediaItem:·gc.churn.PS_Eden_Space.norm              thrpt  200      497.475 ±   353.390    B/op
JsonStdWriteAfterburner.writePojoMediaItem:·gc.churn.PS_Survivor_Space               thrpt  200        0.162 ±     0.167  MB/sec
JsonStdWriteAfterburner.writePojoMediaItem:·gc.churn.PS_Survivor_Space.norm          thrpt  200        0.238 ±     0.244    B/op
JsonStdWriteAfterburner.writePojoMediaItem:·gc.count                                 thrpt  200       20.000              counts
JsonStdWriteAfterburner.writePojoMediaItem:·gc.time                                  thrpt  200       49.000                  ms
JsonStdWriteAfterburnerSingleThread.writePojoMediaItem                               thrpt  200  1158554.881 ±  2654.388   ops/s
JsonStdWriteAfterburnerSingleThread.writePojoMediaItem:·gc.alloc.rate                thrpt  200       64.825 ±     0.144  MB/sec
JsonStdWriteAfterburnerSingleThread.writePojoMediaItem:·gc.alloc.rate.norm           thrpt  200       88.000 ±     0.001    B/op
JsonStdWriteAfterburnerSingleThread.writePojoMediaItem:·gc.count                     thrpt  200          ≈ 0              counts
JsonStdWriteVanilla.writePojoMediaItem                                               thrpt  200   903169.064 ± 24311.624   ops/s
JsonStdWriteVanilla.writePojoMediaItem:·gc.alloc.rate                                thrpt  200      339.893 ±     9.151  MB/sec
JsonStdWriteVanilla.writePojoMediaItem:·gc.alloc.rate.norm                           thrpt  200      592.000 ±     0.001    B/op
JsonStdWriteVanilla.writePojoMediaItem:·gc.churn.PS_Eden_Space                       thrpt  200      341.279 ±   242.417  MB/sec
JsonStdWriteVanilla.writePojoMediaItem:·gc.churn.PS_Eden_Space.norm                  thrpt  200      607.412 ±   441.789    B/op
JsonStdWriteVanilla.writePojoMediaItem:·gc.churn.PS_Survivor_Space                   thrpt  200        0.002 ±     0.002  MB/sec
JsonStdWriteVanilla.writePojoMediaItem:·gc.churn.PS_Survivor_Space.norm              thrpt  200        0.003 ±     0.005    B/op
JsonStdWriteVanilla.writePojoMediaItem:·gc.count                                     thrpt  200       20.000              counts
JsonStdWriteVanilla.writePojoMediaItem:·gc.time                                      thrpt  200       51.000                  ms
JsonStdWriteVanillaSingleThread.writePojoMediaItem                                   thrpt  200  1006790.801 ±  9718.121   ops/s
JsonStdWriteVanillaSingleThread.writePojoMediaItem:·gc.alloc.rate                    thrpt  200      158.969 ±     1.551  MB/sec
JsonStdWriteVanillaSingleThread.writePojoMediaItem:·gc.alloc.rate.norm               thrpt  200      248.000 ±     0.001    B/op
JsonStdWriteVanillaSingleThread.writePojoMediaItem:·gc.churn.PS_Eden_Space           thrpt  200      170.224 ±   175.684  MB/sec
JsonStdWriteVanillaSingleThread.writePojoMediaItem:·gc.churn.PS_Eden_Space.norm      thrpt  200      264.385 ±   272.990    B/op
JsonStdWriteVanillaSingleThread.writePojoMediaItem:·gc.churn.PS_Survivor_Space       thrpt  200        0.136 ±     0.149  MB/sec
JsonStdWriteVanillaSingleThread.writePojoMediaItem:·gc.churn.PS_Survivor_Space.norm  thrpt  200        0.210 ±     0.229    B/op
JsonStdWriteVanillaSingleThread.writePojoMediaItem:·gc.count                         thrpt  200       10.000              counts
JsonStdWriteVanillaSingleThread.writePojoMediaItem:·gc.time                          thrpt  200       30.000                  ms
```


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

1. Add this snippet to your `pom.xml` file
 ```xml
<dependency>
    <groupId>org.appenders.st</groupId>
    <artifactId>appenders-jackson-st</artifactId>
    <version>1.0.1</version>
</dependency>
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

This factory was developed as a part of [log4j2-elasticsearch project](https://github.com/rfoltyns/log4j2-elasticsearch).
