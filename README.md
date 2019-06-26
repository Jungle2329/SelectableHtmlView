# SelectableHtmlView
支持选中，富文本，图片点击的TextView

[![](https://jitpack.io/v/Jungle2329/SelectableHtmlView.svg)](https://jitpack.io/#Jungle2329/SelectableHtmlView)

#### 使用说明
Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
Step 2. Add the dependency

	dependencies {
	        implementation 'com.github.Jungle2329:SelectableHtmlView:v1.1'
	}
  
#### 实现的功能
- 实现读书类软件常用的标记+注释的功能  
```
长按触发标记状态 -> 滑动标记 -> 松手完成标记
点击已添加的标记 -> 添加对标记的注释
```
- 实现分屏操作，支持横竖屏切换


#### 处理的问题
- 处理了因ScrollView中控件获取到焦点导致ScrollView滚动的问题，直接表现为点击富文本中的图片，添加ClickSpan获取到焦点，导致自动滚动，而选中添加了注释的文字假如跟图片链接在一起的时候会导致点击文字也会出现滚动
- 添加标记功能支持标记组合，注释组合
