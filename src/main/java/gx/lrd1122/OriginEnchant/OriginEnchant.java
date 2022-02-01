package gx.lrd1122.OriginEnchant;

import gx.lrd1122.OriginEnchant.Util.EnchantList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class OriginEnchant extends JavaPlugin {
    public static HashMap<Integer, Enchantment> EnchantHash;
    public static String prefix;
    public static JavaPlugin instance;
    @Override
    public void onEnable() {
        prefix = colorize(getConfig().getString("prefix"));
        EnchantHash = EnchantList.enchantmentHashMap();
        instance = this;
        Bukkit.getPluginCommand("oe").setExecutor(new OriginCommands());
        Bukkit.getPluginCommand("oe").setTabCompleter(new OriginTabComplete());
        saveDefaultConfig();
        System.out.println("[UpgradeEnchant] 插件已成功加载");
    }

    @Override
    public void onDisable() {
        System.out.println("[UpgradeEnchant] 插件已成功卸载");
    }
    private String colorize(String arg0) {
        return ChatColor.translateAlternateColorCodes('&', arg0);
    }
}
