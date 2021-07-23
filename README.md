[![Maven Central](https://img.shields.io/maven-central/v/online.senpai/newbase60?style=for-the-badge)](https://search.maven.org/search?q=g:%22online.senpai%22%20AND%20a:%22newbase60%22) 
[![GitHub](https://img.shields.io/github/license/SenpaiOnline/newbase60?style=for-the-badge)](https://www.apache.org/licenses/LICENSE-2.0)

This is yet another implementation of NewBase60 algorithm made for Kotlin and Java.

In a nutshell, this library will be useful if you want to implement a URL shortener, like Bitly or TinyURL,
that is URL-safe and does not contain confusing character pairs, such as an uppercase letter O and 0,
an uppercase letter I and 1.

Check out [this article](http://tantek.pbworks.com/w/page/19402946/NewBase60) of Tantek Çelik for more information.
### Examples
```kotlin
numberToSexagesimal(0) // "0"
numberToSexagesimal(60) // "10"
numberToSexagesimal(1337) // "NH"
numberToSexagesimal(9_223_372_036_854_775_807) // "FFD_YXvLFW7"
sexagesimalToNumber("10") // 60L
sexagesimalToNumber("NH") // 1337L
sexagesimalToNumber("NH🥺") // java.text.ParseException(errorOffset=2, detailMessage="Couldn't parse the character "🥺"")
```
### Installation
The artifact is available on [Maven Central](https://search.maven.org/search?q=a:newbase60), so just add the below dependency to your favorite build automation tool.
#### Apache Maven
```xml
<dependency>
  <groupId>online.senpai</groupId>
  <artifactId>newbase60</artifactId>
  <version>1.0.1</version>
</dependency>
```
#### Gradle Groovy DSL
```groovy
implementation 'online.senpai:newbase60:1.0.1'
```
#### Gradle Kotlin DSL
```kotlin
implementation("online.senpai:newbase60:1.0.1")
```
### License
[Apache License, Version 2.0](https://www.apache.org/licenses/LICENSE-2.0)