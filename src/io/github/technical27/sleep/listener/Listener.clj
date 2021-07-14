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
  (:require [io.github.technical27.sleep.messages :as messages]
            [io.github.technical27.sleep.util :as util]
            [io.github.technical27.sleep.state :as state]))

(defn- update-sleep
  [event fun]
  (when (and (util/is-night?) (not @state/in-animation))
    (Bukkit/broadcastMessage (fun (.getPlayer event)))
    (util/do-sleep)))

(defn- is-anyone-sleeping?
  []
  (> (util/get-sleeping) 0))

(defn- update-sleep-silent
  [event fun]
  (when (is-anyone-sleeping?)
    (update-sleep event fun)))

(defn- check-bed-enter
  [event]
  (= (.getBedEnterResult event) org.bukkit.event.player.PlayerBedEnterEvent$BedEnterResult/OK))

(defn -onBedEnter
  [_ event]
  (when (check-bed-enter event)
    (state/set-sleeping (.getPlayer event) true)
    (update-sleep event messages/bed-enter)))

(defn -onBedLeave
  [_ event]
  (state/set-sleeping (.getPlayer event) false)
  (update-sleep event messages/bed-leave))

(defn -onKick
  [_ event]
  (state/remove-player (.getPlayer event))
  (update-sleep-silent event messages/player-leave))

(defn -onLeave
  [_ event]
  (state/remove-player (.getPlayer event))
  (update-sleep-silent event messages/player-leave))

(defn -onJoin
  [_ event]
  (let [player (.getPlayer event)]
    (state/add-player player)
    (when (util/can-sleep? player)
      (state/set-can-sleep player true)))
  (update-sleep-silent event messages/player-join))

; NOTE: i hate this but this was the most sane way i could do this
(defn -onPortal
  [_ event]
  (let [old-world (.getWorld (.getFrom event))
        new-world (.getWorld (.getTo event))
        is-overworld-old (util/is-overworld? old-world)
        is-overworld-new (util/is-overworld? new-world)
        player (.getPlayer event)]
    (when (and is-overworld-old (not is-overworld-new))
      (state/set-can-sleep player false))
    (when (and (not is-overworld-old) is-overworld-new)
      (state/set-can-sleep player true)))
  (update-sleep-silent event messages/player-portal))
