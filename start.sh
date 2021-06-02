#!/bin/bash
d=`date '+%Y-%m-%d %H:%m'`
docker build -t blogrocketmq:${d} .
docker run -d --name blog-rocketmq -p 8089:8089 blogrocketmq:${d}

