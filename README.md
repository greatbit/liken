# liken
AB-comparison tool for WEB UI regression testing

Liken is a web-based service that allows to perform A-B comparison regression testing for web-based UI.

Liken is a standalone service and can be easily integrated with any test management system using a pluggable architecture.

By default Liken is integrated both ways with [More information on Wiki](https://testquack.com) - a new age test management system also available for free.

Compare production (or any reference version) with a release candidate just by pressing two buttons. No more tab switching needed, the list of testcases will just flow as you perform comparison.

The idea is simple. Each testcases has 2 urls. One - the release candidate, another - the reference to compare with. Service displays urls in frames. Only one frame is shown at a time. By switching between frames you can momentarily see the differences - regressions - and report them immediately from the very same interface.

The service features the list of testcases. You just have to switch between them, executing them one-by-one, by pressing a single button.

#### Comparison view
![Comparison](https://raw.githubusercontent.com/greatbit/greatbit.github.io/master/liken/img/liken-compare.png)

#### AB in action
![Comparison in Action](https://raw.githubusercontent.com/greatbit/greatbit.github.io/master/liken/img/liken-short-low.gif)

How to run test server using docker
==========
1. Just run
```
docker-compose up
```

from the root folder

2. Navigate to ```localhost``` in your browser

3. Use following credentials: "root:rootpass" for admin and "somelogin:somepass" for a regular user

4. Navigate to QuackUi project

5. Go to Launches

6. Get the first one or filter by "Liken" runner

7. Click on the link in Launch to get to Liken

8. Create your own launches from the list of testcases in QuAck using Liken launcher

How to run on a standalone server
==========
1. Install NGINX and add [conf/liken.conf](https://github.com/greatbit/liken/blob/master/assembly/liken.conf)

2. Run mongo, e.g.:
   mongo docker run --name mongodb --restart always -p 27017:27017 -d mongo

3. Place [conf/liken.properties](https://github.com/greatbit/liken/blob/master/assembly/liken.properties) somewhere in the system (e.g. /etc/liken)
and override values according to your setup (e.g., set "mongo.replicaSet=localhost:27017")

4. Override boot classpath when running ```-Xbootclasspath/a:/etc/liken``` and start application

```
java -Xbootclasspath/a:/etc/liken -jar /usr/liken/liken.jar
```

