#!/bin/bash
tag=`date '+%Y-%m-%d %H:%m'`
docker build -t blogrocketmq:${tag} .
docker run -d --name blog-rocketmq -p 8089:8089 blogrocketmq:${tag}

