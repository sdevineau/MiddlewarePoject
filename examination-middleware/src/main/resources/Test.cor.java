    import java.util.*;

interface SemiGroupeAdd<T> {
    T somme(T x);
}
interface MonoideAdd<T> extends SemiGroupeAdd<T> {
    T zero();
}
interface GroupeAdd<T> extends MonoideAdd<T> {
    T oppose();
}
interface SemiGroupeMul<T> {
    T produit(T x);
}
interface MonoideMul<T> extends SemiGroupeMul<T> {
    T un();
}
interface GroupeMul<T> extends MonoideMul<T> {
    T inverse();
}
interface Anneau<T> extends GroupeAdd<T>, SemiGroupeMul<T> {}
interface AnneauUnitaire<T> extends Anneau<T>, MonoideMul<T> {}
interface Corps<T> extends AnneauUnitaire<T>, GroupeMul<T> {}

class Calculs {

    /*<question id="q1"><segment id="q1s1">*/        public static <T extends GroupeAdd<T>> T soustraction(T x, T y)        /*</segment>*/        {
        /*<segment id="q1s2">*/            return x.somme(y.oppose());           /*</segment>*/
        
    }                /*</question>*/

    public static <T extends MonoideAdd<T>> T sommeNAire(T fab, List<T> l){
	T r = fab.zero();
	for(T e : l){
	    r = r.somme(e);
	}
	return r;
    }
}

interface Nat extends MonoideAdd<Nat>, MonoideMul<Nat> {
    boolean estZero(); // Teste à zéro l'entier naturel
    Nat predecesseur(); // Donne le prédécesseur s'il existe
    Nat creer(); // Crée un entier valant zéro 
    Nat creer(Nat pred); // Crée un entier naturel égal au successeur de pred
    int val(); // Convertit l'entier naturel en int   
    boolean equals(Object o); // Renvoie false
                              //   si o n'est pas de type Nat,
                              // teste l'égalité des entiers naturels sinon
    String toString(); // Affiche l'entier renvoyé par val
}

final class Zero implements Nat {
    public static final Nat singleton = new Zero();
    private Zero(){}
    public boolean estZero(){
	return true;
    }
    public Nat predecesseur(){
	return null;
    }
    public Nat creer(){
	return Zero.singleton;
    }
    public Nat creer(Nat pred){
	return new Succ(pred);
    }
    public Nat somme(Nat x){
	return x;
    }
    public Nat zero(){
	return creer();
    }
    public Nat produit(Nat x){
	return this;
    }
    public Nat un(){
	return creer(creer());
    }
    public int val(){
	return 0;
    }
    public boolean equals(Object o){
	if(!(o instanceof Nat)) return false;
	Nat x = (Nat)o;
	if(x.estZero()) return true;
	return false;
    }
    public String toString(){
	return "" + val();
    }
}
final class Succ implements Nat {
    private Nat pred;
    public Succ(Nat pred){
	this.pred = pred;
    }
    public boolean estZero(){
	return false;
    }
    public Nat predecesseur(){
	return pred;
    }
    public Nat creer(){
	return Zero.singleton;
    }
    public Nat creer(Nat pred){
	return new Succ(pred);
    }
    public Nat somme(Nat x){
	return creer(predecesseur().somme(x));
    }
    public Nat zero(){
	return creer();
    }
    public Nat produit(Nat x){
	return x.somme(predecesseur().produit(x));
    }
    public Nat un(){
	return creer(creer());
    }
    public int val(){
	return 1 + predecesseur().val();
    }	
    public boolean equals(Object o){
	if(!(o instanceof Nat)) return false;
	Nat x = (Nat)o;
	if(x.estZero()) return false;
	return predecesseur().equals(x.predecesseur());
    }
    public String toString(){
	return "" + val();
    }
}

class Couple<T1, T2> {
    public Couple(T1 c1, T2 c2){
	pi1 = c1;
	pi2 = c2;
    }
    public final T1 pi1;
    public final T2 pi2;
}


interface Z extends AnneauUnitaire<Z> {
    Z creer(Nat positif, Nat negatif); // Crée un entier relatif correspondant 
                                       //   à la différence positif - négatif  
    Couple<Nat, Nat> representant(); // Renvoie un couple dont la différence
                                     //   correspond à l'entier relatif
    int val(); // Convertit l'entier relatif en int   
    boolean equals(Object o); // Renvoie false
                              //   si o n'est pas de type Z,
                              // teste l'égalité des entiers relatifs sinon
    String toString(); // Affiche l'entier renvoyé par val
}

