-keep class com.liner_exe.smartcart.** { *; }

-keep class * extends androidx.fragment.app.Fragment { *; }
-keep public class * extends androidx.activity.ComponentActivity
-keep public class * extends androidx.lifecycle.ViewModel

-keep class androidx.navigation.** { *; }
-keep interface androidx.navigation.** { *; }
-keep class * extends androidx.navigation.Navigator
-keep class * extends androidx.navigation.NavArgs
-keep class * implements java.io.Serializable

-keep class * extends androidx.room.RoomDatabase
-dontwarn androidx.room.**

-keep class com.liner_exe.data.local.entities.** { *; }

-keep class * extends androidx.databinding.ViewDataBinding
-dontwarn androidx.databinding.**
-keep class **.databinding.** { *; }