# WikiFinder

Projekt ma na celu zindeksowanie oraz przeszukiwanie korpusu Wikipedii.

Na projekt składają się trzy moduły:
* `wiki-indexer` - tworzy indeks na podstawie plik z korpusem Wikipedii
* `wiki-finder`  - przeszukuje utworzony wczesniej index
* `wedt` - RESTful Web Service
 
Plik `wikifinder.properties` - plik z ustawieniami. Znajdują się w nim dwa parametry:
* miejsce, gdzie tworzymy indeks: `index-path=$HOME/tmp/wiki-finder/index`
* źródłowy plik z korpusem: `source-file=$HOME/tmp/wiki-finder/wiki-finder/plwiki-latest-pages-articles-multistream.xml`
 
### Tworzenie + przeszukiwanie indeksu

1. budujemy paczki za pomocą mvn:
    ```
    cd wiki-finder-project
    mvn package
    ````
    
2. jary pojawiają sie w katalogach
    * `wiki-finder-project/wiki-indexer/target/`
    * `wiki-finder-project/wiki-finder/target/`

3. W miejscu, z którego uruchamiamy jary, należy utworzyć plik `wikifinder.properties`

### RESTful Web Service

1. Uruchomienie:

    ```shell
    cd wedt
    mvn spring-boot:run
    ```

2. [Test](http://localhost:8080/wiki-finder?url=http://blog.gridwise.pl/2013/05/text-mining-po-polsku-mo%C5%BCliwe.html)

3. Wynik:
    ```
    {"page":"http://blog.gridwise.pl/2013/05/text-mining-po-polsku-możliwe.html","findings":[{"index":1,"content":"po"},{"index":2,"content":"polski"},{"index":3,"content":"możliwe"}]}
    ```

Póki co dokonuje tylko stemizacji słów w tytule strony i zwraca wynik w strukturze JSON.
<<<<<<< HEAD

=======
>>>>>>> 6f64c1354060fdc8a6f37f739d5347f6d445b8fb
