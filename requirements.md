Job Offers Application Requirements
As an application client, I want to view job offers for Junior Java Developers.

The system integrates with a remote HTTP server (a script that fetches offers from external websites).

Clients must use a valid authentication token to access job offers.

Clients can register within the application to obtain access.

Job offers in the database are updated every 3 hours by querying the remote server.

Duplicate offers are not allowed in the database; uniqueness is determined by the offer URL.

A client can retrieve a single job offer using its unique identifier.

A client can retrieve all available job offers when properly authorized.

If a client makes more than one request within 60 minutes, the data should be served from cache (to reduce database query costs for our customer).

A client can manually add a job offer to the system.

Each job offer must contain the following fields:

Offer URL

Job title

Company name

Salary (may be expressed as a range)




# typical path: user want to see offers but have to be logged in and external server should have some offers

step 1: there are no offers in external HTTP server (http://ec2-3-120-147-150.eu-central-1.compute.amazonaws.com:5057/offers)
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
step 16: user made POST /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and offer and system returned CREATED(201) with saved offer
step 17: user made GET /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and system returned OK(200) with 1 offer