class Relatif implements Z {
    private Nat positif;
    private Nat negatif;
    public Relatif(){
	this(Zero.singleton, Zero.singleton);
    }
    public Relatif(Nat positif, Nat negatif){
	this.positif = positif;
	this.negatif = negatif;
    }
    public Couple<Nat, Nat> representant(){
	return new Couple<Nat, Nat>(positif, negatif);
    }
    public Z creer(Nat positif, Nat negatif){
	return new Relatif(positif, negatif);
    }
    public Z somme(Z x){
	Couple<Nat, Nat> rep = this.representant(); 
	Couple<Nat, Nat> repX = x.representant(); 
	Nat positif = rep.pi1.somme(repX.pi1);
	Nat negatif = rep.pi2.somme(repX.pi2);
	return creer(positif, negatif);
    }
    public Z zero(){
	return creer(Zero.singleton, Zero.singleton);
    }
    public Z oppose(){
	Couple<Nat, Nat> rep = this.representant(); 
	return creer(rep.pi2, rep.pi1);
    }
    public Z produit(Z x){
	Couple<Nat, Nat> rep = this.representant(); 
	Couple<Nat, Nat> repX = x.representant();
	Nat positif = rep.pi1.produit(repX.pi1).somme(rep.pi2.produit(repX.pi2));
	Nat negatif = rep.pi2.produit(repX.pi1).somme(rep.pi1.produit(repX.pi2));
	return creer(positif, negatif);
    }
    public Z un(){
	Nat fab = Zero.singleton;
	Nat zero = Zero.singleton;
	Nat un = fab.creer(zero);
	return creer(un, zero);
    }
    public int val(){
	Couple<Nat, Nat> rep = this.representant(); 
	return (rep.pi1.val() - rep.pi2.val());
    }
    public boolean equals(Object o){
	if(!(o instanceof Z)) return false;
	Z x = (Z)o;
	Couple<Nat, Nat> rep = this.representant(); 
	Couple<Nat, Nat> repX = x.representant();
	return rep.pi1.somme(repX.pi2).equals(rep.pi2.somme(repX.pi1));
    }
   public String toString(){
       return "" + val();
    }
}

interface Symetrise<T extends MonoideAdd<T> & MonoideMul<T>> 
    extends AnneauUnitaire<Symetrise<T>> {
    Symetrise<T> creer(T positif, T negatif); 
    Couple<T, T> representant();
    boolean equals(Object o); 
}

class Diagonale<T extends MonoideAdd<T> & MonoideMul<T>>  
    implements Symetrise<T> {
    private T positif;
    private T negatif;
    public Diagonale(T positif, T negatif){
	this.positif = positif;
	this.negatif = negatif;
    }
    public Couple<T, T> representant(){
	return new Couple<T, T>(positif, negatif);
    }
    public Symetrise<T> creer(T positif, T negatif){
	return new Diagonale<T>(positif, negatif);
    }
    public Symetrise<T> somme(Symetrise<T> x){
	Couple<T, T> rep = this.representant(); 
	Couple<T, T> repX = x.representant(); 
	T positif = rep.pi1.somme(repX.pi1);
	T negatif = rep.pi2.somme(repX.pi2);
	return creer(positif, negatif);
    }
    public Symetrise<T> zero(){
	Couple<T, T> rep = this.representant(); 
	return creer(rep.pi1, rep.pi1);
    }
    public Symetrise<T> oppose(){
	Couple<T, T> rep = this.representant(); 
	return creer(rep.pi2, rep.pi1);
    }
    public Symetrise<T> produit(Symetrise<T> x){
	Couple<T, T> rep = this.representant(); 
	Couple<T, T> repX = x.representant();
	T positif = rep.pi1.produit(repX.pi1).somme(rep.pi2.produit(repX.pi2));
	T negatif = rep.pi2.produit(repX.pi1).somme(rep.pi1.produit(repX.pi2));
	return creer(positif, negatif);
    }
    public Symetrise<T> un(){
	Couple<T, T> rep = this.representant(); 
	T fab = rep.pi1;
	T zero = fab.zero();
	T un = fab.un();
	return creer(un, zero);
    }
    public boolean equals(Object o){
	if(!(o instanceof Symetrise)) return false;
	Symetrise<T> x = (Symetrise<T>)o;
	Couple<T, T> rep = this.representant(); 
	Couple<T, T> repX = x.representant();
	return rep.pi1.somme(repX.pi2).equals(rep.pi2.somme(repX.pi1));
    }
    public String toString(){
	Couple<T, T> rep = this.representant(); 
	return rep.pi1.toString() + " - " + rep.pi2.toString(); 
    }
}

