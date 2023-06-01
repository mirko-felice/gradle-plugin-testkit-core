import io.github.mirkofelice.plugins.file.Permission

plugins {
    id("io.github.mirko-felice.plugins.file")
}

generateFile {
    fileName.set("test.txt")
    content.set("value")
    permissions.set(listOf(Permission.R))
}
