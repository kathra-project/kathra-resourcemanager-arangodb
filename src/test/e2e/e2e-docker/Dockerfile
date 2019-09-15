FROM node:8

WORKDIR /usr/src/app
COPY src/* ./
RUN npm install

ENTRYPOINT [ "node", "index.js" ]