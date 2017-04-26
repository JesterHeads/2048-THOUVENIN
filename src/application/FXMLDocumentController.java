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
import java.util.HashSet;
import java.util.Random;
import javafx.geometry.VPos;
import javafx.scene.layout.ColumnConstraints;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.layout.Priority;
import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;
import javafx.scene.layout.RowConstraints;
import model.Case;
import model.Grille;
import application.Parametres;
//import javafx.scene.paint.Color;
//import javafx.scene.text.Font;




public class FXMLDocumentController implements Initializable, Parametres {
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
  
    
    
    ArrayList<Pane> ListTuile = new ArrayList<Pane>();
    
    private Pane p1 = new Pane(); //Ne doit plus etre utilisé (pas encore a supprimé sinon ereur de compilation (pour l'instant)
    private double x = 24, y = 191; //Plus etre utilisé
    private double objectifx = 24 , objectify = 191; // IDEM
    
    private Grille modelgrille;
    private int typeMouve;// L'oject grille issue du model Grille
 
    private double LargeurCase; // Largeur en px d'une case
    private double[] pixelXCase; // Tableau contenant l'abssise en px de chaque case
    private double[] pixelYCase; // Tableau contenant l'ordonnée en px de chaque case
    private ParallelTransition mouvement = new ParallelTransition();
    
    final private double pX = 24; //Plus utiliser
    final private double pY = 191; //Plus utiliser
    final private int TAILLEGRILLE = 4; //Notre grille est de format 4*4


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        System.out.println("le contrôleur initialise la vue");
        // utilisation de styles pour la grille
   
        modelgrille = new Grille();
        fond.getStyleClass().add("fond");
        pixelXCase = new double[TAILLEGRILLE];
        pixelYCase = new double[TAILLEGRILLE];
        
