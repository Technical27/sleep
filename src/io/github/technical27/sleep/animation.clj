(ns io.github.technical27.sleep.animation
  (:import [org.bukkit.scheduler BukkitRunnable])
  (:require [io.github.technical27.sleep.util :as util]
            [io.github.technical27.sleep.state :as state]))

(defn- runnable
  [world]
  (proxy [BukkitRunnable] []
    (run []
      (if (util/is-night?)
        (.setTime world (+ (.getTime world) 85))
        (do
          (reset! state/in-animation false)
          (.cancel this))))))

(defn task
  []
  (reset! state/in-animation true)
  (.runTaskTimer (runnable (util/get-world)) @state/plugin 0 0))
