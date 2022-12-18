function unique(gem) {
    const nbt = getTag(gem);
    return nbt.get("unique").asString();
}

function gemKey(gem) {
    const nbt = getTag(gem);
    return obj(nbt.getDeep("ITEM_SYSTEM.key"));
}

function gemBuildData(gem) {
    const nbt = getTag(gem);
    return obj(nbt.getDeep("ITEM_SYSTEM.data"));
}

function checkAttr(player) {
    if (AttrAPI == "undefined") {
        soundFail(player)
        player.sendMessage("服务器需要安装AttributeSystem，才能进行ItemSystem的宝石镶嵌")
        return false
    }
    return true
}

function inlay() {
    const player = this.player;
    if (!checkAttr(player)) return;
    const gem = this.cursor;
    const weapon = this.current;
    const itemNbt = getTag(weapon);
    if (!itemNbt.containsKey("gem")) return;
    this.event.setCancelled(true);
    const gemNbt = itemNbt.get("gem").asCompound();
    for (let index = 0; index < gemNbt.size(); index++) {
        const key = index + "";
        const data = gemNbt.get(key).asCompound();
        if (data.get("name").asString() != "空槽位") continue;
        const weaponName = name(weapon);
        const gemName = name(gem);
        const gemData = {
            name: gemName,
            key: gemKey(gem),
            unique: unique(gem),
            data: gemBuildData(gem),
        };
        gemNbt.put(key, mapOf(gemData));
        itemNbt.put("gem", gemNbt);
        itemNbt.saveTo(weapon);
        const gemAttr = AttrAPI.readItemNBT(gem, player, null);
        gemAttr.saveTo(weapon);
        gem.amount--;
        soundSuccess(player)
        taskLater([5, function (task) {
            soundFinish(player)
        }])
        player.sendMessage(
            color(
                "&a你成功地将 &6 " + gemName + " &a镶嵌到了 &b" + weaponName + " &a上!"
            )
        );
        return;
    }
    inlayFail(player, weapon);
}

function inlayFail(player, weapon) {
    const weaponName = name(weapon);
    soundFail(player)
    player.sendMessage(
        color("&c镶嵌失败! &b" + weaponName + " &c上没有空余槽位!")
    );
}

defaultGemData = {
    name: "空槽位",
    unique: "",
};

function buildGem(key, player, data) {
    return ItemAPI.productItem(key, player, function (processData) {
        processData.putAll(data);
    });
}

function disassemble(player, weapon, disassemble, returnGem) {
    if (!checkAttr(player)) return false
    const itemNbt = getTag(weapon);
    if (!itemNbt.containsKey("gem")) return false
    const gemNbt = itemNbt.get("gem").asCompound();
    for (let index = gemNbt.size() - 1; index >= 0; index--) {
        const key = index + "";
        const data = gemNbt.get(key).asCompound();
        if (data.get("name").asString() == "空槽位") continue;
        const weaponName = name(weapon);
        const gemName = data.get("name").asString();
        const unique = data.get("unique").asString();
        if (returnGem) {
            const gemKey = data.get("key").asString();
            const gemBuildData = obj(data.get("data").asCompound());
            player.inventory.addItem(buildGem(gemKey, player, gemBuildData));
        }
        gemNbt.put(key, mapOf(defaultGemData));
        const gemAttrKey = "gem-" + unique;
        itemNbt.removeDeep("ATTRIBUTE_DATA." + gemAttrKey);
        itemNbt.put("gem", gemNbt);
        itemNbt.saveTo(weapon);
        disassemble.amount--;
        soundSuccess(player)
        player.sendMessage(
            color(
                "&a你成功地将 &b" + weaponName + " &a上的 &6" + gemName + " &a卸载了!"
            )
        );
        return true;
    }

    return false;
}

function disassembleWithGem() {
    const player = this.player;
    const weapon = this.current;
    if (disassemble(player, weapon, this.cursor, true)) {
        this.event.setCancelled(true);
    }
}

function disassembleWithOutGem() {
    const player = this.player;
    const weapon = this.current;
    if (disassemble(player, weapon, this.cursor, false)) {
        this.event.setCancelled(true);
    }
}
