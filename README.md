# Flash GN Calculator

一个基于 Kotlin、Jetpack Compose 和 Material 3 的 Android 闪光灯 GN 输出计算器。

## 功能

- 深色极简单页界面
- GN 滑杆实时联动
- 光圈、距离、ISO 三列滚轮选择
- 实时计算并映射到常见闪光输出档位
- 超出满功率或低于最小功率时显示红色状态指示
- 自定义应用图标与启动页图标

## 技术栈

- Kotlin
- Jetpack Compose
- Material 3
- ViewModel
- Android SplashScreen API

## 运行要求

- Android Studio Hedgehog 或更高版本
- JDK 17
- Android SDK 35
- 最低 Android 版本：API 26

## 启动方式

1. 使用 Android Studio 打开项目根目录：
   `D:\Users\licong\Trae_Projects\flash_caculatro_new`
2. 等待 Gradle 同步完成。
3. 选择模拟器或真机运行 `app` 模块。

## 主要交互

- 顶部显示当前 GN 值
- 拖动滑杆可在 `1..80` 范围内调整 GN
- 底部滚轮可选择光圈、距离和 ISO
- 输出结果会自动映射到以下标准档位：
  - `1/1`
  - `1/2`
  - `1/4`
  - `1/8`
  - `1/16`
  - `1/32`
  - `1/64`
  - `1/128`

## 功率计算逻辑

核心公式位于：
[`FlashCalculator.kt`](D:/Users/licong/Trae_Projects/flash_caculatro_new/app/src/main/java/com/example/flashgncalculator/domain/FlashCalculator.kt)

计算逻辑：

```text
GN = 光圈 × 距离
有效 GN = GN × sqrt(ISO / 100)
功率比例 = (所需 GN / 满功率有效 GN)^2
```

然后将理论功率映射到最接近的标准闪光档位。

越界规则：

- 当理论功率大于 `1/1` 时，表示超过满功率
- 当理论功率小于 `1/128` 时，表示低于最小功率
- 越界时顶部状态点显示为红色，并显示中文提示

## 项目结构

```text
app/
├─ src/main/java/com/example/flashgncalculator/
│  ├─ MainActivity.kt
│  ├─ FlashCalculatorUiState.kt
│  ├─ FlashCalculatorViewModel.kt
│  ├─ domain/
│  │  └─ FlashCalculator.kt
│  └─ ui/
│     ├─ screen/
│     │  ├─ FlashCalculatorScreen.kt
│     │  └─ WheelPickerColumn.kt
│     └─ theme/
│        ├─ Color.kt
│        ├─ Theme.kt
│        └─ Type.kt
└─ src/main/res/
   ├─ drawable/
   ├─ mipmap-anydpi-v26/
   └─ values/
```

## 图标与启动页

- 应用图标使用像素风闪光灯图标
- 启动页使用同一图标并做了单独的安全边距处理，避免系统裁切

相关资源文件：

- [`ic_retro_flash.xml`](D:/Users/licong/Trae_Projects/flash_caculatro_new/app/src/main/res/drawable/ic_retro_flash.xml)
- [`ic_retro_flash_inset.xml`](D:/Users/licong/Trae_Projects/flash_caculatro_new/app/src/main/res/drawable/ic_retro_flash_inset.xml)
- [`ic_retro_flash_splash.xml`](D:/Users/licong/Trae_Projects/flash_caculatro_new/app/src/main/res/drawable/ic_retro_flash_splash.xml)
- [`themes.xml`](D:/Users/licong/Trae_Projects/flash_caculatro_new/app/src/main/res/values/themes.xml)

## 后续可扩展项

- 支持米 / 英尺切换
- 支持 1/3 档闪光输出
- 支持不同闪光灯变焦角度
- 支持保存常用参数预设
