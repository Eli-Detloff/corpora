package corpora.modid.entity.custom;

import corpora.modid.init.ModCardinalComponents;
import corpora.modid.init.ModItems;
import corpora.modid.util.EntityRegistryState;
import corpora.modid.util.ShellDataComponent;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ShellEntity extends AnimalEntity {

    DefaultedList<ItemStack> inventory = DefaultedList.ofSize(41, ItemStack.EMPTY);

    public ShellEntity(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 20)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 20);
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return false;
    }

    public boolean isPushable() {
        return false;
    }

    @Override
    public @Nullable PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return null;
    }

    @Override
    protected void initGoals() {
        super.initGoals();
    }

    public void tick() {
        super.tick();

        if (this.getWorld().isClient) return;
        if (!this.isAlive()) return;

        float health = this.getHealth();

        // Prevent transient invalid writes
        if (health <= 0.0F) return;

        EntityRegistryState state =
                EntityRegistryState.get((ServerWorld) this.getWorld());

        ShellDataComponent data = new ShellDataComponent(
                this.getUuid(),
                ModCardinalComponents.SHELL_OWNER_COMPONENT.get(this).get(),
                this.getBlockPos(),
                this.getYaw(),
                this.getPitch(),
                this.headYaw,
                this.bodyYaw,
                this.getWorld().getRegistryKey(),
                this.getName().getString(),
                health
        );

        state.put(data);
    }


    @Override
    public void remove(RemovalReason reason) {
        if (!this.getWorld().isClient) {
            if (this.getWorld() instanceof ServerWorld serverWorld) {
                System.out.println("REMOVING: " + this.getUuid());
                EntityRegistryState state =
                        EntityRegistryState.get(serverWorld);

                state.remove(this.getUuid());
            }
        }

        super.remove(reason);
    }

    public void storePlayerInventory(PlayerEntity player) {

        for (int i = 0; i < 41; i++) {

            ItemStack moved =
                    player.getInventory().removeStack(i);

            this.inventory.set(i, moved);
        }

        player.getInventory().markDirty();
        player.currentScreenHandler.sendContentUpdates();
    }


    public void loadPlayerInventory(PlayerEntity player) {

        for (int i = 0; i < 41; i++) {

            ItemStack moved =
                    this.inventory.get(i);

            player.getInventory().setStack(i, moved);
        }

        this.inventory.clear();

        player.getInventory().markDirty();
        player.currentScreenHandler.sendContentUpdates();
    }

    public void dropStoredInventory() {

        for (ItemStack stack : this.inventory) {

            if (!stack.isEmpty()) {
                this.dropStack(stack);
            }
        }

        this.inventory.clear();
    }

    @Override
    public void onDeath(DamageSource source) {
        super.onDeath(source);

        if (!this.getWorld().isClient) {
            dropStoredInventory();

            ItemStack brokenShell = new ItemStack(ModItems.SHELL_ITEM_BROKEN);
            ItemEntity itemEntity = new ItemEntity(
                    this.getWorld(),
                    this.getX(),
                    this.getY(),
                    this.getZ(),
                    brokenShell
            );
            this.getWorld().spawnEntity(itemEntity);


            ServerPlayerEntity ownerPlayer = (ServerPlayerEntity) this.getWorld().getPlayerByUuid(ModCardinalComponents.SHELL_OWNER_COMPONENT.get(this).get());

            if (ownerPlayer != null) {
                ownerPlayer.sendMessage(
                        this.getDamageTracker().getDeathMessage(),
                        false
                );
            }


        }
    }


    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        if (this.getWorld().isClient) {
            return ActionResult.SUCCESS;
        }




        if (!ModCardinalComponents.SHELL_OWNER_COMPONENT.get(this).get().equals(player.getUuid())){
            return ActionResult.PASS;
        }

        ItemStack stack = player.getStackInHand(hand);

        // Must be empty hand
        if (!stack.isEmpty()) return ActionResult.PASS;

        // Must be sneaking
        if (!player.isSneaking()) return ActionResult.PASS;





        ItemStack egg = new ItemStack(ModItems.SHELL_ITEM);

        egg.set(DataComponentTypes.CUSTOM_NAME, this.getCustomName());

        // Drop or give item
        if (!player.getInventory().insertStack(egg)) {
            this.dropStack(egg);
        }
        if (!this.getWorld().isClient) {
            dropStoredInventory();
        }


        this.discard();

        return super.interactMob(player, hand);
    }


}
