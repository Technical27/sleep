(ns io.github.technical27.sleep.protocol
  (:require [clojure.data.json :as json])
  (:import [org.bukkit Bukkit]))

(def sleep-messages
  "list of Minecraft translation keys that contain sleep messages"
  ["sleep.not_possible"
   "sleep.skipping_night"
   "sleep.players_sleeping"])

(defn- is-chat-packet?
  [packet]
  (= (.getType packet) com.comphenix.protocol.PacketType$Play$Server/CHAT))

(defn- is-chat-game-info?
  [packet]
  (= (.read (.getChatTypes packet) 0) com.comphenix.protocol.wrappers.EnumWrappers$ChatType/GAME_INFO))

(defn- get-json-from-chat-packet
  [packet]
  (json/read-str (.getJson (.read (.getChatComponents packet) 0)) :key-fn keyword))

(defn- on-packet-send [event]
  (let [packet (.getPacket event)]
    (when (and (is-chat-packet? packet)
               (is-chat-game-info? packet))
      (let [msg (get-json-from-chat-packet packet)]
        (when (.contains sleep-messages (:translate msg))
          (.setCancelled event true))))))

(defn adapter
  [plugin]
  (when (.getPlugin (Bukkit/getPluginManager) "ProtocolLib")
    (proxy
     [com.comphenix.protocol.events.PacketAdapter]
     [plugin [com.comphenix.protocol.PacketType$Play$Server/CHAT]]
      (onPacketSending [event] (on-packet-send event)))))
