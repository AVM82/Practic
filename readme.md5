Проект Practic складається з 2х модулів:
    1. Модуль practic - бекенд з використанням Spring Boot 3.
    2. Модуль frontend - фронтенд на Angular 2+.

Проект має налаштовану перевірку стилю кода, файли з налаштуваннями ggogle_cheks.xml,
 suppresions.xml(відключення перевірки Javadoc). Для перевірки в IJ IDE потрібно встановити
 плагін Chekstyle версії > 10. Далі в налаштуваннях плагіну імпортувати файл google_cheks.xml.

Під час розробки фронтенд запускається з папки проекту:
    nmp start
(по замовчуванню запускається сервер http://localhost:4200, до команди npm start дописаний
проксі сервер для запитів на бекенд за адресою http://localhost:5000/,
для повноцінної роботи треба запустити Spring Application).
 
Налаштування середовища для правильної роботи авторизації:
environment.ts:          змінні оточення під час розробки (сервер фронтенду на localhost:4200).
                        В змінні оточення для IDE треба внести наступні параметри:
                        LOGOUT_REDIRECT_URI=http://localhost:4200/login?logout=true
                        REDIRECT_URI=http://localhost:4200/oauth2/redirect,myandroidapp://oauth2/redirect,myiosapp://oauth2/redirect

environment.local.ts:    змінні оточення для перевірки локально зібраного проекту.
                     В pom.xml фронтенду доданий профіль local-build, який треба активувати для локальної
                     перевірки зібраного проекту. Зібраний проект запускати в ОС, не з IDE.
                     В зміні оточення операційної системи треба внести наступні параметри:
                     LOGOUT_REDIRECT_URI=http://localhost:5000/login?logout=true
                     REDIRECT_URI=http://localhost:5000/oauth2/redirect,myandroidapp://oauth2/redirect,myiosapp://oauth2/redirect
environment.prod.ts:  змінні оточення для production.

Збірка проекту:
    mvn clean install

Після збірки і запуску додатку фронтенд і бекенд працють на одному сервері:
    http://localhost:5000

При розробці:
1. НЕ ЗМІНЮВАТИ в корені проекта pom.xml, а також ./frontend/pom.xml
2. Вся розробка по java в ./practic/src/main/java/com/group/practic
3. Всі необхідні для розробки java залежності додавати ./practic/pom.xml