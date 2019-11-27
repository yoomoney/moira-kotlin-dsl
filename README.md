[![Build Status](https://travis-ci.org/yandex-money-tech/moira-kotlin-dsl.svg?branch=master)](https://travis-ci.org/yandex-money-tech/moira-kotlin-dsl)
[![Build status](https://ci.appveyor.com/api/projects/status/lhh7e8jqp582ro9t?svg=true)](https://ci.appveyor.com/project/f0y/moira-kotlin-dsl)
[![codecov](https://codecov.io/gh/yandex-money-tech/moira-kotlin-dsl/branch/master/graph/badge.svg)](https://codecov.io/gh/yandex-money-tech/moira-kotlin-dsl)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Javadoc](https://img.shields.io/badge/javadoc-latest-blue.svg)](https://yandex-money-tech.github.io/moira-kotlin-dsl/)
[![Download](https://api.bintray.com/packages/yandex-money-tech/maven/moira-kotlin-dsl/images/download.svg) ](https://bintray.com/yandex-money-tech/maven/moira-kotlin-dsl/_latestVersion)

# Yandex.Money Kotlin Moira Triggers DSL

Domain Specific Language (DSL) designed to describe Moira triggers.

## Installation

To install client just add it as a dependency to your build tool.

Gradle example:

```groovy
dependencies {
    implementation 'com.yandex.money.tech:moira-kotlin-dsl:1.0.2'
}
```

## Getting started

To start using DSL you just need to call `trigger` or `triggers` method and `println` result (it is just a string). 
The minimal example is shown below:
```kotlin
fun main(args: Array<String>) {
    println(trigger(name = "Test trigger") {
        tags += "test"
        
        val t1 by target("Test.metric.test")
        
        expression {
            simple {
                t1 falling {
                    warn = 20.0
                    error = 10.0
                }
            }
        }
    })
}
```

This DSL is designed to use with kotlin script, so you may have to configure your build tool to 
[run kotlin scripts](https://kotlinlang.org/docs/tutorials/command-line.html#using-the-command-line-to-run-scripts).

The resulting JSON document can be uploaded to Moira via [Yandex.Money Kotlin Moira Client](). 

## User Guide

### Setting trigger ID

To set trigger ID you just need to set `id` argument of `trigger` function. It's highly recommended to set trigger ID 
in large production systems (search is faster by ID).
```kotlin
trigger(id = "my_test_trigger", name = "Test trigger") { /* ... */ }
```

### Setting required fields

In every trigger you must set required fields. A list of required field is shown below:
+ `name` - the name of trigger that will be displayed in Moira UI.
+ `targets` - the list of target metrics in graphite syntax. You have to provide at least one metric 
  (optionally with wildcards).
+ `tags` - the list of tags that used to group triggers for subscriptions. Additionally, tags can be used to search 
  triggers groups. You have to provide at least one tag. 
+ `expression` - the expression that describe condition when trigger should change its state. See below for expression 
  syntax description.
  
```kotlin
trigger(id = "my_test_trigger", name = "Minimal test trigger") { // set ID and name for trigger
    val t1 by target("Test.metric.*") // target for this trigger. Targets are setting up via local variables, so they can be used in expressions
    
    tags += "test" // add some tags
    
    expression { /* ... */ } // set expression. See below for more information about expressions
}
```

### Expressions

To describe condition whether trigger should fires up changing state event and send notification you have to provide 
**trigger expression**. Moira has 2 kinds of such conditions: **simple** and **advanced**.

#### Simple mode

To set up simple trigger expression you have to define thresholds for **WARN** and/or **ERROR** states (at least one of 
them) and condition mode:
+ `rising` - trigger will change its state and send notification when metric value exceeds defined thresholds:
  ```kotlin
  expression {
      t1 rising {
          warn = 20.0     // change state to WARN when metric value >= 20 && < 100
          error = 100.0   // change state to ERROR when metric value >= 100
      }  
  }
  ```
+ `falling` - trigger will change its state and send notification when metric value falls below defined thresholds:
  ```kotlin
  expression {
      t1 falling {
          warn = 100.0     // change state to WARN when metric value <= 100 && > 20
          error = 20.0   // change state to ERROR when metric value <= 20
      }  
  }
  ```
> **Note that** only 1 target can be defined when simple mode is used.

#### Advanced mode

To describe more flexible conditions for triggers you may use **advanced** mode. In this mode trigger condition is just 
a [govaluate](https://github.com/Knetic/govaluate/blob/master/MANUAL.md) expression. In such expressions you can use 
defined targets and previous state of the trigger. For more information about advanced mode see 
[Moira documentation](https://moira.readthedocs.io/en/latest/user_guide/advanced.html).

```kotlin
expression {
    advanced {
        "($t1 > 1000 && $t2 > 100) ? $OK : $ERROR"
    }
}
```
> **Note that** you can use raw string literal in expressions to define states and targets. So, example expression can 
> be re-written: `(t1 > 1000 && t2 > 100) ? OK : ERROR`. It's recommended to use `TriggerState` enum constants to 
> define states to avoid typos. For targets you can use meaningful names when setting up variable: 
> ```kotlin
> val nginxProcessTime95Percentile by target("Nginx.requests.process_time.p95") // will be transformed to 't1'
> val nginxRequestsCount by target("Nginx.requests.count") // will be transformed to 't2'
> 
> expression {
>     advanced {
>         "($nginxProcessTime95Percentile > 1000 && $nginxRequestsCount > 100) ? $OK : $ERROR" // same expression
>     }
> }
> ```

### Time-to-live

When Moira has not been receiving data for some period it can fires up special event. To control such events 
time-to-live configuration is presented. You can configure time-to-live period and state that trigger will have after 
receiving such event. 

```kotlin
trigger(id = "my_test_trigger", name = "Test trigger with TTL") {
    /* ... */
    
    ttl {
        ttl = java.time.Duration.ofSeconds(100) // if no data for 100 seconds then change trigger's state (10 minutes by default)
        state = TriggerState.ERROR              // change trigger's state to ERROR (NODATA by default) 
    }
}
```

### Watch time schedule

In Moira you can flexible configure watch time of triggers. You can disable some days of week or some periods of day. 
Moira will not send notification when its fired up out of watch time. 

```kotlin
trigger(id = "my_test_trigger", name = "Test trigger with schedule") {
    /* ... */
    
    schedule {
        watchTime = "08:00".."17:00" // watch time is just time range
        zoneOffset = java.time.ZoneOffset.ofHours(-3) // zone offset of watch time (system default by default)
        
        -java.time.DayOfWeek.SUNDAY // turn off notifications on sundays (using unary minus operator) 
    }
}
```

> **Note that** 24/7 watch time is by default. 
> 
> **Note that** the end of watch time interval can be less than start. For example, `"17:00".."03:00"` means that watch 
  time starts at `17:00` of one day and ends at `03:00` of next day.  
