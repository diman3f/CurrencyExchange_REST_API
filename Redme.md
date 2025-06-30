**Проект №3. Обмен валют.**

Учебный проект "Обмен валют" предназначен для проведения обменных операций между валютами. Проект написал с целью разобраться в следующих технологиях:
1. Основа передачи данных в сети интернет, доменная область, ip адресация, модель osi 
2. Java Servlet как основа организации web приложения и сборки серверной части проекта. 
3. Приложение Tomcat как основа организации взаимодействия написанного java кода и поддержки развертывания web приложения на удаленном сервере 
4. Организация архитектуры приложения по принципу REST API запросов

**Основной функционал проекта:**
- Обмен валюты по прямому курсу 
- Обмен валют по обратному курсу
- Обмен валют по кросс курсу, возможность вычислить курс между валютами, на которые отсутствует прямой курс
- Добавление новой валюты
- Добавление курса пары валют
- Изменение курса валют 

В случае каких либо ошибок выводится сообщение с пояснением ошибки 

**Проект написан с помощью следующий инструментов:**
**Java 17, Tomcat 10, Sqlite, Maven**

Проект организован на основании готовой front-end части, которая взаимодействует с серверной частью путем отправки REST запросов. Серверная часть в ответ высылает ответ в формате JSON и сформированный HTTP response. 

**Для разворачивания проекта на вашем сервере:** 
Выполнить git clone репозитория
Переименовать host в файле app.js c точным указанием имени  war архива 
Собрать war архив с помощью maven package, нейминг архива задать как в host 
Папка exchangeDB должна располагаться в корне директории Tomcat для успешного подключения бд 



В приложении реализованы следующие **REST endpoints** :
### Currency

**GET** `/currencies` - to get all currencies

```
[
    {
"id" : 1,
  "name" : "Australian dollar",
  "code" : "AUD",
  "sign" : "A$"
}, {
  "id" : 2,
  "name" : "American Dollar",
  "code" : "USD",
  "sign" : "$"
}, {
  "id" : 3,
  "name" : "Russian Federation",
  "code" : "RUB",
  "sign" : "₽"
]
```


**GET** `/currency/RUB` - to get specific currency

```
{
  "id" : 3,
  "name" : "Russian Federation",
  "code" : "RUB",
  "sign" : "₽"
}
```

**POST** `/currencies` - to create a new currency

```
{
  id" : 1,
  "name" : "Australian dollar",
  "code" : "AUD",
  "sign" : "A$"
}
```

### Exchange rates

**GET** `/exchangeRates` - to get all exchange rates

```
[
   "id": 3,
    "baseCurrency": {
      "id": 2,
      "name": "American Dollar",
      "code": "USD",
      "sign": "$"
    },
    "targetCurrency": {
      "id": 7,
      "name": "Denar",
      "code": "MKD",
      "sign": "MDen"
    },
    "rate": 80
  },
  {
    "id": 5,
    "baseCurrency": {
      "id": 6,
      "name": "Belarussian Ruble",
      "code": "BYN",
      "sign": "Br"
    },
    "targetCurrency": {
      "id": 7,
      "name": "Denar",
      "code": "MKD",
      "sign": "MDen"
    },
    "rate": 55
]
```

**GET** `/exchangeRate/USDRUB - to get specific exchange rate

```
{
        {
    "id": 1,
    "baseCurrency": {
      "id": 2,
      "name": "American Dollar",
      "code": "USD",
      "sign": "$"
    },
    "targetCurrency": {
      "id": 3,
      "name": "Russian Federation",
      "code": "RUB",
      "sign": "₽"
    },
    "rate": 60
  }
    }
```

**POST** `/exchangeRate` - to create a new exchange rate

```
{
    "id": 12,
    "baseCurrency": {
        "code": "RUB",
        "name": "Russia Ruble",
        "sign": "₽"
    },
    "targetCurrency": {
        "code": "GEL",
        "name": "Georgian lari",
        "sign": "₾"
    },
    "rate": 21.32
}
```

**PUT** `/exchangeRate/USDGEL` - to change the exchange rate

```
{
    "id": 15,
    "baseCurrency": {
        "code": "RUB",
        "name": "Russia Ruble",
        "sign": "₽"
    },
    "targetCurrency": {
        "code": "GEL",
        "name": "Georgian lari",
        "sign": "₾"
    },
    "rate": 31.20
}
```

### Currency exchange

**GET** `/exchange?from=USD&to=RUB&amount=200 - currency conversion

```
{
    "exchangeRate": {
        "id": 20,
        "baseCurrency": {
            "code": "RUB",
            "name": "Russia Ruble",
            "sign": "₽"
        },
        "targetCurrency": {
            "code": "USD",
            "name": "United States Dollar",
            "sign": "$"
        },
        "rate": 100
    },
    "amount": 200,
    "convertedAmount": 20000
}
```

