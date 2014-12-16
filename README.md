# Semester

[![Build Status](https://travis-ci.org/watermint/Semester.png)](https://travis-ci.org/watermint/Semester)

Collection of small apps and libraries. This project collection is the practice for Scala and designing APIs in real life use cases. 

# Projects

Project names are categorized like below.

* etude-epice ... Basic utilities
* etude-pain ... Wrapper for external libraries.
* etude-gazpacho ... Frontend framework, or wrapper for frontend framework.
* etude-manieres ... Design convention, or rules.
* etude-pintxos ... Wrapper for external services.
* etude-vino ... Business logic.
* etude-table ... Application, or assembled logics.

## etude-epice

* [Http](etude-epice-http) - HTTP Client wrapper API
* [Sel](etude-epice-sel) - HTTP/REST Client API
* [Logging](etude-epice-logging) - Logging API
* [Undisclosed tests](etude-epice-undisclosed) - Test wrapper. Separate secret keys for specific tests.
* [Utility](etude-epice-utility) - Generic utilities

## etude-pain

* [ElasticSearch](etude-pain-elasticsearch) - [ElasticSearch](http://www.elasticsearch.org) 
* [Highlight](etude-pain-highlight) - Source code highlighter by [highlight.js](http://highlightjs.org)
* [Rangement](etude-pain-rangement) - HTML normalizer wrapper API
* [Tika](etude-pain-tika) - File description detector by [Apache Tika](http://tika.apache.org) 
* [Foundation](etude-pain-foundation) - Foundation Kit integration API on Mac OS X

## etude-gazpacho

* [Spray](etude-gazpacho-spray) - [Spray](http://spray.io) configuration base

## etude-manieres

* [Domain](etude-manieres-domain) - Core Libraries for Domain Driven Design (DDD).
* [Domain Event](etude-manieres-event) - Domain Event for DDD.

## etude-pintxos

* [ChatWork](etude-pintxos-chatwork) - [ChatWork](http://chatwork.com) API
* [Pocket](etude-pintxos-pocket) - [Pocket](http://getpocket.com) API
* [Things](etude-pintxos-things) - [Things](https://culturedcode.com/things/) app integration.
* [NSUserNotification](etude-pintxos-nsunc) - Notification Center of Mac OS X integration.

## etude-vino

* [ChatWork](etude-vino-chatwork) - ChatWork Business Logics
* [Code](etude-vino-code) - Source code search engine base logics.

## etude-table

* [Bolognese](etude-table-bolognese) - Practicing [spray](http://spray.io)
* [Chitarra](etude-table-chitarra) - OPML to Markdown

# Requirements

* JDK8u20 or later
* Scala 2.11.1 or later

# License

Unless otherwise noted, all files in this distribution are released under the MIT License.
Exceptions are noted within the associated source files.

----

The MIT License (MIT) Copyright (c) 2013, 2014 Takayuki Okazaki

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
