# Comments Response Automation

Automatyczna odpowiedź na komentarze Facebook/Instagram według reguł słów kluczowych.

**Flow:** Komentarz → dopasowanie słowa kluczowego → DM do autora → odpowiedź pod komentarzem

---

## Architektura

```
adapter/inbound/web          ← kontrolery HTTP (webhook Meta, REST API reguł)
domain/port/inbound          ← interfejsy use case
domain/service               ← logika biznesowa
domain/port/outbound         ← interfejsy repozytoriów i portów zewnętrznych
adapter/outbound/persistence ← JPA + MySQL
adapter/outbound/meta        ← Meta Graph API (DM + komentarze)
```

## Budowanie obrazu Docker

```bash
cp .env.example .env
# edytuj .env i uzupełnij tokeny

docker build -t comments-response-automation:latest .
```

## Wdrożenie na Synology DS918+

### Przez Container Manager (Compose)

1. W DSM otwórz **Container Manager → Project → Create**
2. Wskaż folder z plikami (przez File Station lub SSH)
3. Wgraj `docker-compose.yml` i `.env` do wybranego folderu na NAS-ie
4. Kliknij **Build** — Container Manager uruchomi oba kontenery (`db` + `app`)

Alternatywnie przez SSH:
```bash
ssh admin@<nas-ip>
cd /volume1/docker/comments-automation
docker compose up -d
```

### Wymagane zmienne środowiskowe (`.env`)

| Zmienna | Opis |
|---|---|
| `META_PAGE_ACCESS_TOKEN` | Page Access Token ze Strony Facebook/Instagram |
| `META_WEBHOOK_VERIFY_TOKEN` | Dowolny token weryfikacyjny — wpisujesz go też w panelu Meta |
| `DB_PASSWORD` | Hasło użytkownika bazy danych |
| `MYSQL_ROOT_PASSWORD` | Hasło root MySQL |

## Konfiguracja webhooka w Meta

1. Wejdź na [developers.facebook.com](https://developers.facebook.com) → Twoja aplikacja → Webhooks
2. Dodaj subskrypcję dla **Page** lub **Instagram** z polem `comments`
3. Callback URL: `https://<twoja-domena>/webhook`
4. Verify token: wartość z `META_WEBHOOK_VERIFY_TOKEN`

> NAS musi być dostępny z internetu. Użyj Synology DDNS lub własnej domeny z przekierowaniem portu 8080.

## REST API — zarządzanie regułami

```bash
# Lista reguł
GET /api/rules

# Utwórz regułę
POST /api/rules
{
  "keyword": "promo",
  "dmTemplate": "Hej! Napisz do nas po szczegóły promocji: {keyword}",
  "commentReplyTemplate": "Sprawdź prywatną wiadomość!",
  "pageId": null,       # null = globalna (wszystkie strony)
  "enabled": true
}

# Aktualizuj
PUT /api/rules/{id}

# Usuń
DELETE /api/rules/{id}
```

`{keyword}` w szablonie zostaje zastąpiony dopasowanym słowem kluczowym.

## Health check

```
GET /actuator/health
```
