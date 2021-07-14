(ns io.github.technical27.sleep.state)

(def players (atom {}))

(def in-animation (atom false))

(def afk-plugin (atom nil))
(def plugin (atom nil))

(defn get-player
  [player]
  (get @players player))

(defn get-sleeping
  [player]
  (:sleeping (get-player player)))

(defn set-sleeping
  [player bool]
  (swap! players update player assoc :sleeping bool))

(defn get-can-sleep
  [player]
  (:can-sleep (get-player player)))

(defn set-can-sleep
  [player bool]
  (swap! players update player assoc :can-sleep bool))

(defn add-player
  [player]
  (swap! players assoc player {:sleeping false :can-sleep false}))

(defn remove-player
  [player]
  (swap! players dissoc player))
