package peperact.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelPeperactInside extends ModelBase {
    public ModelRenderer inside = new ModelRenderer(this, 0, 0);

    public ModelPeperactInside()
    {
        this.inside.setTextureSize(16, 16);
        this.inside.addBox(6, 6, 6, -12, -12, -12, 0.0F);
        this.inside.addBox(2, 2, 2, -4, -4, -4, 0.0F);
    }

    public void renderInside()
    {
        this.inside.render(0.0625F);
    }
}
