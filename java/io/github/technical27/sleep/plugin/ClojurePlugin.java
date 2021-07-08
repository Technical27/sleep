package io.github.technical27.sleep.plugin;

import org.bukkit.plugin.java.JavaPlugin;

/**
* A class for plugins written in clojure.
* Will automatically use the correct classloader to prevent ClassNotFoundErrors
*/
public abstract class ClojurePlugin extends JavaPlugin {
  static {
    Thread.currentThread().setContextClassLoader(ClojurePlugin.class.getClassLoader());
  }
}
