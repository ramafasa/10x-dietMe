# Dokument wymagań produktu (PRD) - DietMe

## 1. Przegląd produktu

DietMe to responsywna aplikacja webowa (PWA) zaprojektowana do wsparcia procesu zmiany nawyków żywieniowych poprzez ciągły kontakt między dietetykiem a klientami w ramach programów mentoringowych.

Aplikacja umożliwia dietetykom prowadzenie grup mentoringowych, publikowanie treści edukacyjnych oraz monitorowanie postępów klientów. Klienci otrzymują dostęp do wiedzy o zdrowym żywieniu, mogą wchodzić w interakcje z treściami oraz raportować codzienne postępy w redukcji masy ciała.

Wersja MVP koncentruje się na podstawowych funkcjonalnościach: zarządzanie grupami mentoringowymi, publikowanie treści (tekst, obraz, wideo), interakcje społecznościowe (polubienia, komentarze) oraz codzienne raportowanie wagi z wizualizacją postępów.

Kluczowe założenia:
- Jeden dietetyk w systemie prowadzący wiele grup
- Grupy mentoringowe o domyślnej długości 6 tygodni (konfigurowalna)
- Limit: do 20 grup i 200 klientów łącznie
- Platforma: responsywny web/PWA
- Dostępność: 99,5%
- Retencja danych: 24 miesiące

## 2. Problem użytkownika

Proces zmiany nawyków żywieniowych jest wyzwaniem wymagającym systematycznego wsparcia. Główne problemy:

1. Brak codziennego kontaktu z dietetykiem
   - Klienci tracą motywację między wizytami
   - Brak regularnego feedbacku na temat postępów
   - Trudność w utrzymaniu dyscypliny bez zewnętrznego wsparcia

2. Niewystarczająca edukacja żywieniowa
   - Klienci nie posiadają wystarczającej wiedzy o zdrowym żywieniu
   - Brak dostępu do sprawdzonych informacji w odpowiednim momencie
   - Trudność w zmianie utrwalonych nawyków bez zrozumienia przyczyn

3. Ograniczona możliwość monitorowania postępów
   - Dietetycy nie widzą codziennych postępów swoich klientów
   - Brak narzędzi do grupowego wsparcia mentoringowego
   - Klienci nie mają wizualizacji swoich osiągnięć w czasie rzeczywistym

4. Izolacja w procesie zmiany
   - Klienci czują się osamotnieni w swoim dążeniu do zmiany
   - Brak społeczności wspierającej podobne cele
   - Niewykorzystany potencjał grupowej motywacji

DietMe adresuje te problemy poprzez stworzenie platformy umożliwiającej:
- Codzienny kontakt dietetyka z grupą klientów poprzez treści edukacyjne
- Systematyczne raportowanie postępów z natychmiastową wizualizacją
- Społeczność mentoringową wspierającą się poprzez komentarze i interakcje
- Centralne miejsce dostępu do wiedzy i narzędzi motywacyjnych

## 3. Wymagania funkcjonalne

### 3.1. Zarządzanie kontami użytkowników

System obsługuje dwa typy kont:

Dietetyk:
- Pełne uprawnienia do zarządzania grupami, treściami i moderacji
- Dostęp do zagregowanych danych o postępach wszystkich klientów
- Możliwość tworzenia i edycji postów
- Prawo do usuwania komentarzy i moderacji zgłoszeń

Klient:
- Dostęp ograniczony do przypisanych grup mentoringowych
- Możliwość konsumowania treści, interakcji (polubienia, komentarze)
- Raportowanie własnej wagi i przeglądanie własnych postępów
- Brak dostępu do danych innych klientów poza ich publicznymi interakcjami

### 3.2. Uwierzytelnianie i autoryzacja

Logowanie:
- Logowanie za pomocą adresu e-mail i hasła
- Mechanizm bezpiecznego resetowania hasła (link wysyłany na e-mail)
- Walidacja siły hasła (minimalne wymagania do zdefiniowania)
- Sesje użytkowników z timeout

Onboarding:
- Rejestracja wymaga: adres e-mail, imię/pseudonim, hasło
- Obowiązkowa akceptacja Regulaminu i Polityki Prywatności
- Obowiązkowy checkbox edukacyjny (potwierdzenie, że aplikacja nie zastępuje konsultacji medycznej)

Bezpieczeństwo:
- Kontrola dostępu oparta na rolach (RBAC)
- Szyfrowanie danych w tranzycie (HTTPS) i spoczynku
- Minimalizacja zbieranych danych osobowych (PII)
- Wymóg DPIA (Data Protection Impact Assessment)

### 3.3. Zarządzanie grupami mentoringowymi

Tworzenie grupy (dietetyk):
- Nazwa grupy (wymagane)
- Opis grupy (opcjonalnie)
- Data startu programu (wymagane)
- Długość programu w tygodniach (domyślnie 6, konfigurowalne)
- Dana grupa startuje jednocześnie dla wszystkich członków
- Brak możliwości dołączania klientów po dacie startu w MVP

Zaproszenia do grupy:
- Generowanie linków zaproszeniowych z datą ważności
- Ręczne dodawanie klientów przez dietetyka (podanie e-mail)
- Klient może należeć do jednej grupy w danym momencie

