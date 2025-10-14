<conversation_summary>
<decisions>

Persony i cel: Dietetyk prowadzi jednoczesny mentoring dla kilku klientów (edukacja + kontrola), klient dąży do zmiany nawyków i redukcji masy ciała.

Harmonogram programu: Domyślnie 6 tygodni; długość definiowana przy tworzeniu grupy. Grupa startuje w tym samym czasie dla wszystkich; brak dołączania w trakcie (MVP).

Zakres ról i tenantów: Jeden dietetyk w systemie, wiele grup; separacja danych per grupa (krok w stronę multi-tenancy).

Platforma: Responsywny web/PWA (start).

Onboarding i dane: Minimum danych (e-mail, imię/pseudonim), obowiązkowa akceptacja Regulaminu/Polityki Prywatności oraz checkbox o charakterze edukacyjnym; double opt-in „do rozważenia”.

Zaproszenia do grup: Linki zaproszeniowe z datą ważności oraz możliwość ręcznego dodania przez dietetyka.

Treści: Posty tekst + obraz (lokalnie) + wideo jako link (YouTube/Vimeo). Limity: obraz do 5 MB; wymagane alt-texty. Cykl: szkic → planowanie → publikacja; edycja po publikacji z etykietą „edytowano”; 1 przypięty post/grupę; proste tagi.

Komentarze i polubienia: Komentarze w obrębie grupy; brak możliwości wyłączania komentarzy w MVP; usuwanie przez dietetyka oraz prosty mechanizm zgłaszania nadużyć.

Raportowanie wagi: Wejście w kg z dokładnością do 0,1; możliwość dodania „dzisiejszej” wagi do 02:00 dnia następnego (strefa użytkownika); pole „notatka”; wykres postępów 7/30/90 dni (z poprzednich ustaleń).

Powiadomienia: Brak ustawień powiadomień w MVP (kanały i scenariusze do doprecyzowania).

Analityka: Brak wydarzeń analitycznych w MVP.

Bezpieczeństwo/zgodność: Minimalizacja PII, szyfrowanie w tranzycie i w spoczynku, RBAC (dietetyk/klient), retencja 24 mies., DPIA, łatwy eksport/usuwanie danych.

Uwierzytelnianie: Logowanie hasłem (bez „udziwnień”); reset hasła wymagany.

Limity i niezawodność: Do 20 grup łącznie i 200 klientów; dostępność 99,5%.

Kryterium sukcesu produktu: ≥50% klientów z redukcją masy ≥2 kg na koniec mentoringu; definicje bazowej i końcowej wagi: średnia z pierwszych/ostatnich 3 dni.

</decisions>

<matched_recommendations>

PWA jako pierwszy kanał — zaakceptowane.

Jeden dietetyk, wiele grup, separacja danych per grupa — zaakceptowane.

Linki zaproszeniowe + ręczne dodanie — zaakceptowane.

Post lifecycle (szkice, planowanie, edycja, przypięty, tagi) — zaakceptowane.

Media: S3-kompatybilny storage, 3 rozmiary, proporcje 4:3/1:1, limit 5 MB, alt-text — zaakceptowane.

Komentarze: moderacja przez dietetyka i zgłoszenia nadużyć — zaakceptowane; wyłączanie komentarzy — odrzucone w MVP.

Raportowanie wagi: 0,1 kg, późny wpis do 02:00, notatka, wykres zakresów — zaakceptowane (alerty anomalii i wygładzanie — odłożone).

Powiadomienia: pierwotnie rekomendowano e-mail + web push z preferencjami; w MVP — brak ustawień (częściowo przyjęte/ograniczone, kanały do doprecyzowania).

Analityka i dashboard: rekomendowana — odłożona poza MVP.

Uwierzytelnianie: rekomendowano magic link + 2FA dla dietetyka; wybrano proste hasło — nieprzyjęte (należy zadbać o bezpieczny reset i politykę haseł).
</matched_recommendations>

<prd_planning_summary>
a. Główne wymagania funkcjonalne:

Konta i role: dietetyk (tworzy grupy, publikuje treści, moderuje, przegląda postępy) oraz klient (konsumuje treści, lajkuje/komentuje, loguje wagę).

Zarządzanie grupami: tworzenie grup z konfigurowalną długością (domyślnie 6 tyg.), jednoczesny start, brak dołączania po starcie, zaproszenia linkowe + ręczne dodanie.

Publikowanie treści: posty tekst/obraz (upload) i wideo (link), szkice, planowanie publikacji, edycja po publikacji (etykieta „edytowano”), 1 przypięty, tagi.

