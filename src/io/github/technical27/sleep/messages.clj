(ns io.github.technical27.sleep.messages
  (:import [org.bukkit ChatColor])
  (:require [io.github.technical27.sleep.state :as state]))

(defn- format-sleeping
  []
  (str @state/sleeping "/" @state/needed))

(defn- msg
  [player m]
  (str ChatColor/GOLD (.getName player) ChatColor/DARK_AQUA " " m ", " (format-sleeping) " needed"))

;  YEETPRO420 is now sleeping, 1/3 needed
(defn bed-enter
  [player]
  (msg player "is now sleeping"))

;  YEETPRO420 is no longer sleeping, 1/3 needed
(defn bed-leave
  [player]
  (msg player "is no longer sleeping"))

;  YEETPRO420 joined, 1/3 needed
(defn player-join
  [player]
  (msg player "joined"))

;  YEETPRO420 left, 1/3 needed
(defn player-leave
  [player]
  (msg player "left"))

;  YEETPRO420 went through a portal, 1/3 needed
(defn player-portal
  [player]
  (msg player "went through a portal"))
