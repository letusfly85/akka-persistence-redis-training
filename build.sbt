name := "akka-persistence-redis-training"

version := "1.0"

scalaVersion := "2.12.3"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

dependencyOverrides += "org.scala-lang" %% "scala-compiler" % scalaVersion.value

libraryDependencies ++= {
  val akkaV       = "2.5.3"
  val akkaHttpV = "10.0.8"
  val scalikeJDBCV = "2.5.1"
  val kamonVersion = "0.6.7"
  val macwireV = "2.3.0"
  Seq(
    "com.typesafe.akka" %% "akka-actor"              % akkaV,
    "com.typesafe.akka" %% "akka-persistence"        % akkaV,
    "com.typesafe.akka" %% "akka-slf4j"              % akkaV,
    "ch.qos.logback"    %  "logback-classic"         % "1.1.7",
    "com.typesafe.akka" %% "akka-stream"             % akkaV,
    "com.typesafe.akka" %% "akka-http-core"          % akkaHttpV,
    "com.typesafe.akka" %% "akka-http"               % akkaHttpV,
    "com.typesafe.akka" %% "akka-http-spray-json"    % akkaHttpV,
    "com.typesafe.akka" %% "akka-http-testkit"       % akkaHttpV,

    // for akka persistence
    "com.hootsuite" %% "akka-persistence-redis" % "0.8.0",

    "org.scalikejdbc" %% "scalikejdbc"                     % scalikeJDBCV,
    "org.scalikejdbc" %% "scalikejdbc-config"              % scalikeJDBCV,
    "mysql" % "mysql-connector-java" % "5.1.28",

    "com.github.seratch" %% "awscala" % "0.5.+",

    ("eu.inn" %% "fluentd-scala" % "0.2.8")
      .exclude("org.scala-lang.modules", "scala-parser-combinators_2.11"),

    "commons-io" % "commons-io" % "2.5",

    "org.specs2" % "specs2_2.12" % "2.4.17" % "test",
    "org.scalatest" % "scalatest_2.12" % "3.0.3" % "test",

    "com.softwaremill.macwire" %% "macros" % macwireV % "provided" ,
    "com.softwaremill.macwire" %% "util" % macwireV,
    "com.softwaremill.macwire" %% "proxy" % macwireV,

    "commons-configuration" % "commons-configuration" % "1.10",

    "io.kamon" %% "kamon-core"   % kamonVersion,
    "io.kamon" %% "kamon-akka-2.5"   % kamonVersion,
    "io.kamon" %% "kamon-jdbc"   % kamonVersion,
    "io.kamon" %% "kamon-jmx"    % kamonVersion,
    "io.kamon" %% "kamon-statsd" % kamonVersion,
    "io.kamon" %% "kamon-log-reporter"   % kamonVersion,
    "io.kamon" %% "kamon-system-metrics" % kamonVersion,
    "org.aspectj" % "aspectjweaver" % "1.8.9",

    // for papertrail
    "org.syslog4j" % "syslog4j" % "0.9.30",
    "com.papertrailapp" % "logback-syslog4j" % "1.0.0"
  )
}

//refs: https://github.com/gerferra/amphip/blob/master/build.sbt
scalacOptions ++= Seq(
  "-Ypatmat-exhaust-depth", "off"
)

Revolver.settings

wartremoverErrors ++= Seq(

  Wart.IsInstanceOf,
  Wart.Return,
  Wart.OptionPartial,
  Wart.TryPartial,
  Wart.Null,
  Wart.Product,
  Wart.Serializable,
  Wart.Nothing,
  //Wart.Var,
  //Wart.Throw,
  Wart.Enumeration,
  Wart.ToString,
  Wart.FinalCaseClass,
  Wart.EitherProjectionPartial,
  Wart.ExplicitImplicitTypes,
  Wart.AsInstanceOf)

parallelExecution in Test := false

assemblyMergeStrategy in assembly := {
  case PathList("io", "kamon", xs@_*) => MergeStrategy.last
  case "META-INF/aop.xml" => MergeStrategy.last
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}

import com.typesafe.config._

//todo change database configuration file by environment
val conf = ConfigFactory.parseFile(new File("src/main/resources/database.local.conf")).resolve()

flywayDriver := conf.getString("db.default.driver")

flywayUrl := conf.getString("db.default.url")

flywayUser := conf.getString("db.default.username")

flywayPassword := conf.getString("db.default.password")

//flywayBaselineOnMigrate := true

flywayTarget := "1.0.0"

flywayBaselineVersion := "1.0.0"