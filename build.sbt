// gallia-macros

// ===========================================================================
lazy val root = (project in file("."))
  .settings(
    organizationName     := "Gallia Project",
    organization         := "io.github.galliaproject", // *must* match groupId for sonatype
    name                 := "gallia-macros",
    version              := GalliaCommonSettings.CurrentGalliaVersion,
    homepage             := Some(url("https://github.com/galliaproject/gallia-macros")),
    scmInfo              := Some(ScmInfo(
        browseUrl  = url("https://github.com/galliaproject/gallia-macros"),
        connection =     "scm:git@github.com:galliaproject/gallia-macros.git")),
    licenses             := Seq("Apache 2" -> url("https://github.com/galliaproject/gallia-macros/blob/master/LICENSE")),
    description          := "A Scala library for data manipulation" )
  .settings(GalliaCommonSettings.mainSettings:_*)

// ===========================================================================
libraryDependencies += "io.github.galliaproject" %% "gallia-core" % GalliaCommonSettings.CurrentGalliaVersion

// ===========================================================================
sonatypeRepository     := "https://s01.oss.sonatype.org/service/local"
sonatypeCredentialHost :=         "s01.oss.sonatype.org"
publishMavenStyle      := true
publishTo              := sonatypePublishToBundle.value

// ===========================================================================

