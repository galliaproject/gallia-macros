// gallia-macros; TODO: t210309100048 - relies on symlink to gallia-core's project/*.scala files; no (reasonnable) sbt way? windows users have to copy them instead?

// ===========================================================================
lazy val root = (project in file("."))
  .settings(
    name    := "gallia-macros",
    version := "0.3.0")
  .settings(GalliaCommonSettings.mainSettings:_*)

// ===========================================================================    
lazy val galliaVersion = "0.3.0"

// ---------------------------------------------------------------------------
libraryDependencies += "io.github.galliaproject" %% "gallia-core" % galliaVersion

// ===========================================================================
