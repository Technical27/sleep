(ns io.github.technical27.sleep.listener.AFKListener
  (:gen-class
   :main false
   :constructors {[] []}
   :methods [[^{org.bukkit.event.EventHandler {}} onAFKStart [io.github.technical27.afk.events.AFKStart] void]
             [^{org.bukkit.event.EventHandler {}} onAFKEnd [io.github.technical27.afk.events.AFKEnd] void]]
   :implements [org.bukkit.event.Listener])
  (:require [io.github.technical27.sleep.util :as util]
            [io.github.technical27.sleep.state :as state]))

(defn -onAFKStart
  [_ event]
  (let [player (.getPlayer event)]
    (when (util/in-overworld? player)
      (state/set-can-sleep player false))))

(defn -onAFKEnd
  [_ event]
  (let [player (.getPlayer event)]
    (when (util/in-overworld? player)
      (state/set-can-sleep player true))))
