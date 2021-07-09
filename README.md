
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

