#!/bin/bash

cd /home
exec java -jar -Xmx64m nginxWebUI.jar "${BOOT_OPTIONS}" 2>&1 >/dev/null
