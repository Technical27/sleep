(ns io.github.technical27.sleep.state)

(def sleeping (atom 0))

(def needed (atom 0))

(def in-animation (atom false))

(def afk-plugin (atom nil))
(def plugin (atom nil))
