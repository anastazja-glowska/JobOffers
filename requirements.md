# jako klient aplikacji chcę widzieć oferty pracy dla Junior Java Developera
korzystamy ze zdalnego serwera HTTP (skrypt który pobiera oferty ze stron WWW)
klient musi używać tokena, żeby zobaczyć oferty
klient może się zarejestrować
aktualizacja ofert w bazie danych jest co 3 godziny (wtedy odpytujemy zdalny serwer z pkt. 1)
oferty w bazie nie mogą się powtarzać (decyduje url oferty)
klient może pobrać jedną ofertę pracy poprzez unikalne Id
klient może pobrać wszystkie dostępne oferty kiedy jest zautoryzowany
jeśli klient w ciągu 60 minut robi więcej niż jedno zapytanie, to dane powinny pobierać się z cache (ponieważ pobieranie z bazy danych kosztuję pieniądze naszego klienta)
klient może ręcznie dodać ofertę pracy
każda oferta pracy ma (link do oferty, nazwę stanowiska, nazwę firmy, zarobki (mogą być widełki))




# typical path: user want to see offers but have to be logged in and external server should have some offers

step 1: there are no offers in external HTTP server (http://ec2-3-127-218-34.eu-central-1.compute.amazonaws.com:5057/offers)
step 2: scheduler ran 1st time and made GET to external server and system added 0 offers to database
step 3: user tried to get JWT token by requesting POST /token with username=someUser, password=somePassword and system returned UNAUTHORIZED(401)
step 4: user made GET /offers with no jwt token and system returned UNAUTHORIZED(401)
step 5: user made POST /register with username=someUser, password=somePassword and system registered user with status OK(200)
step 6: user tried to get JWT token by requesting POST /token with username=someUser, password=somePassword and system returned OK(200) and jwttoken=AAAA.BBBB.CCC
step 7: user made GET /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and system returned OK(200) with 0 offers
step 8: there are 2 new offers in external HTTP server
step 9: scheduler ran 2nd time and made GET to external server and system added 2 new offers with ids: 1000 and 2000 to database
step 10: user made GET /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and system returned OK(200) with 2 offers with ids: 1000 and 2000
step 11: user made GET /offers/9999 and system returned NOT_FOUND(404) with message “Offer with id 9999 not found”
step 12: user made GET /offers/1000 and system returned OK(200) with offer
step 13: there are 2 new offers in external HTTP server
step 14: scheduler ran 3rd time and made GET to external server and system added 2 new offers with ids: 3000 and 4000 to database
step 15: user made GET /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and system returned OK(200) with 4 offers with ids: 1000,2000, 3000 and 4000
