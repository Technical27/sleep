(ns io.github.technical27.sleep.listener.Listener
  (:gen-class
   :main false
   :constructors {[] []}
   :methods [[^{org.bukkit.event.EventHandler {}} onBedEnter [org.bukkit.event.player.PlayerBedEnterEvent] void]
             [^{org.bukkit.event.EventHandler {}} onBedLeave [org.bukkit.event.player.PlayerBedLeaveEvent] void]
             [^{org.bukkit.event.EventHandler {}} onJoin [org.bukkit.event.player.PlayerJoinEvent] void]
             [^{org.bukkit.event.EventHandler {}} onKick [org.bukkit.event.player.PlayerKickEvent] void]
             [^{org.bukkit.event.EventHandler {}} onLeave [org.bukkit.event.player.PlayerQuitEvent] void]
             [^{org.bukkit.event.EventHandler {}} onPortal [org.bukkit.event.player.PlayerPortalEvent] void]]
   :implements [org.bukkit.event.Listener])
  (:import [org.bukkit Bukkit])
  (:require [io.github.technical27.sleep.status :as status]
            [io.github.technical27.sleep.messages :as messages]
            [io.github.technical27.sleep.util :as util]
            [io.github.technical27.sleep.state :as state]))

(defn- update-sleep
  [event fun]
  (when (and (util/is-night?) (not @state/in-animation))
    (Bukkit/broadcastMessage (fun (.getPlayer event)))
    (status/do-sleep)))

(defn- is-anyone-sleeping?
  []
  (> @state/sleeping 0))

(defn- check-bed-enter
  [event]
  (= (.getBedEnterResult event) org.bukkit.event.player.PlayerBedEnterEvent$BedEnterResult/OK))

(defn -onBedEnter
  [_ event]
  (when (check-bed-enter event)
    (swap! state/sleeping inc)
    (update-sleep event messages/bed-enter)))

(defn -onBedLeave
  [_ event]
  (swap! state/sleeping dec)
  (update-sleep event messages/bed-leave))

(defn -onKick
  [_ event]
  (when (util/can-sleep? (.getPlayer event))
    (swap! state/needed dec))
  (when (is-anyone-sleeping?)
    (update-sleep event messages/player-leave)))

(defn -onLeave
  [_ event]
  (when (util/can-sleep? (.getPlayer event))
    (swap! state/needed dec))
  (when (is-anyone-sleeping?)
    (update-sleep event messages/player-leave)))

(defn -onJoin
  [_ event]
  (when (util/can-sleep? (.getPlayer event))
    (swap! state/needed inc))
  (when (is-anyone-sleeping?)
    (update-sleep event messages/player-join)))

; NOTE: i hate this but this was the most sane way i could do this
(defn -onPortal
  [_ event]
  (let [old-world (.getWorld (.getFrom event))
        new-world (.getWorld (.getTo event))
        is-overworld-old (util/is-overworld? old-world)
        is-overworld-new (util/is-overworld? new-world)]
    (when (and is-overworld-old (not is-overworld-new))
      (swap! state/needed dec))
    (when (and (not is-overworld-old) is-overworld-new)
      (swap! state/needed inc)))
  (when (is-anyone-sleeping?)
    (update-sleep event messages/player-portal)))
