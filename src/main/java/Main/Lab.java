package Main;

import Classes.Doctor;
import Classes.DoctorDAO;
import Utils.DBUtil;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

public class Lab extends Application {
    //list of all stages
public static ArrayList<Stage> stages = new ArrayList<>();
private static boolean addAnStOpened = false;
private static boolean addDoctorStageOpened = false;
private static boolean editDoctorStageOpened = false;
private static boolean dbSettingsStageOpened = false;
private static String dbName = "";
private static String dbPort = "";
private static boolean connectedToDB = false;
private static ObservableList<Doctor> doctorList = null;
public static void populateDoctorList() throws SQLException, ClassNotFoundException {
    doctorList = DoctorDAO.searchDoctors();
}
private static FilteredList<Doctor> doctorFilteredList;
private static SortedList<Doctor> doctorSortedList;
public static ObservableList<Doctor> getDoctorList(){
    return doctorList;
}
public static void setAddAnStOpened(boolean x){
    addAnStOpened = x;
}
public static void setAddDoctorStageOpened(boolean x){
    addDoctorStageOpened = x;
}
public static void setEditDoctorStageOpened(boolean x) { editDoctorStageOpened = x;}
public static void setDbSettingsStageOpened(boolean x) { dbSettingsStageOpened = x;}
public static void setdbName(String name){
    dbName = name;
}
public static String getDbName(){
    return dbName;
}
public static void setDbPort(String port){
    dbPort = port;
}
public static String getDbPort() {
    return dbPort;
}
private static Properties properties;
public static Properties getProperties(){
    return properties;
}
private double DocWindowX, DocWindowY, DocWindowWidth, DocWindowHeight;

    @Override
    public void init(){
    System.out.println("init");
    properties = new Properties();
    //initProperties(properties);
        try(InputStream is = new FileInputStream("config.properties")){
            properties.load(is);
            DocWindowX = Double.parseDouble(properties.getProperty("DocWindowX"));
            DocWindowY = Double.parseDouble(properties.getProperty("DocWindowY"));
            DocWindowWidth = Double.parseDouble(properties.getProperty("DocWindowWidth"));
            DocWindowHeight = Double.parseDouble(properties.getProperty("DocWindowHeight"));
            dbPort = properties.getProperty("DBport");
            dbName = properties.getProperty("DBname");
        }
        catch (IOException e){}
    }

    @Override
    public void stop(){
    System.out.println("stop");
    }

