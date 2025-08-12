package wornhorseshoes.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ToServerSetRearingPacket implements IMessage {
    private boolean isSetToRear = false;
    private int entityId = 0;
    private int dimId = 0;

    public ToServerSetRearingPacket() {}

    public ToServerSetRearingPacket(Entity entity, boolean isSetToRear){
        this.isSetToRear = isSetToRear;
        this.dimId = entity.world.provider.getDimension();
        this.entityId = entity.getEntityId();
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.isSetToRear = buf.readBoolean();
        this.entityId = buf.readInt();
        this.dimId = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(this.isSetToRear);
        buf.writeInt(this.entityId);
        buf.writeInt(this.dimId);
    }

    public static class ToServerSetRearingPacketHandler implements IMessageHandler<ToServerSetRearingPacket, IMessage> {
        @Override
        public IMessage onMessage(ToServerSetRearingPacket message, MessageContext ctx) {
            FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
            return null;
        }

        private void handle(ToServerSetRearingPacket message, MessageContext ctx) {
            MinecraftServer server = ctx.getServerHandler().player.getServer();
            World world = server.getWorld(message.dimId);
            Entity entity = world.getEntityByID(message.entityId);

            if(!(entity instanceof AbstractHorse)) return;
            AbstractHorse horse = (AbstractHorse) entity;
            horse.setRearing(message.isSetToRear);
        }
    }
}
