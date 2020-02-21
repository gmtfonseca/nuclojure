# Nuclojure

This is my first approach to a Clojure project, and I've not only learned a ton of Clojure main features and nature, but also the beauty of the Functional Programming paradigm.

For my first Clojure project, I've decided to implement a simple financial REST API to create financial "movements" - "transaction" would be a better name here, but it would probably mislead some people to the existing database term. Apart from the domain specific routes, I've also included JWT based authentication.

This project was built mainly with the following libraries/technologies:

- [Compojure](https://github.com/weavejester/compojure): Routing library for [Ring](https://github.com/ring-clojure/ring).
- [Liberator](https://github.com/clojure-liberator/liberator): Great to work with the HTTP protocol and build RESTful APIs.
- [Buddy](https://github.com/funcool/buddy): Security (JWT Authentication + SHA256 encryption).
- [Datomic](https://www.datomic.com/): Immutable database with flexible schema. This projects connects to a local Peer Server and uses in-memory storage, therefore the data is volatile.

## Future work

Since this is an educational project, I would like to list some future work to be done:

- Create more abstractions wtih "protocols" and "defmulti" (one use case for this would be the DB interface, which should support interchangable dabatases)
- Include Async handling
- Include more tests


## Prerequisites

You will need [Leiningen][] 2.0.0 or above installed.

[leiningen]: https://github.com/technomancy/leiningen


## Running

To start a web server for the application, run:

    lein ring server
