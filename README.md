# ItemSystem

插件永久免费

---

## 插件

| 说明   | 内容                            |
|------|-------------------------------|
| 兼容版本 | 1.8 - 1.19                    |
| 硬依赖  | Pouvoir                       |
| 软依赖  | PlaceholderAPI Mythicmobs SkillAPI |

## 介绍

**ItemSystem** 是基于 **TabooLib VI** & **Pouvoir** 编写的一款物品引擎插件  

其主要功能如下:
- 全客制化式的物品配置(基于物品元流程，且支持内联Asahi)
- 自动更新
- 动态name/lore(发包)
- 物品的序列化与反序列化
- 完善的物品动作机制

**ItemSystem** 提供包括但不限于以下[**API**](http://doc.skillw.com/itemsystem/doc/):

- GlobalManager 全局变量管理器
- ActionTypeManager 物品动作管理器
- ItemBuilderManager 物品构建器管理器
- MetaManager 物品元管理器
- OptionManager 物品选项管理器
- VarTypeManager 变量类型管理器

你可以通过编写代码/脚本来拓展**ItemSystem**的诸多功能

### 包括但不限于

#### 物品元 (Meta)

```kotlin
@AutoRegister
object MetaDisplay : BaseMeta("display") {
    override val priority = 1

    override val default = ""

    override fun invoke(memory: Memory) {
        with(memory) {
            builder.name = getString("display").colored()
        }
    }

    override fun loadData(data: ItemData): Any {
        data.itemTag.remove("display")
        return (data.builder.name ?: data.itemStack.getName()).decolored()
    }

}
```

```javascript
//@Meta(custom-meta)

//物品构建流程
//Memory https://doc.skillw.com/itemsystem/com/skillw/itemsystem/api/meta/data/Memory.html
function process(memory) {
    //code
}

//序列化物品流程
//ItemData https://doc.skillw.com/itemsystem/com/skillw/itemsystem/api/builder/ItemData.html
function loadData(data) {
    //code
}
```

#### 变量类型 (VariableType)

```kotlin
@AutoRegister
object VarTypeNumber : VariableType("number", "num") {
    override fun createVar(memory: Memory): Any {
        with(memory) {
            val number = getDouble("value", 0.0)
            val format = getString("format", "#.##")
            val max = getDouble("max", number)
            val min = getDouble("min", number)
            return max(min(number, max), min).format(format)
        }
    }
}
```

```javascript
//@VarType(custom-var-type)

//在物品元流程中创建此类型变量时会被调用
//Memory https://doc.skillw.com/itemsystem/com/skillw/itemsystem/api/meta/data/Memory.html
function createVar(memory) {
    //获取str参数
    const str = memory.getString("str");
    return str;
}

```

#### 物品选项 (BaseOption)

```kotlin
@AutoRegister
object OptionAutoUpdate : BaseOption("auto-update") {
    override fun init(section: ConfigurationSection, builder: BaseItemBuilder) {
        builder.options["auto-update"] = section.getBoolean("auto-update", false)
    }

    val BaseItemBuilder.autoUpdate: Boolean
        get() = options["auto-update"].toString().toBoolean()
}
```

```javascript
//@ItemOption(custom-option)

//生成物品构建器(重载)时调用，用于在ItemBuilder中的options里初始化物品选项
//BaseItemBuilder https://doc.skillw.com/itemsystem/com/skillw/itemsystem/api/builder/BaseItemBuilder.html
//           物品配置节点   物品构建器
function init(section, builder) {
    //获取str参数
    const str = section.getString("example")
    builder.options.put("example-option",str)
}
```

<br/>

## Links

WIKI [http://blog.skillw.com/#sort=itemsystem&doc=README.md](http://blog.skillw.com/#sort=itemsystem&doc=README.md)

JavaDoc [http://doc.skillw.com/itemsystem/](http://doc.skillw.com/itemsystem/)

MCBBS [https://www.mcbbs.net/thread-1379506-1-1.html](https://www.mcbbs.net/thread-1379506-1-1.html)

爱发电 [https://afdian.net/@glom\_](https://afdian.net/@glom_)
