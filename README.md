# Practic

### ⚡Інформація про проєкт

В цьому репозиторії можна побачити реалізацію сайту - збірнику курсів,
де адміністратори можуть додавати курси, ментори ними управляти та змінювати,
а студенти просто навчатися, отримувати знання, готувати доповіді
та робити практичні роботи.

![Here must be a picture of enter page](enter_page.png)

В цьому проєкті були використані такі технології як:
- Spring Boot 3, Spring Security, Spring Data, Hibernate;
- Реєстрація та авторизація відбуваються за допомогою LinkedIn використовуючи OAuth 2.0 та JWT;
- PostgreSQL;
- Angular 2+;

Проєкт Practic складається з 3х модулів:
1. Модуль practic - бекенд з використанням Spring Boot 3;
2. Модуль frontend - фронтенд на Angular 2+;
3. Модуль loadTesting - тести з використанням Gatling;

Проєкт має налаштовану перевірку стилю кода, файли з налаштуваннями ggogle_cheks.xml,
suppresions.xml(відключення перевірки Javadoc). Для перевірки в IntelliJ IDEA потрібно встановити
плагін Chekstyle версії > 10. Далі в налаштуваннях плагіну імпортувати файл google_cheks.xml.

### ⚙ Налаштування ️

Для роботи сервісу потрібно встановити ряд __необхідних__ зміних(найкращий спосіб використовуючи environment variables):
- _LINKEDIN_CI_ - LinkedIn Client ID - ключ аутентифікації, який можна знайти в аппці на лінкедіні;
- _LINKEDIN_CS_ - LinkedIn Client Secret - пароль до ключа аутентифікації, який можна знайти в аппці на лінкедіні;
- _LOGOUT_REDIRECT_URI_=http://localhost:4200/login?logout=true - (приклад для локального використання);
- _MAIL_PASSWORD_ - пароль до пошти, що буде використана для відправки нотифікацій з приводу нових доповідей;
- _MAIL_USERNAME_ - пошта, що буде використана для відправки нотифікацій з приводу нових доповідей;
- _POSTGRESQL_PASSWORD_ - пароль до postgresql бази даних, що використовується для роботи програми;
- _POSTGRESQL_URL_ - url до бази даних, що використовується для роботи програми;
- _POSTGRESQL_USERNAME_ - username користувача, що використовує базу даних;
- _REDIRECT_URI_=http://localhost:4200/oauth2/redirect,myandroidapp://oauth2/redirect,myiosapp://oauth2/redirect - (приклад для локального використання);
- _TOKEN_SECRET_ - токен, що буде використовуватися в програмі для JWT 16-річне число, seed, який буде використаний для генерації токену;

Слід зазначити, що при роботі на віддаленому сервері, всі "localhost:порт" треба замінити на домен сервера, наприклад: 
_http://localhost:4200/login?logout=true_ -> _https://<ваш домен>/login?logout=true_

1. ```git clone``` - клонувати проєкт;
2. встановити node.js(в цьому проєкті використовується версія 16.20 LTS);
3. перейти в frontend/src/main/angular(папка, де знаходиться package.json) та запустити ```npm install```;
4. за необхідності запустити ```npm update```;
5. зробити першу збірку проєкту ```npm clean install```(щоб створився build.properties в маніфесті);
6. запуск фронтенду та бекенду;

Для того щоб задеплоїти програму на продакшин треба внести зміни в файлі 
environment.prod.ts, що знаходиться в папці frontend/src/main/angular/src/environments.

Під час розробки фронтенд запускається з папки проекту(папка frontend): ```npm start```
(по замовчуванню запускається сервер http://localhost:4200, до команди ```npm start``` дописаний
проксі сервер для запитів на бекенд за адресою http://localhost:5000/,
для повноцінної роботи треба запустити Spring Application).

Для запуску бекенду запустити метод main в PracticApplication.

Збірка проекту:
```mvn clean install```

Після збірки і запуску додатку фронтенд і бекенд працють на одному сервері:
    http://localhost:5000