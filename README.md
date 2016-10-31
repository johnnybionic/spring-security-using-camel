# spring-security-using-camel

[![Build status](https://travis-ci.org/johnnybionic/spring-security-using-camel.svg?branch=master)](https://travis-ci.org/johnnybionic/spring-security-using-camel)
[![Coverage Status](https://coveralls.io/repos/github/johnnybionic/spring-security-using-camel/badge.svg?branch=master)](https://coveralls.io/github/johnnybionic/spring-security-using-camel?branch=master)

A basic example of how to use an Apache Camel route as an authentication provider. This allows applications to customise their authentication by modifying the route, for example by adding a custom processor. 

![Alt text](camel-security.jpg?raw=true "Overview")

Starts at http://localhost:8080/index

This project is based on the sample project 'spring-security-samples-boot-insecure' distributed with Spring Security Master 4.1.1.

It replaces the in-memory authentication used for the solution to that project, and uses instead a Camel route. 
The idea is that information about the User is obtained during various stages of the route. For example, the User is first authenticated as step one. In step two, the User's roles are determined. In step three, the information added to the Exchange in the previous two steps is used to construct a User.

There could be any number of processes (steps) in the route, each responsible for one distinct task. 

The sample also uses Bootstrap to improve on the original example's client :)

There's also some basic use of Spring profiles that shows how authentication providers can
be determined at runtime. 
