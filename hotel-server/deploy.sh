#!/bin/bash

# Hotel Application Deployment Script
# 用法: ./deploy.sh [start|stop|restart|status]

APP_NAME="hotel-web-1.0-SNAPSHOT.jar"
APP_HOME="/opt/hotel"
LOG_HOME="/var/log/hotel"
LOG_FILE="${LOG_HOME}/hotel-application.log"
PID_FILE="/var/run/hotel-app.pid"

mkdir -p ${APP_HOME}
mkdir -p ${LOG_HOME}

case "$1" in
    start)
        echo "Starting Hotel Application..."
        if [ -f ${PID_FILE} ]; then
            PID=$(cat ${PID_FILE})
            if ps -p ${PID} > /dev/null 2>&1; then
                echo "Application is already running (PID: ${PID})"
                exit 1
            fi
        fi

        cd ${APP_HOME}
        nohup java -jar ${APP_NAME} --spring.profiles.active=prod > ${LOG_FILE} 2>&1 &
        echo $! > ${PID_FILE}
        echo "Application started (PID: $(cat ${PID_FILE}))"
        echo "Log file: ${LOG_FILE}"
        ;;

    stop)
        echo "Stopping Hotel Application..."
        if [ -f ${PID_FILE} ]; then
            PID=$(cat ${PID_FILE})
            if ps -p ${PID} > /dev/null 2>&1; then
                kill -15 ${PID}
                sleep 5
                if ps -p ${PID} > /dev/null 2>&1; then
                    kill -9 ${PID}
                fi
                echo "Application stopped"
            else
                echo "Application is not running"
            fi
            rm -f ${PID_FILE}
        else
            echo "PID file not found"
        fi
        ;;

    restart)
        $0 stop
        sleep 2
        $0 start
        ;;

    status)
        if [ -f ${PID_FILE} ]; then
            PID=$(cat ${PID_FILE})
            if ps -p ${PID} > /dev/null 2>&1; then
                echo "Application is running (PID: ${PID})"
            else
                echo "Application is not running (stale PID file)"
            fi
        else
            echo "Application is not running"
        fi
        ;;

    log)
        tail -f ${LOG_FILE}
        ;;

    *)
        echo "Usage: $0 {start|stop|restart|status|log}"
        exit 1
        ;;
esac

exit 0
