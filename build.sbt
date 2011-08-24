import Keys._
import AndroidKeys._

name := "greentooth"

version := "0.0.1"

organization := "android.ws"

scalaVersion := "2.8.1"

platformName in Android := "android-10"

seq(AndroidProject.androidSettings: _*)

libraryDependencies ++= Seq(
  "com.google.android" % "android" % "2.3.3",
  "org.specs2" %% "specs2" % "1.5" % "test",
  "org.eclipse.jetty" % "jetty-websocket" % "7.5.0.RC1" % "test",
  "org.eclipse.jetty" % "jetty-servlet" % "7.5.0.RC1" % "test",
  "com.google.android" % "android-test" % "2.3.3",
  "com.github.jbrechtel" %% "robospecs" % "0.1-SNAPSHOT" % "test"
)

resolvers ++= Seq(
  "snapshots" at "http://scala-tools.org/repo-snapshots",
  "releases" at "http://scala-tools.org/repo-releases",
  "robospecs" at "http://jbrechtel.github.com/repo/snapshots"
)

