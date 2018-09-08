package query;

public class printAlign {
    public static String main(String string)
    {
        char c = ' ';    //char to fill the space
        int l = 20;     //full length after filling space
        String str = "";
        String temp = "";
        if (string.length() > l)
            str = string;
        else
            for (int i = 0; i < l - string.length(); i++)
                temp = temp + c;
        str = temp+string;
        return str;
    }

    public static String divinglines()
    {
        String  str = "";
        for(int i=0;i<30;i++){
            str = str + "=";
        }
        return str;
    }

}
