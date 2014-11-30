WikiFinder
==========
Projekt ma na celu zindeksowanie oraz przeszukiwanie korpusu wikipedii.

Na projekt składają się dwa moduły:
 - wiki-indexer - tworzy indeks na podstawie plik z korpusem wikipedii
 - wiki-finder  - przeszukuje utworzony wczesniej index
 
Plik wikifinder.properties - plik z ustawieniami. Znajdują się w nim dwa parametry
miejsce, gdzie tworzymy indeks  index-path=$HOME/tmp/wiki-finder/index
źródłowy plik z korpusem        source-file=$HOME/tmp/wiki-finder/wiki-finder/plwiki-latest-pages-articles-multistream.xml
 
Uruchomienie
============
1) budujemy paczki za pomocą mvn
cd wiki-finder-project
mvn package
2) jary pojawiają sie w katalogach
.../wiki-finder-project/wiki-indexer/target/
.../wiki-finder-project/wiki-finder/target/
3) W miejscy z którego uruchamiamy jary należy utworzyć plik wikifinder.properties

