
# Development Quickstart:
* Install clojure
* Install babashka
* Install npm
* `npm run` to view scripts help in `package.json` 

To run the configuration tool, and start a shadow-cljs dashboard:
```sh
npm run config
npm run watch-all
```

Open `localhost:9630` for the shadow-cljs dashboard. The `:app` will be served on `localhost:3000` and the 
`:test-browser` on `localhost:3021`.

## For VSCode app development,
Install Calva extension in VSCode.
`npm run stop`
Run VSC command `Calva: Start a Project REPL and Connect (aka Jack-in)` and select `shadow-cljs`.
Check both `:app` and `test-browser` to start both. Connect to the `:app` build. Enable notifications on 3021.

To inspect values in code, it is more convenient to use `tap>` rather than `println`. 
Results will appear in the dashboard inspectors which allow navigation of run-time data.

Requiring `shadow.debug` will provide a higher level interface to `tap>`
with useful snapshotting utilities that call `tap>`. See the comment at the end of this file for usage: [shadow.debug](https://github.com/thheller/shadow-cljs/blob/master/src/main/shadow/debug.clj)

## Configuration

> **WARNING:** 
>
> If you come back to this after a while with new data, be particularly careful about sheet names within spreadsheets. Make sure they correspond to the sheet names in config.edn. If they fail to exist when reading you can get a puzzling crash.


## Ilan/Maria/Leila mockup
See https://docs.google.com/presentation/d/1uKk0HyclaTMCb-EIZiyVdJPBjzzHflArWoq8ukwbMGE/edit?usp=sharing

## Design Guidelines (Not yet implemented)
Follow the [NHSBT website guide](https://www.nhsbt.nhs.uk/987643testarea/website-guide/) where possible.

## Contents
This repository contains both a set of configuration utilities and the run-time TRAC tool web-site(s) themselves.

The configuration tools are written in clojure and run on the JVM. JVM 8 or 11 are recommended for clojure. Use a tool such as Jabba to install and switch JVM versions as necessary. Since Oracle JVMs are no longer free we are using `adopt-openj9@1.11.0-6`.

The websites are also written in clojure - in clojurescript - and they compile to js code to run in a browser. The build tools are currently using the [shadow-cljs](https://shadow-cljs.github.io/docs/UsersGuide.html) tool set as this gave simpler access to the few `npm` module dependencies such as react-bootstrap that we are using.

There have however been recent releases on the main [clojurescript compiler](https://clojurescript.org) thread that mean this dependency on shadow-cljs is no longer necessary. It too can now access `npm` modules easily, and it also now has a target which output which is compatible with js bundlers like webpack. We will avoid any run-time code dependencies on shadow-cljs so we retain the ability to use this approach at a later date. 


## Status
**Requirements**

[Spreadsheet at](file:///Users/gmp26/Dropbox (Cambridge University)/Winton Centre/TRANSPLANT/LUNGS/LUNGS - OTHER STUFF/NHS BT MEETINGStransplants/lungs/lungs other stuff/NHSBT meetings/Joel McGrath/)

**Work in Progress**
A new generic transplants repo which is merging clj pre-processing with the cljs-tool.

I'm working on the premise that we can generate all the organ transplant tools from this one repo, and also do the configuration pre-processing here.

See the config files in the data folder. 

## Configuration tools and data
  Tools are in `src/clj/transplants/configure`.
  Configuration is in `data` and is controlled by `config.edn`

  Run `lein check` to check that the xlsx spreadsheets are not too crazy. This catches a lot of potential problems, but probably not all problems as yet. 
  Configuration tests run under the `configure` leiningen profile. 

  Run `lein config` to generate a complete set of edn and csv files in the resources/public directory. This
  also uses the `configure` profile.

  The configuration tool has a profile argument set to either `:kidney` or `:lung` which selects between the kidney or lung xlsx workbooks. The configuration reads in a workbook, validates it, and generates site run-time configuratioqn files in `public/resources`.

### Configuration Development
All data is stored in the `data` folder.
```
data
├── config.edn
├── incoming-kidney
│   ├── Competing\ risks\ CIF\ kidney.xlsx
│   ├── Formal\ post\ tx\ patient\ and\ graft\ survival.xlsx
│   └── post\ tx\ patient\ and\ graft\ survival.xlsx
├── incoming-lung
│   ├── About\ the\ TRAC\ tool\ FINAL.docx
│   ├── Competing\ risks\ CIF\ lung.xlsx
│   ├── Data\ for\ lung\ TRAC\ tool.pdf
│   ├── Post-transplant\ information.xlsx
│   ├── Survival\ from\ listing\ information.xlsx
│   └── leila-ucd.xlsx
├── kidney-models-master.xlsx
└── lung-models-master.xlsx
```

The `incoming-...` files were received from NHSBT. These have now 
been reformatted and transcribed into `kidney-models-master.xlsx` and `lung-models-master.xlsx`.

The job of the clojure configuration app is to read these spreadsheets and validate then, and then write them out again in a form suitable for consumption by the web tools. We don't want the web tools to read in xlsx files directly, and we'd also prefer to use a much simpler validation mechanism within the web tool itself such as a hash code. 

The process is controlled by `config.edn`. This identifies the path and the format of all organ `.xlsx` files that need to be read in. We are using `juxt/aero` to simplify the construction of this configuration file. This provides the ability to reference values that were previously defined in the same file, and a mechanism to allow profiling for organ - `:lung` or `:kidney` may be selected at time of writing. 

### Jacking in from vs-code to work on the *configuration* process
If jacking in from VS-Code be sure to select `leiningen` with `no alias` and then the `:configuration` profile. 

## Trac Tool Development
Not sure whether it is going to be possible to develop all organ tools from one repo, but it seems sensible to start that way to avoid duplication. So far so good.

### Building
We now have a choice of shadow-cljs or core clojurescript builds. 

Shadow-cljs
Calva Jack-in (alt-ctrl-C alt-ctrl-J) and select shadow-cljs (or lein-shadow if that is offered instead) and the :app build. Browse to the `:dev-http` port as specified by `shadow-cljs.edn`. 

### cljs-core unit tests
See https://figwheel.org/docs/testing.html#auto-testing
and https://figwheel.org/config-options#auto-testing
and https://figwheel.org/docs/extra_mains


In `figwheel-main.edn` we have
```
 :auto-testing true
 :extra-main-files {:tests {:main transplants.test-runner}}
```
`:extra-main-files` causes figwheel to generate a cljs-out/test image in addition to the standard dev image.
`:auto-testing true` adds a [heads up test display](http://localhost:9500/figwheel-extra-main/auto-testing), which is useful in development.

These docs are good but don't deal with the webpack bundle case. I've found that the most reliable way of ensuring that a bundle is generated is to run the commands below with or withoiut -r/--repl. Possibly using -Mfig instead of -Afig at least once.

Subsequently we can jack-in with cider and generate a js that will be pulled in to the pregenerated bundle. At least I think that's what is happening.

I'm not yet clear how to make these tests run sensibly on the server. Ideally we'd want some sort of feedback to the github repo
in the form of a pass/fail badge, with a test run log. 

The docs talk about commands like this 

```
clojure -Afig -m figwheel.main  -b dev --repl
```

but this seems to work better with the extra-mains config - without a repl this time though one can be added.
```
clojure -Mfig -m figwheel.main  -b dev
```
The tests will also run with this
In order to create a webpack bundled test build, you may need to run
```
clojure -Afig -m figwheel.main -b test 
```

In order to ensure that tests run when this main is launched, make sure that 
`transplants.test-runner` calls `-main`. 

The problem here is that all of these tests are still requiring user interaction. The test runner has to be called in the 
browser context somehow, with results appearing in the test.html console in the browser, or in the REPL. I'm not clear how to do this server side.


# The front-end trac tool

This is a Shadow-cljs app 
## Getting Started

```
git clone https://github.com/wintoncentre/transplants
cd transplants
npm install
npm start
```

Then, open `http://localhost:3000` in your browser, and enjoy!

## Tests
Run `lein karma` for cljs unit tests
Run `lein check` for xlsx configuration tests


# Original Re-frame template README below
## Getting Started

### Project Overview

* Architecture:
[Single Page Application (SPA)](https://en.wikipedia.org/wiki/Single-page_application)
* Languages
  - Front end ([re-frame](https://github.com/day8/re-frame)): [ClojureScript](https://clojurescript.org/) (CLJS)
* Dependencies
  - UI framework: [re-frame](https://github.com/day8/re-frame)
  ([docs](https://github.com/day8/re-frame/blob/master/docs/README.md),
  [FAQs](https://github.com/day8/re-frame/blob/master/docs/FAQs/README.md)) ->
  [Reagent](https://github.com/reagent-project/reagent) ->
  [React](https://github.com/facebook/react)
  - Client-side routing: [Secretary](https://github.com/gf3/secretary)
* Build tools
  - Project task & dependency management: [Leiningen](https://github.com/technomancy/leiningen)
  - CLJS compilation, REPL, & hot reload: [`shadow-cljs`](https://github.com/thheller/shadow-cljs)
  - Test framework: [cljs.test](https://clojurescript.org/tools/testing)
  - Test runner: [Karma](https://github.com/karma-runner/karma)
* Development tools
  - Debugging: [CLJS DevTools](https://github.com/binaryage/cljs-devtools),
  [`re-frame-10x`](https://github.com/day8/re-frame-10x)

#### Directory structure

* [`/`](/../../): project config files
* [`dev/`](dev/): source files compiled only with the [dev](#running-the-app) profile
  - [`cljs/user.cljs`](dev/cljs/user.cljs): symbols for use during development in the
[ClojureScript REPL](#connecting-to-the-browser-repl-from-a-terminal)
* [`resources/public/`](resources/public/): SPA root directory;
[dev](#running-the-app) / [prod](#production) profile depends on the most recent build
  - [`index.html`](resources/public/index.html): SPA home page
    - Dynamic SPA content rendered in the following `div`:
        ```html
        <div id="app"></div>
        ```
    - Customizable; add headers, footers, links to other scripts and styles, etc.
  - Generated directories and files
    - Created on build with either the [dev](#running-the-app) or [prod](#production) profile
    - Deleted on `lein clean` (run by all `lein` aliases before building)
    - `js/compiled/`: compiled CLJS (`shadow-cljs`)
      - Not tracked in source control; see [`.gitignore`](.gitignore)
* [`src/cljs/transplants/`](src/cljs/transplants/): SPA source files (ClojureScript,
[re-frame](https://github.com/Day8/re-frame))
  - [`core.cljs`](src/cljs/transplants/core.cljs): contains the SPA entry point, `init`
* [`test/cljs/transplants/`](test/cljs/transplants/): test files (ClojureScript,
[cljs.test](https://clojurescript.org/tools/testing))
  - Only namespaces ending in `-test` (files `*_test.cljs`) are compiled and sent to the test runner

### Editor/IDE

Use your preferred editor or IDE that supports Clojure/ClojureScript development. See
[Clojure tools](https://clojure.org/community/resources#_clojure_tools) for some popular options.

### Environment Setup

1. Install [JDK 8 or later](https://openjdk.java.net/install/) (Java Development Kit)
2. Install [Leiningen](https://leiningen.org/#install) (Clojure/ClojureScript project task &
dependency management)
3. Install [Node.js](https://nodejs.org/) (JavaScript runtime environment)
4. Install [karma-cli](https://www.npmjs.com/package/karma-cli) (test runner):
    ```sh
    npm install -g karma-cli
    ```
5. Install [Chrome](https://www.google.com/chrome/) or
[Chromium](https://www.chromium.org/getting-involved/download-chromium) version 59 or later
(headless test environment)
    * For Chromium, set the `CHROME_BIN` environment variable in your shell to the command that
    launches Chromium. For example, in Ubuntu, add the following line to your `.bashrc`:
        ```bash
        export CHROME_BIN=chromium-browser
       ```
7. Clone this repo and open a terminal in the `transplants` project root directory
8. Download project dependencies:
    ```sh
    lein deps && npm install
    ```

### Browser Setup

Browser caching should be disabled when developer tools are open to prevent interference with
[`shadow-cljs`](https://github.com/thheller/shadow-cljs) hot reloading.

Custom formatters must be enabled in the browser before
[CLJS DevTools](https://github.com/binaryage/cljs-devtools) can display ClojureScript data in the
console in a more readable way.

#### Chrome/Chromium

1. Open [DevTools](https://developers.google.com/web/tools/chrome-devtools/) (Linux/Windows: `F12`
or `Ctrl-Shift-I`; macOS: `⌘-Option-I`)
2. Open DevTools Settings (Linux/Windows: `?` or `F1`; macOS: `?` or `Fn+F1`)
3. Select `Preferences` in the navigation menu on the left, if it is not already selected
4. Under the `Network` heading, enable the `Disable cache (while DevTools is open)` option
5. Under the `Console` heading, enable the `Enable custom formatters` option

#### Firefox

1. Open [Developer Tools](https://developer.mozilla.org/en-US/docs/Tools) (Linux/Windows: `F12` or
`Ctrl-Shift-I`; macOS: `⌘-Option-I`)
2. Open [Developer Tools Settings](https://developer.mozilla.org/en-US/docs/Tools/Settings)
(Linux/macOS/Windows: `F1`)
3. Under the `Advanced settings` heading, enable the `Disable HTTP Cache (when toolbox is open)`
option

Unfortunately, Firefox does not yet support custom formatters in their devtools. For updates, follow
the enhancement request in their bug tracker:
[1262914 - Add support for Custom Formatters in devtools](https://bugzilla.mozilla.org/show_bug.cgi?id=1262914).

## CLJS Development

We are using the react-bootstrap npm module as it provides some useful modal and popup behaviour which woud otherwise need jQuery. There are currently two mechanisms available to allow npm modules to be used in a cljs app. These are the clojure CLI tools with a deps.edn configuration file, and a shadow-cljs build. As it seems to be possible to support both with minor changes, that's what we do for now until it becomes clear which mechanism wins out. 

The newer Deps/CLI uses webpack which should minimise the executable better than shadow-cljs as
it supports tree shaking. Shadow-cljs is perhaps simpler to set up however.

### Deps/CLI builds

This is documented from the perspective of using a VSCode development system with the Calva
plugin for Clojure/script. We also use figwheel-main for hot-reload during development.

I've documented the procedure at
https://github.com/gmp26/calva-docs/blob/master/docs/figwheel-main-webpack.md and submitted a 
pull request which has been accepted, though not yet published at time of writing. It should appear at https://calva.io/ in due course.

Here's a quick summary:
    1. deps.edn is a map of dependencies, source-paths, and aliases.
    2. The clojure commandline interface tools (clojure or clj) refer to this file. `clj -Afoo` will cause them to refer to the alias `foo` in deps.edn.
    3. extra-paths, extra-deps, and a main-opt can be declared in an alias. This permits one deps.edn to serve multiple functions.
    4. We run figwheel.main as the main-opt. This has a `--build dev` option which refers to further configuration in dev.cljs.edn. We use bb-script/switch-10x-dev.clj shell script to switch builds between 10x (for heads-up data display during development), dev (development without 10x), and prod (an optimised build).
    5. See also `figwheel-main.edn` where the target folder is specified for compiled outputs.
   
In VSCode you can start a build using a `cider jack in` command.
VSCode looks though the project and offers you available options. 
For a CLJS development build choose either Clojure CLI + figwheel-main. Then go on to check `fig` either with or without
`dev`. 

First time through choose both `fig` and `dev` as this will force a webpack build. It does dump you in a terminal REPL however and as this is a lot less convenient than an in-editor REPL, on subsequent builds I recommend choosing only `fig` here. On the next screen you can select `dev`, but now you will have a Calva connected REPL.

Do the same for a `10x` enabled build after running `bb-script/switch-10x-dev.clj 10x`, or `bb-script/switch-10x-dev.clj dev` if you don't want to use 10x.

For a production build, run `bb-script/switch-10x-dev.clj prod` and then run `clj -Afig:prod`.
   

### Shadow-cljs builds

Start a temporary local web server, build the app with the `dev` profile, and serve the app with
hot reload:


```sh
lein dev
```

Please be patient; it may take over 20 seconds to see any output, and over 40 seconds to complete.

When `[:app] Build completed` appears in the output, browse to
[http://localhost:8280/](http://localhost:8280/).

[`shadow-cljs`](https://github.com/thheller/shadow-cljs) will automatically push ClojureScript code
changes to your browser on save. To prevent a few common issues, see
[Hot Reload in ClojureScript: Things to avoid](https://code.thheller.com/blog/shadow-cljs/2019/08/25/hot-reload-in-clojurescript.html#things-to-avoid).

Opening the app in your browser starts a
[ClojureScript browser REPL](https://clojurescript.org/reference/repl#using-the-browser-as-an-evaluation-environment),
to which you may now connect.

#### Connecting to the browser REPL from your editor

See
[Shadow CLJS User's Guide: Editor Integration](https://shadow-cljs.github.io/docs/UsersGuide.html#_editor_integration).
Note that `lein dev` runs `shadow-cljs watch` for you, and that this project's running build id is
`app`, or the keyword `:app` in a Clojure context.

Alternatively, search the web for info on connecting to a `shadow-cljs` ClojureScript browser REPL
from your editor and configuration.

For example, in Vim / Neovim with `fireplace.vim`
1. Open a `.cljs` file in the project to activate `fireplace.vim`
2. In normal mode, execute the `Piggieback` command with this project's running build id, `:app`:
    ```vim
    :Piggieback :app
    ```

#### Connecting to the browser REPL from a terminal

1. Connect to the `shadow-cljs` nREPL:
    ```sh
    lein repl :connect localhost:8777
    ```
    The REPL prompt, `shadow.user=>`, indicates that is a Clojure REPL, not ClojureScript.

2. In the REPL, switch the session to this project's running build id, `:app`:
    ```clj
    (shadow.cljs.devtools.api/nrepl-select :app)
    ```
    The REPL prompt changes to `cljs.user=>`, indicating that this is now a ClojureScript REPL.
3. See [`user.cljs`](dev/cljs/user.cljs) for symbols that are immediately accessible in the REPL
without needing to `require`.

### Running Tests

Build the app with the `prod` profile, start a temporary local web server, launch headless
Chrome/Chromium, run tests, and stop the web server:

```sh
lein karma
```

Please be patient; it may take over 15 seconds to see any output, and over 25 seconds to complete.

### Running `shadow-cljs` Actions

See a list of [`shadow-cljs CLI`](https://shadow-cljs.github.io/docs/UsersGuide.html#_command_line)
actions:
```sh
lein run -m shadow.cljs.devtools.cli --help
```

Please be patient; it may take over 10 seconds to see any output. Also note that some actions shown
may not actually be supported, outputting "Unknown action." when run.

Run a shadow-cljs action on this project's build id (without the colon, just `app`):
```sh
lein run -m shadow.cljs.devtools.cli <action> app
```
### Debug Logging

The `debug?` variable in [`config.cljs`](src/cljs/transplants/config.cljs) defaults to `true` in
[`dev`](#running-the-app) builds, and `false` in [`prod`](#production) builds.

Use `debug?` for logging or other tasks that should run only on `dev` builds:

```clj
(ns transplants.example
  (:require [transplants.config :as config])

(when config/debug?
  (println "This message will appear in the browser console only on dev builds."))
```

## Production

Build the app with the `prod` profile:

```sh
lein prod
```

Please be patient; it may take over 15 seconds to see any output, and over 30 seconds to complete.

The `resources/public/js/compiled` directory is created, containing the compiled `app.js` and
`manifest.edn` files.

The [`resources/public`](resources/public/) directory contains the complete, production web front
end of your app.

Always inspect the `resources/public/js/compiled` directory prior to deploying the app. Running any
`lein` alias in this project after `lein dev` will, at the very least, run `lein clean`, which
deletes this generated directory. Further, running `lein dev` will generate many, much larger
development versions of the files in this directory# rise-dashboard