interface Q extends Corps<Q> {
    Z getNumerateur(); // Renvoie le numérateur
    Z getDenominateur(); // Renvoie le dénominateur
    Q creer(Z numerateur, Z denominateur); // Crée le rationnel numerateur/denominateur
    String toString(); // Représente le rationnel sous la forme "numerateur/denominateur"
    boolean equals(Object o); // Renvoie false
                              //   si o n'est pas de type Q,
                              // teste l'égalité des rationnels sinon
}

class Rationnel implements Q {
    private Z numerateur;
    private Z denominateur;
    public Rationnel(Z numerateur, Z denominateur){
	this.numerateur = numerateur;
	this.denominateur = denominateur;
    }
    public Z getNumerateur(){
	return numerateur;
    }
    public Z getDenominateur(){
	return denominateur;
    }
    public Q creer(Z numerateur, Z denominateur){
	return new Rationnel(numerateur, denominateur);
    }
    public Q somme(Q x){
	Z n = getNumerateur().produit(x.getDenominateur()).somme(x.getNumerateur().produit(getDenominateur()));
	Z d = getDenominateur().produit(x.getDenominateur());
	return creer(n, d);
    }
    public Q zero(){
	Z fab = new Relatif();
	return creer(fab.zero(), fab.un());
    }
    public Q oppose(){
	return creer(getNumerateur().oppose(), getDenominateur());
    }
    public Q produit(Q x){
	return creer(getNumerateur().produit(x.getNumerateur()), 
		     getDenominateur().produit(x.getDenominateur()) );
    }
    public Q un(){
	Z fab = new Relatif();
	return creer(fab.un(), fab.un());
    }
    public Q inverse(){
	return creer(getDenominateur(), getNumerateur());
    }
    public String toString(){
	return getNumerateur() + "/" + getDenominateur();
    }
    public boolean equals(Object o){
	if(!(o instanceof Q)) return false;
	Q x = (Q)o;
	return
	    getNumerateur().produit(x.getDenominateur())
	    .equals(x.getNumerateur().produit(getDenominateur()));
    }
}

interface Fraction<T extends AnneauUnitaire<T>> 
    extends Corps<Fraction<T>> {
    T getNumerateur();
    T getDenominateur();
    Fraction<T> creer(T numerateur, T denominateur);
    String toString();
    boolean equals(Object o);
}

class Rapport<T extends AnneauUnitaire<T>>  
    implements Fraction<T> {
    private T numerateur;
    private T denominateur;
    public Rapport(T numerateur, T denominateur){
	this.numerateur = numerateur;
	this.denominateur = denominateur;
    }
    public T getNumerateur(){
	return numerateur;
    }
    public T getDenominateur(){
	return denominateur;
    }
    public Fraction<T> creer(T numerateur, T denominateur){
	return new Rapport<T>(numerateur, denominateur);
    }
    public Fraction<T> somme(Fraction<T> x){
	T n = getNumerateur().produit(x.getDenominateur()).somme(x.getNumerateur().produit(getDenominateur()));
	T d = getDenominateur().produit(x.getDenominateur());
	return creer(n, d);
    }
    public Fraction<T> zero(){
	T fab = numerateur;
	return creer(fab.zero(), fab.un());
    }
    public Fraction<T> oppose(){
	return creer(getNumerateur().oppose(), getDenominateur());
    }
    public Fraction<T> produit(Fraction<T> x){
	return creer(getNumerateur().produit(x.getNumerateur()), 
		     getDenominateur().produit(x.getDenominateur()) );
    }
    public Fraction<T> un(){
	T fab = numerateur;
	return creer(fab.un(), fab.un());
    }
    public Fraction<T> inverse(){
	return creer(getDenominateur(), getNumerateur());
    }
    public String toString(){
	return "(" + getNumerateur() + ")/(" + getDenominateur() + ")";
    }
    public boolean equals(Object o){
	if(!(o instanceof Fraction)) return false;
	Fraction<T> x = (Fraction<T>)o;
	return
	    getNumerateur().produit(x.getDenominateur())
	    .equals(x.getNumerateur().produit(getDenominateur()));
    }
}
			  

