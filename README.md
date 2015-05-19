# plasmid-bank
===============

This is plasmid bank sources.
What we want to achieve is a decentralised webservice to share:
 * plasmids
 * different cDNAs (like primers)
 * bacteria strains
 * etc

The project has just started but in general we are thinking about something similar to addgene
( https://www.addgene.org/search/advanced/) in terms of interface but decentralized
(many people and labs instead of one bank).

We only started. The code is in Scala/ScalaJS, akka-http ( http://doc.akka.io/docs/akka-stream-and-http-experimental/current/scala.html )
is used as webserver. 
The project contains 3 subprojects:: 
    * backend - scala bakcned
    * shared -  code (shared between backend and frontend)
    * frontend - ScalaJS frontend 
To run the project you must have SBT ( http://www.scala-sbt.org/ ) installed.

To run project::
    sbt //to open sbt console
    re-start //to run it
    Open localhost:1234 to see the result, it reloads whenever any sources are changed