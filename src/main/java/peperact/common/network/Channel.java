package peperact.common.network;

import net.minecraft.nbt.NBTTagCompound;

import java.util.Objects;

public class Channel implements Comparable<Channel> {

    public static final int GROUP_PUBLIC = 0;
    public static final int FREQUENCY_NONE = -1;

    public static Channel INVALID = new Channel(GROUP_PUBLIC, FREQUENCY_NONE);

    public static class Serializer {
        public static final String GROUP_TAG = "group";
        public static final String FREQUENCY_TAG = "freq";

        public static NBTTagCompound serializeNBT(Channel channel, NBTTagCompound tag) {
            tag.setInteger(GROUP_TAG, channel.group);
            tag.setInteger(FREQUENCY_TAG, channel.frequency);
            return tag;
        }

        public static Channel deserializeNBT(NBTTagCompound nbt) {
            return Channel.make(nbt.getInteger(GROUP_TAG), nbt.getInteger(FREQUENCY_TAG));
        }
    }

    public static Channel make(int group, int frequency) {
        Channel chan = new Channel(group, frequency);
        return chan.isValid() ? chan : INVALID;
    }

    public final int group;
    public final int frequency;

    private Channel(int group, int frequency) {
        this.group = group;
        this.frequency = frequency;
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Channel)) return false;
        Channel oc = (Channel) o;
        return group == oc.group && frequency == oc.frequency;
    }

    @Override
    public int hashCode() {
        return Objects.hash(group, frequency);
    }

    @Override
    public int compareTo(Channel o) {
        if (group != o.group) return group - o.group;
        return frequency - o.frequency;
    }

    public boolean isValid() {
        return group >= 0 && frequency >= 0;
    }
}
