#!/bin/bash
set -xe

# You can run it from any directory.
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

# This will:
#    1. Clean the project
#    2. Run Android Lint
#    3. Run tests under JVM

# We run each step separately to consume less memory (free CI sometimes fails with OOM)

GRADLE=""$DIR"/gradlew -PdisablePreDex --no-daemon"

# 1
eval "$GRADLE clean"

# 2
eval "$GRADLE lintDebug"
eval "$GRADLE lintRelease"

# 3
eval "$GRADLE testDebugUnitTest"
eval "$GRADLE testReleaseUnitTest"