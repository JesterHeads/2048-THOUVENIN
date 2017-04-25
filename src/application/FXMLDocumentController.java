/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import java.util.ArrayList;
import java.util.Random;
//import javafx.scene.paint.Color;
//import javafx.scene.text.Font;

/**
 *
 * @author castagno
 */
public class FXMLDocumentController implements Initializable {
    /*
     * Variables globales correspondant à des objets définis dans la vue (fichier .fxml)
     * Ces variables sont ajoutées à la main et portent le même nom que les fx:id dans Scene Builder
     */
    @FXML
    private Label score; // value will be injected by the FXMLLoader
    
    @FXML
    private Label bestScore;
    @FXML
    private GridPane grille;
    @FXML
    private Pane fond; // panneau recouvrant toute la fenêtre

    // variables globales non définies dans la vue (fichier .fxml)
    @FXML
    private final Pane p1 = new Pane(); // panneau utilisé pour dessiner une tuile "2"
    @FXML
    private final Label c = new Label("2");
    ArrayList<Pane> ListTuile = new ArrayList<Pane>();
    private double x = 24, y = 191;
    private double objectifx = 24, objectify = 191;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        System.out.println("le contrôleur initialise la vue");
        // utilisation de styles pour la grille et la tuile (voir styles.css)

        p1.getStyleClass().add("pane"); 
        c.getStyleClass().add("tuile");
        grille.getStyleClass().add("gridpane");
        ListTuile.add(p1);
        Pane FirstTuile = ListTuile.get(0);
        GridPane.setHalignment(c, HPos.CENTER);
        fond.getChildren().add(FirstTuile);
        FirstTuile.getChildren().add(c);
       
