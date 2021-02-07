# HelperQueryDSL
Helper to usage of QueryDSL

### About

The project provides the resources to use QueryDSL's `BooleanExpression` in **Spring** repositories.
It also provides a utility class for creating classes that do dynamic filters on an entity.

### How to use

Add dependency:

```xml
<dependency>
    <groupId>io.fitnezz.helper.querydsl</groupId>
    <artifactId>helper-query-dsl</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

Add `QueryDSL` dependencies:

```xml
<properties>
    <querydsl.version>4.2.2</querydsl.version>
</properties>
<dependencies>
    <dependency>
        <groupId>com.querydsl</groupId>
        <artifactId>querydsl-apt</artifactId>
        <version>${querydsl.version}</version>
    </dependency>
    <dependency>
        <groupId>com.querydsl</groupId>
        <artifactId>querydsl-jpa</artifactId>
        <classifier>apt</classifier>
        <version>${querydsl.version}</version>
    </dependency>
</dependencies>
```

Configure the build:

```xml
<plugin>
    <groupId>com.mysema.maven</groupId>
    <artifactId>apt-maven-plugin</artifactId>
    <version>1.1.3</version>
    <executions>
        <execution>
            <goals>
                <goal>process</goal>
            </goals>
            <configuration>
                <outputDirectory>target/generated-sources/java</outputDirectory>
                <processor>com.querydsl.apt.jpa.JPAAnnotationProcessor</processor>
            </configuration>
        </execution>
    </executions>
</plugin> 
```
