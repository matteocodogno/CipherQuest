#!/bin/sh
set -e

APP_VERSION=$(cat ./package.json | grep version | head -1 | awk -F':' '{ print $2 }' | sed 's/[", ]//g')

echo "[Cipher Quest - frontend - Version ${APP_VERSION}] Listening on port 7777 🚀"

nginx -g "daemon off;"
