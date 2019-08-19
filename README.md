# liken
AB-comparison tool for WEB UI regression testing

Liken is a web-based service that allows to perform A-B comparison regression testing for web-based UI.

Liken is a standalone service and can be easily integrated with any test management system using a pluggable architecture.

By default Liken is integrated both ways with [More information on Wiki](https://testquack.com) - a new age test management system also available for free.

Compare production (or any reference version) with a release candidate just by pressing two buttons. No more tab switching needed, the list of testcases will just flow as you perform comparison.

The idea is simple. Each testcases has 2 urls. One - the release candidate, another - the reference to compare with. Service displays urls in frames. Only one frame is shown at a time. By switching between frames you can momentarily see the differences - regressions - and report them immediately from the very same interface.

The service features the list of testcases. You just have to switch between them, executing them one-by-one, by pressing a single button.