        InitGrille();

    }
    
    private void InitGrille() {

        //Création des colonnes et des lignes
        grille.getRowConstraints().clear();
        grille.getColumnConstraints().clear();
        for (int i = 0; i < TAILLEGRILLE; i++) {
            grille.getRowConstraints().add(new RowConstraints(25, 100, USE_COMPUTED_SIZE, Priority.SOMETIMES, VPos.TOP, true));
            grille.getColumnConstraints().add(new ColumnConstraints(25, 100, USE_COMPUTED_SIZE, Priority.SOMETIMES, HPos.LEFT, true));
        }

        //Définition des limites des case en pixels
        LargeurCase = grille.getPrefWidth() / TAILLEGRILLE;
        System.out.println(grille.getLayoutX() + (LargeurCase * 0));
        System.out.println(grille.getLayoutY());
        for (int i = 0; i < TAILLEGRILLE; i++) {
            pixelXCase[i] = grille.getLayoutX() + (LargeurCase * i);
            pixelYCase[i] = grille.getLayoutY() + (LargeurCase * i);
        }

        //Initialisation du fond de la grille avec le css
        for (int i = 0; i < TAILLEGRILLE; i++) {
            for (int j = 0; j < TAILLEGRILLE; j++) {
                Pane casevide = new Pane();
                casevide.getStyleClass().add("gridpane");
                grille.add(casevide, i, j);
            }
        }
        
       // fond.getChildren().remove(PaneEndGame); A FAIRE QUAND ON REINITIALISE LA PERTIE 
       
       // Creation de la premiere case lorsque l'on commence le jeux
       Case fistCase = new Case(0,0,2);
       this.modelgrille.getGrille().add(fistCase); //ajout de la Case au modele "manuelement car elle doit avoir une potition et un label particulier
       this.addCase(); //Appele de l a methode qui permettra d'ajouter la case graphiquement 
       System.out.println(this.modelgrille.getGrille());
    }
    
    public void addCase() {
        // Recuperation des objets constitué des Cases contenu dans la grille du modele
        
        Case newcase = getNewcase();
        
        // Récupération des coordonnées des pixiels de la tuile
        double pixX = pixelXCase[(newcase.getX())];
        double pixY = pixelYCase[(newcase.getY())];
        
        // Attibution du label 
        Label val = new Label(Integer.toString(newcase.getValeur()));
        Pane tuile = new Pane();

        //String valeur = val.getText();
        // application du style css sur la nouvelle tuile a afficher
        
        // Application du css sur la tuile 
        tuile.getStyleClass().add("pane");
        val.getStyleClass().add("tuile");
        GridPane.setHalignment(val, HPos.CENTER);
        fond.getChildren().add(tuile);
        tuile.getChildren().add(val);
        tuile.setPrefHeight(LargeurCase);
        tuile.setPrefWidth(LargeurCase); 
        
        // Positionnement de la Tuile
        tuile.setLayoutX(pixX);
        tuile.setLayoutY(pixY);
        tuile.setVisible(true);
        val.setVisible(true);
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
   
       /* if (x > y) {
            for (int i = 0; i < grille.getChildren().size(); i++) { //pour chaque colonne
                //for (int j = 0; j < grille.getRowConstraints().size(); j++) { //pour chaque ligne
                System.out.println("ok1");
                grille.getChildren().remove(i);

                /*Node tuile = grille.getChildren().get(i);
                 if (tuile != null) {
                 int rowIndex = GridPane.getRowIndex(tuile);
                 int rowEnd = GridPane.getRowIndex(tuile);
                 }
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
        } */
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
            typeMouve = GAUCHE;
        } else if (touche.compareTo("d") == 0) { // utilisateur appuie sur "d" pour envoyer la tuile vers la droite
            typeMouve = DROITE;
        } else if (touche.compareTo("w") == 0) { // utilisateur appuie sur "w" pour envoyer la tuile vers le bas
            typeMouve = BAS;
        } else if (touche.compareTo("z") == 0) { // utilisateur appuie sur "z" pour envoyer la tuile vers le haut
            typeMouve = HAUT;
        }
        
        boolean deplacementok = this.modelgrille.lanceurDeplacerCases(typeMouve);
        if (deplacementok) {
             moving();
        }
    }
    
    /* Enciene methode utilisé, unitilse car deja persante dans les modeles
    public void initPane(Pane p) {

        if (Math.random()>0.5) {
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
    } */

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
    
    // Fonction appeler lorsque un mouvement est effectué
    public void moving () {
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
                            
                                mouvement.play();
                                mouvement.getChildren().clear();
                        }
                    }
                    );
                    Thread.sleep(50);
                } // end while
            
                    Platform.runLater(new Runnable() { // classe anonyme
                        @Override
                        public void run() {
                            //javaFX operations should go here
                            modelgrille.nouvelleCase(); //Creation d'une tuile avec une positione et un label entre "2" et "4"
                            addCase(); //Ajout de la case dans le jeux
                        }
                    }       
                    );
                    Thread.sleep(1); 
                
                return null; // la méthode call doit obligatoirement retourner un objet. Ici on n'a rien de particulier à retourner. Du coup, on utilise le type Void (avec un V majuscule) : c'est un type spécial en Java auquel on ne peut assigner que la valeur null
            } // end call

        };
        
        Thread th = new Thread(task); // on crée un contrôleur de Thread
        th.setDaemon(true); // le Thread s'exécutera en arrière-plan (démon informatique)
        th.start(); // et on exécute le Thread pour mettre à jour la vue (déplacement continu de la tuile horizontalement)
        int sc=Integer.parseInt(score.getText());//valeur du score
        int bsc= Integer.parseInt(bestScore.getText());//valeur du meilleur score
        if (sc > bsc){
            bsc = sc;
            bestScore.setText(Integer.toString(bsc));
        }
    }
    
    // Methode qui permet de récuperer facilement la deniere case crée afin de l'incérer dans le jeux
    public Case getNewcase() {
        // Recuperation des objets constitué des Cases contenu dans la grille du modele
        Object[] tabcase = this.modelgrille.getGrille().toArray();
        Case newcase = (Case)tabcase[tabcase.length - 1];
        return newcase;
    }
    
    /*
    public boolean canMove() {
        boolean canmove = false;
         if (x!=objectifx && y!=objectify) {
             canmove = true; 
         }
         return canmove;
    } */
}
