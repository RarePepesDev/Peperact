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

import javax.annotation.Nonnull;
import java.util.*;

@Mod.EventBusSubscriber(modid = Peperact.MODID)
public class ChannelManager extends WorldSavedData {
    public static final String CHANNEL_SAVE_ID = Peperact.MODID + "_channels";
    public static final String GROUP_LIST_TAG = "groups";
    public static final String GROUP_ID_TAG = "id";
    public static final String GROUP_NAME_TAG = "name";
    public static final String CHANNEL_LIST_TAG = "channels";
    public static final String CHANNEL_FREQUENCY_TAG = "freq";
    public static final String CHANNEL_NAME_TAG = "name";
    public static final String CHANNEL_VERSION_TAG = "version";
    // This is so if the format changes significantly in the future it can be updated
    public static final int VERSION = 0;
    public static final int GROUP_PUBLIC = 0;

    public static ChannelManager get(World world) {
        MapStorage storage = world.getMapStorage();
        ChannelManager instance = (ChannelManager) storage.getOrLoadData(ChannelManager.class, CHANNEL_SAVE_ID);
        if(instance != null) return instance;
        instance = new ChannelManager();
        storage.setData(CHANNEL_SAVE_ID, instance);
        return instance;
    }

    private final Set<Channel> channels = new HashSet<>();
    private final Map<Channel, String> channel_names = new TreeMap<>();
    private final Map<Integer, Set<Channel>> group_channels = new HashMap<>();
    private final Map<Integer, String> group_names = new TreeMap<>();

    public ChannelManager() {
        super(CHANNEL_SAVE_ID);
        //TODO: Translate this... somehow?
        group_names.put(GROUP_PUBLIC, "Public");
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        boolean wasDirty = this.isDirty();
        // Version
        int version = compound.getInteger(CHANNEL_VERSION_TAG);
        // List of Groups
        NBTTagList groupsTag = compound.getTagList(GROUP_LIST_TAG, Constants.NBT.TAG_COMPOUND);
        for (NBTBase elem : groupsTag) {
            NBTTagCompound groupTag = (NBTTagCompound) elem;

            // Group ID and Name
            int group = groupTag.getInteger(GROUP_ID_TAG);
            String groupName = groupTag.getString(GROUP_NAME_TAG);
            if (group != GROUP_PUBLIC)
                addGroup(group, groupName);

            // List of channels
            NBTTagList channelsTag = groupTag.getTagList(CHANNEL_LIST_TAG, Constants.NBT.TAG_COMPOUND);
            for(NBTBase elem2 : channelsTag) {
                NBTTagCompound channelTag = (NBTTagCompound) elem2;
                // Frequency and Name
                int frequency = channelTag.getInteger(CHANNEL_FREQUENCY_TAG);
                String channelName = groupTag.getString(CHANNEL_NAME_TAG);
                addChannel(Channel.make(group, frequency), channelName);
            }
        }
        this.setDirty(wasDirty);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        // Version
        compound.setInteger(CHANNEL_VERSION_TAG, VERSION);
        // List of Groups
        NBTTagList groupsTag = new NBTTagList();
        for(Map.Entry<Integer, String> i : group_names.entrySet()) {
            NBTTagCompound groupTag = new NBTTagCompound();

            // Group ID and Name
            groupTag.setInteger(GROUP_ID_TAG, i.getKey());
            groupTag.setString(GROUP_NAME_TAG, i.getValue());

            // List of Channels
            NBTTagList channelsTag = new NBTTagList();
            for(Channel channel : group_channels.get(i.getKey())) {
                NBTTagCompound channelTag = new NBTTagCompound();
                // Freqeuncy and Name
                channelTag.setInteger(CHANNEL_FREQUENCY_TAG, channel.frequency);
                channelTag.setString(CHANNEL_NAME_TAG, channel_names.get(channel));
                // Add to list tag
                channelsTag.appendTag(channelTag);
            }
            // Save list tag
            groupTag.setTag(CHANNEL_LIST_TAG, channelsTag);

            // Add to list tag
            groupsTag.appendTag(groupTag);
        }
        // Save list tag
        compound.setTag(GROUP_LIST_TAG, groupsTag);
        return compound;
    }

    private void onAddChannel(@Nonnull Channel channel, @Nonnull String name) {
        Peperact.log.info("Added Channel #%d '%s' to group #%d '%s'", channel.frequency, name, channel.group, group_names.get(channel.group));
    }

    public void onRenameChannel(@Nonnull Channel channel, @Nonnull String prev, @Nonnull String next) {
        Peperact.log.info("Renamed Channel #%d '%s' to '%s' in group #%d '%s'", channel.frequency, prev, next, channel.group, group_names.get(channel.group));
    }

    private void onRemoveChannel(@Nonnull Channel channel, @Nonnull String name) {
        Peperact.log.info("Removed Channel #%d '%s' from group #%d '%s'", channel.frequency, name, channel.group, group_names.get(channel.group));
    }

    private void onAddGroup(int group, @Nonnull String name) {
        Peperact.log.info("Added Group #%d '%s'", group, name);
    }

    public void onRenameGroup(@Nonnull int group, @Nonnull String prev, @Nonnull String next) {
        Peperact.log.info("Renamed Group #%d '%s' to '%s'", group, prev, next);
    }

    private void onRemoveGroup(int group, @Nonnull String name) {
        Peperact.log.info("Removed Group #%d '%s'", group, name);
    }

    public boolean addChannel(@Nonnull Channel channel, @Nonnull String name) {
        if(!channel.isValid()) return false;
        if (!group_names.containsKey(channel.group)) return false;
        boolean changed = channels.add(channel);
        if (changed) {
            group_channels.get(channel.group).add(channel);
            onAddChannel(channel, name);
            channel_names.put(channel, name);
        } else {
            String previous = channel_names.put(channel, name);
            if (!previous.equals(name)) {
                onRenameChannel(channel, previous, name);
                changed = true;
            }
        }
        if(changed) this.markDirty();
        return changed;
    }

    public boolean removeChannel(@Nonnull Channel channel) {
        if(!channel.isValid()) return false;
        boolean changed = channels.remove(channel);
        if (changed) {
            group_channels.get(channel.group).remove(channel);
            String name = channel_names.remove(channel);
            onRemoveChannel(channel, name);
            this.markDirty();
        }
        return changed;
    }

    public boolean addGroup(int group, @Nonnull String name) {
        if(group < 0) return false;
        if(group == GROUP_PUBLIC) return false;
        boolean changed = group_names.put(group, name) == null;
        if (changed) {
            group_channels.put(group, new HashSet<>());
            onAddGroup(group, name);
        } else {
            String previous = group_names.put(group, name);
            if (!previous.equals(name)) {
                onRenameGroup(group, previous, name);
                changed = true;
            }
        }
        if(changed) this.markDirty();
        return changed;
    }

    public boolean removeGroup(int group) {
        if(group < 0) return false;
        if(group == GROUP_PUBLIC) return false;
        String name = group_names.remove(group);
        boolean changed = name != null;
        if (changed) {
            List<Channel> toRemove = new ArrayList<>(group_channels.get(group));
            for (Channel channel : toRemove) {
                this.removeChannel(channel);
            }
            group_channels.remove(group);
            onRemoveGroup(group, name);
            this.markDirty();
        }
        return changed;
    }
}
