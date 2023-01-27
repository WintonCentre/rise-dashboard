<h1 align="center">RISE - Earthquake forecast</h1>
<p align="center">
<img src="https://img.shields.io/badge/to--inform-not--persuade-informational">
<img src="https://img.shields.io/badge/License-EPL_1.0-red.svg">
</p>

_This website was designed by the **[Winton Centre for Risk & Evidence Communication](https://wintoncentre.maths.cam.ac.uk/)** at the University of Cambridge, funded by Horizon 2020, as part of the [Work Package 5 (on Society)](http://www.rise-eu.org/activities/society/) of the European project RISE. It aims to display Operational Earthquake Forecasts in a way that is most helpful to a variety of audiences, and was the result of iterative design and testing with over 100 people, in Iceland, Switzerland, and Italy, ranging from seismologists to the general public, journalists to civil protection. The way that the numbers are displayed is the result of quantitative testing with many thousands of members of the general public in California, Switzerland and Iceland. The full reports of the testing and good practice recommendations are found as deliverables of WP5 on the **[RISE website](http://www.rise-eu.org/home/)**._

---
This is a single page web application written in
[clojurescript](https://clojurescript.org/).
The clojurescript compiler generates javascript which runs in an HTML5
capable browser (Chrome, Safari, Firefox, Opera, IE11). It uses Bootstrap
styling to achieve a responsive display that adapts to desktop or mobile
screen sizes.

# Development Quickstart:
* Install clojure
* (Install babashka)
* Install npm
* `npm run` to view scripts help in `package.json` 

To run the configuration tool, and start a shadow-cljs dashboard:
```sh
npm run watch-all
```
Open `localhost:9630` for the shadow-cljs dashboard. The `:app` will be served on `localhost:3000` and the 
`:test-browser` on `localhost:3021`.

## For VSCode app development,
Install Calva extension in VSCode.
`npm run stop`
Run VSC command `Calva: Start a Project REPL and Connect (aka Jack-in)` and select `shadow-cljs`.
Check both `:app` and `test-browser` to start both. Connect to the `:app` build. Enable notifications on 3021.

To inspect values in code, it is often more convenient to use `tap>` rather than `println`. 
Results will appear in the dashboard inspectors which allow navigation of run-time data.

Requiring `shadow.debug` will provide a slightly higher level interface to `tap>`
with useful snapshotting utilities that call `tap>`. See the comment at the end of this file for usage: [shadow.debug](https://github.com/thheller/shadow-cljs/blob/master/src/main/shadow/debug.clj)

