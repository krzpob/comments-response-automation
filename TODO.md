# Backlog

## Funkcje główne

- [ ] Obsługa Instagram Business Login (wymagane do wysyłki DM przez Instagram Messaging API)
- [ ] Flow po dopasowaniu komentarza: wysyłka DM z przyciskiem (Button Template) + odpowiedź pod komentarzem że DM poszedł
- [ ] Obsługa webhooka `messaging_postbacks` — reakcja na kliknięcie przycisku w wiadomości

## Backoffice API

Podstawowy CRUD (`GET/POST/PUT/DELETE /api/rules`) już działa.
Do rozszerzenia:

- [ ] Wiele słów kluczowych na regułę (np. `["promo", "rabat", "kod"]`) — dopasowanie gdy komentarz zawiera DOWOLNE z nich
- [ ] Wiele wariantów odpowiedzi pod komentarzem — losowanie jednej z X przy każdym dopasowaniu
- [ ] Endpoint do podglądu historii zdarzeń (`GET /api/events`) z filtrowaniem po stronie, regule, dacie
- [ ] Endpoint do statystyk (`GET /api/stats`) — ile DM wysłanych, ile odrzuconych, ile postbacków

## Nice to have

- [ ] Prosta autoryzacja backoffice API (Basic Auth lub API key w nagłówku)
