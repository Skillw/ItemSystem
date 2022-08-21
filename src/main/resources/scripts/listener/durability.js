ItemTagData = find("com.skillw.itemsystem.taboolib.module.nms.ItemTagData");
ItemCache = com.skillw.itemsystem.internal.feature.ItemCache.INSTANCE;

//@Listener(-event org.bukkit.event.player.PlayerItemDamageEvent)
function sync(event) {
  const item = event.item;
  const tag = ItemCache.cacheTag(item);
  if (!tag.containsKey("耐久")) return;
  event.setCancelled(true);
  const maxDurability = tag.getDeep("耐久.总耐久").asDouble().intValue();
  const durability = tag.getDeep("耐久.当前耐久").asDouble().intValue();
  tag.putDeep("耐久.当前耐久", durability - 1);
  tag.saveTo(item);
  const typeDurability = item.type.maxDurability;
  item.setDurability(typeDurability * (1 - durability / maxDurability));
}
