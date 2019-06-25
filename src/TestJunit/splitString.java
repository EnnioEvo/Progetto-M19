package TestJunit;

public class splitString {

    public splitString() {
    }

    public String split(String s){
        String print = "";
        String[] stringa = s.split("--");
        print = stringa[0];
        return print;
    }
}
