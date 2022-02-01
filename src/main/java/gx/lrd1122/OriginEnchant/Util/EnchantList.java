package gx.lrd1122.OriginEnchant.Util;

import org.bukkit.enchantments.Enchantment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class EnchantList{
    public static HashMap<Integer, Enchantment> enchantmentHashMap()
    {
        List<Enchantment> enchantments = new ArrayList<>(Arrays.asList(Enchantment.values()));
        HashMap<Integer, Enchantment> hashMap = new HashMap<>();
        for(int i = 0 ; i < enchantments.size(); i++)
        {
            hashMap.put(i, enchantments.get(i));
        }
        return hashMap;
    }
}
