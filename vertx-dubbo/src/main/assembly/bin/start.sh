#!/bin/bash
. `dirname $0`/env.sh
cd $DEPLOY_DIR

SERVER_NAME=gateway-server
SERVER_PORT=9088
LOGS_FILE=/opt/logs


if [ -z "$SERVER_NAME" ]; then
    SERVER_NAME=`hostname`
fi

PIDS=`ps -ef | grep java | grep "$CONF_DIR" |awk '{print $2}'`
if [ -n "$PIDS" ]; then
    echo "ERROR: The $SERVER_NAME already started!"
    echo "PID: $PIDS"
    exit 1
#    kill -9 $PIDS
fi

if [ -n "$SERVER_PORT" ]; then
    SERVER_PORT_COUNT=`netstat -tln | grep $SERVER_PORT | wc -l`
    if [ $SERVER_PORT_COUNT -gt 0 ]; then
        echo "ERROR: The $SERVER_NAME port $SERVER_PORT already used!"
        exit 1
    fi
fi

LOGS_DIR=""
if [ -n "$LOGS_FILE" ]; then
    LOGS_DIR=`dirname $LOGS_FILE`
fi
if [ -z "$LOGS_DIR" ] ;then
    LOGS_DIR="$DEPLOY_DIR/logs"
    if [ ! -e "$LOGS_DIR" ] ;then
        if [ ! -d "$LOGS_DIR" ] ;then
            mkdir "$LOGS_DIR"
        fi
    fi

else
    if [ ! -d $LOGS_DIR ]; then
        mkdir $LOGS_DIR
    fi
fi

LIB_DIR=$DEPLOY_DIR/lib
LIB_JARS=`ls $LIB_DIR|grep .jar|awk '{print "'$LIB_DIR'/"$0}'|tr "\n" ":"`

#
LOG_DIR="/data/logs/order-server"
JAVA_OPTS=" -Dlog.dir=$LOG_DIR  -Djava.net.preferIPv4Stack=true"
JAVA_OPTS="$JAVA_OPTS -server -Xmx4096m -Xms4096m -Xmn2560m -XX:SurvivorRatio=6 -XX:PermSize=256m -XX:MaxPermSize=256m"
JAVA_OPTS="$JAVA_OPTS -XX:+UseConcMarkSweepGC -XX:+UseParNewGC -XX:+PrintGCTimeStamps -XX:+PrintGCDateStamps -XX:+PrintGCDetails -Xloggc:$LOG_DIR/gc.log"
JAVA_OPTS="$JAVA_OPTS -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=$LOG_DIR/"

JAVA_DEBUG_OPTS=""
if [ "$1" = "debug" ]; then
    JAVA_DEBUG_OPTS=" -Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,address=8000,server=y,suspend=n "
fi
JAVA_JMX_OPTS=""
if [ "$1" = "jmx" ]; then
    JAVA_JMX_OPTS=" -Dcom.sun.management.jmxremote.port=1099 -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false "
fi



echo -e "Starting the $SERVER_NAME ...\c"
nohup java $JAVA_OPTS $JAVA_DEBUG_OPTS $JAVA_JMX_OPTS -classpath $CONF_DIR:$LIB_JARS com.zxk.starter.StartRunner > main.out 2>&1 &

COUNT=0
while [ $COUNT -lt 1 ]; do    
    echo -e ".\c"
    sleep 1 
    	COUNT=`ps -ef | grep java | grep "$DEPLOY_DIR" | awk '{print $2}' | wc -l`
    if [ $COUNT -gt 0 ]; then
        break
    fi
done

echo "OK!"
PIDS=`ps -ef | grep java | grep "$DEPLOY_DIR" | awk '{print $2}'`
echo "PID: $PIDS"
echo "LOGS_DIR: $LOGS_DIR"
