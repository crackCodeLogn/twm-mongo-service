APP_NAME="twm-mongo-service"
JAVA_PARAM="-Xmx1g"

BIN_PATH=$TWM_HOME_PARENT/TWM/$APP_NAME/bin     #TWM-HOME-PARENT :: exported in .bashrc
cd $BIN_PATH/../target/
JAR_NAME=`ls *jar`
JAR_PATH=$BIN_PATH/../target/$JAR_NAME
JAVA_PATH=$HOME/.jdks/jdk17/bin/java

echo "Starting '$APP_NAME' with params: '$PARAMS', at '$JAR_PATH'"
$JAVA_PATH $PARAMS -jar $JAR_PATH
