package com.maxzxwd.voidkiller;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.projectiles.ProjectileSource;

public class Plugin extends JavaPlugin implements Listener {
  public static Map<Player, Player> lastDamagers = new HashMap<>(Bukkit.getServer().getMaxPlayers());

  @Override
  public void onEnable() {
    super.onEnable();
    getServer().getPluginManager().registerEvents(this, this);
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onEntityDamage(EntityDamageEvent ede) {
    if (!(ede.getEntity() instanceof Player)) {
      return;
    }

    if (ede instanceof EntityDamageByEntityEvent) {
      EntityDamageByEntityEvent edbee = (EntityDamageByEntityEvent) ede;

      if (edbee.getDamager() instanceof Player) {
        lastDamagers.put((Player) ede.getEntity(), (Player) edbee.getDamager());
        return;
      }
      if (edbee.getDamager() instanceof Projectile) {
        ProjectileSource ps = ((Projectile) edbee.getDamager()).getShooter();
        if (ps instanceof Player) {
          lastDamagers.put((Player) ede.getEntity(), (Player) ps);
        }
        return;
      }
    }

    if (ede.getCause() != DamageCause.VOID) {
      lastDamagers.remove(ede.getEntity());
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onPlayerDeath(PlayerDeathEvent pde) {
    Player killer = lastDamagers.get(pde.getEntity());
    if (killer != null && pde.getEntity().getKiller() == null) {
      NmsHelper nms = NmsHelper.get();
      Object entityHandler = nms.cleClassGetHandleMethod(pde.getEntity());
      nms.setLivingEntityKiller(entityHandler, nms.cheClassGetHandleMethod(killer));
      lastDamagers.remove(pde.getEntity());
    }
  }

  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent pqe) {
    lastDamagers.remove(pqe.getPlayer());
  }
}