Separacja danych:
- Pełna separacja treści i danych między grupami
- Klient widzi tylko treści i dane w ramach swoich grup
- Dietetyk ma dostęp do wszystkich swoich grup

### 3.4. Publikowanie i zarządzanie treściami

Typy postów:
- Post tekstowy (wymagane pole tekstowe, min. 1 znak)
- Post z obrazem (tekst + upload obrazu, formaty: JPG, PNG, WebP)
- Post z wideo (tekst + link do YouTube/Vimeo)
- Post może zawierać tekst + obraz lub tekst + wideo jednocześnie

Limity techniczne:
- Maksymalny rozmiar obrazu: 5 MB
- Obowiązkowy alt-text dla obrazów (dostępność)
- Automatyczne generowanie 3 wariantów rozmiaru obrazu (thumbnail, medium, full)
- Proporcje obrazu: 4:3 lub 1:1
- Storage: S3-kompatybilny

Lifecycle posta:
1. Szkic - post zapisany, niewidoczny dla klientów, można edytować
2. Zaplanowany - post z datą i godziną publikacji w przyszłości
3. Opublikowany - post widoczny dla klientów w przypisanych grupach

Edycja i zarządzanie:
- Edycja szkiców bez ograniczeń
- Usuwanie postów (tylko szkice i zaplanowane w MVP)
- Proste tagowanie postów (np. "przepis", "motywacja", "wiedza")

Widoczność:
- Post widoczny tylko dla wszystkich członków danej grupy w której został opublikowany
- Chronologiczna kolejność wyświetlania (najnowsze na górze)
- Przypięty post zawsze na szczycie listy

### 3.5. Interakcje społecznościowe

Polubienia:
- Klient może polubić każdy opublikowany post jednokrotnie
- Możliwość cofnięcia polubienia
- Licznik polubieniań widoczny dla wszystkich członków grupy

Komentarze:
- Klient może dodawać komentarze pod postami w swoich grupach
- Komentarze widoczne dla wszystkich członków danej grupy
- Chronologiczna kolejność komentarzy
- Dietetyk może komentować jako członek z wyróżnieniem (badge "Dietetyk")
- Brak możliwości wyłączania komentarzy pod postem w MVP
- Brak odpowiedzi na komentarze (threading)

Moderacja:
- Dietetyk może usuwać dowolny komentarz
- Brak automatycznej moderacji w MVP

### 3.6. Raportowanie wagi

Wprowadzanie danych:
- Codzienne raportowanie wagi przez klienta
- Wartość w kilogramach z dokładnością do 0,1 kg (np. 78.5 kg)
- Możliwość dodania "dzisiejszej" wagi do godziny 02:00 dnia następnego (według strefy czasowej użytkownika)
- Opcjonalne pole "notatka" (tekst do 500 znaków)
- Możliwość edycji wpisu tego samego dnia
- Brak możliwości usuwania historycznych wpisów przez klienta
- Brak możliwości wprowadzania wagi "wstecz" poza oknem 02:00

Wizualizacja dla klienta:
- Wykres liniowy postępów wagi
- Zakresy czasowe do wyboru: 7, 30, 90 dni
- Oznaczenie daty startu i końca programu na wykresie
- Wyświetlenie najniższej, najwyższej i aktualnej wagi w wybranym zakresie
- Oznaczenie dni bez wpisu wagi
- Możliwość podglądu notatek na wykresie (tooltip)

Widok dla dietetyka:
- Zagregowany widok postępów wszystkich klientów w grupie
- Lista klientów z kluczowymi metrykami:
  - Waga bazowa (średnia z pierwszych 3 dni)
  - Aktualna waga
  - Delta (różnica od wagi bazowej)
  - Data ostatniego wpisu
  - Compliance (% dni z wpisaną wagą)
- Wykres postępów pojedynczego klienta (po kliknięciu)
- Filtrowanie: wszystkie grupy / wybrana grupa
- Sortowanie po: nazwie, delcie, dacie ostatniego wpisu

Odłożone poza MVP:
- Automatyczne alerty przy anomaliach (np. nagły spadek/wzrost > 2kg)
- Wygładzanie wykresu (moving average)
- Eksport danych do CSV/PDF

### 3.7. Powiadomienia

W MVP:
- Brak ustawień powiadomień dla użytkownika
- Domyślny scenariusz powiadomień do minimalnego doprecyzowania (np. informacja o nowym poście, przypomnienie o wpisaniu wagi)
- Kanały dostarczania: e-mail

Odłożone poza MVP:
- Personalizowane preferencje powiadomień
- Wybór kanałów dostarczania przez użytkownika
- Zaawansowane scenariusze (np. powiadomienie o komentarzu, polubienie, osiągnięcie celu)

### 3.8. Zgodność z RODO i zarządzanie danymi

Minimalizacja danych:
- Zbieranie tylko niezbędnych danych osobowych
- Brak zbierania wrażliwych kategorii danych medycznych poza wagą
- Checkbox edukacyjny informujący, że aplikacja nie zastępuje konsultacji medycznej

Prawa użytkownika:
- Żądanie eksportu danych (format JSON lub PDF)
- Żądanie usunięcia konta i danych (Right to be forgotten)
- Realizacja żądań w ciągu 30 dni
- Informacja o przetwarzaniu danych dostępna w Polityce Prywatności

