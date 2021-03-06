import org.spongepowered.gradle.plugin.config.PluginLoaders
import org.spongepowered.plugin.metadata.PluginDependency
import java.util.Locale

plugins {
  id("carbon.shadow-platform")
  id("org.spongepowered.gradle.plugin")
}

dependencies {
  implementation(projects.carbonchatCommon)
  implementation(libs.cloudSponge)
  //implementation(libs.bstatsSponge) // not updated for api 8 yet
}

tasks {
  shadowJar {
    dependencies {
      // included in sponge
      exclude(dependency("io.leangen.geantyref:geantyref"))
      exclude(dependency("com.google.inject:guice"))
      exclude(dependency("aopalliance:aopalliance"))
      exclude(dependency("javax.inject:javax.inject"))
    }
  }
}

sponge {
  injectRepositories(false) // We specify repositories in settings.gradle.kts
  apiVersion("8.0.0")
  plugin(rootProject.name.toLowerCase(Locale.ROOT)) {
    loader(PluginLoaders.JAVA_PLAIN)
    displayName(rootProject.name)
    mainClass("net.draycia.carbon.sponge.CarbonChatSponge")
    description(project.description)
    links {
      homepage(GITHUB_REPO_URL)
      source(GITHUB_REPO_URL)
      issues("$GITHUB_REPO_URL/issues")
    }
    contributor("Vicarious") {
      description("Lead Developer")
    }
    contributor("Glare") {
      description("Moral Support")
    }
    dependency("spongeapi") {
      loadOrder(PluginDependency.LoadOrder.AFTER)
      optional(false)
    }
  }
}
