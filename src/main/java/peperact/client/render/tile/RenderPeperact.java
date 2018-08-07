package peperact.client.render.tile;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import peperact.Peperact;
import peperact.client.model.ModelPeperactInside;
import peperact.common.block.peperact.TilePeperact;

public class RenderPeperact extends TileEntitySpecialRenderer<TilePeperact> {
    private static final ResourceLocation PEPERACT_INTERNAL_TEXTURE = new ResourceLocation(Peperact.MODID, "textures/block/peperact_inside.png");
    private final ModelPeperactInside model = new ModelPeperactInside();

    @Override
    public void render(TilePeperact te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        GlStateManager.pushMatrix();
        // Move to the middle of the block
        GlStateManager.translate((float)x + 0.5F, (float)y + 0.5F, (float)z + 0.5F);
        this.bindTexture(PEPERACT_INTERNAL_TEXTURE);
        GlStateManager.pushMatrix();

        model.renderInside();

        GlStateManager.popMatrix();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();
    }
}
