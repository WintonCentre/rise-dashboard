Babashka scripts
================

Install babashka before running these.
Babashka provides a native code clojure interpreter with very fast startup.
Use `which bb` to check you have it.
`brew install borkdude/brew/babashka` works nicely.

## switch-10x-dev.clj
    Sets up Calva Jack-in. 
        Run with no arguments for a Calva REPL. Choose fig amd the run the `dev` build.

        Run `switch-10x-dev.clj 10x` to include the re-frame-10x window.

        Run `switch-10x-dev.clj prod` for a production build. Note that this reconfigures the `dev`
        build to create a production build. You must still run the build called `dev`.

    After setting up the build, Calva Jack-in will create the correct version. Note that with this Calva setting you can get Jack-in to select the fig alias automatically. Might be useful to add and edit if you need more than one alias.
```
        "calva.replConnectSequences": [
        {
            "name": "Compile and run app with cljs REPL ",
            "projectType": "Clojure CLI",
            "cljsType": "Figwheel Main",
            "menuSelections": {
                "cljAliases": [
                    "fig"
                ]
            }
        }
```

## build.clj:  
    Creates a production build.

## config.clj
    Runs the configuration tool `transplants.configure/-main` to import the excel spreadsheets and convert them to run time EDN text resource files. 
    
    The app can then read them as necessary. We don't want the app to have a direct read dependency on Excel xlsx data.

    EDN files are text files contain data expressed in clojure's [EDN syntax](https://github.com/edn-format/edn)


## upload-xlsx.clj
    Places a copy of the master xlsx spreadsheets on the Winton Centre Dropbox. 