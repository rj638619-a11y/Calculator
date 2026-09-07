# ===================================================================
# Optimized Production ProGuard & R8 Configuration
# ===================================================================

# Optimization & Obfuscation Settings
-repackageclasses ''
-allowaccessmodification
-dontusemixedcaseclassnames
-verbose

# Preserve essential annotations and attributes
-keepattributes *Annotation*,Signature,InnerClasses,EnclosingMethod,SourceFile,LineNumberTable

# -------------------------------------------------------------------
# Kotlin Coroutines & Standard Library
# -------------------------------------------------------------------
-keepclassmembers class kotlinx.coroutines.** {
    volatile <fields>;
}
-dontwarn kotlinx.coroutines.**
-dontwarn kotlin.Unit

# -------------------------------------------------------------------
# Jetpack Compose & Material 3
# -------------------------------------------------------------------
-keepclassmembers class androidx.compose.ui.** {
    public <methods>;
}
-dontwarn androidx.compose.**

# -------------------------------------------------------------------
# AndroidX Lifecycle, ViewModel, Navigation
# -------------------------------------------------------------------
-keepclassmembers class * extends androidx.lifecycle.ViewModel {
    <init>(...);
}
-keep class * extends androidx.lifecycle.ViewModel
-dontwarn androidx.lifecycle.**
-dontwarn androidx.navigation.**

# -------------------------------------------------------------------
# Room Database & SQLite
# -------------------------------------------------------------------
-keep class * extends androidx.room.RoomDatabase
-dontwarn androidx.room.paging.**
-keepclassmembers class * extends androidx.room.RoomDatabase {
    <init>();
}
-keep @androidx.room.Entity class * {
    <fields>;
    <init>(...);
}
-keep @androidx.room.Dao interface * {
    <methods>;
}
-keep class * extends androidx.sqlite.db.SupportSQLiteOpenHelper$Factory {
    <init>(...);
}

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
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

# Moshi JSON Models & Adapters
-keepattributes *Annotation*, Signature
-keepclassmembers class * {
    @com.squareup.moshi.Json <fields>;
    @com.squareup.moshi.JsonQualifier <fields>;
}
-keep @com.squareup.moshi.JsonClass class * {
    <fields>;
    <init>(...);
}
-keep class * extends com.squareup.moshi.JsonAdapter {
    public <init>(...);
}

# -------------------------------------------------------------------
# App Models & Data Classes (Engine, DB, & Network DTOs)
# -------------------------------------------------------------------
-keep class com.example.data.** { *; }
-keep class com.example.model.** { *; }
-keep class com.example.engine.** { *; }
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# -------------------------------------------------------------------
# Firebase & Google Services
# -------------------------------------------------------------------
-keep class com.google.firebase.** { *; }
-dontwarn com.google.firebase.**
-keep class com.google.android.gms.** { *; }
-dontwarn com.google.android.gms.**

# -------------------------------------------------------------------
# Generic Android Components (Activity, Services, Receivers)
# -------------------------------------------------------------------
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgent

# Custom Views & Reflection
-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}
-keepclassmembers class * extends android.view.View {
    public void set*(...);
    public int get*();
    public boolean is*();
}

# WebView JavaScript Interface
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

# Keep Parcelable CREATORs
-keepclassmembers class * implements android.os.Parcelable {
    public static final ** CREATOR;
}
