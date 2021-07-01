import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import net.kyori.indra.git.IndraGitExtension
import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.Project
import org.gradle.kotlin.dsl.the

/**
 * Relocate a package into the `net.draycia.carbon.libs` namespace.
 */
fun ShadowJar.relocateDependency(pkg: String) {
  relocate(pkg, "net.draycia.carbon.libs.$pkg")
}

/**
 * Relocates dependencies which we bundle and relocate on all platforms.
 */
fun ShadowJar.standardRelocations() {
  relocateDependency("org.bstats")
  relocateDependency("net.kyori.adventure.text.minimessage")
  relocateDependency("net.kyori.adventure.serializer.configurate4")
  relocateDependency("net.kyori.event")
  relocateDependency("net.kyori.registry")
  relocateDependency("cloud.commandframework")
  relocateDependency("org.spongepowered.configurate")
  relocateDependency("com.typesafe.config")
  relocateDependency("com.google.common")
  relocateDependency("com.google.thirdparty.publicsuffix")
  relocateDependency("com.proximyst.moonshine")
  relocateDependency("it.unimi.dsi.fastutil")
}

fun ShadowJar.configureShadowJar() {
  minimize()
  standardRelocations()
  dependencies {
    // not needed at runtime
    exclude(dependency("com.google.code.findbugs:jsr305"))
    exclude(dependency("org.checkerframework:checker-qual"))
    exclude(dependency("com.google.errorprone:error_prone_annotations"))
    exclude(dependency("com.google.j2objc:j2objc-annotations"))
  }
}

fun Project.latestGitHash(): String? =
  the<IndraGitExtension>().commit()?.name?.substring(0, 7)

val Project.libs: LibrariesForLibs
  get() = the()
