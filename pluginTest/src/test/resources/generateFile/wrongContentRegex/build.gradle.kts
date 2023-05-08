plugins {
    id("io.github.mirko-felice.plugins.file")
}

generateFile {
    fileName.set("test.txt")
    content.set("Line 1: value\nLine 2: test")
}
