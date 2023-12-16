left = ItemAPI.registerAction("left"); //org.bukkit.event.player.PlayerInteractEvent
right = ItemAPI.registerAction("right");
shiftRight = ItemAPI.registerAction("shift_right");
shiftLeft = ItemAPI.registerAction("shift_left");
clickItem = ItemAPI.registerAction("click_item"); // org.bukkit.event.inventory.InventoryClickEvent
attack = ItemAPI.registerAction("attack"); // org.bukkit.event.entity.EntityDamageByEntityEvent
build = ItemAPI.registerAction("build"); // com.skillw.itemsystem.api.event.ItemBuildEvent$Post
consume = ItemAPI.registerAction("consume"); // org.bukkit.event.player.PlayerItemConsumeEvent
swapToMainHand = ItemAPI.registerAction("swap_to_main_hand"); // org.bukkit.event.player.PlayerSwapHandItemsEvent
swapToOffHand = ItemAPI.registerAction("swap_to_offhand");
rightClickEntity = ItemAPI.registerAction("right_click_entity"); // org.bukkit.event.player.PlayerInteractAtEntityEvent
place = ItemAPI.registerAction("place"); // org.bukkit.event.block.BlockPlaceEvent
breakBlock = ItemAPI.registerAction("break_block"); // org.bukkit.event.block.BlockBreakEvent
drop = ItemAPI.registerAction("drop"); // org.bukkit.event.player.PlayerDropItemEvent 和 com.skillw.itemsystem.api.event.ItemDropEvent
pickUp = ItemAPI.registerAction("pick_up"); // org.bukkit.event.entity.EntityPickupItemEvent
damage = ItemAPI.registerAction("damage"); // org.bukkit.event.player.PlayerItemDamageEvent

Action = org.bukkit.event.block.Action;

//@Listener(-event org.bukkit.event.player.PlayerInteractEvent)
function onInteract(event) {
    const player = event.player;
    const item = player.inventory.itemInMainHand;
    if (item.isAir) return;
    const action = event.action;
    const left =
        action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK;
    const right =
        action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK;
    const shift = player.isSneaking();
    if (shift && left) {
        ItemAPI.callAction("shift_left", player, item, function (map) {
        });
        return;
    }
    if (shift && right) {
        ItemAPI.callAction("shift_right", player, item, function (map) {
        });
        return;
    }
    if (left) {
        ItemAPI.callAction("left", player, item, function (map) {
        });
        return;
    }
    if (right) {
        ItemAPI.callAction("right", player, item, function (map) {
        });
        return;
    }
}

//@Listener(-event org.bukkit.event.inventory.InventoryClickEvent -priority HIGHEST --ignoreCancelled)
function onClickItem(event) {
    if (event.clickedInventory == null) return;
    if (event.click != "LEFT") return;
    const player = event.whoClicked;
    const cursor = event.cursor;
    const current = event.currentItem;
    if (cursor == null || current == null || cursor.isAir || current.isAir)
        return;
    ItemAPI.callAction("click_item", player, cursor, function (map) {
        map.put("cursor", cursor);
        map.put("current", current);
        map.put("event", event);
    });
}

//@Listener(-event org.bukkit.event.player.PlayerInteractAtEntityEvent -priority HIGHEST --ignoreCancelled)
function onInteractEntity(event) {
    const player = event.player;
    const item = player.itemInHand;
    const clicked = event.rightClicked;
    ItemAPI.callAction("right_click_entity", player, item, function (map) {
        map.put("clicked", clicked);
        map.put("event", event);
    });
}

LivingEntity = org.bukkit.entity.LivingEntity;

//@Listener(-event org.bukkit.event.entity.EntityDamageByEntityEvent -priority HIGHEST --ignoreCancelled)
function onAttack(event) {
    const attacker = event.damager;
    const defender = event.defender;
    if (attacker == null || defender == null) return;
    if (!(attacker instanceof LivingEntity && defender instanceof LivingEntity))
        return;
    const item = attacker.itemInHand;
    ItemAPI.callAction("attack", attacker, item, function (map) {
        map.put("attacker", attacker);
        map.put("defender", defender);
        map.put("event", event);
    });
}

//@Listener(-event com.skillw.itemsystem.api.event.ItemBuildEvent$Post -priority HIGHEST --ignoreCancelled)
function onBuild(event) {
    const entity = event.entity;
    if (entity == null) return;
    const builder = event.builder;
    const item = event.itemStack;
    const data = event.data;
    ItemAPI.callAction("build", entity, item, function (map) {
        map.putAll(data);
        map.put("builder", builder);
        map.put("event", event);
    });
}

//@Listener(-event org.bukkit.event.player.PlayerItemConsumeEvent -priority HIGHEST --ignoreCancelled)
function onConsume(event) {
    const player = event.player;
    const item = event.item;
    ItemAPI.callAction("consume", player, item, function (map) {
        map.put("event", event);
    });
}

//@Listener(-event org.bukkit.event.player.PlayerSwapHandItemsEvent -priority HIGHEST --ignoreCancelled)
function onSwapHand(event) {
    const player = event.player;
    const main = event.mainHandItem;
    const offhand = event.offHandItem;
    ItemAPI.callAction("swap_to_main_hand", player, main, function (map) {
        map.put("event", event);
    });
    ItemAPI.callAction("swap_to_offhand", player, offhand, function (map) {
        map.put("event", event);
    });
}

//@Listener(-event org.bukkit.event.block.BlockPlaceEvent -priority HIGHEST --ignoreCancelled)
function onPlaceBlock(event) {
    const player = event.player;
    const item = player.itemInHand;
    const hand = event.hand.name;
    const location = event.block.location;
    ItemAPI.callAction("place", player, item, function (map) {
        map.put("location", location);
        map.put("hand", hand);
        map.put("event", event);
    });
}

//@Listener(-event org.bukkit.event.block.BlockBreakEvent -priority HIGHEST --ignoreCancelled)
function onBreakBlock(event) {
    const player = event.player;
    const item = player.itemInHand;
    ItemAPI.callAction("break_block", player, item, function (map) {
        map.put("event", event);
    });
}

//@Listener(-event org.bukkit.event.player.PlayerDropItemEvent -priority HIGHEST --ignoreCancelled)
function onItemDrop1(event) {
    const player = event.player;
    const item = event.itemDrop.itemStack;
    ItemAPI.callAction("drop", player, item, function (map) {
        map.put("event", event);
    });
}

//@Listener(-event com.skillw.itemsystem.api.event.ItemDropEvent -priority HIGHEST --ignoreCancelled)
function onItemDrop2(event) {
    const player = event.player;
    if (player == null) return;
    const item = event.item.itemStack;
    ItemAPI.callAction("drop", player, item, function (map) {
        map.put("event", event);
    });
}

//@Listener(-event org.bukkit.event.entity.EntityPickupItemEvent -priority HIGHEST --ignoreCancelled)
function onPickupItem(event) {
    const entity = event.entity;
    const item = event.item.itemStack;
    ItemAPI.callAction("pick_up", entity, item, function (map) {
        map.put("event", event);
    });
}

//@Listener(-event org.bukkit.event.player.PlayerItemDamageEvent -priority HIGHEST --ignoreCancelled)
function onItemDamage(event) {
    const entity = event.entity;
    const item = event.item.itemStack;
    if (item == null || item == undefined) return;
    const damage = event.damage;
    ItemAPI.callAction("damage", entity, item, function (map) {
        map.put("damage", damage);
        map.put("event", event);
    });
}
