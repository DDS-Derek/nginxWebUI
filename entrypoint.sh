#!/bin/sh

cd /home
exec java -jar nginxWebUI.jar "${BOOT_OPTIONS}" > /dev/null