Retencja i usuwanie:
- Retencja danych przez 24 miesiące od zakończenia programu lub usunięcia konta
- Automatyczne usuwanie danych po upływie okresu retencji

Bezpieczeństwo:
- Szyfrowanie danych w tranzycie (TLS 1.3)
- Szyfrowanie danych w spoczynku (AES-256)

### 3.9. Wymagania niefunkcjonalne

Wydajność:
- Czas ładowania strony głównej: < 3s
- Czas renderowania wykresu wagi: < 2s
- Czas uploadu obrazu (5MB): < 10s

Dostępność:
- Docelowa dostępność: 99,5% (≤3,65 dnia niedostępności rocznie)

Skalowalność:
- System zaprojektowany dla max. 20 grup
- Max. 200 klientów łącznie
- Możliwość rozbudowy architektury w przyszłości

Responsywność:
- Wsparcie dla urządzeń mobilnych (iOS, Android)
- Wsparcie dla tabletów
- Wsparcie dla desktopów

## 4. Granice produktu

W skład MVP wchodzą:
- Zarządzanie grupami mentoringowymi przez jednego dietetyka
- Publikowanie treści tekstowych, graficznych i wideo (linki)
- Podstawowe interakcje społecznościowe (polubienia, komentarze)
- Codzienne raportowanie wagi z wizualizacją
- Podstawowe uwierzytelnianie (e-mail + hasło)
- Minimalna moderacja (usuwanie, zgłaszanie nadużyć)
- Separacja danych per grupa

Poza zakresem MVP:
- Multi-tenancy (wiele organizacji/dietetyków z własną przestrzenią)
- Tworzenie dedykowanych planów dietetycznych
- Automatyczne alerty i powiadomienia o anomaliach wagi
- Zaawansowane analityki i dashboard dla dietetyka (metryki aktywności, engagement)
- Personalizowane ustawienia powiadomień dla użytkowników
- Threading w komentarzach (odpowiedzi na komentarze)
- Wyłączanie komentarzy pod postem
- Wiadomości prywatne między klientem a dietetykiem
- Gamifikacja (odznaki, achievement, streaks)
- Integracje z urządzeniami IoT (smart scales, fitness trackers)
- Aplikacje natywne (iOS, Android)
- Kalendarz posiłków i przepisy
- Społeczność między grupami (cross-group interactions)
- Płatności i subskrypcje
- Marketplace z materiałami dietetycznymi
- Wideo-konsultacje
- Chat na żywo
- Eksport raportów PDF dla klienta
- Role pomocnicze (np. asystent dietetyka)
- Automatyczna moderacja AI
- Double opt-in przy rejestracji (do rozważenia w przyszłości)
- Magic link authentication
- 2FA dla dietetyka

## 5. Historyjki użytkowników

### 5.1. Uwierzytelnianie i onboarding

US-001: Rejestracja nowego klienta przez link zaproszeniowy
Jako potencjalny klient
Chcę zarejestrować się w aplikacji za pomocą linku zaproszeniowego
Aby dołączyć do grupy mentoringowej

Kryteria akceptacji:
- Link zaproszeniowy prowadzi do formularza rejestracji z pre-wypełnioną informacją o grupie
- Formularz wymaga: adres e-mail, imię/pseudonim, hasło (min. 8 znaków)
- Formularz zawiera obowiązkowe checkboxy: Regulamin, Polityka Prywatności, oświadczenie edukacyjne
- System waliduje unikalność adresu e-mail
- System waliduje ważność linku zaproszeniowego (data ważności, limit użyć)
- Po poprawnej rejestracji użytkownik jest automatycznie dodany do grupy
- Użytkownik otrzymuje e-mail powitalny z potwierdzeniem rejestracji
- Link wygasły lub wykorzystany wyświetla odpowiedni komunikat błędu

US-002: Logowanie do aplikacji
Jako zarejestrowany użytkownik
Chcę zalogować się do aplikacji
Aby uzyskać dostęp do swoich grup i treści

Kryteria akceptacji:
- Formularz logowania zawiera pola: e-mail, hasło
- System waliduje poprawność danych logowania
- Po poprawnym logowaniu użytkownik jest przekierowywany do głównego widoku aplikacji
- Po błędnym logowaniu wyświetlany jest ogólny komunikat błędu (bez ujawniania, czy e-mail istnieje)
- Sesja użytkownika jest zachowana przez określony czas (24h)
- Użytkownik widzi odpowiedni widok w zależności od roli (dietetyk/klient)

US-003: Reset hasła
Jako użytkownik, który zapomniał hasła
Chcę zresetować swoje hasło
Aby odzyskać dostęp do konta

Kryteria akceptacji:
- Formularz resetowania wymaga podania adresu e-mail
- System wysyła link resetujący na podany e-mail (jeśli konto istnieje)
- System zawsze wyświetla komunikat o wysłaniu linku (niezależnie czy e-mail istnieje - bezpieczeństwo)
- Link resetujący jest ważny przez 24 godziny
- Po kliknięciu w link użytkownik może ustawić nowe hasło
- Nowe hasło musi spełniać wymagania bezpieczeństwa (min. 8 znaków)
- Po zmianie hasła wszystkie aktywne sesje są unieważniane
- Użytkownik otrzymuje e-mail z potwierdzeniem zmiany hasła

