#!/usr/bin/env bash

ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)
source ${ABSDIR}/profile.sh

function switch_proxy() {
  # Nginx 실행 보장
  if ! systemctl is-active --quiet nginx; then
    echo "> Nginx가 꺼져 있어서 시작합니다."
    sudo systemctl start nginx
  fi

  IDLE_PORT=$(find_idle_port)

  echo "> 전환할 Port: $IDLE_PORT"
  echo "> Port 전환"
  echo "set \$service_url http://127.0.0.1:${IDLE_PORT};" | sudo tee /etc/nginx/conf.d/service-url.inc

  echo "> 엔진엑스 Reload"
  sudo service nginx reload
}