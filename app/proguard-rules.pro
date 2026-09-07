# ===================================================================
# Optimized Production ProGuard & R8 Configuration
# ===================================================================

# Optimization & Obfuscation Settings
-dontusemixedcaseclassnames
-verbose

# Preserve essential annotations and attributes for Reflection, Serialization & Runtime
-keepattributes *Annotation*,Signature,InnerClasses,EnclosingMethod,SourceFile,LineNumberTable,RuntimeVisibleAnnotations,RuntimeVisibleParameterAnnotations,AnnotationDefault

# -------------------------------------------------------------------
# Core Android Application Components
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
# Jetpack Compose & Material Components
# -------------------------------------------------------------------
-keepclassmembers class androidx.compose.ui.** {
    public <methods>;
}
-keep class androidx.compose.material3.** { *; }
-keep class androidx.compose.material.** { *; }
-dontwarn androidx.compose.**

# -------------------------------------------------------------------
# Navigation & ViewModel Lifecycle
# -------------------------------------------------------------------
-keep class * extends androidx.lifecycle.ViewModel {
    public <init>(...);
}
-keep class * extends androidx.lifecycle.AndroidViewModel {
    public <init>(...);
}
-keep class androidx.navigation.** { *; }

# -------------------------------------------------------------------
# Room Database & SQLite
# -------------------------------------------------------------------
-keep class * extends androidx.room.RoomDatabase
-keepclassmembers class * extends androidx.room.RoomDatabase {
    <init>();
    *;
}
-keep class **.*_Impl {
    public <init>();
    *;
}
-keep @androidx.room.Entity class * { *; }
-keep @androidx.room.Dao interface * { *; }
-keep class * extends androidx.sqlite.db.SupportSQLiteOpenHelper$Factory {
    <init>(...);
}

# -------------------------------------------------------------------
# DataStore & Preferences
# -------------------------------------------------------------------
-keep class androidx.datastore.** { *; }

# -------------------------------------------------------------------
# Retrofit, OkHttp & Moshi / Gson / Kotlin Serialization
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
-keepclassmembers class * {
    @com.squareup.moshi.Json <fields>;
}

-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

-keepattributes *Annotation*, ElementType, RetentionPolicy
-keepclassmembers class * {
    @kotlinx.serialization.Serializable <fields>;
}

# Preserve app data models used by JSON parsing and DB persistence
-keepclassmembers class com.example.data.remote.** {
    <fields>;
    <methods>;
}
-keepclassmembers class com.example.data.local.** {
    <fields>;
    <methods>;
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

# -------------------------------------------------------------------
# Kotlin Coroutines & Reflection
# -------------------------------------------------------------------
-keepclassmembers class kotlinx.coroutines.** {
    volatile <fields>;
}
-dontwarn kotlinx.coroutines.**
-keepclassmembers class kotlin.reflect.jvm.internal.** { *; }

