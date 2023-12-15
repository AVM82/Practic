<h1 align="center">Practic</h1>

### ⚡Інформація про проєкт

В цьому репозиторії можна побачити реалізацію сайту - збірнику курсів,
де адміністратори можуть додавати курси, ментори ними управляти та змінювати,
а студенти просто навчатися, отримувати знання, готувати доповіді
та робити практичні роботи.

![Here must be a picture of enter page](enter_page.png)

В проєкті використані такі технології:
-	Spring Boot 3, Spring Security, Spring Data, Hibernate;
-	PostgreSQL;
-	Angular 16.2+;
-	Реєстрація та авторизація відбуваються за допомогою LinkedIn, використовуючи OAuth 2.0 та JWT;

Проєкт Practic складається з 3х модулів:
1.	Модуль practic - бекенд з використанням Spring Boot 3;
2.	Модуль frontend - фронтенд на Angular 2+;
3.	Модуль loadTesting - тести з використанням Gatling;
      
Проєкт має налаштовану перевірку стилю кода, файли з налаштуваннями ggogle_cheks.xml, suppresions.xml (відключення перевірки Javadoc).

### ⚙ Налаштування і запуск проекту️

Для роботи сервісу необхідно:
1.	git clone - клонувати проєкт;
2.	встановити node.js (не нижче версії 16.20 LTS);
3.  встановити ряд необхідних зміних (найкращий спосіб – застосувати environment variables):
- _LINKEDIN_CI_ - LinkedIn Client ID - ключ аутентифікації, який можна знайти в аппці на лінкедіні;
- _LINKEDIN_CS_ - LinkedIn Client Secret - пароль до ключа аутентифікації, який можна знайти в аппці на лінкедіні (зверніться до колег);
- _LOGOUT_REDIRECT_URI_=http://localhost:4200/login?logout=true - (приклад для локального використання);
- _MAIL_PASSWORD_ - пароль до пошти, що буде використана для відправки нотифікацій з приводу нових доповідей;
- _MAIL_USERNAME_ - пошта, що буде використана для відправки нотифікацій з приводу нових доповідей;
- _POSTGRESQL_PASSWORD_ - пароль до postgresql бази даних, що використовується для роботи програми (локальний);
- _POSTGRESQL_URL_ - url до бази даних, що використовується для роботи програми (локальний);
- _POSTGRESQL_USERNAME_ - username користувача, що використовує базу даних (локальний);
- _REDIRECT_URI_=http://localhost:4200/oauth2/redirect,   myandroidapp://oauth2/redirect, myiosapp://oauth2/redirect - (приклад для локального використання);
- _TOKEN_SECRET_ - токен, що буде використовуватися в програмі для JWT (16-річне число, seed, який буде використаний для генерації токену);

*Слід зазначити, що при роботі на віддаленому сервері, всі "localhost:порт" треба замінити на домен сервера, наприклад: http://localhost:4200/login?logout=true -> https://<ваш домен>/login?logout=true

4.	імпортувати файл google_cheks.xml (файл знаходиться в корені проекту): зайти в Settings  Tools  Checkstyle. Далі в Configuration file додати файл google_cheks.xml і поставити галочку на Active. Checkstyle version має бути > 10 (напр. 10.12.5).
      В Idea в закладці  Checkstyle обрати в Rules той файл, який внесено в Tools.
<p align="center">
<img src="google_checks_example.png" alt="Checkstyle" width="300" height="200" />
</p>
5.	перейти в frontend/src/main/angular (папка, де знаходиться package.json) та запустити ```npm install``` (за необхідності запустити ```npm update```);

6.	зробити першу збірку проєкту ```npm ci``` (або - ```npm clean install```) – щоб створився build.properties в маніфесті;

7.	перейти в корінь проекту і зібрати весь проект - ```mvn clean install```;

8.	запуск фронтенду та бекенду:
-	фронтенд запускається з папки проекту (папка frontend) –  npm start (по замовчуванню запускається сервер http://localhost:4200/);
-	для запуску бекенду запустити метод main в PracticApplication (до команди npm start дописаний проксі сервер для запитів на бекенд за адресою http://localhost:5001/).
     
Після збірки і запуску додатку фронтенд і бекенд працють на одному сервері: http://localhost:5001/

Для того щоб задеплоїти програму на продакшин треба внести зміни в файлі environment.prod.ts, що знаходиться в папці frontend/src/main/angular/src/environments.

