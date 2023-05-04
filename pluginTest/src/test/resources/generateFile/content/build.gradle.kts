plugins {
    id("io.github.mirko-felice.plugins.file")
}

generateFile {
    fileName.set("test.txt")
    content.set("value")
}
