package gx.lrd1122;

import com.sun.org.apache.xerces.internal.xs.StringList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class UpgradeEnchant extends JavaPlugin {
    public String prefix = colorize(getConfig().getString("prefix"));
    @Override
    public void onEnable() {
        System.out.println("[UpgradeEnchant] 插件已成功加载");
        getCommand("ue").setExecutor(this);
        getCommand("ue").setTabCompleter(this);
        saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        System.out.println("[UpgradeEnchant] 插件已成功卸载");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if(player.hasPermission("ue.help") && args.length == 0 || args[0].equalsIgnoreCase("help"))
        {
            sender.sendMessage(colorize("&e&l=========" + prefix +"->指令帮助=========" +
                    "\n§8==>§a/ue upgrade <enchant> <level> 提高/减少手中物品的某项附魔 &7ue.upgrade" +
                    "\n§8==>§a/ue set <enchant> <level> 设置手中物品的某项附魔 &7ue.set" +
                    "\n§8==>§a/ue list 查看附魔列表 &7ue.list" +
                    "\n§8==>§a/ue randomup <enchant> <min> <max> 随机升级手中物品的某项附魔 &7ue.randomup" +
                    "\n§8==>§a/ue randomset <enchant> <min> <max> 随机设置手中物品的某项附魔 &7ue.randomset" +
                    "\n§8==>§a/ue reload 重载插件 &7ue.reload"));
            return true;
        }
        if(player.hasPermission("ue.list") && args[0].equalsIgnoreCase("list"))
        {
            for(int nums = 0; nums< enchants().size(); nums++)
            {
                Enchantment enchantment = Enchantment.getByName(enchants().get(nums));
                int EnchantID = enchantment.getId();
                sender.sendMessage(colorize(getConfig().get("prefix") +" " +Integer.toString(EnchantID) + ". " + enchants().get(nums)));
            }
            return true;
        }
        if(player.hasPermission("ue.upgrade") && args.length >= 2 && args[0].equalsIgnoreCase("upgrade"))
        {
            if(IsNumber(args[1])) {
                int enchant = Integer.valueOf(args[1]);
                Enchantment enchantment = Enchantment.getById(enchant);
                try {
                    UpgradeEnchant(player, enchantment, Integer.valueOf(args[2]));
                }
                catch (Exception e)
                {
                    UpgradeEnchant(player, enchantment, 1);
                }
            }
            else {
                Enchantment enchantment = Enchantment.getByName(args[1]);
                try {
                    UpgradeEnchant(player, enchantment, Integer.valueOf(args[2]));
                }
                catch (Exception e)
                {
                    UpgradeEnchant(player, enchantment, 1);
                }
            }
            return true;
        }
        if(player.hasPermission("ue.randomup") && args.length >= 2 && args[0].equalsIgnoreCase("randomup"))
        {
            if(IsNumber(args[2]) && IsNumber(args[3])) {
                int min = Integer.valueOf(args[2]);
                int max = Integer.valueOf(args[3]);
                Random random = new Random();
                int addlevel = (random.nextInt(max - min +1) +min);
                if (IsNumber(args[1])) {
                    int enchant = Integer.valueOf(args[1]);
                    Enchantment enchantment = Enchantment.getById(enchant);
                    UpgradeEnchant(player, enchantment, addlevel);
                } else {
                    Enchantment enchantment = Enchantment.getByName(args[1]);
                    UpgradeEnchant(player, enchantment, addlevel);
                }
            }
            return true;
        }
        if(player.hasPermission("ue.randomset") && args.length >= 2 && args[0].equalsIgnoreCase("randomset"))
        {
            try {
                if (IsNumber(args[2]) && IsNumber(args[3])) {
                    int min = Integer.valueOf(args[2]);
                    int max = Integer.valueOf(args[3]);
                    Random random = new Random();
                    int setlevel = (random.nextInt(max - min + 1) + min);
                    if (IsNumber(args[1])) {
                        int enchant = Integer.valueOf(args[1]);
                        Enchantment enchantment = Enchantment.getById(enchant);
                        SetEnchant(player, enchantment, setlevel);
                    } else {
                        Enchantment enchantment = Enchantment.getByName(args[1]);
                        SetEnchant(player, enchantment, setlevel);
                    }
                }
            }
            catch(Exception e){}
            return true;
        }
        if(player.hasPermission("ue.set") && args.length >= 2 && args[0].equalsIgnoreCase("set")) {
            try {
                Random random = new Random();
                int setlevel = Integer.valueOf(args[2]);
                if (IsNumber(args[1])) {
                    int enchant = Integer.valueOf(args[1]);
                    Enchantment enchantment = Enchantment.getById(enchant);
                    SetEnchant(player, enchantment, setlevel);
                } else {
                    Enchantment enchantment = Enchantment.getByName(args[1]);
                    SetEnchant(player, enchantment, setlevel);
                }
            }
            catch(Exception e){}
            return true;
        }
        if(player.hasPermission("ue.reload") && args.length >= 1 && args[0].equalsIgnoreCase("reload")) {
            reloadConfig();
            sender.sendMessage(prefix + "§f 插件已重新加载");
            return true;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> args0 = new ArrayList<>();
            args0.add("upgrade");
            args0.add("set");
            args0.add("reload");
            args0.add("randomup");
            args0.add("randomset");
            return args0;
        }
        return null;
    }

    public void UpgradeEnchant(Player player, Enchantment enchantment, int addlevel)
    {
        try {
            ItemStack itemStack = player.getItemInHand();
            ItemMeta meta = itemStack.getItemMeta();
            int level = meta.getEnchantLevel(enchantment);
            meta.addEnchant(enchantment, level + addlevel, true);
            itemStack.setItemMeta(meta);
            if(getConfig().get("notify").equals(true))
            player.sendMessage("您当前的附魔等级为 " + (level+addlevel) + " 级");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public void SetEnchant(Player player, Enchantment enchantment, int setlevel)
    {
        try {
            ItemStack itemStack = player.getItemInHand();
            ItemMeta meta = itemStack.getItemMeta();
            int level = meta.getEnchantLevel(enchantment);
            meta.addEnchant(enchantment, setlevel, true);
            itemStack.setItemMeta(meta);
            if(getConfig().get("notify").equals(true))
            player.sendMessage("您当前的附魔等级为 " + setlevel + " 级");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public static List<String> enchants()
    {
        List<String> returnname = new ArrayList<>();
        for(int i = 0; i <100; i++)
        {
            try
            {
                Enchantment enchantment = Enchantment.getById(i);
                String name = enchantment.getName();
                returnname.add(name);
            }
            catch (Exception e)
            {

            }

        }
        return returnname;
    }
    public boolean IsNumber(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }
    private String colorize(String arg0) {
        return ChatColor.translateAlternateColorCodes('&', arg0);
    }
}
