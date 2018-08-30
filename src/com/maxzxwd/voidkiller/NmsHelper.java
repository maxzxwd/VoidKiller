package com.maxzxwd.voidkiller;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public final class NmsHelper {

  private static NmsHelper inst = null;
  private static String version = null;
  private static Method cleClassGetHandleMethod = null;
  private static Method cheClassGetHandleMethod = null;
  private static Field elClassKillerField = null;
  private NmsHelper() {
  }

  public static NmsHelper get() {
    if (inst == null) {
      inst = new NmsHelper();
    }

    return inst;
  }

  public String getVersion() {
    if (version == null) {
      version = Bukkit.getServer().getClass().getPackage().getName();
      version = version.substring(version.lastIndexOf('.') + 1);
    }

    return version;
  }

  public Object cleClassGetHandleMethod(Player player) {
    try {
      if (cleClassGetHandleMethod == null) {
        Class<?> cleClass = Class
            .forName("org.bukkit.craftbukkit." + getVersion() + ".entity.CraftLivingEntity");
        cleClassGetHandleMethod = cleClass.getDeclaredMethod("getHandle");
      }
      return cleClassGetHandleMethod.invoke(player);
    } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
    }

    return null;
  }

  public Object cheClassGetHandleMethod(Player player) {
    try {
      if (cheClassGetHandleMethod == null) {
        Class<?> cheClass = Class
            .forName("org.bukkit.craftbukkit." + getVersion() + ".entity.CraftHumanEntity");
        cheClassGetHandleMethod = cheClass.getDeclaredMethod("getHandle");
      }
      return cheClassGetHandleMethod.invoke(player);
    } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
    }

    return null;
  }

  public void setLivingEntityKiller(Object cle, Object che) {
    if (cle != null || che != null) {
      try {
        if (elClassKillerField == null) {
          Class<?> elClass = Class
              .forName("net.minecraft.server." + getVersion() + ".EntityLiving");
          elClassKillerField = elClass.getDeclaredField("killer");
        }
        elClassKillerField.set(cle, che);
      } catch (IllegalAccessException | NoSuchFieldException | ClassNotFoundException e) {
        e.printStackTrace();
      }
    }
  }
}
