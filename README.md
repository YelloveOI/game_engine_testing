# TS1 Semester work

## Návrh testovací strategie
### Popis projektu pro testování
Projektem je jednoduchý herní engine, který jsem vytvořil v rámcích semestrální práce z předmětu PJV. Ze specifikací požadavku semestralky z PJV můžeme vyjádřit systémové požadavky projektu rozdělením původních požadavků na dílčí části.

![image](https://user-images.githubusercontent.com/68948498/153920723-246dfbce-0120-452d-966e-3be34eb5a109.png)
![image](https://user-images.githubusercontent.com/68948498/153920741-3e3323d8-52c7-4b00-ab95-74add869870c.png)

### Testovaci strategie
Samotný program byl sestaven podle architektury MVC a podle ní bych rozděloval celý program na komponenty.
Tím pádem máme části:
- Modul správce úrovně
-	Modul správce hrdiny
-	Modul správce nepřátele
-	Modul zobrazení
-	Modul správce souborů

A procesy:
-	Načtení soubora
-	Zápis do soubora
- Zobrazení (grafické)
-	Ošetření události hrdiny
-	Ošetření události nepřátele

![image](https://user-images.githubusercontent.com/68948498/153921045-21a4195b-b8f5-4a44-8e7a-6f8f135d0b11.png)

V rámcích této semestralky budeme uvažovat Unit testy a Procesní testy. V projektu GameEngine nejsou poskytnuté souvislostí s jinými systémy a proto, v souladu s definicí, Integrační testy provázet nebudeme.

![image](https://user-images.githubusercontent.com/68948498/153921209-ba084bc4-ad10-4b24-83b0-e8d6bcdb0567.png)
![image](https://user-images.githubusercontent.com/68948498/153921228-88b9444c-d57e-4954-8411-538932ae7c03.png)

## Testovací scénáře
### Třídy ekvivalence pro funkci setDirection *EnemyController*
Z dokumentací víme: tato funkce vrací prostou stringovou reprezentaci směrového vektoru nepřítele. Tato funkce počítá délky úseček od konce jednotkového směrového vektoru do dvou diagonálních bodu (např. do bodu UP-LEFT a UP-RIGHT), pak skládá ty hodnoty a porovnává s délkou ¼ kružnice. Když ten součet je menší, zapisuje do řetězce slovní reprezentaci směru (napr. ÚP).

Tříd ekvivalence bude 4 podle výstupů a každá bude reprezentována intervalem vektoru mezi diagonálními body V dokumentaci není jednoznačně určený mezní podmínky a proto řekneme, že vektory s končí v UP-LEFT a DOWN-LEFT patří do EC “LEFT” a vektory s konci UP-RIGHT a DOWN-RIGHT patří do EC “RIGHT”. Jedná se o jednotkových vektorech a o kružnici s r = 1. Z toho určíme vektory, generující každou EC (A = (〖_(sin α cosα)^(cos α -sinα)〗), v = (jednotkový) vektor s konci v UP-RIGHT) :
	LEFT =  {v_L | v_L  =Av,60≤α≤120 }
	RIGHT = {v_R | v_R  =Av,-60≤α≤0 }
	UP = {v_U | v_U  =Av,0<α<60 }
	DOWN = {v_D | v_D  =Av,-120<α<-60 }
V programu pro reprezentaci vektorů se používá třída Position mající parametry x a y. Z toho máme:
	LEFT = {x,y| -1≤x≤-0.707,|y| ≤ 0.707}
	RIGHT = {x,y| 0.707≤x≤1,|y| ≤ 0.707}
	UP = {x,y| |x|≤0.707,0.707≤y≤1}
	DOWN = {x,y| |x|≤0.707,-1≤y≤-0.707}
 
 ![image](https://user-images.githubusercontent.com/68948498/153921434-de1e8d0d-a9d2-4d6c-95ed-d187f118205a.png)
 
Vím, že tato funkce má triviální vstup, proto uměle zkomplikujeme vstupy a řekněme, že kromě směrového vektoru funkce bude přijímat na vstup jese 3 parametry: int a, int b, String č. Doplňková podmínka je, když a > b výstup je c, jinak podle logiky funkci výše. Z toho určíme nové EC:

	LEFT =  {v_(L ),a,b,c| (a>b∩c="LEFT")∪(a≤ b∩v_L=Av∩60≤α≤120)}
	RIGHT = {v_R,a,b,c| (a>b∩c="RIGHT")∪(a≤b∩v_R=Av∩-60≤α≤0)}
	UP = {v_U,a,b,c|(a>b∩c="UP")∪(a≤b∩v_U=Av∩0<α<60)}
	DOWN = {v_D,a,b,c|(a>b∩c="DOWN")∪(a≤b∩v_D=Av∩-120<α<-60)}
Určíme vstupní data pokrývající všechny EC:
	v_L=(〖_0.707^(-0.707)〗), v_L=(〖_(-0.707)^(-0.707)〗)
	v_R=(〖_0.707^0.707〗), v_R=(〖_(-0.707)^0.707〗)
	v_U=(〖_1^0〗)
	v_D=(〖_(-1)^0〗)
	c="LEFT",c="RIGHT", c="UP", c="DOWN"
