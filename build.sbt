// gallia-macros; TODO: t210309100048 - relies on symlink to gallia-core's project/*.scala files; no (reasonnable) sbt way? windows users have to copy them instead?

// ===========================================================================
lazy val root = (project in file("."))
  .settings(
    organizationName     := "Gallia Project",
    organization         := "io.github.galliaproject", // *must* match groupId for sonatype
    name                 := "gallia-macros",
    version              := "0.3.1",    
    homepage             := Some(url("https://github.com/galliaproject/gallia-macros")),
    scmInfo              := Some(ScmInfo(
        browseUrl  = url("https://github.com/galliaproject/gallia-macros"),
        connection =     "scm:git@github.com:galliaproject/gallia-macros.git")),
    licenses             := Seq("BSL 1.1" -> url("https://github.com/galliaproject/gallia-macros/blob/master/LICENSE")),
    description          := "A Scala library for data manipulation" )
  .settings(GalliaCommonSettings.mainSettings:_*)

// ===========================================================================    
lazy val galliaVersion = "0.3.0"

// ---------------------------------------------------------------------------
libraryDependencies += "io.github.galliaproject" %% "gallia-core" % galliaVersion

// ===========================================================================
sonatypeRepository     := "https://s01.oss.sonatype.org/service/local"
sonatypeCredentialHost :=         "s01.oss.sonatype.org"        
publishMavenStyle      := true
publishTo              := sonatypePublishToBundle.value

// ===========================================================================

