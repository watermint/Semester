# Mac OS X Notification Center Integration

## Requirements

* Application must be packaged as Mac's execution format .app. See [Packaging a Java App for Distribution on a Mac](http://docs.oracle.com/javase/7/docs/technotes/guides/jweb/packagingAppsForMac.html)

## ant build.xml sample

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project name="Semester Bundle" default="bundle" basedir=".">

    <taskdef name="bundleapp"
             classname="com.oracle.appbundler.AppBundlerTask"
             classpath="appbundler-1.0.jar" />

    <target name="bundle">
        <bundleapp outputdirectory="."
            name="Semester"
            displayname="semester-service-nsunc"
            identifier="semester.service-nsunc"
            mainclassname="semester.service.nsunc.Notification">
            <classpath file="semester-service-nsunc-assembly-0.14.5.jar" />
        </bundleapp>
    </target>

</project>
```

# License


[Original codes](https://github.com/JetBrains/intellij-community/blob/master/platform/platform-impl/src/com/intellij/ui/MountainLionNotifications.java) are from JetBrains under the Apache 2.0 License.
Portions Copyright (c) 2014 Takayuki Okazaki.

```java
/*
 * Copyright 2000-2012 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
```

