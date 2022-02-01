package gx.lrd1122.OriginEnchant;

import gx.lrd1122.OriginEnchant.Util.EnchantList;
import it.unimi.dsi.fastutil.Hash;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OriginTabComplete implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
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
}
