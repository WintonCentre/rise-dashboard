# Running Tests

There are two full test runners for the configure tools: `com.cognitect/test-runner` and  `lambdaisland/kaocha`, and one REPL based test runner. 

[com.cognitect/test-runner](https://github.com/cognitect-labs/test-runner) is the simpler of the two, 
offering automatic discovery of tests. Run it from a shell with 
```
clj -M:cfg-test
```

[kaocha] is a little more user friendly, offering colourful messages and a very useful watch mode. It is configured through the top-level `tests.edn` file. Run it with
```
bin/kaocha
```

Calva (the VSCode clojure plug-in) also offers a very useful REPL based runner for individual tests. Start a REPL using the :tcfg alias, and it is then possible to invoke individual tests by placing the cursor on a `deftest` form and hitting `ctl-C ctl-T`. Very useful in development.  