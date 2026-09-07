# ===================================================================
# Optimized Production ProGuard & R8 Configuration
# ===================================================================

# Optimization & Obfuscation Settings
-dontusemixedcaseclassnames
-verbose

# Preserve essential annotations and attributes
-keepattributes *Annotation*,Signature,InnerClasses,EnclosingMethod,SourceFile,LineNumberTable,RuntimeVisibleAnnotations,RuntimeVisibleParameterAnnotations,AnnotationDefault

# -------------------------------------------------------------------
# Keep All Application Classes & Members (ViewModels, Entities, UI, Engine)
# -------------------------------------------------------------------
-keep class com.example.** { *; }
-keepclassmembers class com.example.** { *; }

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
# AndroidX Lifecycle, ViewModel, Navigation
# -------------------------------------------------------------------
-keep class * extends androidx.lifecycle.ViewModel {
    public <init>(...);
}
-keep class * extends androidx.lifecycle.AndroidViewModel {
    public <init>(...);
}
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application

# -------------------------------------------------------------------
# Kotlin Coroutines & Standard Library
# -------------------------------------------------------------------
-keepclassmembers class kotlinx.coroutines.** {
    volatile <fields>;
}
-dontwarn kotlinx.coroutines.**

# -------------------------------------------------------------------
# Jetpack Compose & Material 3
# -------------------------------------------------------------------
-keepclassmembers class androidx.compose.ui.** {
    public <methods>;
}
-dontwarn androidx.compose.**

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
-keepclassmembers class * {
    @com.squareup.moshi.Json <fields>;
}
