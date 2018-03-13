#!/bin/bash
. `dirname $0`/env.sh
cd $DEPLOY_DIR/bin


SERVER_NAME=`sed '/dubbo.application.name/!d;s/.*=//' $CONF_FILE | tr -d '\r'`
SERVER_PID=`ps -ef|grep ${DEPLOY_DIR}|grep -v grep |awk '{print $2}'`
#echo $PID_FILE
if [ -n "$SERVER_PID" ] ; then
    echo "Stopping $SERVER_NAME ..."
    kill -15 "$SERVER_PID"
    sleep 5
    ps -p $SERVER_PID | grep -q $SERVER_PID
    if [ $? -eq 0 ] ;then
        echo "PID $SERVER_PID is still running, stop it with 9 signal." 1>&2
        kill -9 "$SERVER_PID"
        exit 1
    fi
else
    echo "$SERVER_NAME does not running." 1>&2
    exit 0
fi

echo "stop ok."

