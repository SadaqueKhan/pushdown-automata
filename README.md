# Pushdown Automata

Animated visual toolkit for pushdown automata.
The centre of this project is the design and development of an animated visual simulator for pushdown automata, together with a GUI suitable for using the simulator as an educational tool for beginners. The suggested implementation language is JAVA or C++, but other high level languages would also be possible. A collection of good examples (preferably in XML format) should also be constructed.

# Configuration
1. <a href="#section1">Languages and frameworks</a>
1. <a href="#section2">Installation Instructions</a>
1. <a href="#section3">How to execute the program</a>
1. <a href="#section4">How to execute the tests</a>

<a name="section1" />

## Languages and frameworks 
The app is built overall using the following:
* [Java](https://www.java.com/en/download/), is a programming language that is used to produce this software.
* [Maven](https://maven.apache.org/), is a software project management and comprehension tool used in this software.
* [JavaFX](https://github.com/openjdk/jfx), is a software platform used to create and deliver this desktop application.
* [ControlsFX (version 8.40)](https://github.com/controlsfx/controlsfx),  is an open source project for JavaFX that aims to provide really high quality UI controls and other tools to complement the core JavaFX distribution used in this project. 
* [Junit (version 4.12)](https://junit.org/junit4/), is a unit testing framework for the Java programming language used to build this application. 
* [TestFX](https://github.com/TestFX/TestFX), is a library for simple and clean testing of the JavaFX components used in this project. 
* [Hamcrest](http://hamcrest.org/JavaHamcrest/), is a library of matchers, which can be combined to create flexible expressions of intent for the tests used in this project. 
 
<a name="section2" />

## Installation instructions

### Prerequisites
You will need to have the following installed on your machine
* [Java](https://www.java.com/en/download/)
* [Maven](https://maven.apache.org/)

***
### WARNING

Please note if any of the modules in the current stable version of the program have become deprecated, the application may fail to execute. If this occurs please feel free to email me, so that I can update the program accordingly. If you wish to remedy this yourself, you will have to install a version of the module that isn't yet deprecated and adjust the current source code to meet the implementations of the non-deprecated module.

***
<a name="section3" />

## How to execute the program

_**Disclaimer:**_ **MUST** have the installed the prerequisites specified in the installation instructions for the commands below to work.

1.	Open the terminal
2.	Navigate to the program directory
3.	Run the commands below to either execute the program or create a JAR

### Command to run the program
**Option 1 -**

Executable JAR is pre-configured with the program directory. Double-clicking the JAR is sufficient to run the program. May need to adjust security to allow installation of the program on specific operating systems. Steps in how to do this should be provided by the operating system. 

**Option 2 -**

`mvn exec:java -Dexec.mainClass="app.presenter.MainStagePresenter"`


### Command to create an executable JAR

`mvn clean compile assembly:single`

_**Note:**_ This command takes control of your mouse for about 5 seconds to run the automated interface tests before creating the executable JAR, please wait until these are finished. The executable JAR will be created in the target directory which is within the program directory.


<a name="section4" />

## How to execute tests

_**Disclaimer:**_ **MUST** have the installed the prerequisites specified in the installation instructions for the command below to work.

1.	Open the terminal
2.	Navigate to the program directory
3.	Run the below command to execute the tests for the program

### Command to run the tests
 
`mvn test`

# User Guide

[Creating state onto the state diagram.](https://www.youtube.com/watch?v=ohl_LHHfhf4&feature=youtu.be)

[Creating transition onto the state diagram.](https://youtu.be/tMjc_vaEHlE)

[Creating transitions onto the transition table.](https://youtu.be/ZaheIuI1wxY)

[Removing a state from the state diagram.](https://youtu.be/lPCrkA7tvH4)

[Removing a transition from the state diagram.](https://youtu.be/GQOp5PHeiBQ)

[Dragging a state on the state diagram.](https://youtu.be/wDy0bl3sFgM)

[Changing the type of a state.](https://youtu.be/6HDGtL_KZUw)

[Changing acceptance mode of the machine.](https://youtu.be/lZg7plPf5VU)

[Executing quick run simulation of a machine.](https://youtu.be/bNuHzc3VE3A)

[Executing step run simulation of a machine.](https://youtu.be/AvYBnWf3aY4)

[Saving a machine.](https://youtu.be/NTFECOGyu6U)

[Loading a machine.](https://youtu.be/blwCrKSUSuc)
