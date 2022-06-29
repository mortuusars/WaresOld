import io.github.mortuusars.wares.core.WareItem;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;

public class Testing {
    public static void main(String[] args){

//
//        Ware ware = new Ware();
//        ware.title = "test ware";
//        ware.experience = 20f;
//
//        ware.requestedItems.add(new WareItem("minecraft:apple", null, 32));
//        ware.requestedItems.add(new WareItem("minecraft:barrel", null, 6));
//        ware.requestedItems.add(new WareItem(null, "minecraft:coals", 6));
//
//        ware.paymentItems.add(new WareItem("minecraft:emerald", null, 32));
//        ware.paymentItems.add(new WareItem("minecraft:gold_ingot", null, 21));
//
//        Gson gson = new GsonBuilder().setPrettyPrinting().create();
//        String serializedWare = gson.toJson(ware);



        WareItem regularSmall = new WareItem("minecraft:gold_ingot", null, 21);
        WareItem regularBig = new WareItem("minecraft:gold_ingot", null, 224);

        WareItem tagSmall = new WareItem(null, "minecraft:coals", 21);
        WareItem tagBig = new WareItem(null, "minecraft:coals", 221);

//        String serializedregularSmall = new Gson().toJson(regularSmall);
//        String serializedregularBig = new Gson().toJson(regularBig);
//
//        String serializedtagSmall = new Gson().toJson(tagSmall);
//        String serializedtagBig = new Gson().toJson(tagBig);


        ArrayList<WareItem> items = new ArrayList<>();
        items.add(regularSmall);
        items.add(regularBig);
        items.add(tagSmall);
        items.add(tagBig);

        for (WareItem wareItem : items) {
            NonNullList<ItemStack> stacks = wareItem.createItemStacks();
        }

//        String serializedItem = gson.toJson(item);
//        System.out.println("\n\n");
//        System.out.println(serializedWare);
//        System.out.println("\n\n");
//        System.out.println(serializedItem);
//        System.out.println("\n\n");
    }
}
