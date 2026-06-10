plugins {
	id("net.fabricmc.fabric-loom")
	`maven-publish`
}

val mod_id: String by properties
val mod_version: String by properties
val maven_group :String by properties

version = mod_version
group = maven_group

repositories {
	// Add repositories to retrieve artifacts from in here.
	// You should only use this when depending on other mods because
	// Loom adds the essential maven repositories to download Minecraft and libraries from automatically.
	// See https://docs.gradle.org/current/userguide/declaring_repositories.html
	// for more information about repositories.

	forGroup(libs.frappe.asProvider()) {
		name = "Sylv"
		url = uri("https://maven.sylv.gay/releases")
	}

	forGroup(libs.modmenu) {
		name = "TerraformersMC"
		url = uri("https://maven.terraformersmc.com/releases")
	}

	forGroup(libs.sodium) {
		name = "CaffeineMC"
		url = uri("https://maven.caffeinemc.net/releases")
	}
}

loom {
	splitEnvironmentSourceSets()

	mods {
		register(mod_id) {
			sourceSet(sourceSets["main"])
			sourceSet(sourceSets["client"])
		}
	}
}

fabricApi {
	configureDataGeneration {
		client = true
	}
}

dependencies {
	// Libraries
	minecraft(libs.minecraft)
	implementation(libs.fabric.loader)
	implementation(libs.fabric.api)
	implementation(libs.frappe.ext.terrain.material)

	// Runtime
	runtimeOnly(libs.frappe.asProvider())
	runtimeOnly(libs.sodium)
	runtimeOnly(libs.modmenu)
}

tasks.processResources {
	inputs.property("version", version)

	filesMatching("fabric.mod.json") {
		expand(
			"version" to version,
			"mod_id" to mod_id
		)
	}
}

tasks.withType<JavaCompile>().configureEach {
	options.release = 25
}

java {
	// Loom will automatically attach sourcesJar to a RemapSourcesJar task and to the "build" task
	// if it is present.
	// If you remove this line, sources will not be generated.
	withSourcesJar()

	sourceCompatibility = JavaVersion.VERSION_25
	targetCompatibility = JavaVersion.VERSION_25
}

tasks.jar {
	inputs.property("projectName", project.name)

	from("LICENSE") {
		rename { "${it}_${project.name}" }
	}
}

// configure the maven publication
publishing {
	publications {
		register<MavenPublication>("mavenJava") {
			from(components["java"])
		}
	}

	// See https://docs.gradle.org/current/userguide/publishing_maven.html for information on how to set up publishing.
	repositories {
		// Add repositories to publish to here.
		// Notice: This block does NOT have the same function as the block in the top level.
		// The repositories here will be used for publishing your artifact, not for
		// retrieving dependencies.
	}
}

fun RepositoryHandler.forMaven(
	groups: List<String?>? = null,
	modules: List<String?>? = null,
	inner: Action<MavenArtifactRepository>
) {
	exclusiveContent {
		forRepository {
			maven(inner)
		}

		filter {
			if (groups != null) {
				for ((i, element) in groups.withIndex()) {
					val group = element
					val module = modules?.get(i)

					if (group != null) {
						if (module != null) {
							includeModule(group, module)
						} else {
							includeGroup(group)
						}
					}
				}
			}
		}
	}
}

fun <T : ExternalDependency> RepositoryHandler.forGroup(vararg dependencies: Provider<T>, inner: Action<MavenArtifactRepository>) {
	val modules = dependencies.map { it.get().module }
	forMaven(
		groups = modules.map { it.group },
		inner = inner
	)
}

fun <T : ExternalDependency> RepositoryHandler.forModule(vararg dependencies: Provider<T>, inner: Action<MavenArtifactRepository>) {
	val modules = dependencies.map { it.get().module }
	forMaven(
		groups = modules.map { it.group },
		modules = modules.map { it.name },
		inner = inner
	)
}

/**
 * implementationInclude for modern Gradle
 */
fun DependencyHandler.implementationInclude(dependencyNotation: Any): Dependency? {
	val a = implementation(dependencyNotation)
	val b = include(dependencyNotation)
	if (a != b) throw AssertionError()
	return a
}

fun DependencyHandler.apiInclude(dependencyNotation: Any): Dependency? {
	val a = api(dependencyNotation)
	val b = include(dependencyNotation)
	if (a != b) throw AssertionError()
	return a
}
