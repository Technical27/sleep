(ns io.github.technical27.sleep.plugin.Plugin
  (:gen-class
   :main false
   :extends io.github.technical27.sleep.plugin.ClojurePlugin)
  (:require [io.github.technical27.sleep.state :as state])
  (:import [io.github.technical27.sleep.listener Listener]))

(defn -onEnable
  [this]
  (println "sleep: enable")
  (let [plugin-manager (.getPluginManager (.getServer this))]
    (.registerEvents plugin-manager (Listener.) this)
    (reset! state/afk-plugin (.getPlugin plugin-manager "afk"))
    (reset! state/plugin this)))

(defn -onDisable
  [_]
  (println "sleep: disable"))
