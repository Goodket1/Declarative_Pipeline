#!/bin/bash
var = docker tag artifactory.legsup.co.uk:8082/web_app:$1 artifactory.legsup.co.uk:8082/web_app:2
eval $var