# Build
```
sbt stage
sbt docker:publishLocal
```

# Akka & Docker

```bash
sbt docker:publishLocal
docker run --name seed-1 gdiama-akka-cluster-docker-sample:0.1 --seed
docker run --name seed-2 gdiama-akka-cluster-docker-sample:0.1 --seed <ip-of-your-seed-1>:2551
docker run --name node-1 gdiama-akka-cluster-docker-sample:0.1 <ip-of-your-seed-1>:2551 <ip-of-your-seed-2>:2551
docker run --name node-2 gdiama-akka-cluster-docker-sample:0.1 <ip-of-your-seed-1>:2551 <ip-of-your-seed-2>:2551
```

# SBT - none docker

Of course you can run your cluster within sbt for test purposes.

```
sbt runSeed
sbt runNode
```

# Links and References

* [Akka Docker for Scala Apps](https://medium.com/jeroen-rosenberg/lightweight-docker-containers-for-scala-apps-11b99cf1a666)
* [Akka k8s](https://github.com/ouven/akka-k8s-seednode)
* [Akka Cluster k8s](https://github.com/lkysow/akka-cluster-on-kubernetes)
* [Akka router](https://medium.com/akka-for-newbies/routers-5a501cdf616d)

* [Akka Docker Cluster Example Blog](http://blog.michaelhamrah.com/2014/03/running-an-akka-cluster-with-docker-containers/)
* [Akka Docker Cluster Example Github](https://github.com/mhamrah/akka-docker-cluster-example)
* [Docker Networking](https://docs.docker.com/articles/networking/)
* [Docker Cheat Sheet](https://github.com/wsargent/docker-cheat-sheet)
* [Docker Env Variables](http://mike-clarke.com/2013/11/docker-links-and-runtime-env-vars/)
* [Docker Ambassador Pattern Linking](http://docs.docker.com/articles/ambassador_pattern_linking/)