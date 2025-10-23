/**
 * Precompiled [com.tteoli.android.library.gradle.kts][Com_tteoli_android_library_gradle] script plugin.
 *
 * @see Com_tteoli_android_library_gradle
 */
public
class Com_tteoli_android_libraryPlugin : org.gradle.api.Plugin<org.gradle.api.Project> {
    override fun apply(target: org.gradle.api.Project) {
        try {
            Class
                .forName("Com_tteoli_android_library_gradle")
                .getDeclaredConstructor(org.gradle.api.Project::class.java, org.gradle.api.Project::class.java)
                .newInstance(target, target)
        } catch (e: java.lang.reflect.InvocationTargetException) {
            throw e.targetException
        }
    }
}
