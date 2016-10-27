
name := """Oilcol"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava,PlayEbean,LauncherJarPlugin)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  evolutions,
  javaJdbc,
  cache,
  javaWs,
  "org.postgresql" % "postgresql" % "9.4-1206-jdbc42",
  "org.apache.shiro" % "shiro-web" % "1.2.3",
  "com.sun.jersey" % "jersey-servlet" % "1.13"
)


fork in run := false