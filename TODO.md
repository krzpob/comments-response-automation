# Backlog

## Funkcje główne

- [ ] Obsługa Instagram Business Login (wymagane do wysyłki DM przez Instagram Messaging API)
- [ ] Flow po dopasowaniu komentarza: wysyłka DM z przyciskiem + odpowiedź pod komentarzem że DM poszedł
- [ ] Obsługa webhooka `messaging_postbacks` — reakcja na kliknięcie przycisku → wysyłka wiadomości 2 (z linkiem)

### Ograniczenie Meta — dozwolony flow wiadomości

Meta nie pozwala na linki w pierwszej wiadomości do użytkownika.
Jedyna dozwolona interakcja w wiadomości 1 to przycisk wywołujący odpowiedź ze słowem kluczowym (postback).

Docelowy flow:
```
Komentarz z keyword
  → [Wiadomość 1] tekst + przycisk (bez linków, postback payload)
  → odpowiedź pod komentarzem "sprawdź DM"
  → użytkownik klika przycisk
  → [Wiadomość 2] właściwa treść z linkiem / ofertą
```

Dwa dozwolone warianty wiadomości 1 (wybierane per reguła):

**Wariant A — text prompt:**
> "Napisz X by otrzymać więcej informacji / link do oferty"
> Użytkownik odpisuje słowem kluczowym → webhook `messages` → wysyłka wiadomości 2

**Wariant B — przycisk:**
> "Kliknij przycisk by otrzymać więcej informacji" + [przycisk]
> Użytkownik klika → webhook `messaging_postbacks` → wysyłka wiadomości 2

Model `KeywordRule` musi przechowywać:
- `dmType`            — enum: `TEXT_PROMPT` | `BUTTON`
- `dmMessage`         — treść wiadomości 1 (bez linków)
- `dmTriggerKeyword`  — (wariant A) słowo które użytkownik musi wpisać
- `dmButtonLabel`     — (wariant B) etykieta przycisku
- `dmButtonPayload`   — (wariant B) payload postbacku
- `followUpMessage`   — treść wiadomości 2 (może zawierać link)
- `commentReply`      — odpowiedź pod komentarzem

## Backoffice API

Podstawowy CRUD (`GET/POST/PUT/DELETE /api/rules`) już działa.
Do rozszerzenia:

- [ ] Wiele słów kluczowych na regułę (np. `["promo", "rabat", "kod"]`) — dopasowanie gdy komentarz zawiera DOWOLNE z nich
- [ ] Wiele wariantów odpowiedzi pod komentarzem — losowanie jednej z X przy każdym dopasowaniu
- [ ] Endpoint do podglądu historii zdarzeń (`GET /api/events`) z filtrowaniem po stronie, regule, dacie
- [ ] Endpoint do statystyk (`GET /api/stats`) — ile DM wysłanych, ile odrzuconych, ile postbacków

## Nice to have

- [ ] Prosta autoryzacja backoffice API (Basic Auth lub API key w nagłówku)
