# Semester

[![Build Status](https://travis-ci.org/watermint/Semester.png)](https://travis-ci.org/watermint/Semester)

Collection of small apps and libraries. This project collection is the practice for Scala and designing APIs in real life use cases. 

# Projects

## Application

* [Arrabbiata](etude/app/arrabbiata) - ChatWork utility UI
    * Requires JDK8 Update 20 (tested on JDK 8 update 20 build 19, 20)
* [Bolognese](etude/app/bolognese) - Practicing [spray](http://spray.io)
* [Gare](etude/app/gare) - Application dispatcher
    * Note: Broken on scala 2.11 due to twitter libs are not prepared for scala 2.11

## Libraries

* [Pocket](etude/bookmark/pocket) - [Pocket](http://getpocket.com) API
    * Note: Authentication callbacks are broken on scala 2.11 due to twitter libs are not prepared for scala 2.11
* [ChatWork](etude/messaging/chatwork) - [ChatWork](http://chatwork.com) API
* [Fextile](etude/desktop/fextile) - Fextile. Twitter bootstrap like UI framework for ScalaFX.
* [Domain](etude/domain/core) - Libraries for Domain Driven Development (DDD).
* [Html](etude/foundation/html) - HTML parse wrapper API
* [Http](etude/foundation/http) - HTTP Client wrapper API
* [i18n](etude/foundation/i18n) - Internationalization (i18n) API
* [Utility](etude/foundation/utility) - Generic utilities
* [Things](etude/ticket/things) - [Things](https://culturedcode.com/things/) app integration.
* [Undisclosed tests](etude/test/undisclosed) - Test wrapper. Separate secret keys for specific tests.

# Requirements

* Java SE 8 or later
* Scala 2.10.4, 2.11.1

# License

The MIT License (MIT) Copyright (c) 2013, 2014 Takayuki Okazaki

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
