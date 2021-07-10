(ns io.github.technical27.sleep.status
  (:import [com.google.common.collect ImmutableList]
           [org.bukkit Bukkit])
  (:require [io.github.technical27.sleep.state :as state]
            [io.github.technical27.sleep.animation :as animation]
            [io.github.technical27.sleep.util :as util]))

(defn filter-players
  "filters players on if they can sleep"
  [players]
  (filter util/can-sleep? players))

(defn get-online
  "get a list of online players"
  []
  (ImmutableList/copyOf (Bukkit/getOnlinePlayers)))

(defn do-sleep
  []
  (when (>= @state/sleeping (util/get-needed @state/needed))
    (reset! state/in-animation true)
    (animation/task)))
