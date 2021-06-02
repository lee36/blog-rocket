d=${date '+%Y-%m-%d %H:%m'}
docker build -t blog-rocketmq:${d} .
docker run -d --name blog-rocketmq -p 8089:8089 blog-rocketmq:${d}

