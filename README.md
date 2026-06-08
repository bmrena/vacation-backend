## Teoretická otázka 1: Koľko služieb?

Keďže podľa zadania ide hlavne o zobrazenie jednej obrazovky s dovolenkami, navrhol som jeden hlavný endpoint, `GET /api/v1/vacation-screen?userId=1`,
ktorý vráti všetky dáta potrebné pre frontend.

Okrem hlavného endpointu som doplnil aj samostatné endpointy pre používateľov a dovolenky:
```
GET /api/v1/users
GET /api/v1/vacations
POST /api/v1/vacations
PATCH /api/v1/vacations/{id}/cancel
PATCH /api/v1/vacations/{id}/approve-manager
PATCH /api/v1/vacations/{id}/approve-director
```
## Teoretická otázka 2: Technológie
Použil som:
- Java
- Spring Boot
- Sping Web
- Spring Data JPA
- Hibernate
- PostgreSQL
- Lombok
- Swagger
- Maven

## Teoretická otázka 3: Zabezpečenie

V demo verzii nie je implementované prihlasovanie. Schvaľujúci používateľ sa posiela cez managerId alebo directorId.
V reálnej aplikácii by som použil Spring Security a JWT/session autentifikáciu.


V backend aplikácii je možné:
- zobraziť dáta pre obrazovku dovoleniek
- zobraziť používateľov
- zobraziť dovolenkové žiadosti
- zrušiť žiadosť
- schváliť dovolenku

Po spustení sa cez DataSeeder automaticky vložia ukážkové dáta.

Aplikácia používa PostgreSQL databázu, do ktorej sa ukladajú používatelia, ich roly a dovolenkové žiadosti.

Pre spustenie je potrebné si vytvoriť PostgreSQL databázu a nastaviť pripojenie v application.properties.

## Návrhové rozhodnutia

Projekt som rozdelil na vrstvy controller, service, repository, dto a mapper, aby bola oddelená REST časť, biznis logika a práca s databázou.

DTO triedy používam preto, aby controller nevracal priamo entity z databázy.

Pre výpočet počtu dní dovolenky používam BigDecimal, pretože dovolenka môže byť aj pol dňa.

Na stav dovolenky, časť dňa a používateľské roly používam enumy.

Schvaľovanie je riešené cez managerId a directorId.

Žiadosť o dovolenku sa po vytvorení uloží so stavom `PENDING`. Následne ju môže schváliť manažér. Zrušená žiadosť má stav `CANCELED` a už sa nedá ďalej schvaľovať.
## Vzorové requesty a response

Dáta pre obrazovku

Request:
`
GET /api/v1/vacation-screen?userId=1
`
Response:
```
{
"teams": [
"Všichni uživatelé",
"Development"
],
"users": [
{
"id": 1,
"fullName": "Michal Makas",
"username": "mmakas",
"team": "Development",
"roles": ["USER", "MANAGER"]
}
],
"selectedUser": {
"id": 1,
"fullName": "Michal Makas",
"username": "mmakas",
"team": "Development",
"roles": ["USER", "MANAGER"]
},
"vacations": [
{
"id": 1,
"createdDate": "2016-08-14",
"status": "APPROVED_BY_MANAGER",
"firstDay": "2016-09-05",
"firstDayPart": "FULL_DAY",
"lastDay": "2016-09-08",
"lastDayPart": "FULL_DAY",
"days": 4.0,
"approvedByManager": "Martin",
"approvedByDirector": null,
"canCancel": true
}
]
}
```
Vytvorenie dovolenky

Request:

`POST /api/v1/vacations`

Content-Type: application/json
```
{
"userId": 1,
"firstDay": "2026-07-01",
"firstDayPart": "MORNING",
"lastDay": "2026-07-01",
"lastDayPart": "EVENING"
}
```
Response:
```
{
"id": 7,
"createdDate": "2026-06-08",
"status": "PENDING",
"firstDay": "2026-07-01",
"firstDayPart": "MORNING",
"lastDay": "2026-07-01",
"lastDayPart": "EVENING",
"days": 1.0,
"approvedByManager": null,
"approvedByDirector": null,
"canCancel": true
}
```
Zrušenie dovolenky

Request:

`PATCH /api/v1/vacations/7/cancel`

Response:
```
{
"id": 7,
"status": "CANCELED",
"days": 1.0,
"approvedByManager": null,
"approvedByDirector": null,
"canCancel": false
}
```
Schválenie manažérom

Request:

`PATCH /api/v1/vacations/7/approve-manager?managerId=2`

Response:

```
{
"id": 7,
"status": "APPROVED_BY_MANAGER",
"days": 1.0,
"approvedByManager": "Martin",
"approvedByDirector": null,
"canCancel": true
}
```
Kompletný prehľad endpointov je dostupný aj cez Swagger:

http://localhost:8080/swagger-ui.html

<img width="1919" height="1026" alt="swagger" src="https://github.com/user-attachments/assets/1349a28b-4192-4345-8a6b-357a469d804a" />

## Bonusová úloha:
Unit testy a integračné testy nie sú spravené.

Unit testy by som doplnil hlavne pre service vrstvu, napríklad pre výpočet počtu dní dovolenky, vytvorenie novej dovolenky so statusom `PENDING`, zrušenie dovolenky so statusom `CANCELED`, schvaľovanie používateľom bez správnej role.

Integračné testy by som doplnil pre REST endpointy, napríklad `GET /api/v1/vacation-screen?userId=1`, `POST /api/v1/vacations`


