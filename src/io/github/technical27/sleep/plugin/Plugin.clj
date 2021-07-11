(ns io.github.technical27.sleep.plugin.Plugin
  (:gen-class
   :main false
   :extends io.github.technical27.sleep.plugin.ClojurePlugin)
  (:require [io.github.technical27.sleep.state :as state]
            [io.github.technical27.sleep.protocol :as protocol])
  (:import [io.github.technical27.sleep.listener Listener]))

(defn -onEnable
  [this]
  (println "sleep: enable")
  (let [plugin-manager (.getPluginManager (.getServer this))]
    (.registerEvents plugin-manager (Listener.) this)
    (reset! state/plugin this)
    (when-let [adapter (protocol/adapter this)]
      (.addPacketListener (com.comphenix.protocol.ProtocolLibrary/getProtocolManager) adapter))
    (reset! state/afk-plugin (.getPlugin plugin-manager "afk"))))

(defn -onDisable
  [_]
  (println "sleep: disable"))