US-004: Wylogowanie z aplikacji
Jako zalogowany użytkownik
Chcę wylogować się z aplikacji
Aby zakończyć sesję i zabezpieczyć swoje konto

Kryteria akceptacji:
- Przycisk "Wyloguj" dostępny w nawigacji/menu użytkownika
- Po kliknięciu użytkownik jest natychmiast wylogowywany
- Sesja użytkownika jest unieważniana
- Użytkownik jest przekierowywany do strony logowania
- Próba dostępu do chronionych zasobów przekierowuje do logowania

US-005: Ręczne dodanie klienta przez dietetyka
Jako dietetyk
Chcę ręcznie dodać klienta do grupy
Aby włączyć go do programu mentoringowego bez linku zaproszeniowego

Kryteria akceptacji:
- Formularz dodawania klienta wymaga: adres e-mail, imię/pseudonim, wybór grupy
- System sprawdza, czy użytkownik o podanym e-mailu już istnieje
- Jeśli użytkownik istnieje, jest dodawany do wybranej grupy
- Jeśli użytkownik nie istnieje, system wysyła e-mail z prośbą o dokończenie rejestracji (ustawienie hasła)
- Link w e-mailu jest ważny przez 7 dni
- Po dokończeniu rejestracji użytkownik jest automatycznie dodany do grupy
- Dietetyk widzi status dodania klienta (oczekuje na rejestrację / aktywny)

### 5.2. Zarządzanie grupami (dietetyk)

US-006: Tworzenie nowej grupy mentoringowej
Jako dietetyk
Chcę stworzyć nową grupę mentoringową
Aby rozpocząć program dla kolejnych klientów

Kryteria akceptacji:
- Formularz tworzenia grupy zawiera: nazwa (wymagane), opis (opcjonalnie), data startu, długość w tygodniach (domyślnie 6)
- System waliduje, że data startu nie jest w przeszłości
- System sprawdza, czy liczba grup nie przekracza limitu 20
- Po utworzeniu grupa ma status "oczekuje na start" do daty startu
- Data zakończenia jest automatycznie wyliczana na podstawie daty startu i długości
- Grupa jest widoczna na liście grup dietetyka ze statusem

US-007: Przeglądanie listy grup
Jako dietetyk
Chcę przeglądać listę wszystkich moich grup
Aby zarządzać wieloma programami mentoringowymi

Kryteria akceptacji:
- Lista wyświetla wszystkie grupy utworzone przez dietetyka
- Każda grupa pokazuje: nazwę, status (oczekuje/aktywna/zakończona), daty, liczbę członków
- Lista umożliwia filtrowanie po statusie
- Lista umożliwia sortowanie po dacie startu
- Kliknięcie w grupę otwiera widok szczegółów grupy

US-009: Przeglądanie członków grupy
Jako dietetyk
Chcę zobaczyć listę członków grupy
Aby wiedzieć, kto uczestniczy w programie

Kryteria akceptacji:
- Lista członków dostępna w szczegółach grupy
- Dla każdego członka wyświetlane: imię/pseudonim, e-mail, data dołączenia
- Lista umożliwia wyszukiwanie po imieniu/e-mailu
- Kliknięcie w członka pokazuje jego szczegółowe postępy

US-010: Usuwanie członka z grupy
Jako dietetyk
Chcę usunąć członka z grupy
Aby zarządzać składem grupy (np. rezygnacja klienta)

Kryteria akceptacji:
- Opcja usunięcia dostępna przy każdym członku grupy
- System wymaga potwierdzenia akcji
- Po usunięciu członek traci dostęp do treści grupy
- Historyczne dane członka (waga, komentarze) są zachowane dla celów raportowania
- System nie usuwa konta użytkownika, tylko przypisanie do grupy
- Dietetyk widzi potwierdzenie usunięcia

### 5.3. Zarządzanie treściami (dietetyk)

US-011: Tworzenie szkicu posta tekstowego
Jako dietetyk
Chcę stworzyć szkic posta tekstowego
Aby przygotować treść edukacyjną dla swoich klientów

Kryteria akceptacji:
- Formularz tworzenia posta zawiera: pole tekstowe (wymagane, min. 1 znak), wybór grupy (multi-select), tagi (opcjonalnie)
- Przycisk "Zapisz jako szkic" zapisuje post bez publikacji
- Szkic jest widoczny tylko dla dietetyka na liście szkiców
- Szkic można edytować bez ograniczeń
- System zapisuje datę utworzenia i ostatniej modyfikacji szkicu

US-012: Dodanie obrazu do posta
Jako dietetyk
Chcę dodać obraz do posta
Aby wzbogacić treść wizualnie

Kryteria akceptacji:
- Formularz posta zawiera opcję uploadu obrazu
- Akceptowane formaty: JPG, PNG, WebP
- Maksymalny rozmiar pliku: 5 MB
- System waliduje format i rozmiar przed uploadem
- Wymagane pole alt-text dla obrazu (dostępność)
- System generuje 3 warianty rozmiaru: thumbnail, medium, full
- Podgląd obrazu w formularzu przed zapisaniem
- Możliwość usunięcia i wymiany obrazu przed publikacją

