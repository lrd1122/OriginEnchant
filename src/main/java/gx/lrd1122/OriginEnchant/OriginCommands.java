package gx.lrd1122.OriginEnchant;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OriginCommands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if (player.hasPermission("oe.help") && args.length == 0 || args[0].equalsIgnoreCase("help")) {
            sender.sendMessage(colorize("&e&l=========" + OriginEnchant.prefix + "->指令帮助=========" +
                    "\n§8==>§a/oe upgrade <enchant> <level> 提高/减少手中物品的某项附魔 &7oe.upgrade" +
                    "\n§8==>§a/oe set <enchant> <level> 设置手中物品的某项附魔 &7oe.set" +
                    "\n§8==>§a/oe list 查看附魔列表 &7oe.list" +
                    "\n§8==>§a/oe randomup <enchant> <min> <max> 随机升级手中物品的某项附魔 &7oe.randomup" +
                    "\n§8==>§a/oe randomset <enchant> <min> <max> 随机设置手中物品的某项附魔 &7oe.randomset" +
                    "\n§8==>§a/oe reload 重载插件 &7oe.reload"));
            return true;
        }
        if (player.hasPermission("oe.list") && args[0].equalsIgnoreCase("list")) {
            for (int nums = 0; nums < enchants().size(); nums++) {
                Enchantment enchantment = Enchantment.getByName(enchants().get(nums));
                int EnchantID = nums;
                sender.sendMessage(colorize(OriginEnchant.instance.getConfig().get("prefix") + " " + Integer.toString(EnchantID) + ". " + enchants().get(nums)));
            }
            return true;
        }
        if (player.hasPermission("oe.upgrade") && args.length >= 2 && args[0].equalsIgnoreCase("upgrade")) {
            if (IsNumber(args[1])) {
                int enchant = Integer.valueOf(args[1]);
                Enchantment enchantment = OriginEnchant.EnchantHash.get(enchant);
                try {
                    UpgradeEnchant(player, enchantment, Integer.valueOf(args[2]));
                } catch (Exception e) {
                    UpgradeEnchant(player, enchantment, 1);
                }
            } else {
                Enchantment enchantment = Enchantment.getByName(args[1]);
                try {
                    UpgradeEnchant(player, enchantment, Integer.valueOf(args[2]));
                } catch (Exception e) {
                    UpgradeEnchant(player, enchantment, 1);
                }
            }
            return true;
        }
        if (player.hasPermission("oe.randomup") && args.length >= 2 && args[0].equalsIgnoreCase("randomup")) {
            if (IsNumber(args[2]) && IsNumber(args[3])) {
                int min = Integer.valueOf(args[2]);
                int max = Integer.valueOf(args[3]);
                Random random = new Random();
                int addlevel = (random.nextInt(max - min + 1) + min);
                if (IsNumber(args[1])) {
                    int enchant = Integer.valueOf(args[1]);
                    Enchantment enchantment = OriginEnchant.EnchantHash.get(enchant);
                    UpgradeEnchant(player, enchantment, addlevel);
                } else {
                    Enchantment enchantment = Enchantment.getByName(args[1]);
                    UpgradeEnchant(player, enchantment, addlevel);
                }
            }
            return true;
        }
        if (player.hasPermission("oe.randomset") && args.length >= 2 && args[0].equalsIgnoreCase("randomset")) {
            try {
                if (IsNumber(args[2]) && IsNumber(args[3])) {
                    int min = Integer.valueOf(args[2]);
                    int max = Integer.valueOf(args[3]);
                    Random random = new Random();
                    int setlevel = (random.nextInt(max - min + 1) + min);
                    if (IsNumber(args[1])) {
                        int enchant = Integer.valueOf(args[1]);
                        Enchantment enchantment = OriginEnchant.EnchantHash.get(enchant);
                        SetEnchant(player, enchantment, setlevel);
                    } else {
                        Enchantment enchantment = Enchantment.getByName(args[1]);
                        SetEnchant(player, enchantment, setlevel);
                    }
                }
            } catch (Exception e) {
            }
            return true;
        }
        if (player.hasPermission("oe.set") && args.length >= 2 && args[0].equalsIgnoreCase("set")) {
            try {
                Random random = new Random();
                int setlevel = Integer.valueOf(args[2]);
                if (IsNumber(args[1])) {
                    int enchant = Integer.valueOf(args[1]);
                    Enchantment enchantment = OriginEnchant.EnchantHash.get(enchant);
                    SetEnchant(player, enchantment, setlevel);
                } else {
                    Enchantment enchantment = Enchantment.getByName(args[1]);
                    SetEnchant(player, enchantment, setlevel);
                }
            } catch (Exception e) {
            }
            return true;
        }
        if (player.hasPermission("oe.reload") && args.length >= 1 && args[0].equalsIgnoreCase("reload")) {
            OriginEnchant.instance.reloadConfig();
            sender.sendMessage(OriginEnchant.prefix + "§f 插件已重新加载");
            return true;
        }
        return true;
    }

    public void UpgradeEnchant(Player player, Enchantment enchantment, int addlevel) {
        try {
            ItemStack itemStack = player.getItemInHand();
            ItemMeta meta = itemStack.getItemMeta();
            int level = meta.getEnchantLevel(enchantment);
            meta.addEnchant(enchantment, level + addlevel, true);
            itemStack.setItemMeta(meta);
            if (OriginEnchant.instance.getConfig().get("notify").equals(true))
                player.sendMessage(OriginEnchant.instance.getConfig().getString("message").replace("%level%", String.valueOf(level + addlevel)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void SetEnchant(Player player, Enchantment enchantment, int setlevel) {
        try {
            ItemStack itemStack = player.getItemInHand();
            ItemMeta meta = itemStack.getItemMeta();
            int level = meta.getEnchantLevel(enchantment);
            meta.addEnchant(enchantment, setlevel, true);
            itemStack.setItemMeta(meta);
            if (OriginEnchant.instance.getConfig().get("notify").equals(true))
                player.sendMessage(OriginEnchant.instance.getConfig().getString("message").replace("%level%", String.valueOf(level)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<String> enchants() {
        List<String> returnname = new ArrayList<>();
        for (int i = 0; i < OriginEnchant.EnchantHash.size(); i++) {
            Enchantment enchantment = OriginEnchant.EnchantHash.get(i);
            String name = enchantment.getName();
            returnname.add(name);
        }
        return returnname;
    }

    public boolean IsNumber(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    private String colorize(String arg0) {
        return ChatColor.translateAlternateColorCodes('&', arg0);
    }
}
