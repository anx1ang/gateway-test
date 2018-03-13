cd `dirname $0`
BIN_DIR=`pwd`
cd ..
DEPLOY_DIR=`pwd`
CONF_DIR=$DEPLOY_DIR/conf

export JAVA_HOME=/usr/java/jdk1.8.0_131
export JAVA_BIN=/usr/java/jdk1.8.0_131/bin
export PATH=$JAVA_BIN:$PATH