package school.hei.patrimoine.modele;
public class Devise {
    private String code;

    public Devise(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Devise devise = (Devise) o;
        return code.equals(devise.code);
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }
}