    public void start(Stage primaryStage) throws SQLException, ClassNotFoundException{
        //showPatientsWindow.set(true);

        //Main Stage
        stages.add(primaryStage);
        primaryStage.setTitle("Lab application");
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: rgba(70, 110, 180, .80); ");

        // Managing menuBar
        MenuBar topLevelMenuBar = new MenuBar();
        root.setTop(topLevelMenuBar);
        Menu mainMenu = new Menu("Main");
        Menu settingsMenu = new Menu("Settings");
        topLevelMenuBar.getMenus().addAll(mainMenu, settingsMenu);

        final CheckMenuItem patientsMenuItem = new CheckMenuItem("Analysis");
        final CheckMenuItem antibodyMenuItem = new CheckMenuItem("Antibodies");
        final CheckMenuItem patientsListMenuItem = new CheckMenuItem("Patients");
        final CheckMenuItem docMenuItem = new CheckMenuItem("Doctors");
        mainMenu.getItems().addAll(patientsMenuItem, antibodyMenuItem, new SeparatorMenuItem(), patientsListMenuItem, docMenuItem);

        MenuItem dbSettings = new MenuItem("DB settings");
        settingsMenu.getItems().add(dbSettings);
        dbSettings.setOnAction((e) -> {
            if(!dbSettingsStageOpened) {
                Stage settings = StagesFactory.dbSettingsStage();
                settings.show();

            }
            setDbSettingsStageOpened(true);
        });
        //Creating patients scene
        Stage patientsWindow = new Stage();
        stages.add(patientsWindow);
        patientsWindow.setTitle("Analysis");
        patientsWindow.maxHeightProperty().bind(primaryStage.heightProperty().subtract(60));
        //Not allowing moving Scene to high
        patientsWindow.yProperty().addListener((ov, nv, olv) -> {
            if(patientsWindow.getY() <= 60) patientsWindow.setY(60);
        });
        patientsWindow.setMinHeight(300);
        patientsWindow.setMinWidth(300);
        patientsWindow.setY(65);
        patientsWindow.setX(0);
        patientsWindow.initStyle(StageStyle.UTILITY); //util style (no hide button only close)

        GridPane patientsGridPane = new GridPane();
        patientsGridPane.setHgap(10);
        patientsGridPane.setVgap(10);
        patientsGridPane.setPadding(new Insets(5));
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setHgrow(Priority.ALWAYS);
        ColumnConstraints col2 = new ColumnConstraints(150,150,200);
        ColumnConstraints col3 = new ColumnConstraints(200,200, Double.MAX_VALUE);
        patientsGridPane.getColumnConstraints().addAll(col1, col2, col3);
        Scene patientsScene = new Scene(patientsGridPane);
        patientsWindow.setScene(patientsScene);
        patientsWindow.setAlwaysOnTop(true);

        //Table view for analysis list Col#1
        TableView analysisTableView = new TableView();
        analysisTableView.prefHeightProperty().bind(patientsWindow.heightProperty());
        patientsGridPane.add(analysisTableView, 0,0);

        //VBOX with labels Col#2
        VBox textBoxPatients = new VBox();
        Text nameText = new Text("ФИО:");
        Text dateText = new Text("Дата:");
        Text materialText = new Text("Материал:");
        Text analysisTypeText = new Text("Анализ:");
        Text docText = new Text("Направил(а):");
        Text tubeText = new Text("Пробирок сделано:");
        textBoxPatients.setSpacing(10);

        //Button for new Analysis
        Button addAnalysisButton = new Button("ADD Analysis");
        addAnalysisButton.prefWidthProperty().bind(col2.prefWidthProperty());
        addAnalysisButton.prefHeightProperty().bind(addAnalysisButton.prefWidthProperty());
        addAnalysisButton.setOnAction((e) ->{
            if(!addAnStOpened) {
                setAddAnStOpened(true);
                Stage newAnStage = StagesFactory.addNewAnalysisStage();
                newAnStage.setAlwaysOnTop(true);
                stages.add(newAnStage);
                newAnStage.show();

            }
        });

        //Adding button and labeles to VBOX in Col#2
        textBoxPatients.getChildren().addAll(nameText, dateText, materialText, analysisTypeText, docText, tubeText, addAnalysisButton);
        patientsGridPane.add(textBoxPatients, 1, 0);

        //Labels for showing analysis data
        VBox labelsBoxPatients = new VBox();
        Label nameLabel = new Label();
        Label dateLabel = new Label();
        Label materialLabel = new Label();
        Label analysisTypeLabel = new Label();
        Label docLabel = new Label();
        Label tubeLabel = new Label();
        labelsBoxPatients.setSpacing(10);

        //Adding labels to VBOX on Col#3
        labelsBoxPatients.getChildren().addAll(nameLabel, dateLabel, materialLabel, analysisTypeLabel, docLabel, tubeLabel);
        patientsGridPane.add(labelsBoxPatients, 2, 0);

        //Action on analysis window close (hiding and unchecking)
        patientsWindow.setOnCloseRequest((e) -> {
            patientsWindow.hide();
            patientsMenuItem.setSelected(false);
        });
        //Check box in menu action
        patientsMenuItem.setOnAction((e)->{
            if(patientsMenuItem.isSelected())patientsWindow.show();
            else patientsWindow.hide();
        });

        //Creating doctor scene
        Stage doctorStage = new Stage();
        stages.add(doctorStage);
        doctorStage.setTitle("Doctor list");
        doctorStage.initStyle(StageStyle.UTILITY);
                doctorStage.maxHeightProperty().bind(primaryStage.heightProperty().subtract(60));
        //Not allowing moving Scene to high
        doctorStage.yProperty().addListener((ov, nv, olv) -> {
            if(doctorStage.getY() <= 60) doctorStage.setY(60);
        });
        doctorStage.setHeight(DocWindowHeight);
        doctorStage.setWidth(DocWindowWidth);
        doctorStage.setY(DocWindowY);
        doctorStage.setX(DocWindowX);
        //doctorPane
        GridPane doctorPane = new GridPane();
        doctorPane.setVgap(10);
        doctorPane.setHgap(10);
        doctorPane.setPadding(new Insets(5));
        ColumnConstraints doccol1 = new ColumnConstraints();
        doccol1.setHgrow(Priority.ALWAYS);
        ColumnConstraints doccol2 = new ColumnConstraints(150,150,200);
        ColumnConstraints doccol3 = new ColumnConstraints(200,500, Double.MAX_VALUE);
        doctorPane.getColumnConstraints().addAll(doccol1, doccol2, doccol3);
        //Table view for analysis list Col#1

        VBox vbox1 = new VBox();
        vbox1.setSpacing(20);
        HBox hbox1 = new HBox();
        hbox1.setSpacing(10);
        TextField docSearchField = new TextField();
        hbox1.getChildren().addAll(new Text("Search:"), docSearchField);
        Button refreshTable = new Button();
        populateDoctorList();

        TableView docTableView = new TableView();

        docTableView.setPrefWidth(650);

        docTableView.setItems(getSortedList(doctorList, docSearchField));

        //doctorList.add(new Classes.Doctor("123",",", "123"));
        TableColumn<Doctor, String> NameCol = new TableColumn<>("Classes.Doctor");
        NameCol.setCellValueFactory(new PropertyValueFactory("NameSurname"));
        NameCol.setPrefWidth(docTableView.getPrefWidth() * 0.8);

        TableColumn<Doctor, String> depCol = new TableColumn<>("Department");
        depCol.setCellValueFactory(new PropertyValueFactory("department"));
        depCol.setPrefWidth(docTableView.getPrefWidth() *0.2);



        docTableView.getColumns().setAll(NameCol, depCol);
        docTableView.prefHeightProperty().bind(doctorStage.heightProperty());

        refreshTable.setOnAction((e) -> {
            try {
                populateDoctorList();
                //doctorList.filtered()

                docTableView.setItems(getSortedList(doctorList, docSearchField));


            }
            catch (Exception exc){}
        });

        vbox1.getChildren().addAll(hbox1, docTableView, refreshTable);
        doctorPane.add(vbox1, 0,0);

        //VBOX with labels Col#2
        VBox textBoxDoc = new VBox();
        Text docNameText = new Text("ФИО врача:");
        Text departmentText = new Text("Отделение:");
        Text phoneText = new Text("Телефон:");
        textBoxPatients.setSpacing(10);

        //Button for new Analysis
        Button addDocButton = new Button("ADD Doctor");
        addDocButton.prefWidthProperty().bind(doccol2.prefWidthProperty());
        addDocButton.prefHeightProperty().bind(addDocButton.prefWidthProperty());
        addDocButton.setOnAction((e) ->{
            if(!addDoctorStageOpened) {
                System.out.println("add button clicked");
                setAddDoctorStageOpened(true);
                Stage newStage = StagesFactory.addNewDocStage();
                newStage.setAlwaysOnTop(true);
                stages.add(newStage);
                newStage.show();

            }
        });

        textBoxDoc.getChildren().addAll(docNameText, departmentText, phoneText, addDocButton);
        doctorPane.add(textBoxDoc, 1, 0);

        VBox docLabelBox = new VBox();
        Label docNameLabel = new Label();
        Label docDepartmentLabel = new Label();
        Label docPhoneLabel = new Label();

        docLabelBox.getChildren().addAll(docNameLabel, docDepartmentLabel, docPhoneLabel);
        doctorPane.add(docLabelBox, 2, 0);
        doctorStage.setAlwaysOnTop(true);
docTableView.getSelectionModel().selectedItemProperty().addListener((o, ov, nv) ->{
    if(o != null && o.getValue()!= null) {
        Doctor selected = (Doctor) docTableView.getSelectionModel().getSelectedItem();
        docNameLabel.textProperty().bind(selected.NameSurnameProperty());
        docDepartmentLabel.textProperty().bind(selected.departmentProperty());
        if(selected.phoneNumberProperty().get() != null){
           docPhoneLabel.textProperty().bind(selected.phoneNumberProperty());
        }

    }

        }
);
        docTableView.setOnMouseClicked((e) -> {
            if(!e.isPrimaryButtonDown()&& e.getClickCount() == 2 && !editDoctorStageOpened){
                Doctor selected = (Doctor) docTableView.getSelectionModel().getSelectedItem();
                if(selected != null) {
                    Stage st = StagesFactory.editDocStage(selected);
                    setEditDoctorStageOpened(true);
                    st.show();

                }
                docTableView.getSelectionModel().clearSelection();
            }


        });

        Scene doctorScene = new Scene(doctorPane);
        doctorStage.setScene(doctorScene);

        doctorStage.setOnCloseRequest((e) -> {
            doctorStage.hide();
            docMenuItem.setSelected(false);
            try(OutputStream os = new FileOutputStream("config.properties")){
                properties.setProperty("DocWindowX", Double.toString(doctorStage.getX()));
                properties.setProperty("DocWindowY", Double.toString(doctorStage.getY()));
                properties.setProperty("DocWindowWidth", Double.toString(doctorStage.getWidth()));
                properties.setProperty("DocWindowHeight", Double.toString(doctorStage.getHeight()));
                properties.store(os,null);
            }
            catch (IOException exc){}
        });
        //Check box in menu action

        docMenuItem.setOnAction((e)->{

            if(docMenuItem.isSelected()) {
                Stage docSt = StagesFactory.doctorStage();
                docSt.show();
            }
            else {
                for(Stage st: stages){
                    if(st.getTitle().equals("Doctor list")) st.close();
                }
            }
        });

        //Final scene managing
        Scene scene = new Scene(root);
        root.prefHeightProperty().bind(primaryStage.maxHeightProperty());
        root.prefWidthProperty().bind(primaryStage.maxWidthProperty());

        //set Stage boundaries to visible bounds of the main screen (FULL SCREEN props)
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        primaryStage.setX(primaryScreenBounds.getMinX());
        primaryStage.setY(primaryScreenBounds.getMinY());
        primaryStage.setWidth(primaryScreenBounds.getWidth());
        primaryStage.setHeight(primaryScreenBounds.getHeight());
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        //closing all added to array Stages
        primaryStage.setOnCloseRequest((e) -> {
            /*try(OutputStream os = new FileOutputStream("config.properties")){
                properties.setProperty("DocWindowX", Double.toString(doctorStage.getX()));
                properties.setProperty("DocWindowY", Double.toString(doctorStage.getY()));
                properties.setProperty("DocWindowWidth", Double.toString(doctorStage.getWidth()));
                properties.setProperty("DocWindowHeight", Double.toString(doctorStage.getHeight()));
                properties.store(os,null);
            }
            catch (IOException exc){}
            */
            for(Stage x : stages){
                System.out.println(x);
            x.close();
        }

});

    }
    public static void main(String[] args){
        launch(args);
    }
    public void initProperties(Properties props){
        try(OutputStream os = new FileOutputStream("config.properties")){
props.setProperty("DocWindowX", "500");
props.setProperty("DocWindowY", "100");
props.store(os, null);
        }
        catch (IOException e){}
    }
    public SortedList<Doctor> getSortedList(ObservableList<Doctor> doclist, TextField searchField){
        doctorFilteredList = new FilteredList<>(doclist, p->true);
        searchField.textProperty().addListener((ov, o, nv) -> {
            doctorFilteredList.setPredicate(doctor -> {
                if (nv == null || nv.equals("")) {
                    return true;
                }
                String filter = nv.toLowerCase();
                if (doctor.getNameSurname().toLowerCase().contains(filter)) {
                    return true;
                } else if (doctor.getDepartment().toLowerCase().contains(filter)) {
                    return true;
                } else return false;


            });

        });
        doctorSortedList = new SortedList<>(doctorFilteredList);
        return doctorSortedList;
    }
}