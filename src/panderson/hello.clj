(ns panderson.hello)

(defn greet [name]
  (str "Hello, " name "!"))

(defn -main [& args]
  (println (greet "World")))
