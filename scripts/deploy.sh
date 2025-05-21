#!/bin/bash

APP_DIR=/home/ec2-user/app/step2
ZIP_DIR=$APP_DIR/zip

echo "> 환경 변수 설정"
export $(cat $APP_DIR/.env | xargs)

echo "> 압축 해제"
unzip -o $ZIP_DIR/cotato-11th-weather.zip -d $ZIP_DIR

echo "> 기존 JAR 파일 삭제"
rm -f $APP_DIR/*.jar

echo "> JAR 복사"
cp $ZIP_DIR/*.jar $APP_DIR/

echo "> JAR 파일 복사 결과 확인"
ls -ltr $APP_DIR/*.jar

echo "> 복사 후 상태:"
ls -l $APP_DIR/*.jar

JAR_NAME=$(ls -tr $APP_DIR/*.jar | tail -n 1)

if [ -z "$JAR_NAME" ]; then
  echo "> JAR_NAME 비어 있음. 배포 중단"
  exit 1
fi

echo "> 복사된 JAR: $JAR_NAME"

echo "> 현재 실행 중인 애플리케이션 PID 확인"
CURRENT_PID=$(pgrep -f $JAR_NAME)

if [ -z "$CURRENT_PID" ]; then
  echo "> 실행중인 애플리케이션이 없습니다."
else
  echo "> kill -15 $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5
fi

echo "> 실행 권한 부여: $JAR_NAME"
chmod +x $JAR_NAME

echo "> 애플리케이션 실행"
nohup java \
  -Dspring.config.location=file:$APP_DIR/application-prod.yml \
  -Dspring.profiles.active=prod \
  -jar $JAR_NAME > $APP_DIR/applications.log 2>&1 &

echo "> 애플리케이션 시작됨: $(pgrep -f $JAR_NAME)"