US-013: Dodanie linku wideo do posta
Jako dietetyk
Chcę dodać link do wideo YouTube/Vimeo w poście
Aby udostępnić materiały wideo swoim klientom

Kryteria akceptacji:
- Formularz posta zawiera pole URL wideo
- System waliduje, czy URL pochodzi z YouTube lub Vimeo
- System wyciąga thumbnail z platformy wideo (jeśli możliwe)
- Podgląd embedowanego wideo w formularzu
- Możliwość usunięcia linku wideo przed publikacją

US-014: Planowanie publikacji posta
Jako dietetyk
Chcę zaplanować publikację posta na konkretną datę i godzinę
Aby automatycznie dostarczać treści zgodnie z harmonogramem programu

Kryteria akceptacji:
- Opcja "Zaplanuj publikację" w formularzu posta
- Wybór daty i godziny publikacji (nie wcześniej niż teraz)
- Post z zaplanowaną publikacją ma status "zaplanowany"
- Lista zaplanowanych postów widoczna dla dietetyka z datą publikacji
- System automatycznie publikuje post o zaplanowanej godzinie
- Możliwość anulowania zaplanowanej publikacji i powrotu do szkicu
- Możliwość edycji zaplanowanego posta przed publikacją

US-015: Publikacja posta natychmiastowa
Jako dietetyk
Chcę opublikować post natychmiast
Aby udostępnić treść swoim klientom w czasie rzeczywistym

Kryteria akceptacji:
- Przycisk "Opublikuj teraz" w formularzu posta
- System waliduje, że wszystkie wymagane pola są wypełnione
- Post staje się natychmiast widoczny dla członków wybranych grup
- Post otrzymuje timestamp publikacji
- Dietetyk widzi potwierdzenie publikacji
- Opublikowany post pojawia się na liście postów wybranych grup

US-017: Przypinanie posta
Jako dietetyk
Chcę przypiąć ważny post na górze listy
Aby zapewnić mu większą widoczność

Kryteria akceptacji:
- Opcja "Przypnij" dostępna dla opublikowanych postów
- Tylko 1 post może być przypięty w ramach grupy
- Przypięcie nowego posta odpina poprzedni
- Przypięty post wyświetlany na szczycie listy z ikoną pinezki
- Opcja "Odepnij" dla przypiętego posta
- Przypięty post działa w obrębie konkretnej grupy (nie globalnie)

US-018: Usuwanie posta
Jako dietetyk
Chcę usunąć post (szkic lub zaplanowany)
Aby usunąć niepotrzebną treść

Kryteria akceptacji:
- Opcja "Usuń" dostępna dla szkiców i zaplanowanych postów
- System wymaga potwierdzenia usunięcia
- Po usunięciu post znika z listy
- Brak możliwości usunięcia opublikowanego posta w MVP (tylko edycja)
- System usuwa powiązane pliki (obrazy) z storage

US-020: Tagowanie postów
Jako dietetyk
Chcę dodać tagi do posta
Aby kategoryzować treści dla łatwiejszego wyszukiwania

Kryteria akceptacji:
- Pole tagów w formularzu posta (multi-select lub comma-separated)
- Możliwość wyboru z istniejących tagów lub utworzenia nowego
- System sugeruje tagi podczas wpisywania
- Możliwość dodania/usunięcia tagów z opublikowanego posta
- Tagi widoczne dla klientów pod postem
- Brak filtrowania po tagach dla klientów w MVP

### 5.4. Przeglądanie treści i interakcje (klient)

US-021: Przeglądanie feed postów w grupie
Jako klient
Chcę przeglądać posty w mojej grupie mentoringowej
Aby konsumować treści edukacyjne od dietetyka

Kryteria akceptacji:
- Feed wyświetla posty chronologicznie (najnowsze na górze)
- Przypięty post zawsze na szczycie listy z wizualnym oznaczeniem
- Każdy post pokazuje: treść tekstową, obraz lub wideo, datę publikacji, liczbę polubieniń, liczbę komentarzy
- Etykieta "(edytowano)" widoczna dla edytowanych postów
- Lazy loading / paginacja dla długiej listy postów
- Możliwość odświeżenia feed (pull-to-refresh na mobile)

US-022: Wyświetlanie szczegółów posta
Jako klient
Chcę zobaczyć szczegóły pojedynczego posta
Aby przeczytać pełną treść i komentarze

Kryteria akceptacji:
- Kliknięcie w post otwiera widok szczegółów
- Widok zawiera pełną treść, obraz/wideo, datę publikacji, tagi
- Lista komentarzy widoczna pod postem
- Przycisk polubienia z licznikiem
- Formularz dodawania komentarza
- Możliwość powrotu do feed

US-023: Polubienie posta
Jako klient
Chcę polubić post
Aby wyrazić swoje zaangażowanie i aprobatę

Kryteria akceptacji:
- Przycisk "polub" (ikona serca) widoczny pod każdym postem
- Kliknięcie zmienia stan z "nie polubiony" na "polubiony"
- Ikona zmienia wygląd (np. wypełnione serce)
- Licznik polubieniań aktualizuje się natychmiast
- Możliwość cofnięcia polubienia (toggle)
- Jeden użytkownik może polubić post tylko raz

