(ns io.github.technical27.sleep.status
  (:import [com.google.common.collect ImmutableList]
           [org.bukkit Bukkit])
  (:require [io.github.technical27.sleep.state :as state]
            [io.github.technical27.sleep.animation :as animation]
            [io.github.technical27.sleep.util :as util]))

(defn filter-players
  "filters players on if they can sleep"
  [players]
  (let [plugin (.getPlugin (Bukkit/getPluginManager) "afk")]
    (filter (fn [x] (and (util/in-overworld? x) (not (util/is-afk? plugin x)))) players)))

(defn get-online
  "get a list of online players"
  []
  (ImmutableList/copyOf (Bukkit/getOnlinePlayers)))

(defn get-needed
  "gets the number of players that need to sleep"
  []
  (int (Math/ceil (/ (max (count (filter-players (get-online))) 1) 2))))

; debug version
; (defn get-needed
;   "gets the number of players that need to sleep"
;   []
;   (max (count (filter-players (get-online))) 2))

; TODO: this doesn't work on portals, the event is before portal
(defn update-needed
  []
  (reset! state/needed (get-needed)))

(defn do-sleep
  []
  (when (>= @state/sleeping @state/needed)
    (reset! state/in-animation true)
    (animation/task)))
