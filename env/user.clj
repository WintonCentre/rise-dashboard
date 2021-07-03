(ns user
  "This is a test placeholder for a function that can be called from the figwheel post-build-hook.
   Aim was to see if we can use the hook to call webpack when jacking in to an editor repl.
   
   It's not yet clear if this works in the Calva Jack-in context. You get an error if env is not 
   on the classpath but we don't see Hello world in any output stream when user/foo is added as a 
   hook.")

(defn foo
  [args]
  (println "Hello world: args " args))