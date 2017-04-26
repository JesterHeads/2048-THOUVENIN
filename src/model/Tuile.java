package model;

import javafx.geometry.Pos;

import javafx.scene.control.Label;

/**
 * Classe tuile, représente une tuile du jeu a laquelle on attribuera différente valeur
 * selon l'avancée du joueur.
 * @author benjamin
 */
public class Tuile extends Label{
    int x,y,value;

    public Tuile(int value,int x,int y){
        this.value = value;
        this.setText(""+value);
        this.x=x;
        this.y=y;
        this.setProprieties(value);
    }

    public void setProprieties(int value){
        this.setAlignment(Pos.CENTER);
        this.setGraphicTextGap(5.0);
        this.setPrefSize(60.0, 60.0);
        switch(value){
            case 2:
                this.setStyle("-fx-background-color: #eee4da ; -fx-border-width: 1;  -fx-text-fill:white;");
                break;
            case 4:
                this.setStyle("-fx-background-color: #ede0c8; -fx-border-width: 1;  -fx-text-fill:white;");
                break;
            case 8:
                this.setStyle("-fx-background-color: #f2b179 ;-fx-border-width: 1;  -fx-text-fill:white;");
                break;
            case 16:
                this.setStyle("-fx-background-color: #f59563; -fx-border-width: 1;  -fx-text-fill:white;");
                break;
            case 32:
                this.setStyle("-fx-background-color: #f67c5f; -fx-border-width: 1;  -fx-text-fill:white;");
                break;
            case 64:
                this.setStyle("-fx-background-color: #f65e3b ;-fx-border-width: 1;  -fx-text-fill:white;");
                break;
            case 128:
                this.setStyle("-fx-background-color: #edcf72; -fx-border-width: 1;  -fx-text-fill:white;");
                break;
            case 256:
                this.setStyle("-fx-background-color: #edcc61; -fx-border-width: 1;  -fx-text-fill:white;");
                break;
            case 512:
                this.setStyle("-fx-background-color: #edc850 ;-fx-border-width: 1;  -fx-text-fill:white;");
                break;
            case 1024:
                this.setStyle("-fx-background-color: fuchsia; -fx-border-width: 1;  -fx-text-fill:white;");
                break;
            case 2048:
                this.setStyle("-fx-background-color: red; -fx-border-width: 1;  -fx-text-fill:white;");
                break;
            default:
                this.setStyle("-fx-background-color: whitesmoke; -fx-border-width: 1;  -fx-text-fill:white;");
                this.setText("");
                break;
        }
        this.setVisible(true);

    }

    public int getValue(){
        return this.value;
    }

    public void setValue(int value){
        this.value = value;
        this.setProprieties(value);
    }


    public int getX(){
        return this.x;
    }

    public void setX(int x){
        this.x=x;
    }

    public void setY(int y){
        this.y=y;
    }

    public int getY(){
        return this.y;
    }
}
