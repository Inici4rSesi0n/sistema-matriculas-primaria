package modelo;
/**
 *
 * @author inici4rsesi0n
 */
public class Director extends Usuario {
    public Director() {}
    public Director(String cod, String pwd, String dni, String nom, String ape, int age) {
        super(cod, pwd, dni, nom, ape, age);}
    public static final String COD_POR_DEFECTO = "C22251227";
    public static final String HASH_PWD_POR_DEFECTO = "240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9";
    @Override
    public String toString() {
        return "Director{" + super.toString() + "}";
    }
}
