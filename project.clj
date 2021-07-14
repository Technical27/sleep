(defproject io.github.technical27/sleep "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/data.json "2.3.1"]]
  :aot :all
  :target-path "target/%s"
  :repositories [["spigot-repo" "https://hub.spigotmc.org/nexus/content/repositories/snapshots/"]
                 ["protocollib-repo" "https://repo.dmulloy2.net/repository/public/"]
                 ["afk-gh" "https://maven.pkg.github.com/Technical27/afk"]]
  :java-source-paths ["java"]
  :profiles {:provided {:dependencies [[org.spigotmc/spigot-api "1.17-R0.1-SNAPSHOT" :scope "runtime"]
                                       [com.comphenix.protocol/ProtocolLib "4.7.0" :scope "runtime"]
                                       [io.github.technical27/afk "0.1.0-SNAPSHOT" :scope "runtime"]]}
             :uberjar {:jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})

;:jvm-opts ["-Dclojure.compiler.direct-linking=true"]
