[33mcommit 766eea534b827be9a3cb54b369d5a1bd26c6e498[m[33m ([m[1;36mHEAD -> [m[1;32mhearts[m[33m)[m
Author: eliot shea <eliot@colbatic.com>
Date:   Mon Oct 23 18:42:26 2023 -0400

    Oh god i hope this works. Added emoji parsing

[1mdiff --git a/.gitignore b/.gitignore[m
[1mindex de6cdcf..4726e2c 100755[m
[1m--- a/.gitignore[m
[1m+++ b/.gitignore[m
[36m@@ -32,3 +32,6 @@[m [mworkspace/[m
 [m
 # macOS[m
 *.DS_Store[m
[32m+[m
[32m+[m[32m# examples[m
[32m+[m[32mexample.toml[m
[1mdiff --git a/build.gradle.kts b/build.gradle.kts[m
[1mindex bde32e2..36d9aaa 100755[m
[1m--- a/build.gradle.kts[m
[1m+++ b/build.gradle.kts[m
[36m@@ -24,6 +24,7 @@[m
 	// for more information about repositories.[m
 	maven("https://oss.sonatype.org/content/repositories/snapshots")[m
 	maven("https://s01.oss.sonatype.org/content/repositories/snapshots")[m
[32m+[m	[32mmaven("https://repo1.maven.org/maven2/")[m
 	repositories {[m
 		exclusiveContent {[m
 			forRepository {[m
[36m@@ -37,6 +38,7 @@[m
 			}[m
 		}[m
 	}[m
[32m+[m
 	maven("https://maven.nucleoid.xyz/") { name = "Nucleoid" }[m
 }[m
 [m
[36m@@ -82,6 +84,11 @@[m
 	modImplementation(libs.placeholder.api) {[m
 		exclude(group = "net.fabricmc")[m
 	}[m
[32m+[m
[32m+[m	[32minclude(libs.emoji)[m
[32m+[m	[32mmodImplementation(libs.emoji){[m
[32m+[m
[32m+[m	[32m}[m
 }[m
 [m
 val includeBlacklist = setOf<String>([m
[1mdiff --git a/filetree.txt b/filetree.txt[m
[1mnew file mode 100644[m
[1mindex 0000000..64ff128[m
[1m--- /dev/null[m
[1m+++ b/filetree.txt[m
[36m@@ -0,0 +1,61 @@[m
[32m+[m[32m.[m
[32m+[m[32m├── build.gradle.kts[m
[32m+[m[32m├── filetree.txt[m
[32m+[m[32m├── gradle[m
[32m+[m[32m│   ├── libs.versions.toml[m
[32m+[m[32m│   └── wrapper[m
[32m+[m[32m│       ├── gradle-wrapper.jar[m
[32m+[m[32m│       └── gradle-wrapper.properties[m
[32m+[m[32m├── gradle.properties[m
[32m+[m[32m├── gradlew[m
[32m+[m[32m├── gradlew.bat[m
[32m+[m[32m├── LICENSE.md[m
[32m+[m[32m├── README.md[m
[32m+[m[32m├── settings.gradle.kts[m
[32m+[m[32m└── src[m
[32m+[m[32m    └── main[m
[32m+[m[32m        ├── java[m
[32m+[m[32m        │   └── org[m
[32m+[m[32m        │       └── ecorous[m
[32m+[m[32m        │           └── polyhopper[m
[32m+[m[32m        │               ├── JavaUtil.java[m
[32m+[m[32m        │               ├── mixin[m
[32m+[m[32m        │               │   ├── FabricTailorWackCompatMixin.java[m
[32m+[m[32m        │               │   ├── PlayerAdvancementMixin.java[m
[32m+[m[32m        │               │   ├── PlayerDeathMixin.java[m
[32m+[m[32m        │               │   ├── PlayerJoinAndCommandMixin.java[m
[32m+[m[32m        │               │   ├── PlayerLeaveAndChatMessageMixin.java[m
[32m+[m[32m        │               │   └── TellRawMixin.java[m
[32m+[m[32m        │               └── PolyHopperMixinPlugin.java[m
[32m+[m[32m        ├── kotlin	# discord-end chat bridge bot?[m
[32m+[m[32m        │   └── org[m
[32m+[m[32m        │       └── ecorous[m
[32m+[m[32m        │           └── polyhopper[m
[32m+[m[32m        │               ├── compat[m
[32m+[m[32m        │               │   └── fabrictailor[m
[32m+[m[32m        │               │       ├── FabricTailorContextFactory.kt[m
[32m+[m[32m        │               │       └── SkinHolder.kt[m
[32m+[m[32m        │               ├── config[m
[32m+[m[32m        │               │   ├── Config.kt[m
[32m+[m[32m        │               │   └── MessageMode.kt[m
[32m+[m[32m        │               ├── DiscordCommandOutput.kt[m
[32m+[m[32m        │               ├── extensions[m
[32m+[m[32m        │               │   └── MainExtension.kt[m
[32m+[m[32m        │               ├── helpers[m
[32m+[m[32m        │               │   ├── ChatCommandContextFactory.kt[m
[32m+[m[32m        │               │   ├── ChatCommandContext.kt[m
[32m+[m[32m        │               │   ├── DiscordMessageSender.kt[m
[32m+[m[32m        │               │   └── VanillaContextFactory.kt[m
[32m+[m[32m        │               ├── HopperBot.kt[m
[32m+[m[32m        │               ├── LinkedAccounts.kt[m
[32m+[m[32m        │               ├── MessageHooks.kt[m
[32m+[m[32m        │               ├── PolyHopper.kt[m
[32m+[m[32m        │               └── Utils.kt[m
[32m+[m[32m        └── resources[m
[32m+[m[32m            ├── assets[m
[32m+[m[32m            │   └── polyhopper[m
[32m+[m[32m            │       └── icon.png[m
[32m+[m[32m            ├── polyhopper.mixins.json[m
[32m+[m[32m            └── quilt.mod.json[m
[32m+[m
[32m+[m[32m22 directories, 37 files[m
[1mdiff --git a/gradle/libs.versions.toml b/gradle/libs.versions.toml[m
[1mindex 6c6005e..540c1e9 100755[m
[1m--- a/gradle/libs.versions.toml[m
[1m+++ b/gradle/libs.versions.toml[m
[36m@@ -25,6 +25,9 @@[m [mquilt_mappings = { module = "org.quiltmc:quilt-mappings", version.ref = "mapping[m
 fabric_tailor = { group = "maven.modrinth", name = "fabrictailor", version = "2.1.2" }[m
 placeholder_api = { module = "eu.pb4:placeholder-api", version.ref="placeholder_api"}[m
 [m
[32m+[m[32memoji = { module = "com.vdurmont:emoji-java", version = "5.1.1"}[m
[32m+[m[41m	[m
[32m+[m
 [bundles][m
 quilted_fabric_api = ["qfapi", "qfapi_deprecated"][m
 [m
[1mdiff --git a/src/main/kotlin/org/ecorous/polyhopper/Utils.kt b/src/main/kotlin/org/ecorous/polyhopper/Utils.kt[m
[1mindex f2531a1..bf63a8c 100755[m
[1m--- a/src/main/kotlin/org/ecorous/polyhopper/Utils.kt[m
[1m+++ b/src/main/kotlin/org/ecorous/polyhopper/Utils.kt[m
[36m@@ -12,6 +12,8 @@[m
 import org.quiltmc.qkl.library.text.buildText[m
 import java.util.*[m
 [m
[32m+[m[32mimport com.vdurmont.emoji.*[m
[32m+[m
 object Utils {[m
 [m
     private const val OBFUSCATION_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890"[m
[36m@@ -70,6 +72,7 @@[m [mfun discordMessageToMinecraftText(message: String): Text {[m
             val userMentionPattern = """(<@!?([0-9]{16,20})>)""".toRegex()[m
             val channelMentionPattern = """(<#([0-9]{16,20})>)""".toRegex()[m
             val emojiMentionPattern = """(<a?:([a-zA-Z]{2,32}):[0-9]{16,20}>)""".toRegex()[m
[32m+[m
             for (match in userMentionPattern.findAll(message)) {[m
                 val value = match.value[m
                 val id = Snowflake(value.replace("<@", "").replace(">", ""))[m
[36m@@ -79,6 +82,7 @@[m [mfun discordMessageToMinecraftText(message: String): Text {[m
 [m
                 messageResult = messageResult.replace(value, "<gold><hover:show_text:'$id'>$username</hover></gold>")[m
             }[m
[32m+[m
             for (match in channelMentionPattern.findAll(message)) {[m
                 val value = match.value[m
                 val id = Snowflake(value.replace("<#", "").replace(">", ""))[m
[36m@@ -93,11 +97,13 @@[m [mfun discordMessageToMinecraftText(message: String): Text {[m
                 messageResult =[m
                     messageResult.replace(value, "t<gold><hover:show_text:'$hoverText'>#$channelName</hover></gold>".trimIndent().trim())[m
             }[m
[32m+[m
             for (match in emojiMentionPattern.findAll(message)) {[m
                 val name = match.value.substringAfter(":").substringBefore(":")[m
                 val id = match.value.substringAfterLast(":").replace(">", "")[m
                 messageResult = messageResult.replace(match.value, "<gold><hover:show_text:'$id'>:$name:</hover></gold>")[m
             }[m
[32m+[m
             result = PARSER.parseText(messageResult, PARSER_CONTEXT)[m
         }[m
         return result[m
[36m@@ -123,7 +129,7 @@[m [mfun minecraftTextToDiscordMessage(message: Text): String {[m
             return@visit Optional.empty<String>()[m
         }, Style.EMPTY)[m
 [m
[31m-        return builder.toString()[m
[32m+[m[32m        return EmojiParser.parseToAliases(builder.toString());[m
     }[m
 [m
     fun obfuscatedMessage(length: Int): String {[m