US-024: Dodawanie komentarza do posta
Jako klient
Chcę dodać komentarz pod postem
Aby wejść w interakcję z dietetykiem i innymi członkami grupy

Kryteria akceptacji:
- Formularz komentarza dostępny pod postem
- Pole tekstowe (min. 1 znak, max. 1000 znaków)
- Przycisk "Dodaj komentarz"
- Komentarz pojawia się natychmiast na liście po dodaniu
- Komentarz zawiera: treść, imię/pseudonim autora, datę dodania
- Brak możliwości edycji komentarza przez autora w MVP
- Brak możliwości usunięcia własnego komentarza przez autora w MVP

US-025: Zgłaszanie komentarza jako nadużycie
Jako klient
Chcę zgłosić nieodpowiedni komentarz
Aby poinformować dietetyka o potencjalnym naruszeniu zasad

Kryteria akceptacji:
- Opcja "Zgłoś" dostępna przy każdym komentarzu
- System wymaga potwierdzenia zgłoszenia
- Zgłoszenie trafia do dietetyka z informacją: treść komentarza, autor, zgłaszający, data
- Użytkownik widzi potwierdzenie zgłoszenia
- Brak limitu zgłoszeń w MVP
- Zgłoszony komentarz pozostaje widoczny do czasu moderacji

### 5.5. Moderacja (dietetyk)

US-027: Przeglądanie zgłoszeń nadużyć
Jako dietetyk
Chcę przeglądać zgłoszenia nadużyć
Aby moderować nieodpowiednie treści w grupach

Kryteria akceptacji:
- Lista zgłoszeń dostępna w panelu dietetyka
- Każde zgłoszenie zawiera: treść komentarza, autor komentarza, zgłaszający, data zgłoszenia, link do posta
- Możliwość filtrowania: nowe/zarchiwizowane
- Możliwość oznaczenia zgłoszenia jako "rozpatrzone"
- Opcja usunięcia zgłoszonego komentarza bezpośrednio z listy zgłoszeń

### 5.6. Raportowanie wagi (klient)

US-030: Dodawanie dziennego pomiaru wagi
Jako klient
Chcę codziennie wprowadzić swoją wagę
Aby śledzić postępy w redukcji masy ciała

Kryteria akceptacji:
- Formularz wprowadzania wagi dostępny w widoku głównym
- Pole wartości wagi w kg z dokładnością do 0,1 kg (np. 78.5)
- Opcjonalne pole notatki (max. 500 znaków)
- Możliwość dodania wagi "dzisiejszej" do godziny 02:00 następnego dnia (strefa czasowa użytkownika)
- System waliduje format (liczba dodatnia, max. 2 miejsca po przecinku)
- Po zapisaniu system wyświetla potwierdzenie
- Wpis pojawia się na wykresie postępów

US-031: Edycja dziennego pomiaru wagi
Jako klient
Chcę edytować dzisiejszy pomiar wagi
Aby poprawić błąd w wprowadzonych danych

Kryteria akceptacji:
- Możliwość edycji tylko wpisu z bieżącego dnia (do 02:00 następnego dnia)
- Formularz edycji pokazuje obecne wartości
- Możliwość zmiany wagi i notatki
- System zapisuje timestamp ostatniej edycji
- Brak możliwości edycji historycznych wpisów (> 1 dzień wstecz)
- Brak wizualnego oznaczenia edycji dla innych użytkowników

US-032: Przeglądanie wykresu własnych postępów
Jako klient
Chcę zobaczyć wykres swoich postępów wagowych
Aby śledzić zmiany swojej wagi w czasie

Kryteria akceptacji:
- Wykres liniowy widoczny w dedykowanym widoku "Moje postępy"
- Opcja wyboru zakresu: 7, 30, 90 dni
- Oś X: daty, oś Y: waga w kg
- Punkty na wykresie reprezentują pomiary
- Oznaczenie dat startu i końca programu (linie pionowe)
- Wyświetlenie metryk: waga bazowa, aktualna, najniższa, najwyższa, delta
- Dni bez wpisu oznaczone wizualnie (np. przerwa w linii)
- Tooltip z wartością i notatką po najechaniu na punkt

US-033: Przeglądanie historii wpisów wagi
Jako klient
Chcę zobaczyć listę wszystkich moich wpisów wagi
Aby mieć tabelaryczny widok moich pomiarów

Kryteria akceptacji:
- Lista wpisów widoczna pod wykresem lub w osobnej zakładce
- Każdy wpis zawiera: datę, wagę, notatkę (jeśli była)
- Lista posortowana chronologicznie (najnowsze na górze)
- Możliwość wyszukiwania/filtrowania po dacie
- Oznaczenie edytowanych wpisów (opcjonalnie)

US-034: Przypomnienie o braku wpisu wagi
Jako klient
Chcę otrzymać przypomnienie, jeśli nie wprowadziłem wagi danego dnia
Aby utrzymać regularność raportowania

Kryteria akceptacji:
- Przypomnienie domyślnie wysyłane, jeśli brak wpisu do godziny 18:00
- Kanał dostarczania: e-mail
- Przypomnienie zawiera bezpośredni link do formularza

### 5.7. Monitorowanie postępów (dietetyk)

