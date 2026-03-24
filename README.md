# Flash GN Calculator

一个基于 Kotlin + Jetpack Compose + Material 3 的 Android 闪光灯 GN 输出计算器。

## 当前版本特性

- 全黑极简界面，单 Activity 架构
- GN 滑杆实时更新（范围 `1..80`）
- `OUTPUT` 实时显示标准闪光档位
- 当理论功率超出范围时：
  - 显示最近可用档位（`1/1` ~ `1/128`）
  - 显示中文提示（超过满功率 / 低于最小功率）
  - 顶部状态点切换为红色
- 参数选择区使用三行下拉选框（非滚轮）：
  1. 光圈
  2. 距离 (m)
  3. 感光度
- 已移除 `OUTPUT` 下方箭头
- 输出区与参数区布局已上移，适配小屏设备可视区域

## 技术栈

- Kotlin
- Jetpack Compose
- Material 3
- ViewModel + StateFlow
- Android SplashScreen API

## 运行要求

- Android Studio Hedgehog 或更高
- JDK 17
- Android SDK 35
- Min SDK 26

## 启动方式

1. 用 Android Studio 打开项目目录：
   `D:\Users\licong\Trae_Projects\flash_caculatro_new`
2. 等待 Gradle Sync 完成
3. 运行 `app` 模块

## 安装到真机（小米/澎湃 OS 常见问题）

如果出现 `INSTALL_FAILED_USER_RESTRICTED`：

- 打开开发者选项中的 `USB 调试` 与 `USB 安装`
- 连接设备后确认 USB 调试授权弹窗
- 使用 `adb devices` 确认状态为 `device`

## 功率计算逻辑

核心文件：
- `app/src/main/java/com/example/flashgncalculator/domain/FlashCalculator.kt`

计算流程：

```text
1) requiredGn = aperture * distance
2) effectiveGn = gn * sqrt(iso / 100)
3) rawPower = (requiredGn / effectiveGn)^2
4) 将 rawPower 映射到最近标准档位
```

标准档位：
- `1/1`
- `1/2`
- `1/4`
- `1/8`
- `1/16`
- `1/32`
- `1/64`
- `1/128`

越界规则：
- `rawPower > 1/1`：超过满功率
- `rawPower < 1/128`：低于最小功率
- 越界时仅提示，不自动改动光圈/距离/ISO

## 关键代码结构

```text
app/src/main/java/com/example/flashgncalculator/
├─ MainActivity.kt
├─ FlashCalculatorUiState.kt
├─ FlashCalculatorViewModel.kt
├─ domain/
│  └─ FlashCalculator.kt
└─ ui/
   ├─ screen/
   │  ├─ FlashCalculatorScreen.kt
   │  └─ WheelPickerColumn.kt   // 当前实现为下拉选框组件
   └─ theme/
      ├─ Color.kt
      ├─ Theme.kt
      └─ Type.kt
```

## 图标与启动页

- 应用图标：像素风闪光灯
- 启动页：同风格图标，含安全边距，避免系统裁切

相关资源：
- `app/src/main/res/drawable/ic_retro_flash.xml`
- `app/src/main/res/drawable/ic_retro_flash_inset.xml`
- `app/src/main/res/drawable/ic_retro_flash_splash.xml`
- `app/src/main/res/values/themes.xml`

## 后续建议

- 增加横竖屏布局适配
- 增加 `m/ft` 单位切换
- 增加参数预设保存
