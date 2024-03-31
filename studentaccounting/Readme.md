# Приложение предназначено для учета студентов с использованием консоли для ввода команд

## Основные функции приложения:
* Добавление студента в базу;
* Удаление студента по его идентификатору;
* Просмотр списка студентов;
* Загрузка списка студентов (в случае значения init для свойства app.storage);

## Запуск приложения
- Запуск приложения через среду разработки. Для загрузки студентов в базу перед запуском приложения необходимо в файле конфигурации `src/resources/application.yaml` указать расположение, 
для свойства app.storage указать значение init
```yaml
app:
  storage: init
```
- Запуск приложения в docker-контейнере. 
Перед запуском контейнера необходимо создать образ, используя команду: `docker build -t <image name> <path>`  
После создания образа необходимо запустить контейнер с помощью команды: `docker run -it <image name>`  
Для активации свойства app.storage необходимо указать соответствующую переменную среды: `docker run --env app.storage=init -it <image name>`


## Команды
В приложении доступны следующие команды:
* print - выводит список сохраненных студентов
* dall - удаляет всех студентов из базы
* del - удаляет студента из базы, после команды через пробел указывает id студента 
(в случае отсутствия студента с указанным id в базе будет выведено соответствующее сообщение)
* add - добавляет студента в базу, после команды через пробел указывается имя фамилия возраст студента  
`Иван Иванов 20`