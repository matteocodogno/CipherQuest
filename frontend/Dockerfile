FROM node:20.10 as build

WORKDIR /usr/src/app
#COPY .env ./
COPY package.json ./
COPY package-lock.json ./
COPY index.html ./
COPY tsconfig.json ./
COPY tsconfig.app.json ./
COPY tsconfig.node.json ./
COPY vite.config.ts ./
COPY src/ ./src/
COPY public/ ./public/

RUN npm install && npm run build

FROM nginx:1.27-alpine

COPY docker-entrypoint.sh package.json ./
COPY --from=build /usr/src/app/dist/ /usr/share/nginx/html
COPY default.conf /etc/nginx/conf.d

RUN chmod +x docker-entrypoint.sh

EXPOSE 7777
CMD [ "/usr/src/app/docker-entrypoint.sh" ]
