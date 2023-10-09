# Сервис ресурсов

<div style="text-align: center;">
  <img src="docs/logo.jpg" alt="Логотип проекта">
</div>

Сервис представляет собой RESTful API, который позволяет пользователям создавать и получать объекты.
Для аутентификации и авторизации используются JWT (JSON Web Tokens).

## Используемые технологии

- Java
- Spring Boot
- Spring WebFlux
- Spring Security
- MongoDB
- Redis
- Kafka
- JWT

## Запуск сервиса

Для запуска сервис необходимо запустить контейнер mongodb, определенный в файле `docker-compose.yml`.

## Аутентификация и авторизация

Сервис использует JWT для аутентификации и авторизации. Для доступа к эндпоинтам ресурса пользователи
должны получить токены. Access и refresh токены могут быть получены через соответствующий
эндпоинт аутентификации. Сервис также предоставляет возможность обновления всех типов токенов.

Для реализации использован кастомный фильтр (см. `JwtFilter`).

Секреты, используемые для генерации токенов, хранятся в файле `application.yml` (см. `JwtService`).

## Роли пользователей и права доступа

- Пользователь:
    - Роль: USER
    - Права доступа: GET запросы к uri (/users/***)

- Администратор:
    - Роль: ADMIN
    - Права доступа: POST, PUT, DELETE и GET запросы ко всем uri

Эти пользователи создаются в скрипте инициализации БД (см. `create-db.js`).

## Эндпоинты

### Аутентификация

- URL: `/resource-service/api/auth/login`
- Метод: POST
- Тело запроса: Учетные данные пользователя (имя пользователя и пароль)
- Ответ: Access и refresh токены

Этот эндпоинт позволяет пользователям аутентифицироваться и получить access и refresh токены.

- URL: `/resource-service/api/auth/logout`
- Метод: GET
- Ответ: Boolean

Этот эндпоинт позволяет пользователям удалить свой refresh токен из системы.

### Обновление access токена

- URL: `/resource-service/api/auth/token`
- Метод: POST
- Тело запроса: Refresh токен
- Ответ: Новый access токен

Этот эндпоинт позволяет пользователям обновить access токен с помощью действующего refresh токена.

### Обновление refresh токена

- URL: `/resource-service/api/auth/refresh`
- Метод: POST
- Тело запроса: Refresh токен
- Ответ: Новый refresh токен

Этот эндпоинт позволяет пользователям обновить refresh токен с помощью действующего refresh токена.

## База данных

Объекты ресурсов хранятся в базе данных MongoDB.
Конфигурация базы данных может быть найдена в файле `application.yml`.

Access Token действителен в течение 5 минут, Refresh Token хранится в Redis и действителен в течение 30 дней

При login/logout операциях отправляется событие в Kafka-топик user-auth-topic с сообщением вида {user login} login/logout
и ключом user login.

## Мониторинг

- Необходимо запустить контейнеры с помощью команды docker-compose up, либо через Intellij IDEA run configuration с 
именем "infrastructure"
- Запустить Resource-service
- Создать пользовательскую нагрузку с помощью скриптов (см. src/main/resources/docker/scripts/), которые можно запустить
вручную, либо через Intellij IDEA run configuration по соответствующему имени, например error_curl_script
- Перейти по адресу http://localhost:3000/ (имя пользователя - admin, пароль - admin)
- Перейти в раздел Dashboards (Datasource и дашборды создаются автоматически при старте контейнера, см. src/main/resources/docker/prometheus, 
/src/main/resources/docker/grafana и docker-compose.yml)

