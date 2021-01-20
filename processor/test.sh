#!/bin/bash -x
cd `dirname $0`

/usr/local/java/jdk-13.0.1/bin/javap -cp target/test-classes/ org.mobicents.slee.annotations.examples.profile.CompleteExampleAnnotatedProfile 
/usr/local/java/jdk-13.0.1/bin/javap -cp target/test-classes/ org.mobicents.slee.annotations.examples.sbb.NoInterfaceSbb
