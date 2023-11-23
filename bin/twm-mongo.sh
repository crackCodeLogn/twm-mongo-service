APP_NAME="twm-mongo-service"
APP_VERSION="2.0-SNAPSHOT"
JAVA_PARAM="-Xmx1g"
TWM_PARAM=""

BIN_PATH=$TWM_HOME_PARENT/TWM/$APP_NAME/bin     #TWM-HOME-PARENT :: exported in .bashrc
JAR_PATH=$BIN_PATH/../target/$APP_NAME-$APP_VERSION.jar
PARAMS=$JAVA_PARAM" "$TWM_PARAM
JAVA_PATH=$HOME/.jdks/corretto-17.0.9/bin/java

echo "Starting '$APP_NAME' with params: '$PARAMS', at '$JAR_PATH'"
$JAVA_PATH $PARAMS -jar $JAR_PATH
