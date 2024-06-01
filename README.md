# FloatingPointBinaryEncoder

## Overview

The `FloatingPointBinaryEncoder` provides an interface and multiple implementations for converting 
floating-point number strings to their binary-encoded representations based on IEEE-754 standard. 
The project includes a facade (`FloatingPointBinaryEncoders`) that simplifies access to different encoding strategies.

## Features

- **Binary 32**: Single precision binary encoding of floating-point numbers.
- **Binary 64**: Double precision binary encoding of floating-point numbers.

## Installation

### Prerequisites

- Java Development Kit (JDK) 17 or higher
- Apache Maven 3.6 or higher

## Build 

```shell
mvn clean install
```

## Usage

```java
import com.github.golgotha.floatingpoint.FloatingPointBinaryEncoders;

public class Main {
    public static void main(String[] args) {
        String binary32 = FloatingPointBinaryEncoders.binary32().encodeWithBasic("123.32");
        String binary64 = FloatingPointBinaryEncoders.binary64().encodeWithAdvanced("123.32");

        System.out.println("Binary32 Encoded: " + binary32);
        System.out.println("Binary64 Encoded: " + binary64);
    }
}
```
