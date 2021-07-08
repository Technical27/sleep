(ns io.github.technical27.sleep.util
  (:import [org.bukkit Bukkit]))

(defn is-overworld?
  "checks if a world is the overworld/normal"
  [world]
  (= (.getEnvironment world) org.bukkit.World$Environment/NORMAL))

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
  [plugin player]
  (if plugin
    (.isAFK plugin player)
    false))