public class Test {
    public static void main(String[] args){

	/* Nat */
	System.out.println("******* Test de Nat *******");      
	Nat fab = Zero.singleton;
	Nat zero = fab.creer();
	Nat un = fab.creer(zero);
	Nat deux = fab.creer(un);
	Nat quatre = deux.somme(deux);      
	Nat huit = deux.produit(quatre);
	
	System.out.println("zéro : " + zero);
	System.out.println("un : " + un);
	System.out.println("deux : " + deux);
	System.out.println("quatre : " + quatre);      
	System.out.println("huit : " + huit);      

	System.out.println("égal : " + zero.equals(un.zero()));      
	System.out.println("égal : " + un.equals(un.un()));      

	/* Z */
	System.out.println("******* Test de Z *******");      
	Z fabZ = new Relatif();
        Z zeroZ = fabZ.zero();
	Z moinsUn = fabZ.creer(un, deux);
	Z moinsDeux = fabZ.creer(deux, quatre);
	Z quatreZ = moinsDeux.produit(moinsDeux);
        System.out.println("zéro Z : " + zeroZ);    
	System.out.println("moins un : " + moinsUn);
	System.out.println("moins deux : " + moinsDeux);
	System.out.println("quatre Z : " + quatreZ); 

	System.out.println("égal : " + moinsDeux.equals(moinsDeux));
	System.out.println("inégal : " + moinsDeux.equals(quatreZ));

	/* Symetrise<Nat> */
	System.out.println("******* Test de Symetrise<Nat> *******");      
	Symetrise<Nat> fabS = new Diagonale<Nat>(Zero.singleton, Zero.singleton);
	Symetrise<Nat> zeroS = fabS.zero();
	Symetrise<Nat> moinsUnS = fabS.creer(un, deux);
	Symetrise<Nat> moinsDeuxS = fabS.creer(deux, quatre);
	Symetrise<Nat> quatreS = moinsDeuxS.produit(moinsDeuxS);
	System.out.println("zéro S: " + zeroS);  
	System.out.println("moins un S: " + moinsUnS);
	System.out.println("moins deux S: " + moinsDeuxS);  
	System.out.println("quatre S: " + quatreS); 

	System.out.println("un S: " + moinsUnS.oppose());
	System.out.println("égal S: " + moinsDeuxS.equals(moinsDeuxS));
	System.out.println("inégal S: " + moinsDeuxS.equals(quatreS));

	/* Q */
	System.out.println("******* Test de Q *******");      
	Q fabQ = new Rationnel(fabZ, fabZ);
	Q unDemi = fabQ.creer(moinsUn, moinsDeux);
	Q deuxQ = fabQ.creer(moinsDeux, moinsUn); 
	Q unQ = unDemi.somme(unDemi);
	Q unQuart = unDemi.produit(unDemi);
	System.out.println("un demi : " + unDemi);
	System.out.println("deux : " + deuxQ);
	System.out.println("un : " + unQ); 
	System.out.println("un quart : " + unQuart);
	System.out.println("un demi égal à inverse de deux: " + unDemi.equals(deuxQ.inverse()));

	System.out.println("égal : " + unQuart.equals(unQuart));
	System.out.println("inégal : " + unQuart.equals(deuxQ));

	/* Fraction<Symetrise<Nat>> */
	System.out.println("******* Test de Fraction<Symetrise<Nat>> *******");      
	Fraction<Symetrise<Nat>> fabFS = new Rapport<Symetrise<Nat>>(fabS, fabS);
	Fraction<Symetrise<Nat>> unDemiFS = fabFS.creer(moinsUnS, moinsDeuxS);
	Fraction<Symetrise<Nat>> deuxFS = fabFS.creer(moinsDeuxS, moinsUnS); 
	Fraction<Symetrise<Nat>> unFS = unDemiFS.somme(unDemiFS);
	Fraction<Symetrise<Nat>> unQuartFS = unDemiFS.produit(unDemiFS);
	System.out.println("un demi : " + unDemiFS);
	System.out.println("deux : " + deuxFS);
	System.out.println("un : " + unFS); 
	System.out.println("un quart : " + unQuartFS);
	System.out.println("un demi égal à inverse de deux: " + unDemiFS.equals(deuxFS.inverse()));
    }
    public static void main(String[] args) {
        /*<validation/>*/
    }
    
    public static double validationQ1F1(){
        double note = 0;
        //@@@
        return note;
    }
}