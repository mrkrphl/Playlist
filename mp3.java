import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.Stage;

public class mp3 extends Application{

    int width = 700;
    int height = 800;

    ObservableList<String> contents= FXCollections.observableArrayList();
    
    @Override
    public void start(Stage stage) throws Exception {
        TextField artist = new TextField();
        artist.setPromptText("Artist");
        TextField songname = new TextField();
        songname.setPromptText("Song Name");
        TextField PlaylistName = new TextField();
        PlaylistName.setPromptText("Playlist Name");
        artist.setPrefWidth(width/2);
        songname.setPrefWidth(width/2);
        Text dash = new Text("-");

        HBox details = new HBox(artist, dash, songname);
        details.setSpacing(20);

        Button AddSong = new Button("Add Song");
        AddSong.setOnAction(a -> AddSong(artist, songname));
        AddSong.setAlignment(Pos.CENTER);
        AddSong.setDefaultButton(true);
        
        Button deleteSongs = new Button("Delete All Songs in List");
        deleteSongs.setOnAction(b -> deleteAllSongs(contents));

        Button addFile = new Button("Add Text File");
        addFile.setOnAction(f -> dragFile(stage));

        HBox fileControl = new HBox(AddSong, deleteSongs, addFile);
        fileControl.setSpacing(20);
        fileControl.setAlignment(Pos.CENTER);

        Button CreatePlaylist = new Button("Create Playlist");
        CreatePlaylist.setAlignment(Pos.CENTER);
        CreatePlaylist.setOnAction(e -> createPlaylist(PlaylistName));

        readTo(contents, "songs.txt");

        ListView<String> songlist = new ListView<String>();
        songlist.setItems(contents);
        songlist.setPrefWidth(width/2);
        songlist.setPrefHeight(height/2);

        VBox main = new VBox(details, fileControl, songlist,  PlaylistName, CreatePlaylist);
        main.setSpacing(20);
        main.setPadding(new Insets(50));
        main.setAlignment(Pos.CENTER);

        BorderPane root = new BorderPane();
        root.setBackground(new Background(new BackgroundFill(Color.valueOf("Seagreen"), CornerRadii.EMPTY, new Insets(0))));
        
        root.setCenter(main);
        
        Scene scene = new Scene(root);
        
        stage.setWidth(width);
        stage.setHeight(height);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.setTitle("Playlist Maker");
        stage.show();
    }

    private void dragFile(Stage stage) {
        
        Label instr = new Label("Drag a text file here.");
        Label dragged = new Label("");
        VBox file = new VBox(instr, dragged);
        file.setBackground(new Background(new BackgroundFill(Color.valueOf("seagreen"), new CornerRadii(20), new Insets(0))));
        file.setBorder(new Border(new BorderStroke(Color.valueOf("Lightblue"), BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(3))));
        file.setPadding(new Insets(20));


        file.setOnDragOver(new EventHandler<DragEvent>(){
            @Override
            public void handle(DragEvent d) {
                if(d.getGestureSource() != file && d.getDragboard().hasFiles()) {
                    d.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                }
                d.consume();
            }
        });

        
        Popup drag = new Popup();
        
        drag.getContent().addAll(file);
        drag.show(stage);
        drag.setHideOnEscape(true);
        

        file.setOnDragDropped(new EventHandler<DragEvent>(){
            @Override
            public void handle(DragEvent d) {
                Dragboard db = d.getDragboard();
                boolean done = false;
                if(db.hasFiles()){
                    if(!contents.isEmpty()){
                        contents.clear();
                    }
                    try (FileReader fr = new FileReader(db.getFiles().get(0).toString());
                        FileWriter fw = new FileWriter("songs.txt");) {
                        int c = fr.read();
                        while(c!=-1) {
                            fw.write(c);
                            c = fr.read();
                        }
                        
                    } catch(IOException e) {
                        e.printStackTrace();
                    }
                }
                readTo(contents, "songs.txt");
                d.setDropCompleted(done);
                d.consume();
                
                drag.hide();
            }
        });
    }

    private void readTo(ObservableList<String> contents, String fileName) {
        try{
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;
            while((line = reader.readLine()) != null){
                contents.add(line);
            }
            reader.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    void AddSong(TextField artist, TextField songname){
        String data = artist.getText() + " - " + songname.getText() + "\n";
        try{
            File songs = new File("songs.txt");
            if(!songs.exists()){
                songs.createNewFile();
            }

            FileWriter songsfile = new FileWriter(songs.getName(), true);
            BufferedWriter writer = new BufferedWriter(songsfile);
            writer.write(data);
            writer.close();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            contents.clear();
            readTo(contents, "songs.txt");
        }
    }
    void createPlaylist(TextField name){
        String data = name.getText().replaceAll(" ", "");
        try{
            File vars = new File("vars.txt");

            FileWriter var = new FileWriter(vars.getName());
            BufferedWriter writer = new BufferedWriter(var);
            writer.write(data);
            writer.close();
        }catch(Exception e){
            e.printStackTrace();
        }

        runExe();
        Alert alert= new Alert(AlertType.INFORMATION);
        alert.setTitle("Plalyist Creation");
        alert.setHeaderText("Playlist Created!");
        alert.setContentText("Your playlist is now saved to your Desktop!");
        alert.showAndWait();
    }

    void deleteAllSongs(ObservableList<String> contents){
        contents.clear();
        File songs = new File("songs.txt");
        try{
            if(songs.delete()){
                songs.createNewFile();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    void runExe(){
        Runtime runtime = Runtime.getRuntime();
        try {
            runtime.exec("cmd /c start /min cmd /c mp3.exe");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}