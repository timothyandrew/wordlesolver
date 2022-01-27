(ns wordlesolver.core
  (:gen-class)
  (:require [clojure.string :as str]
            [clojure.java.io :as io]))

(defn cull-words [candidate words pattern]
  (let [preds
        (map-indexed
         (fn [i letter]
           (case letter
             "Y" #(= (get candidate i) (get % i))
             "M" #(and (not= (get candidate i) (get % i))
                       (str/includes? % (str (get candidate i))))
             "N" #(not (str/includes? % (str (get candidate i))))))
         (-> pattern str/trim (str/split #"")))]
    (filter (fn [word] (every? #(% word) preds)) words)))

(defn main-loop [words]
  (loop [words (shuffle words)]
    (if (= 1 (count words))
      (first words)
      (let [candidate (first words)]
        (println "Try word:" candidate)
        (print "Enter pattern: ")
        (flush)
        (recur (cull-words candidate words (read-line)))))))

(defn -main
  []
  (let [words (-> "wordlist"
                  io/resource
                  slurp
                  (str/split #"\n"))
        words (filter #(= (count %) 5) words)
        result (main-loop words)]
    (println "Result:" result)))
