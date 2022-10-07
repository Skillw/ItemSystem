ItemTagData = find("com.skillw.itemsystem.taboolib.module.nms.ItemTagData");

//@Listener(-event org.bukkit.event.player.PlayerItemDamageEvent)
function sync(event) {
  const item = event.item;
  const tag = getTag(item);
  if (!tag.containsKey("耐久")) return;
  const maxDurability = tag.getDeep("耐久.总耐久").asDouble().intValue();
  const durability = tag.getDeep("耐久.当前耐久").asDouble().intValue();
  const typeDurability = item.type.maxDurability;
  if (durability > 0) {
    event.setCancelled(true);
  } else {
    item.setDurability(typeDurability);
    return;
  }
  tag.putDeep("耐久.当前耐久", durability - 1);
  tag.saveTo(item);
  item.setDurability(typeDurability * (1 - durability / maxDurability));
}
