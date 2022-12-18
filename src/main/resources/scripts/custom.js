//创建此类型变量时调用
// save(保存 ) 和 cache会自动处理
//memory是Memory https://doc.skillw.com/itemsystem/com/skillw/itemsystem/api/meta/data/Memory.html
//@VarType(costom-var-type)
function createVar(memory) {
  //获取str参数
  const str = memory.getString("str");
  return str;
}

//memory是Memory https://doc.skillw.com/itemsystem/com/skillw/itemsystem/api/meta/data/Memory.html
//@Meta(custom-meta)
function customMeta(memory) {
  //获取str参数
  const str = memory.getString("str");
  memory.builder.lore.add("来自CustomMeta: " + str);
}

// ActionType = com.skillw.itemsystem.api.action.ActionType;

// function reg() {
//   new ActionType("key").register();
// }

// function call() {
//   ActionType = com.skillw.itemsystem.api.action.ActionType;
//   const player = this.sender.cast();
//   const item = player.inventory.itemInMainHand;
//   ActionType.call("key", player, item, function (data) {});
// }
