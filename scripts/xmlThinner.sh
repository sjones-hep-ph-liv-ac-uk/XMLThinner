#!/bin/sh

if [ ! -x $JAR_DIR/XMLThinner.jar ]; then
  echo Somehow, this program has been installed poorly.
  exit 1
fi

java -cp $JAR_DIR/XMLThinner-$JAR_VERSION.jar  com/basingwerk/xmlthinner/XMLThinner

