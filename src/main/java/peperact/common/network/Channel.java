package peperact.common.network;

import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.UUID;

public class Channel {

    public enum Scope {
        PUBLIC,
        SHARED,
        PERSONAL
    }

    public static class Serializer {
        public static final String SCOPE_TAG = "scope";
        public static final String OWNER_M_TAG = "ownerM";
        public static final String OWNER_L_TAG = "ownerL";
        public static final String NAME_TAG = "name";

        public static NBTTagCompound serializeNBT(Channel channel) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setInteger(SCOPE_TAG, channel.scope.ordinal());
            if (channel.scope != Scope.PUBLIC) {
                tag.setLong(OWNER_M_TAG, channel.owner.getMostSignificantBits());
                tag.setLong(OWNER_L_TAG, channel.owner.getLeastSignificantBits());
            }
            tag.setString(NAME_TAG, channel.name);
            return tag;
        }

        public static Channel deserializeNBT(NBTTagCompound nbt) {
            Scope scope = Scope.values()[nbt.getInteger(SCOPE_TAG)];
            UUID owner = null;
            if (scope != Scope.PUBLIC) {
                owner = new UUID(nbt.getLong(OWNER_M_TAG), nbt.getLong(OWNER_L_TAG));
            }
            String name = nbt.getString(NAME_TAG);
            return new Channel(scope, owner, name);
        }
    }

    public final Scope scope;
    public final UUID owner;
    public final String name;

    private Channel(@Nonnull Scope scope, @Nullable UUID owner, @Nonnull String name) {
        // If the scope is public, then there should be no owner
        // If the scope isn't public, there there must be an owner
        assert scope == Scope.PUBLIC ? owner == null : owner != null;
        this.scope = scope;
        this.owner = owner;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Channel)) return false;
        Channel oc = (Channel) o;
        return scope == oc.scope && (scope == Scope.PUBLIC || owner.equals(oc.owner)) && name.equals(oc.name);
    }

    @Override
    public int hashCode() {
        if (scope == Scope.PUBLIC) return Objects.hash(scope, name);
        return Objects.hash(scope, owner, name);
    }
}
