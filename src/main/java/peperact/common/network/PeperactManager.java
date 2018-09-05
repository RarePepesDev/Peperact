package peperact.common.network;

import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import peperact.common.block.peperact.TilePeperact;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

public class PeperactManager {
    public static WeakHashMap<MapStorage, Map<Channel, TilePeperact>> managers = new WeakHashMap<>();

    public static Map<Channel, TilePeperact> get(World world) {
        return managers.computeIfAbsent(world.getMapStorage(), m -> new HashMap<>());
    }

    public static void validate(World world, TilePeperact tile) {
        Map<Channel, TilePeperact> map = get(world);
        // TODO: Implement
    }

    public static void invalidate(World world, TilePeperact tile) {
        Map<Channel, TilePeperact> map = get(world);
        // TODO: Implement
    }
}
