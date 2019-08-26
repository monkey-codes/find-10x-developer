# Find the 10x Developer

## Background

Jessie, Evan, John, Sarah and Matt are all engineers in the same delivery team and each of them has a different level of 
coding skill to the other. This means it possible to order them from best to... "least best". Importantly, the best of 
them is the mythical 10x Developer!!!

But which one is it?!?

## What We Know

* Jessie is not the best developer
* Evan is not the worst developer
* John is not the best developer or the worst developer
* Sarah is a better developer than Evan
* Matt is not directly below or above John as a developer
* John is not directly below or above Evan as a developer

## Challenge

1) Who is the 10x developer on the team?
2) What is the correct ordering of the members of the team according to their coding skills?

## Prerequisites

* ***Docker*** to run the solution
* ***JDK 11*** to build it

## Running

***Docker*** is required to run the `go` script.

```
$ ./go
...

Jessie is not the best developer
Evan is not the worst developer
John is not the best developer or the worst developer
Sarah is a better developer than Evan
Matt is not directly below or above John as a developer
John is not directly below or above Evan as a developer

Order: [Sarah, John, Jessie, Evan, Matt]
10xDeveloper: Sarah

```

## Building

***JDK 11*** is required to build it

```
$ ./mvnw clean verify
```


  