US-035: Przeglądanie zbiorczego widoku postępów klientów
Jako dietetyk
Chcę zobaczyć postępy wszystkich moich klientów
Aby monitorować skuteczność programu mentoringowego

Kryteria akceptacji:
- Tabelaryczny widok z listą klientów
- Kolumny: imię/pseudonim, grupa, waga bazowa, aktualna waga, delta, ostatni wpis, compliance (% dni z wpisem)
- Możliwość filtrowania: wszystkie grupy / konkretna grupa
- Możliwość sortowania po dowolnej kolumnie
- Waga bazowa = średnia z pierwszych 3 dni programu
- Delta = różnica między wagą aktualną a bazową
- Compliance = (liczba dni z wpisem / liczba dni programu) * 100%
- Wizualne oznaczenie klientów bez wpisu > 3 dni

US-036: Przeglądanie szczegółów postępów pojedynczego klienta
Jako dietetyk
Chcę zobaczyć szczegółowy wykres postępów konkretnego klienta
Aby przeanalizować jego indywidualny proces

Kryteria akceptacji:
- Kliknięcie w klienta w zbiorczym widoku otwiera jego szczegóły
- Wykres liniowy identyczny jak w widoku klienta (7/30/90 dni)
- Widoczne wszystkie wpisy wagi z notatkami
- Metryki: waga bazowa, aktualna, najniższa, najwyższa, delta, compliance
- Możliwość powrotu do zbiorczego widoku

### 5.8. Zarządzanie danymi i RODO

US-041: Akceptacja Regulaminu i Polityki Prywatności
Jako nowy użytkownik
Chcę zapoznać się z Regulaminem i Polityką Prywatności
Aby świadomie zaakceptować warunki korzystania z aplikacji

Kryteria akceptacji:
- Linki do Regulaminu i Polityki Prywatności dostępne w formularzu rejestracji
- Obowiązkowe checkboxy: "Akceptuję Regulamin", "Akceptuję Politykę Prywatności"
- Trzeci checkbox: "Rozumiem, że aplikacja nie zastępuje konsultacji medycznej" (edukacyjny)
- Niemożliwa rejestracja bez zaznaczenia wszystkich checkboxów
- Dokumenty otwierają się w nowej karcie
- Data akceptacji zapisywana w systemie

US-042: Dostęp do Regulaminu i Polityki Prywatności
Jako zalogowany użytkownik
Chcę mieć dostęp do Regulaminu i Polityki Prywatności
Aby móc je przeglądać w dowolnym momencie

Kryteria akceptacji:
- Linki dostępne w stopce aplikacji
- Linki dostępne w ustawieniach konta
- Dokumenty otwierają się w nowej karcie lub modal
- Widoczna data ostatniej aktualizacji dokumentów
- W przypadku aktualizacji dokumentów użytkownik informowany przy następnym logowaniu (opcjonalnie - do rozważenia)

### 5.9. Nawigacja i UI/UX

US-043: Responsywna nawigacja
Jako użytkownik na różnych urządzeniach
Chcę mieć dostęp do nawigacji dostosowanej do rozmiaru ekranu
Aby wygodnie korzystać z aplikacji na mobile, tablecie i desktopie

Kryteria akceptacji:
- Na mobile: hamburger menu z rozwijaną nawigacją
- Na tablecie/desktopie: poziomy pasek nawigacji lub sidebar
- Nawigacja zawiera: Feed/Posty, Moje postępy (klient) / Postępy klientów (dietetyk), Moje grupy, Ustawienia, Wyloguj
- Aktywna sekcja wizualnie wyróżniona
- Nawigacja dostępna z poziomu klawiatury (accessibility)

US-044: Strona główna po zalogowaniu
Jako użytkownik
Chcę zobaczyć odpowiedni widok domyślny po zalogowaniu
Aby szybko rozpocząć korzystanie z aplikacji

Kryteria akceptacji:
- Klient widzi feed postów z grupy do której jest przypisany
- Dietetyk widzi dashboard z listą grup i szybkim dostępem do tworzenia posta
- Widoczne powiadomienia o nowych aktywnościach (jeśli są)
- Szybki dostęp do formularza dodawania wagi (klient)


US-046: Powiadomienie o nowych treściach
Jako klient
Chcę być informowany o nowych postach w mojej grupie
Aby nie przegapić ważnych treści edukacyjnych

Kryteria akceptacji:
- Brak konfiguracji powiadomień w MVP
- Kanał dostarczania - e-mail
- Powiadomienie zawiera: tytuł/fragment posta, link bezpośredni

US-047: Komunikaty błędów i walidacji
Jako użytkownik
Chcę widzieć jasne komunikaty błędów
Aby zrozumieć, co poszło nie tak i jak to naprawić

Kryteria akceptacji:
- Komunikaty błędów wyświetlane bezpośrednio przy polach formularza
- Komunikaty w języku polskim, zrozumiałe dla użytkownika
- Błędy walidacji w czasie rzeczywistym (np. za słabe hasło)
- Błędy serwera wyświetlane w formie toast/modal
- Możliwość zamknięcia komunikatu błędu

US-048: Loading states i feedback
Jako użytkownik
Chcę widzieć informację o trwających operacjach
Aby wiedzieć, że system przetwarza moje żądanie

