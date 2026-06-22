package corpora.modid.gui;


import corpora.modid.networking.custom.SelectShellC2SPayload;
import corpora.modid.util.ShellDataComponent;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.List;

public class ShellGui extends Screen {

    private final List<ShellDataComponent> shells;

    private int scrollOffset = 0;

    private static final int ENTRY_HEIGHT = 24;
    private static final int VISIBLE_ENTRIES = 8;

    public ShellGui(List<ShellDataComponent> shells) {
        super(Component.literal("Shells"));
        this.shells = shells;
    }

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {


        context.fill(0, 0, width, height, 0xAA000000);

        int centerX = width / 2;
        int startY = 40;
        int boxWidth = 220;

        context.drawCenteredString(
                font,
                "Available Shells",
                centerX,
                20,
                0xFFFFFF
        );

        for (int i = 0; i < VISIBLE_ENTRIES; i++) {

            int shellIndex = i + scrollOffset;

            if (shellIndex >= shells.size()) break;

            ShellDataComponent shell = shells.get(shellIndex);

            int x = centerX - boxWidth / 2;
            int y = startY + (i * ENTRY_HEIGHT);

            boolean hovered =
                    mouseX >= x && mouseX <= x + boxWidth &&
                            mouseY >= y && mouseY <= y + 20;

            int color = hovered ? 0xFF666666 : 0xFF333333;

            context.fill(x, y, x + boxWidth, y + 20, color);

            String shellDimension = String.valueOf(shell.dimension().location())
                    .replace("minecraft:", "");
            ;
            String shellPosition = shell.pos().getX() + "," + shell.pos().getY() + "," + shell.pos().getZ();

            context.drawString(
                    font,
                    (shell.name() + " (" + shellDimension + ") Pos:" + shellPosition),
                    x + 8,
                    y + 6,
                    0xFFFFFF,
                    false
            );
        }

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontal, double vertical) {

        if (vertical > 0) scrollOffset--;
        if (vertical < 0) scrollOffset++;

        int maxScroll = Math.max(0, shells.size() - VISIBLE_ENTRIES);

        if (scrollOffset < 0) scrollOffset = 0;
        if (scrollOffset > maxScroll) scrollOffset = maxScroll;

        return true;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {

        int centerX = width / 2;
        int startY = 40;
        int boxWidth = 220;

        for (int i = 0; i < VISIBLE_ENTRIES; i++) {

            int shellIndex = i + scrollOffset;

            if (shellIndex >= shells.size()) break;

            int x = centerX - boxWidth / 2;
            int y = startY + (i * ENTRY_HEIGHT);

            boolean hovered =
                    mouseX >= x && mouseX <= x + boxWidth &&
                            mouseY >= y && mouseY <= y + 20;

            if (hovered) {
                confirmSelection(shells.get(shellIndex));
                return true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    private void confirmSelection(ShellDataComponent selected) {


        ClientPlayNetworking.send(
                new SelectShellC2SPayload(
                        selected.uuid()
                )
        );

        onClose();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void renderBackground(GuiGraphics context, int mouseX, int mouseY, float delta) {
        // removes blur
    }

}