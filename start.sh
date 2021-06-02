#!/bin/bash
tag=`date '+%s'`
docker build -t blogrocketmq:${tag} .
docker run -d --name blog-rocketmq -p 8089:8089 blogrocketmq:${tag}