Kryteria akceptacji:
- Spinner/loader podczas ładowania treści
- Disabled state przycisków podczas przetwarzania
- Komunikaty sukcesu po zakończeniu akcji (np. "Post opublikowany")
- Skeleton screens podczas ładowania list/feed
- Smooth transitions między stanami

### 5.10. Administracja i ustawienia

US-049: Edycja profilu użytkownika
Jako użytkownik
Chcę edytować swoje dane profilu
Aby aktualizować swoje informacje

Kryteria akceptacji:
- Formularz edycji profilu w ustawieniach konta
- Możliwość zmiany: imię/pseudonim
- Brak możliwości zmiany adresu e-mail w MVP (wymaga weryfikacji)
- Walidacja danych przed zapisaniem
- Potwierdzenie pomyślnej aktualizacji

US-050: Zmiana hasła
Jako użytkownik
Chcę zmienić swoje hasło
Aby zwiększyć bezpieczeństwo konta

Kryteria akceptacji:
- Formularz zmiany hasła w ustawieniach konta
- Wymagane: obecne hasło, nowe hasło, potwierdzenie nowego hasła
- Walidacja siły nowego hasła (min. 8 znaków)
- Po zmianie wszystkie aktywne sesje są unieważniane (opcjonalnie: poza bieżącą)
- E-mail z potwierdzeniem zmiany hasła
- Komunikat sukcesu w aplikacji

## 6. Metryki sukcesu

### 6.1. Główne KPI produktu

Kryterium sukcesu MVP:
≥50% klientów z redukcją masy ciała ≥2 kg na koniec programu mentoringowego

Metoda pomiaru:
- Waga bazowa = średnia arytmetyczna z pierwszych 3 dni programu (z wpisami wagi)
- Waga końcowa = średnia arytmetyczna z ostatnich 3 dni programu (z wpisami wagi)
- Redukcja = waga bazowa - waga końcowa
- Klient uznawany za sukces, jeśli redukcja ≥ 2.0 kg
- % sukcesu = (liczba klientów z redukcją ≥2kg / liczba wszystkich klientów w programie) × 100%

Warunki pomiaru:
- Klient musi mieć co najmniej 3 wpisy wagi w pierwszych 7 dniach programu (dla wagi bazowej)
- Klient musi mieć co najmniej 3 wpisy wagi w ostatnich 7 dniach programu (dla wagi końcowej)
- Jeśli klient nie spełnia warunków, nie jest brany pod uwagę w obliczeniach
- Pomiar wykonywany po zakończeniu programu (data końca + 7 dni buffer)

### 6.3. Metryki techniczne (NFR)

Dostępność:
- Uptime: 99,5%
- Maksymalna niedostępność: ≤3,65 dnia/rok (≤7,3h/miesiąc)

Wydajność:
- Czas ładowania strony głównej (p95): <3s
- Czas renderowania wykresu wagi (p95): <2s
- Czas uploadu obrazu 5MB (p95): <10s

Reliability:
- RPO (Recovery Point Objective): do zdefiniowania
- RTO (Recovery Time Objective): do zdefiniowania
- Częstość backupów: do zdefiniowania

### 6.4. Metryki biznesowe (poza zakresem MVP)

Skalowalność:
- Limit grup: 20
- Limit klientów: 200
- Średnia wielkość grupy: 10 klientów

Koszty:
- Koszt storage na klienta/miesiąc: do monitorowania
- Koszt compute na aktywnego użytkownika: do monitorowania

### 6.5. Plan pomiaru i raportowania

Częstotliwość pomiaru:
- Główne KPI: po zakończeniu każdego programu (6 tygodni)
- Metryki engagement: tygodniowo (odłożone poza MVP)
- Metryki techniczne: real-time monitoring (odłożone poza MVP)

Narzędzia:
- Dashboard dla dietetyka: wbudowany widok z kluczowymi metrykami sukcesu programu
- Analityka: odłożona poza MVP, do zaimplementowania w przyszłości
- Monitoring techniczny: do wyboru (np. Sentry, Datadog)

Responsibility:
- Product Owner: analiza KPI produktu, decyzje o iteracjach
- Dietetyk: monitoring postępów klientów, feedback o funkcjonalnościach
- Tech Lead: monitoring metryk technicznych, performance, reliability

### 6.6. Kryteria sukcesu MVP jako całości

MVP uznawane za sukces, jeśli:
1. Główne KPI: ≥50% klientów z redukcją ≥2kg (MUST HAVE)
2. Completion rate: ≥70% klientów kończy program (dodaje wagę w ostatnim tygodniu) (SHOULD HAVE)
3. Technical: Dostępność ≥99,5%, brak critical bugs (MUST HAVE)
4. User satisfaction: Feedback jakościowy od dietetyka i próbki klientów pozytywny (SHOULD HAVE)

Jeśli MVP spełni kryteria sukcesu:
- Iteracja na podstawie feedbacku użytkowników
- Implementacja zaawansowanych funkcjonalności (analityka, powiadomienia, gamifikacja)
- Przygotowanie do skalowania (multi-tenancy, więcej dietetyków)

Jeśli MVP nie spełni kryteriów:
- Analiza przyczyn niepowodzenia (tech vs. product vs. user adoption)
- Pivot lub iteracja z uwzględnieniem learnings
- Rozważenie kontynuacji projektu