        // on place la tuile en précisant les coordonnées (x,y) du coin supérieur gauche
        FirstTuile.setLayoutX(x);
        FirstTuile.setLayoutY(y);
        FirstTuile.setVisible(true);
        c.setVisible(true);
    }

    /*
     * Méthodes listeners pour gérer les événements (portent les mêmes noms que
     * dans Scene Builder
     */
    @FXML
    private void handleDragAction(MouseEvent event) {
        System.out.println("Glisser/déposer sur la grille avec la souris");
        double x = event.getX();//translation en abscisse
        double y = event.getY();//translation en ordonnée
        System.out.println(x);
        System.out.println(y); 
   
        if (x > y) {
            for (int i = 0; i < grille.getChildren().size(); i++) { //pour chaque colonne
                //for (int j = 0; j < grille.getRowConstraints().size(); j++) { //pour chaque ligne
                System.out.println("ok1");
                grille.getChildren().remove(i);

                /*Node tuile = grille.getChildren().get(i);
                 if (tuile != null) {
                 int rowIndex = GridPane.getRowIndex(tuile);
                 int rowEnd = GridPane.getRowIndex(tuile);
                 }*/
                // }
            }
        } else if (x < y) {
            System.out.println("ok2");
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    Pane p = new Pane();
                    p.getStyleClass().add("pane");
                    grille.add(p, i, j);
                    p.setVisible(true);
                    grille.getStyleClass().add("gridpane");
                }
            }
        }
    }

    @FXML
    private void handleButtonAction(MouseEvent event) {
        System.out.println("Clic de souris sur le bouton menu");
    }

    @FXML
    public void keyPressed(KeyEvent ke) {
        System.out.println("touche appuyée");
        
        String touche = ke.getText();
        if (touche.compareTo("q") == 0) { // utilisateur appuie sur "q" pour envoyer la tuile vers la gauche
            if (objectifx > 24) { // possible uniquement si on est pas dans la colonne la plus à gauche
                objectifx -= (int) 3*397/4; // on définit la position que devra atteindre la tuile en abscisse (modèle). Le thread se chargera de mettre la vue à jour
                score.setText(Integer.toString(Integer.parseInt(score.getText()) + 1)); // mise à jour du compteur de mouvement
            }
        } else if (touche.compareTo("d") == 0) { // utilisateur appuie sur "d" pour envoyer la tuile vers la droite
            if (objectifx < (int) 445 - 2 * 397 / 4 - 24) { // possible uniquement si on est pas dans la colonne la plus à droite (taille de la fenêtre - 2*taille d'une case - taille entre la grille et le bord de la fenêtre)
                objectifx += (int) 3*397/4;
                score.setText(Integer.toString(Integer.parseInt(score.getText()) + 1));
            }
        } else if (touche.compareTo("w") == 0) { // utilisateur appuie sur "w" pour envoyer la tuile vers le bas
            if (objectify < (int) 613 - 2 * 397 / 4 - 25) { // possible uniquement si on est pas dans la colonne la plus à droite (taille de la fenêtre - 2*taille d'une case - taille entre la grille et le bord de la fenêtre)
                objectify += (int) 3*397/4;
                score.setText(Integer.toString(Integer.parseInt(score.getText()) + 1));

            }
        } else if (touche.compareTo("z") == 0) { // utilisateur appuie sur "z" pour envoyer la tuile vers le haut
            if (objectify > 191) { // possible uniquement si on est pas dans la colonne la plus à droite (taille de la fenêtre - 2*taille d'une case - taille entre la grille et le bord de la fenêtre)
                objectify -= (int) 3*397/4;
                score.setText(Integer.toString(Integer.parseInt(score.getText()) + 1));
            }
        }
        
        System.out.println("objectifx=" + objectifx);
        System.out.println("objectify=" + objectify);
        

        Task task = new Task<Void>() { // on définit une tâche parallèle pour mettre à jour la vue
            @Override 
            public Void call() throws Exception { // implémentation de la méthode protected abstract V call() dans la classe Task
                while (x != objectifx) { // si la tuile n'est pas à la place qu'on souhaite attendre en abscisse
                    if (x < objectifx) {
                        x += 1; // si on va vers la droite, on modifie la position de la tuile pixel par pixel vers la droite
                    } else {
                        x -= 1; // si on va vers la gauche, idem en décrémentant la valeur de x
                    }
                    // Platform.runLater est nécessaire en JavaFX car la GUI ne peut être modifiée que par le Thread courant, contrairement à Swing où on peut utiliser un autre Thread pour ça
                    Platform.runLater(new Runnable() { // classe anonyme
                        @Override
                        public void run() {
                            //javaFX operations should go here
                            
                                p1.relocate(x, y); // on déplace la tuile d'un pixel sur la vue, on attend 5ms et on recommence jusqu'à atteindre l'objectif
                                p1.setVisible(true);    
                        }
                    }
                    );
                    Thread.sleep(1);
                } // end while
                while (y != objectify) { // si la tuile n'est pas à la place qu'on souhaite attendre en abscisse
                    if (y < objectify) {
                        y += 1; // si on va vers le haut, on modifie la position de la tuile pixel par pixel vers le haut
                    } else {
                        y -= 1; // si on va vers le bas, idem en décrémentant la valeur de y
                    }
                    // Platform.runLater est nécessaire en JavaFX car la GUI ne peut être modifiée que par le Thread courant, contrairement à Swing où on peut utiliser un autre Thread pour ça
                    Platform.runLater(new Runnable() { // classe anonyme
                        @Override
                        public void run() {
                            //javaFX operations should go here
                            p1.relocate(x, y); // on déplace la tuile d'un pixel sur la vue, on attend 5ms et on recommence jusqu'à atteindre l'objectif
                            p1.setVisible(true);    
                        }
                    }
                    );
                    Thread.sleep(1);
                } // end while
                return null; // la méthode call doit obligatoirement retourner un objet. Ici on n'a rien de particulier à retourner. Du coup, on utilise le type Void (avec un V majuscule) : c'est un type spécial en Java auquel on ne peut assigner que la valeur null
            } // end call
        };
        
        Thread th = new Thread(task); // on crée un contrôleur de Thread
        th.setDaemon(true); // le Thread s'exécutera en arrière-plan (démon informatique)
        th.start(); // et on exécute le Thread pour mettre à jour la vue (déplacement continu de la tuile horizontalement)
        int sc=Integer.parseInt(score.getText());//valeur du score
        int bsc= Integer.parseInt(bestScore.getText());//valeur du meilleur score
        if (sc > bsc){
            bestScore = score;
            bestScore.setText(Integer.toString(Integer.parseInt(bestScore.getText())));
        }
        ListTuile.add(new Pane());
        if(ListTuile.size()<24){
            initPane(ListTuile.get(ListTuile.size()-1));
        }
    }
    
    public void initPane(Pane p) {
        
        if (Math.random()>0.7) {
            Label l = new Label("4");
            p.getStyleClass().add("pane"); 
            l.getStyleClass().add("tuile");
            grille.getStyleClass().add("gridpane");
            GridPane.setHalignment(l, HPos.CENTER);
            fond.getChildren().add(p);
            p.getChildren().add(l);

            // on place la tuile en précisant les coordonnées (x,y) du coin supérieur gauche
            p.setLayoutX(x);
            p.setLayoutY(y);

            p.setVisible(true);
            l.setVisible(true);
        } 
        else {
            Label l = new Label("2");
            p.getStyleClass().add("pane");
            l.getStyleClass().add("tuile");
            grille.getStyleClass().add("gridpane");
            GridPane.setHalignment(l, HPos.CENTER);
            fond.getChildren().add(p);
            p.getChildren().add(l);

            // on place la tuile en précisant les coordonnées (x,y) du coin supérieur gauche
            p.setLayoutX(x);
            p.setLayoutY(y);

            p.setVisible(true);
            l.setVisible(true);
        }   
    }

    public boolean verifierTuile(Pane p){
        boolean present=false;
        for(Pane t : ListTuile){
            if(t.getLayoutX()==p.getLayoutX() && t.getLayoutY()==p.getLayoutY()){
                present=true;
                break;
            }
        }
        return present;
    }
}
