package com.gmail.okostina74;

public class RGB {
    private String R;
    private String G;
    private String B;

    RGB (String color){

        if (color.substring(0,4).toLowerCase().equals("rgba")) {
            String[] colorParts = color.substring(5, color.length() - 1).split(", ");
            this.R = colorParts[0];
            this.G = colorParts[1];
            this.B = colorParts[2];
        }
        else if (color.substring(0,4).toLowerCase().equals("rgb("))
        {
            String[] colorParts = color.substring(4, color.length() - 1).split(", ");
            this.R = colorParts[0];
            this.G = colorParts[1];
            this.B = colorParts[2];
        }
    }

    public String getR(){
        return this.R;
    }
    public String getG(){
        return this.G;
    }
    public String getB(){
        return this.B;
    }
}
