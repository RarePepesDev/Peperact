package peperact.common.network;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.Mod;
import peperact.Peperact;

import java.util.*;

@Mod.EventBusSubscriber(modid = Peperact.MODID)
public class ChannelManager extends WorldSavedData implements Set<Channel> {
    public static final String CHANNEL_SAVE_ID = Peperact.MODID + "_channels";
    public static final String CHANNEL_LIST_TAG = "channels";
    public static final String CHANNEL_VERSION_TAG = "version";
    // This is so if the format changes significantly in the future it can be updated
    public static final int VERSION = 0;

    public static ChannelManager get(World world) {
        MapStorage storage = world.getMapStorage();
        ChannelManager instance = (ChannelManager) storage.getOrLoadData(ChannelManager.class, CHANNEL_SAVE_ID);
        if(instance != null) return instance;
        instance = new ChannelManager();
        storage.setData(CHANNEL_SAVE_ID, instance);
        return instance;
    }

    private final Set<Channel> channels = new HashSet<>();
    private final Set<Channel> public_channels = new HashSet<>();
    private final Map<UUID, Set<Channel>> shared_channels = new HashMap<>();
    private final Map<UUID, Set<Channel>> personal_channels = new HashMap<>();

    public ChannelManager() {
        super(CHANNEL_SAVE_ID);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        NBTTagList list = compound.getTagList(CHANNEL_LIST_TAG, Constants.NBT.TAG_COMPOUND);
        boolean wasDirty = this.isDirty();
        for(NBTBase elem : list) {
            NBTTagCompound channelNBT = (NBTTagCompound) elem;
            add(Channel.Serializer.deserializeNBT(channelNBT));
        }
        this.setDirty(wasDirty);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setInteger(CHANNEL_VERSION_TAG, VERSION);
        NBTTagList list = new NBTTagList();
        for(Channel channel : channels) {
            list.appendTag(Channel.Serializer.serializeNBT(channel));
        }
        compound.setTag(CHANNEL_LIST_TAG, list);
        return compound;
    }

    public void onAdd(Channel channel) {
        this.markDirty();
        switch(channel.scope) {
            case PUBLIC: {
                public_channels.add(channel);
                break;
            }
            case SHARED: {
                Set<Channel> yay = shared_channels.computeIfAbsent(channel.owner, k -> new HashSet<>());
                yay.add(channel);
                break;
            }
            case PERSONAL: {
                Set<Channel> yay = personal_channels.computeIfAbsent(channel.owner, k -> new HashSet<>());
                yay.add(channel);
                break;
            }
            default:
                Peperact.log.error("Unknown Scope: " + channel.scope.ordinal());
        }
    }

    public void onRemove(Channel channel) {
        this.markDirty();
        switch(channel.scope) {
            case PUBLIC: {
                public_channels.remove(channel);
                break;
            }
            case SHARED: {
                Set<Channel> yay = shared_channels.computeIfAbsent(channel.owner, k -> new HashSet<>());
                yay.remove(channel);
                // TODO: Dunno if need to clean up
                break;
            }
            case PERSONAL: {
                Set<Channel> yay = personal_channels.computeIfAbsent(channel.owner, k -> new HashSet<>());
                yay.remove(channel);
                // TODO: Clean up if needed
                break;
            }
            default:
                Peperact.log.error("Unknown Scope: " + channel.scope.ordinal());
        }
    }

    // Set methods wrapping

    @Override
    public int size() {
        return channels.size();
    }

    @Override
    public boolean isEmpty() {
        return channels.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return channels.contains(o);
    }

    @Override
    public Iterator<Channel> iterator() {
        Iterator<Channel> internal = channels.iterator();
        return new Iterator<Channel>() {
            Channel last = null;

            @Override
            public boolean hasNext() {
                return internal.hasNext();
            }

            @Override
            public Channel next() {
                return last = internal.next();
            }

            @Override
            public void remove() {
                internal.remove();
                onRemove(last);
            }
        };
    }

    @Override
    public Object[] toArray() {
        return channels.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return channels.toArray(a);
    }

    @Override
    public boolean add(Channel channel) {
        boolean changed = channels.add(channel);
        if(changed) this.onAdd(channel);
        return changed;
    }

    @Override
    public boolean remove(Object channel) {
        boolean changed = channels.remove(channel);
        if(changed) this.onRemove((Channel) channel);
        return changed;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return channels.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends Channel> c) {
        boolean changed = false;
        for(Channel channel : c) {
            changed = changed || add(channel);
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        Iterator<Channel> iter = this.iterator();
        while(iter.hasNext()) {
            Channel channel = iter.next();
            if(!c.contains(channel)) {
                iter.remove();
                changed = true;
            }
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for(Object channel : c) {
            changed = changed || remove(channel);
        }
        return changed;
    }

    @Override
    public void clear() {
        Iterator<Channel> iter = this.iterator();
        while(iter.hasNext()) {
            Channel channel = iter.next();
            iter.remove();
        }
    }
}
