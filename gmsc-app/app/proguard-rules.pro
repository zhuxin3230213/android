# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

#不混淆规则
#一般以下情况都会不混淆：
 #1.使用了自定义控件那么要保证它们不参与混淆
 #2.使用了枚举要保证枚举不被混淆
 #3.对第三方库中的类不进行混淆
 #4.运用了反射的类也不进行混淆
# 5.使用了 Gson 之类的工具要使 JavaBean 类即实体类不被混淆
# 6.在引用第三方库的时候，一般会标明库的混淆规则的，建议在使用的时候就把混淆规则添加上去，免得到最后才去找
# 7.有用到 WebView 的 JS 调用也需要保证写的接口方法不混淆，原因和第一条一样
# 8.Parcelable 的子类和 Creator 静态成员变量不混淆，否则会产生 Android.os.BadParcelableException 异常

## 代码混淆压缩比，在0~7之间，默认为5，一般不做修改
#-optimizationpasses 5
#
## 混合时不使用大小写混合，混合后的类名为小写
#-dontusemixedcaseclassnames
#
## 这句话能够使我们的项目混淆后产生映射文件
## 包含有类名->混淆后类名的映射关系
#-verbose
#
##--------------------------1.实体类---------------------------------
## 如果使用了Gson之类的工具要使被它解析的JavaBean类即实体类不被混淆。（这里填写自己项目中存放bean对象的具体路径）
#-keep class cn.gmuni.sc.model.entities.**{*;}
#
##--------------------------2.第三方包-------------------------------
#
##Gson
#-keepattributes Signature
#-keepattributes *Annotation*
#-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }
#-keep class com.google.gson.examples.android.model.** { *; }
#-keep class com.google.gson.* { *;}
#-dontwarn com.google.gson.**
#
##butterknife
#-keep class butterknife.** { *; }
#-dontwarn butterknife.internal.**
#-keep class **$$ViewBinder { *; }
#
#
##友盟推送
#-dontwarn com.umeng.**
#-dontwarn com.taobao.**
#-dontwarn anet.channel.**
#-dontwarn anetwork.channel.**
#-dontwarn org.android.**
#-dontwarn org.apache.thrift.**
#-dontwarn com.xiaomi.**
#-dontwarn com.huawei.**
#-dontwarn com.meizu.**
#
#-keepattributes *Annotation*
#
#-keep class com.taobao.** {*;}
#-keep class org.android.** {*;}
#-keep class anet.channel.** {*;}
#-keep class com.umeng.** {*;}
#-keep class com.xiaomi.** {*;}
#-keep class com.huawei.** {*;}
#-keep class com.meizu.** {*;}
#-keep class org.apache.thrift.** {*;}
#
#-keep class com.alibaba.sdk.android.**{*;}
#-keep class com.ut.**{*;}
#-keep class com.ta.**{*;}
#
#-keep public class **.R$*{
#   public static final int *;
#}
#
## 不混淆R文件中的所有静态字段，我们都知道R文件是通过字段来记录每个资源的id的，字段名要是被混淆了，id也就找不着了。
#-keepclassmembers class **.R$* {
#    public static <fields>;
#}
#
##如果引用了v4或者v7包
#-dontwarn android.support.**
#
## ============忽略警告，否则打包可能会不成功=============
#-ignorewarnings
#
#
## 不做预校验，preverify是proguard的四个步骤之一，Android不需要preverify，去掉这一步能够加快混淆速度。
#-dontpreverify
#
## 保留Annotation不混淆
#-keepattributes *Annotation*,InnerClasses
#
## 避免混淆泛型
#-keepattributes Signature
#
## 抛出异常时保留代码行号
#-keepattributes SourceFile,LineNumberTable
#
## 指定混淆是采用的算法，后面的参数是一个过滤器
## 这个过滤器是谷歌推荐的算法，一般不做更改
#-optimizations !code/simplification/cast,!field/*,!class/merging/*
#
#
##############################################
##
## Android开发中一些需要保留的公共部分
##
##############################################
#
## 保留我们使用的四大组件，自定义的Application等等这些类不被混淆
## 因为这些子类都有可能被外部调用
#-keep public class * extends android.app.Activity
#-keep public class * extends android.app.Appliction
#-keep public class * extends android.app.Service
#-keep public class * extends android.content.BroadcastReceiver
#-keep public class * extends android.content.ContentProvider
#-keep public class * extends android.app.backup.BackupAgentHelper
#-keep public class * extends android.preference.Preference
#-keep public class * extends android.view.View
#-keep public class com.android.vending.licensing.ILicensingService
#
#
#
## 保留继承的
#-keep public class * extends android.support.v4.**
#-keep public class * extends android.support.v7.**
#-keep public class * extends android.support.annotation.**
#
#
## 对于带有回调函数的onXXEvent、**On*Listener的，不能被混淆
#-keepclassmembers class * {
#    void *(**On*Event);
#    void *(**On*Listener);
#}
#
## webView处理，项目中没有使用到webView忽略即可
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#    public *;
#}
#-keepclassmembers class * extends android.webkit.webViewClient {
#    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
#    public boolean *(android.webkit.WebView, java.lang.String);
#}
#-keepclassmembers class * extends android.webkit.webViewClient {
#    public void *(android.webkit.webView, jav.lang.String);
#}
#
## 保留本地native方法不被混淆
#-keepclasseswithmembernames class * {
#    native <methods>;
#}
#
## 保留在Activity中的方法参数是view的方法，
## 这样以来我们在layout中写的onClick就不会被影响
#-keepclassmembers class * extends android.app.Activity{
#    public void *(android.view.View);
#}
#
## 保留枚举类不被混淆
#-keepclassmembers enum * {
#    public static **[] values();
#    public static ** valueOf(java.lang.String);
#}
#
## 保留我们自定义控件（继承自View）不被混淆
#-keep public class * extends android.view.View{
#    *** get*();
#    void set*(***);
#    public <init>(android.content.Context);
#    public <init>(android.content.Context, android.util.AttributeSet);
#    public <init>(android.content.Context, android.util.AttributeSet, int);
#}
#
## 保留Parcelable序列化类不被混淆
#-keep class * implements android.os.Parcelable {
#    public static final android.os.Parcelable$Creator *;
#}
#
## 保留Serializable序列化的类不被混淆
#-keepclassmembers class * implements java.io.Serializable {
#    static final long serialVersionUID;
#    private static final java.io.ObjectStreamField[] serialPersistentFields;
#    !static !transient <fields>;
#    !private <fields>;
#    !private <methods>;
#    private void writeObject(java.io.ObjectOutputStream);
#    private void readObject(java.io.ObjectInputStream);
#    java.lang.Object writeReplace();
#    java.lang.Object readResolve();
#}
#
## Retrofit
#-dontnote retrofit2.Platform
#-dontnote retrofit2.Platform$IOS$MainThreadExecutor
#-dontwarn retrofit2.Platform$Java8
#-keepattributes Signature
#-keepattributes Exceptions
#
#
##okhttp
#-dontwarn okhttp3.**
#-keep class okhttp3.**{*;}
#
##okio
#-dontwarn okio.**
#-keep class okio.**{*;}
#
## 代码混淆压缩比，在0~7之间，默认为5，一般不做修改
#-optimizationpasses 5
#
## 混合时不使用大小写混合，混合后的类名为小写
#-dontusemixedcaseclassnames
#
## 这句话能够使我们的项目混淆后产生映射文件
## 包含有类名->混淆后类名的映射关系
#-verbose
#
##--------------------------1.实体类---------------------------------
## 如果使用了Gson之类的工具要使被它解析的JavaBean类即实体类不被混淆。（这里填写自己项目中存放bean对象的具体路径）
#-keep class cn.gmuni.sc.model.entities.**{*;}
#
##--------------------------2.第三方包-------------------------------
#
##Gson
#-keepattributes Signature
#-keepattributes *Annotation*
#-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }
#-keep class com.google.gson.examples.android.model.** { *; }
#-keep class com.google.gson.* { *;}
#-dontwarn com.google.gson.**
#
##butterknife
#-keep class butterknife.** { *; }
#-dontwarn butterknife.internal.**
#-keep class **$$ViewBinder { *; }
#
#
##友盟推送
#-dontwarn com.umeng.**
#-dontwarn com.taobao.**
#-dontwarn anet.channel.**
#-dontwarn anetwork.channel.**
#-dontwarn org.android.**
#-dontwarn org.apache.thrift.**
#-dontwarn com.xiaomi.**
#-dontwarn com.huawei.**
#-dontwarn com.meizu.**
#
#-keepattributes *Annotation*
#
#-keep class com.taobao.** {*;}
#-keep class org.android.** {*;}
#-keep class anet.channel.** {*;}
#-keep class com.umeng.** {*;}
#-keep class com.xiaomi.** {*;}
#-keep class com.huawei.** {*;}
#-keep class com.meizu.** {*;}
#-keep class org.apache.thrift.** {*;}
#
#-keep class com.alibaba.sdk.android.**{*;}
#-keep class com.ut.**{*;}
#-keep class com.ta.**{*;}
#
#-keep public class **.R$*{
#   public static final int *;
#}
#
## 不混淆R文件中的所有静态字段，我们都知道R文件是通过字段来记录每个资源的id的，字段名要是被混淆了，id也就找不着了。
#-keepclassmembers class **.R$* {
#    public static <fields>;
#}
#
##如果引用了v4或者v7包
#-dontwarn android.support.**
#
## ============忽略警告，否则打包可能会不成功=============
#-ignorewarnings
#
#
## 不做预校验，preverify是proguard的四个步骤之一，Android不需要preverify，去掉这一步能够加快混淆速度。
#-dontpreverify
#
## 保留Annotation不混淆
#-keepattributes *Annotation*,InnerClasses
#
## 避免混淆泛型
#-keepattributes Signature
#
## 抛出异常时保留代码行号
#-keepattributes SourceFile,LineNumberTable
#
## 指定混淆是采用的算法，后面的参数是一个过滤器
## 这个过滤器是谷歌推荐的算法，一般不做更改
#-optimizations !code/simplification/cast,!field/*,!class/merging/*
#
#
##############################################
##
## Android开发中一些需要保留的公共部分
##
##############################################
#
## 保留我们使用的四大组件，自定义的Application等等这些类不被混淆
## 因为这些子类都有可能被外部调用
#-keep public class * extends android.app.Activity
#-keep public class * extends android.app.Appliction
#-keep public class * extends android.app.Service
#-keep public class * extends android.content.BroadcastReceiver
#-keep public class * extends android.content.ContentProvider
#-keep public class * extends android.app.backup.BackupAgentHelper
#-keep public class * extends android.preference.Preference
#-keep public class * extends android.view.View
#-keep public class com.android.vending.licensing.ILicensingService
#
#
#
## 保留继承的
#-keep public class * extends android.support.v4.**
#-keep public class * extends android.support.v7.**
#-keep public class * extends android.support.annotation.**
#
#
## 对于带有回调函数的onXXEvent、**On*Listener的，不能被混淆
#-keepclassmembers class * {
#    void *(**On*Event);
#    void *(**On*Listener);
#}
#
## webView处理，项目中没有使用到webView忽略即可
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#    public *;
#}
#-keepclassmembers class * extends android.webkit.webViewClient {
#    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
#    public boolean *(android.webkit.WebView, java.lang.String);
#}
#-keepclassmembers class * extends android.webkit.webViewClient {
#    public void *(android.webkit.webView, jav.lang.String);
#}
#
## 保留本地native方法不被混淆
#-keepclasseswithmembernames class * {
#    native <methods>;
#}
#
## 保留在Activity中的方法参数是view的方法，
## 这样以来我们在layout中写的onClick就不会被影响
#-keepclassmembers class * extends android.app.Activity{
#    public void *(android.view.View);
#}
#
## 保留枚举类不被混淆
#-keepclassmembers enum * {
#    public static **[] values();
#    public static ** valueOf(java.lang.String);
#}
#
## 保留我们自定义控件（继承自View）不被混淆
#-keep public class * extends android.view.View{
#    *** get*();
#    void set*(***);
#    public <init>(android.content.Context);
#    public <init>(android.content.Context, android.util.AttributeSet);
#    public <init>(android.content.Context, android.util.AttributeSet, int);
#}
#
## 保留Parcelable序列化类不被混淆
#-keep class * implements android.os.Parcelable {
#    public static final android.os.Parcelable$Creator *;
#}
#
## 保留Serializable序列化的类不被混淆
#-keepclassmembers class * implements java.io.Serializable {
#    static final long serialVersionUID;
#    private static final java.io.ObjectStreamField[] serialPersistentFields;
#    !static !transient <fields>;
#    !private <fields>;
#    !private <methods>;
#    private void writeObject(java.io.ObjectOutputStream);
#    private void readObject(java.io.ObjectInputStream);
#    java.lang.Object writeReplace();
#    java.lang.Object readResolve();
#}
#
## Retrofit
#-dontnote retrofit2.Platform
#-dontnote retrofit2.Platform$IOS$MainThreadExecutor
#-dontwarn retrofit2.Platform$Java8
#-keepattributes Signature
#-keepattributes Exceptions
#
#
##okhttp
#-dontwarn okhttp3.**
#-keep class okhttp3.**{*;}
#
##okio
#-dontwarn okio.**
#-keep class okio.**{*;}
#
