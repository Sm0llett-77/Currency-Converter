# 💱 Currency Converter

Мини-сервис для получения актуального курса валют с использованием API [OpenExchangeRates.org](https://openexchangerates.org/). Доступны версии на **Kotlin** и **Java**.

---

## 🔧 Требования

- Java **21+**
- Доступ в интернет
- API-ключ с сайта [https://openexchangerates.org](https://openexchangerates.org) *(вставляется в `application.yml`)*

---

## ▶️ Запуск

### 📁 Шаг 1: Распакуйте архив



### 🚀 Шаг 2: Запуск сервера

#### Kotlin-приложение:
Перейдите в папку:

```
currency-converter-kotlin/build/libs/
```

И запустите:

```
java -jar currency-converter-0.1.0.jar
```

#### Java-приложение:
Перейдите в папку:

```
currency-converter-java/build/libs/
```

И запустите:

```
currency-converter-java-0.1.0.jar
```

### 🌐 Шаг 3: Работа с веб-интерфейсом

1. Откройте в браузере: [http://localhost:8080](http://localhost:8080)
2. Выберите две валюты из выпадающего списка
3. Нажмите **"Get Rate"** — отобразится курс
4. Для завершения сервера нажмите кнопку **"Shutdown Server"**

---

## 📌 Возможности

- 🔄 Поддержка выбора двух валют
- 📊 Получение актуального курса обмена через API
- ⛔ Кнопка завершения работы сервиса
- 🌍 Доступ через веб-интерфейс

---
