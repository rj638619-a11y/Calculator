# ===================================================================
# Optimized Production ProGuard & R8 Configuration
# ===================================================================

-dontusemixedcaseclassnames
-verbose

# Preserve essential annotations and attributes for Reflection, Serialization & Runtime
-keepattributes *Annotation*,Signature,InnerClasses,EnclosingMethod,SourceFile,LineNumberTable,RuntimeVisibleAnnotations,RuntimeVisibleParameterAnnotations,AnnotationDefault

# -------------------------------------------------------------------
# Android Core Components & Activities
# -------------------------------------------------------------------
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.app.backup.BackupAgent
-keep public class * extends android.app.job.JobService
-keep public class * extends androidx.fragment.app.Fragment
-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

# Preserve Keep annotations for reflection-based classes and methods
-keep @interface androidx.annotation.Keep
-keep @androidx.annotation.Keep class * { *; }
-keepclasseswithmembers class * {
    @androidx.annotation.Keep <fields>;
}
-keepclasseswithmembers class * {
    @androidx.annotation.Keep <methods>;
}

# -------------------------------------------------------------------
# Kotlin Enums (CRITICAL: Prevents crash in Enum.valueOf() / Theme / Tabs)
# -------------------------------------------------------------------
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
    public static ** entries;
    public ** name();
    public int ordinal();
    <fields>;
}
-keep enum com.example.** { *; }

# -------------------------------------------------------------------
# AndroidX Lifecycle, ViewModels & State
# -------------------------------------------------------------------
-keep class * extends androidx.lifecycle.ViewModel {
    <init>(...);
    public <init>(...);
}
-keep class * extends androidx.lifecycle.AndroidViewModel {
    <init>(...);
    public <init>(...);
}
-keep class * extends androidx.lifecycle.ViewModelProvider$Factory { *; }
-keep class com.example.ui.viewmodel.** {
    <init>(...);
    public <init>(...);
    *;
}

# -------------------------------------------------------------------
# Room Database & SQLite (CRITICAL: Reflection instantiates AppDatabase_Impl)
# -------------------------------------------------------------------
-keep class androidx.room.** { *; }
-dontwarn androidx.room.**
-keep class * extends androidx.room.RoomDatabase {
    <init>();
    *;
}
-keep class **.*_Impl {
    <init>();
    *;
}
-keep @androidx.room.Entity class * { *; }
-keepclassmembers @androidx.room.Entity class * { *; }
-keep @androidx.room.Dao interface * { *; }
-keepclassmembers @androidx.room.Dao interface * { *; }
-keep class * extends androidx.sqlite.db.SupportSQLiteOpenHelper$Factory {
    <init>(...);
}
-keep class com.example.data.local.** { *; }

# -------------------------------------------------------------------
# Retrofit, OkHttp & Moshi (Network & JSON Parsing)
# -------------------------------------------------------------------
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

-dontwarn okhttp3.**
-dontwarn okio.**
-keep class okhttp3.** { *; }

-dontwarn com.squareup.moshi.**
-keep class com.squareup.moshi.** { *; }
-keep class * implements com.squareup.moshi.JsonAdapter { *; }
-keep class * extends com.squareup.moshi.JsonAdapter {
    <init>(...);
    *;
}
-keepclassmembers class * {
    @com.squareup.moshi.Json <fields>;
    @com.squareup.moshi.JsonClass <fields>;
}
-keep @com.squareup.moshi.JsonClass class * { *; }
-keep class com.example.data.remote.** { *; }

# -------------------------------------------------------------------
# Kotlin Coroutines & Main Dispatcher (CRITICAL for ServiceLoader)
# -------------------------------------------------------------------
-keepclassmembers class kotlinx.coroutines.** {
    volatile <fields>;
}
-keep class kotlinx.coroutines.android.AndroidDispatcherFactory { *; }
-keep class kotlinx.coroutines.android.AndroidExceptionPreHandler { *; }
-keep class kotlinx.coroutines.internal.MainDispatcherFactory { *; }
-keep class * implements kotlinx.coroutines.internal.MainDispatcherFactory { *; }
-dontwarn kotlinx.coroutines.**
-dontwarn kotlin.reflect.jvm.internal.**

# -------------------------------------------------------------------
# Jetpack Compose & Material 3
# -------------------------------------------------------------------
-keepclassmembers class androidx.compose.ui.** {
    public <methods>;
}
-keep class androidx.compose.material3.** { *; }
-keep class androidx.compose.material.** { *; }
-keep class androidx.compose.animation.** { *; }
-keep class androidx.compose.runtime.** { *; }
-dontwarn androidx.compose.**

# -------------------------------------------------------------------
# App Engine, Calculators, State & Theme Models
# -------------------------------------------------------------------
-keep class com.example.engine.** { *; }
-keep class com.example.ui.theme.** { *; }
-keep class com.example.ui.components.** { *; }

# -------------------------------------------------------------------
# DataStore & Preferences
# -------------------------------------------------------------------
-keep class androidx.datastore.** { *; }

# -------------------------------------------------------------------
# Gson & Kotlinx Serialization
# -------------------------------------------------------------------
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}
-keepclassmembers class * {
    @kotlinx.serialization.Serializable <fields>;
}

# -------------------------------------------------------------------
# WorkManager, Notifications, Widgets, Broadcast Receivers & Services
# -------------------------------------------------------------------
-keep class * extends androidx.work.ListenableWorker {
    public <init>(android.content.Context, androidx.work.WorkerParameters);
}
-keep class * extends android.appwidget.AppWidgetProvider

# -------------------------------------------------------------------
# Firebase & Google Services
# -------------------------------------------------------------------
-keep class com.google.firebase.** { *; }
-keep class com.google.android.gms.** { *; }
-dontwarn com.google.firebase.**
-dontwarn com.google.android.gms.**

# -------------------------------------------------------------------
# WebView & JavaScriptInterface
# -------------------------------------------------------------------
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

# -------------------------------------------------------------------
# Biometric Authentication
# -------------------------------------------------------------------
-keep class androidx.biometric.** { *; }


