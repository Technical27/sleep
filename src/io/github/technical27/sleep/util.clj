(ns io.github.technical27.sleep.util
  (:require [io.github.technical27.sleep.state :as state])
  (:import [org.bukkit Bukkit]
           [org.bukkit.scheduler BukkitRunnable]))

(defn is-overworld?
  "checks if a world is the overworld/normal"
  [world]
  (= (.getEnvironment world) org.bukkit.World$Environment/NORMAL))

(defn is-nether?
  "checks if a world is the overworld/normal"
  [world]
  (= (.getEnvironment world) org.bukkit.World$Environment/NETHER))

(defn in-overworld?
  "checks if a player is in the overworld"
  [player]
  (is-overworld? (.getWorld player)))

; TODO: check if this world is actually the overworld
(defn get-world
  "gets the first world"
  []
  (first (Bukkit/getWorlds)))

(defn is-night?
  "checks if it is night"
  []
  (let [world (get-world)
        time (.getTime world)]
    (and (> time 12541) (< time 23850))))

(defn is-afk?
  "checks if a player is afk"
  [player]
  (if-let [plugin @state/afk-plugin]
    (.isAFK plugin player)
    false))

(defn can-sleep?
  [player]
  (and (in-overworld? player) (not (is-afk? player))))

(defn get-needed
  "gets the number of players that need to sleep"
  []
  (int (Math/ceil (/ (max (count (filter state/get-can-sleep (keys @state/players))) 1) 2))))

(defn get-sleeping
  "gets the number of players that are sleeping"
  []
  (count (filter state/get-sleeping (keys @state/players))))

(defn- animation-runnable
  [world]
  (proxy [BukkitRunnable] []
    (run []
      (if (is-night?)
        (.setTime world (+ (.getTime world) 85))
        (do
          (reset! state/in-animation false)
          (.cancel this))))))

(defn- animation
  []
  (reset! state/in-animation true)
  (.runTaskTimer (animation-runnable (get-world)) @state/plugin 0 0))

(defn do-sleep
  []
  (when (>= (get-sleeping) (get-needed))
    (reset! state/in-animation true)
    (animation)))