Interakcje: polubienia i komentarze w obrębie grupy; zgłaszanie nadużyć; usuwanie komentarzy przez dietetyka.

Raportowanie wagi: codzienny wpis w kg (0,1), możliwość późnego wpisu do 02:00 lokalnie, notatki; wizualizacja postępów (7/30/90 dni) oraz podgląd zbiorczy dla dietetyka.

Powiadomienia: w MVP brak ustawień; minimalny domyślny scenariusz do ustalenia (np. informacja o nowym poście/przypomnienie wagi).

Zgodność i bezpieczeństwo: minimalne PII, szyfrowanie, retencja 24 mies., RBAC, eksport/usuwanie danych, DPIA.

Uwierzytelnianie: logowanie hasłem, reset hasła; polityka haseł minimalna (do doprecyzowania).

NFR/limity: do 20 grup, 200 klientów, dostępność 99,5%.

b. Kluczowe historie użytkownika i ścieżki:

Dietetyk: (1) Loguje się → (2) Tworzy grupę (nazwa, długość, data startu) → (3) Generuje linki zaproszeniowe lub dodaje klientów ręcznie → (4) Tworzy szkic posta, planuje publikację, opcjonalnie przypina → (5) Moderuje komentarze i zgłoszenia → (6) Przegląda wagę klientów na wykresie i w podglądzie zbiorczym.

Klient: (1) Otrzymuje zaproszenie → (2) Rejestracja z e-mailem i imieniem/pseudonimem + akceptacje → (3) Dołącza do grupy i czyta posty → (4) Lajkuje/komentuje → (5) Codziennie wpisuje wagę (z notatką), może uzupełnić do 02:00 następnego dnia → (6) Śledzi swój wykres 7/30/90 dni.

Administracja/zgodność: (1) Użytkownik żąda eksportu/usunięcia danych → (2) System realizuje żądanie zgodnie z retencją i RODO.

c. Kryteria sukcesu i pomiar:

Główne KPI produktu: ≥50% klientów z redukcją ≥2 kg na koniec programu. Pomiar: waga bazowa = średnia z pierwszych 3 dni, wynik końcowy = średnia z ostatnich 3 dni; porównanie per klient, następnie udział % w grupie/programie.

Dodatkowe (poza MVP/analityka): aktywacja, retencja tyg., DAU/WAU, % przeczytanych postów, mediana czasu do 1. komentarza — odłożone.

NFR: dostępność 99,5% (ok. ≤3,65 dnia niedostępności/rok); metryki RPO/RTO do doprecyzowania.

d. Uzupełnienia techniczne (wynikające z decyzji):

Storage: S3-kompatybilny, generowanie 3 wariantów obrazu; egzekwowanie limitów po stronie backendu.

Moderacja: log działań (kto/co/kiedy usunął), proste zgłoszenia nadużyć.

Wykresy: zakresy 7/30/90 dni; widok klienta i agregat dla dietetyka.
</prd_planning_summary>

<unresolved_issues>

Powiadomienia: czy w MVP mają działać domyślne powiadomienia (jakie kanały: e-mail, web push? jakie zdarzenia: nowy post, przypomnienie wagi?), skoro „brak ustawień” — potrzebna jasna definicja minimum.

Double opt-in dla zaproszeń: wdrażać w MVP czy później?

Polityka haseł i reset: minimalne wymagania (długość/kompleksowość), blokada brute-force, timeout sesji.

Analityka a pomiar sukcesu: bez eventów w MVP jak weryfikować otwarcia/zaangażowanie w treści; czy wystarczy pomiar wyłącznie na podstawie wpisów wagi?

Alerty anomalii wagi i „quiet hours”: czy dodawać proste reguły (np. brak wpisu X dni, skok >5%) i kiedy wysyłać komunikaty (jeśli w ogóle w MVP)?

RPO/RTO, backupy i monitorowanie: wartości docelowe i narzędzia (backupy bazy/obiektów, alerting).

Zakres dostępności wykresu: czy dietetyk widzi porównania między klientami w obrębie grupy (anonimizacja/poszanowanie prywatności)?

i18n/a11y: języki w MVP (PL-only?) i minimalne wymagania dostępności (kontrast, aria-label, klawiatura).

Regulamin/Polityka: finalne brzmienie klauzul (w tym „to nie porada medyczna”) i proces eksportu/usunięcia danych (SLA).

Limity techniczne: maks. długości/rozmiary pól (post, komentarz), rate-limity API, retencja logów.
</unresolved_issues>
</conversation_summary>