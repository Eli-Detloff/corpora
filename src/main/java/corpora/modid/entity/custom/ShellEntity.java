package corpora.modid.entity.custom;

import corpora.modid.init.ModCardinalComponents;
import corpora.modid.init.ModItems;
import corpora.modid.util.EntityRegistryState;
import corpora.modid.util.ShellDataComponent;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class ShellEntity extends Animal {

    NonNullList<ItemStack> inventory = NonNullList.withSize(41, ItemStack.EMPTY);

    public ShellEntity(EntityType<? extends Animal> entityType, Level world) {
        super(entityType, world);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20)
                .add(Attributes.KNOCKBACK_RESISTANCE, 20);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return false;
    }

    public boolean isPushable() {
        return false;
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel world, AgeableMob entity) {
        return null;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
    }

    public void tick() {
        super.tick();

        if (this.level().isClientSide) return;
        if (!this.isAlive()) return;

        float health = this.getHealth();

        // Prevent transient invalid writes
        if (health <= 0.0F) return;

        EntityRegistryState state =
                EntityRegistryState.get((ServerLevel) this.level());

        ShellDataComponent data = new ShellDataComponent(
                this.getUUID(),
                ModCardinalComponents.SHELL_OWNER_COMPONENT.get(this).get(),
                this.blockPosition(),
                this.getYRot(),
                this.getXRot(),
                this.yHeadRot,
                this.yBodyRot,
                this.level().dimension(),
                this.getName().getString(),
                health
        );

        state.put(data);
    }


    @Override
    public void remove(RemovalReason reason) {
        if (!this.level().isClientSide) {
            if (this.level() instanceof ServerLevel serverWorld) {
                System.out.println("REMOVING: " + this.getUUID());
                EntityRegistryState state =
                        EntityRegistryState.get(serverWorld);

                state.remove(this.getUUID());
            }
        }

        super.remove(reason);
    }

    public void storePlayerInventory(Player player) {

        for (int i = 0; i < 41; i++) {

            ItemStack moved =
                    player.getInventory().removeItemNoUpdate(i);

            this.inventory.set(i, moved);
        }

        player.getInventory().setChanged();
        player.containerMenu.broadcastChanges();
    }


    public void loadPlayerInventory(Player player) {

        for (int i = 0; i < 41; i++) {

            ItemStack moved =
                    this.inventory.get(i);

            player.getInventory().setItem(i, moved);
        }

        this.inventory.clear();

        player.getInventory().setChanged();
        player.containerMenu.broadcastChanges();
    }

    public void dropStoredInventory() {

        for (ItemStack stack : this.inventory) {

            if (!stack.isEmpty()) {
                this.spawnAtLocation(stack);
            }
        }

        this.inventory.clear();
    }

    @Override
    public void die(DamageSource source) {
        super.die(source);

        if (!this.level().isClientSide) {
            dropStoredInventory();

            ItemStack brokenShell = new ItemStack(ModItems.SHELL_ITEM_BROKEN);
            ItemEntity itemEntity = new ItemEntity(
                    this.level(),
                    this.getX(),
                    this.getY(),
                    this.getZ(),
                    brokenShell
            );
            this.level().addFreshEntity(itemEntity);


            ServerPlayer ownerPlayer = (ServerPlayer) this.level().getPlayerByUUID(ModCardinalComponents.SHELL_OWNER_COMPONENT.get(this).get());

            if (ownerPlayer != null) {
                ownerPlayer.displayClientMessage(
                        this.getCombatTracker().getDeathMessage(),
                        false
                );
            }


        }
    }


    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (this.level().isClientSide) {
            return InteractionResult.SUCCESS;
        }


        if (!ModCardinalComponents.SHELL_OWNER_COMPONENT.get(this).get().equals(player.getUUID())) {
            return InteractionResult.PASS;
        }

        ItemStack stack = player.getItemInHand(hand);

        // Must be empty hand
        if (!stack.isEmpty()) return InteractionResult.PASS;

        // Must be sneaking
        if (!player.isShiftKeyDown()) return InteractionResult.PASS;


        ItemStack egg = new ItemStack(ModItems.SHELL_ITEM);

        egg.set(DataComponents.CUSTOM_NAME, this.getCustomName());

        // Drop or give item
        if (!player.getInventory().add(egg)) {
            this.spawnAtLocation(egg);
        }
        if (!this.level().isClientSide) {
            dropStoredInventory();
        }


        this.discard();

        return super.mobInteract(player, hand);
    }


}
