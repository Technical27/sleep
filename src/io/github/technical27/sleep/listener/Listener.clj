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
    (status/update-needed)
    (Bukkit/broadcastMessage (fun (.getPlayer event)))
    (status/do-sleep)))

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
  (when (> @state/sleeping 0)
    (update-sleep event messages/player-leave)))

(defn -onLeave
  [_ event]
  (when (> @state/sleeping 0)
    (update-sleep event messages/player-leave)))

(defn -onJoin
  [_ event]
  (when (> @state/sleeping 0)
    (update-sleep event messages/player-join)))

(defn -onPortal
  [_ event]
  (when (> @state/sleeping 0)
    (update-sleep event messages/player-portal)))
