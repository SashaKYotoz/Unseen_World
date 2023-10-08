package net.sashakyotoz.unseenworld.client.gui;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.GuiGraphics;

import java.util.HashMap;

import com.mojang.blaze3d.systems.RenderSystem;

public class GoldenChestGUIScreen extends AbstractContainerScreen<GoldenChestGUIMenu> {
	private final static HashMap<String, Object> guistate = GoldenChestGUIMenu.guistate;

	public GoldenChestGUIScreen(GoldenChestGUIMenu container, Inventory inventory, Component text) {
		super(container, inventory, text);
		Level world = container.world;
		int x = container.x;
		int y = container.y;
		int z = container.z;
		Player entity = container.entity;
		this.imageWidth = 197;
		this.imageHeight = 170;
	}

	private static final ResourceLocation texture = new ResourceLocation("unseen_world:textures/screens/golden_chest_gui.png");

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(guiGraphics);
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
		this.renderTooltip(guiGraphics, mouseX, mouseY);
	}

	@Override
	protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int gx, int gy) {
		RenderSystem.setShaderColor(1, 1, 1, 1);
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		guiGraphics.blit(texture, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);
		guiGraphics.blit(new ResourceLocation("unseen_world:textures/screens/golden_chest_decor.png"), this.leftPos + 50, this.topPos + 36, 0, 0, 96, 32, 96, 32);

		RenderSystem.disableBlend();
	}

	@Override
	public boolean keyPressed(int key, int b, int c) {
		if (key == 256) {
			assert this.minecraft != null;
			this.minecraft.player.closeContainer();
			return true;
		}
		return super.keyPressed(key, b, c);
	}

	@Override
	public void containerTick() {
		super.containerTick();
	}

	@Override
	protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
	}

	@Override
	public void onClose() {
		super.onClose();
	}

	@Override
	public void init() {
		super.init();
	}
}
