package peperact.client.render.tile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import peperact.Peperact;
import peperact.client.model.ModelPeperactInside;
import peperact.common.block.peperact.TilePeperact;
import peperact.common.registry.RegistryItems;

public class RenderPeperact extends TileEntitySpecialRenderer<TilePeperact> {
    private static final ResourceLocation PEPERACT_INTERNAL_TEXTURE = new ResourceLocation(Peperact.MODID, "textures/block/peperact_inside.png");
    private static final ItemStack PEPE_STACK = new ItemStack(RegistryItems.pepe);
    private static final int PEPE_ROTATION_PERIOD = 20 * 10;
    private final ModelPeperactInside model = new ModelPeperactInside();

    @Override
    public void render(TilePeperact te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        GlStateManager.pushAttrib();
        GlStateManager.pushMatrix();

        // Move to the middle of the block
        GlStateManager.translate((float)x + 0.5F, (float)y + 0.5F, (float)z + 0.5F);
        this.bindTexture(PEPERACT_INTERNAL_TEXTURE);

        // From LayerEndermanEyes
        // I have no idea what I'm doing LUL
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        //GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
        GlStateManager.disableLighting();
        GlStateManager.depthMask(true);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 61680.0F, 0.0F);
        GlStateManager.enableLighting();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
        // End LayerEndermanEyes

        GlStateManager.pushMatrix();
        model.renderInside();
        GlStateManager.popMatrix();

        GlStateManager.disableLighting();
        GlStateManager.pushMatrix();
        GlStateManager.rotate(360f * ((this.getWorld().getTotalWorldTime() + partialTicks) / (PEPE_ROTATION_PERIOD)), 0.0f, 1.0f, 0.0f);

        GlStateManager.translate(0, -0.05, 0);
        GlStateManager.scale(0.5, 0.5, 0.5);
        Minecraft.getMinecraft().getRenderItem().renderItem(PEPE_STACK, ItemCameraTransforms.TransformType.GROUND);
        GlStateManager.popMatrix();

        // From LayerEndermanEyes
        Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
        GlStateManager.depthMask(true);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        // End LayerEndermanEyes

        GlStateManager.popMatrix();
        GlStateManager.popAttrib();
    }
}
