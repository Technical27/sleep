(ns io.github.technical27.sleep.plugin.Plugin
  (:gen-class
   :main false
   :extends io.github.technical27.sleep.plugin.ClojurePlugin)
  (:import [io.github.technical27.sleep.listener Listener]))

(defn -onEnable
  [this]
  (println "sleep: enable")
  (.registerEvents (.getPluginManager (.getServer this)) (Listener.) this))

(defn -onDisable
  [_]
  (println "sleep: disable"))
