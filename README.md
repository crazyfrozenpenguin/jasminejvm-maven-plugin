jasminejvm-maven-plugin
==================

Run Jasmine from the command line.

Plugin Usage
------------

### configuration and defaults

			<plugin>
				<groupId>com.github.crazyfrozenpenguin</groupId>
				<artifactId>jasminejvm-maven-plugin</artifactId>
				<version>0.0.1-SNAPSHOT</version>
				<configuration>
					<classpathScope>test</classpathScope>
					<pattern>target/**/*Spec.js</pattern>
					<format>DOC</format>
					<compileMode>OFF</compileMode>
				</configuration>
			</plugin>

* __Classpath Scope:__ system, compile, runtime or test. Defaults to test.
* __Spec files pattern:__ Defaults to ___"target/**/*Spec.js"___
* __Format:__ DOC, PROGRESS. Defaults to DOC.
* __Compile Mode:__ OFF, FORCE, JIT. Defaults to OFF.

Tied by default to ___pre-integration-phase___ maven execution phase.

### Goal: jasminejvm:run

Run Jasmine tests

        mvn jasminejvm:run

