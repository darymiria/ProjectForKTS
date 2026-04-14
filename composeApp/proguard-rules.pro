
-keep class io.ktor.** { *; }
-keep class kotlinx.serialization.** { *; }

-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *

-keep class org.koin.** { *; }

-keep class net.openid.appauth.** { *; }

-keep class io.github.aakira.napier.** { *; }

-keep class androidx.datastore.** { *; }

-dontwarn java.lang.management.ManagementFactory
-dontwarn java.lang.management.RuntimeMXBean