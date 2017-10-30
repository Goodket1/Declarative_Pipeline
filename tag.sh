#!/bin/bash
docker tag artifactory.legsup.co.uk:8082/web_app:$1 artifactory.legsup.co.uk:8082/web_app:$2
docker push rtifactory.legsup.co.uk:8082/web_app:$2