Panda Java Framework
========================

A lightweight Java Framework.


Requirements
------------

 - Java 6


Features
------------

 - panda.bean: Java Bean Utilities
 - panda.bind: Java XML/JSON Serializer
 - panda.castor: Java Type Converter
   - String <-> Number
   - String <-> Date, Calendar
   - Map <-> Java Bean
   - etc.
 - panda.dao: Annotation Based Object/Relational Mapping Library
 - panda.io: IO Utilities
 - panda.lang: Standard java utilities like commons-lang
 - panda.log: Logger library


Ivy Settings
------------
    <ivysettings>
        <settings defaultResolver="default" />
        <resolvers>
            <chain name="public">
                <ibiblio name="maven" m2compatible="true" />
                <ibiblio name="panda" m2compatible="true" root="https://raw.github.com/foolite/panda-repo/master/" />
            </chain>
        </resolvers>
        <include url="${ivy.default.settings.dir}/ivysettings-shared.xml" />
        <include url="${ivy.default.settings.dir}/ivysettings-local.xml" />
        <include url="${ivy.default.settings.dir}/ivysettings-main-chain.xml" />
        <include url="${ivy.default.settings.dir}/ivysettings-default-chain.xml" />
    </ivysettings>


Change Log
-----------

### 1.0.0 (Released: 2013-11-07)
 - Considered stable



OSS
-----------
 - https://github.com/rjeschke/txtmark
 - https://code.google.com/archive/p/markdown4j

