#!/bin/bash

set -e
echo "Publishing to development server"
TARGET='transplants-dev.wintoncentre.uk:/var/www/transplants-dev.wintoncentre.uk/'
echo "generating configuration data"
#lein config
LOCAL_DEV='resources/public/'
echo "===============rsync-ing files==================="
rsync -avz --omit-dir-times --del --no-perms --no-owner --no-group $LOCAL_DEV $USER@$TARGET
echo "================================="
echo "Uploaded to " $TARGET
echo "================================="