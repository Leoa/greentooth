name := "greentooth"

version := "0.0.1"

organization := "android.ws"

scalaVersion := "2.9.0-1"

libraryDependencies ++= Seq(
    "org.specs2" %% "specs2" % "1.5" % "test",
    "org.eclipse.jetty" % "jetty-websocket" % "7.5.0.RC1" % "test",
    "org.eclipse.jetty" % "jetty-servlet" % "7.5.0.RC1" % "test"
)

resolvers ++= Seq("snapshots" at "http://scala-tools.org/repo-snapshots",
                    "releases" at "http://scala-tools.org/repo-releases")

