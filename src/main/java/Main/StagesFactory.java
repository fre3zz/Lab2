package Main;

import Classes.Department;
import Classes.DepartmentDAO;
import Classes.Doctor;
import Classes.DoctorDAO;
import static Utils.DataOperations.*;
import Main.Lab;
import Utils.Corrections;
import Utils.DBUtil;
import Utils.Validations;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.controlsfx.control.textfield.TextFields;

import java.io.*;
import java.sql.SQLException;
import java.util.Properties;
import java.util.stream.Collectors;

public class StagesFactory {
    private static boolean addDoctorStageOpened = false;
    private static boolean editDoctorStageOpened = false;
    private static TableView<Doctor> docTableView = new TableView<Doctor>();


    public static Stage addNewAnalysisStage(){
        Stage newAnStage = new Stage();
        newAnStage.setTitle("Add new analysis");
        newAnStage.initStyle(StageStyle.DECORATED);
        GridPane gp = new GridPane();
        Scene sc = new Scene(gp);
        ColumnConstraints col1 = new ColumnConstraints();
        ColumnConstraints col2 = new ColumnConstraints();

        gp.getColumnConstraints().addAll(col1, col2);
        VBox textBox = new VBox();

        Text nameText = new Text("ФИО:");
        Text dateText = new Text("Дата:");
        Text materialText = new Text("Материал:");
        Text analysisTypeText = new Text("Анализ:");
        Text docText = new Text("Направил(а):");
        Text tubeText = new Text("Пробирок сделано:");

        textBox.setSpacing(10);

        textBox.getChildren().addAll(nameText,dateText, materialText,analysisTypeText,docText, tubeText);
        gp.add(textBox,0,0);
        Button cancel = new Button("cancel");
        gp.add(cancel, 0,1);
        cancel.setOnAction((e) -> {
            newAnStage.close();
            Lab.setAddAnStOpened(false);
            Lab.stages.remove(newAnStage);
        });



        VBox textFieldBox = new VBox();
        newAnStage.setScene(sc);
        newAnStage.setOnCloseRequest((e) -> {
            newAnStage.close();
            Lab.setAddAnStOpened(false);
            Lab.stages.remove(newAnStage);
        });
        return newAnStage;
    }

    /*
    Stage для добавления новых докторов
    Поля - ФИО доктора, название отделения, номер телефона;
    Кнопка отмена - закрывает окно
    Кнопка сохранить добавляет нового врача, если выполняются условия
     */
     public static Stage addNewDocStage(TableView docTableView){
         //При открытии окна подгружаем список отделений для выпадающего меню

         ObservableList<Department> deptList = DepartmentDAO.getDepartmentList();
         Stage newStage = new Stage();
         newStage.setResizable(false);
         newStage.setTitle("Add new doc");
         newStage.initStyle(StageStyle.DECORATED);

         //все элементы расположены в GridPane
         //2 колонки
         //

         GridPane gp = new GridPane();
         Scene sc = new Scene(gp);
         gp.setHgap(10);
         gp.setVgap(10);
         gp.setPadding(new Insets(10));

         ColumnConstraints cc1 = new ColumnConstraints();
         ColumnConstraints cc2 = new ColumnConstraints();
         gp.getColumnConstraints().addAll(cc1,cc2);

         Text nameText = new Text("ФИО врача:");
         gp.add(nameText, 0,0);
         Text departmentText = new Text("Отделение:");
         gp.add(departmentText, 0,1);
         Text phoneText = new Text("Телефон:");
         gp.add(phoneText,0,2);
         Button cancel = new Button("cancel");
         gp.add(cancel, 0, 3);

         //Кнопка для закрытия окна
         //Разблокирует открытие подобных окон

         cancel.setPrefHeight(40);
         cancel.setOnAction((e) -> {
             addDoctorStageOpened = false;
             newStage.close();
             Lab.stages.remove(newStage);
         });

         //TextField для заполнения данных

         TextField nameTextField = new TextField();
         nameTextField.setTooltip(new Tooltip("ФИО врача: Фамилия Имя Отчество или Фамилия И.О."));
         gp.add(nameTextField, 1, 0);
         TextField departmentTextField = new TextField();
         departmentTextField.setTooltip(new Tooltip("Отделение"));
         gp.add(departmentTextField, 1, 1);
         TextField phoneTextField = new TextField();
         phoneTextField.setTooltip(new Tooltip("Телефон: +71234567890 или 12-34"));
         gp.add(phoneTextField, 1, 2);

         //Для текстового поля с отделениями делаем автозаполнение
         TextFields.bindAutoCompletion(departmentTextField, deptList.stream().map(e -> e.getDepartmantName()).collect(Collectors.toList()));

         //Кнопка для добавления врача

         Button add = new Button("add doc");
         gp.add(add, 1, 3);
         add.setPrefHeight(40);
         add.setPrefWidth(80);
         add.setDisable(true);
         Tooltip tp = new Tooltip("Невозможно добавить, так как такой врач уже есть");
         add.setTooltip(tp);

         //Значения в каждом TextField проверяются на валидность и если проходят проверку, то кнопка add разблокируется

         nameTextField.textProperty().addListener((ov, o, nv) -> {
            if(Validations.isNameValid(nameTextField.textProperty().get())
            && Validations.isDepartmentValid(departmentTextField.textProperty().get())
            && Validations.isPhoneValid(phoneTextField.textProperty().get()))
                {
                    add.setDisable(false);
                }
            else add.setDisable(true);
         });

         departmentTextField.textProperty().addListener((ov, o, nv) -> {
            if(Validations.isNameValid(nameTextField.textProperty().get())
            && Validations.isDepartmentValid(departmentTextField.textProperty().get())
            && Validations.isPhoneValid(phoneTextField.textProperty().get()))
                {
                    add.setDisable(false);
                }
            else add.setDisable(true);
         });

        phoneTextField.textProperty().addListener((ov, o, nv) -> {
            if(Validations.isNameValid(nameTextField.textProperty().get())
                    && Validations.isDepartmentValid(departmentTextField.textProperty().get())
                    && Validations.isPhoneValid(phoneTextField.textProperty().get()))
                {
                 add.setDisable(false);
                }
                else add.setDisable(true);
         });


/*
При нажатии на кнопку add создается новый объект doctor c параметрами имя и номер телефона.
После этого вызывается метод DoctorDAO.addDoctor() и получаем номер добавленного доктора.
Если номер меньше, чем размер массива, значит добавили нового. Если нет, значит такой доктор уже есть.
Если такой уже есть, то не закрываем окно, а выводим предупреждение.
 */

        add.setOnAction((e)  ->
        {
            Doctor doctor = new Doctor(nameTextField.getText());
            doctor.setPhoneNumber(phoneTextField.getText());
            for(Doctor d : DoctorDAO.getDoctorList()){
                if(d.equals(doctor)) {
                    tp.show(newStage);
                    return;
                }
            }
            int size = DoctorDAO.getDoctorList().size();
            DoctorDAO.addDoctor(doctor, departmentTextField.getText());
            if(DoctorDAO.getDoctorList().size()>size){
                newStage.close();
                addDoctorStageOpened = false;
                Lab.stages.remove(newStage);
            }
            else{
                tp.show(newStage);
                return;
            }
        });

        //Действия при закрытии сцены

        newStage.setScene(sc);
        newStage.setOnCloseRequest((e) -> {
            addDoctorStageOpened = false;
            newStage.close();
            Lab.stages.remove(newStage);
        });

        return newStage;
    }


//Stage для редактирования врачей, во многом аналогична Stage для добавления
    public static Stage editDocStage(Doctor doctor){
        //Сохранение текущих значений полей класса

        int docId = doctor.getDoctorId();
        String docName = doctor.getNameSurname();
        Department docDept = doctor.getDepartment();
        String docPhone = doctor.getPhoneNumber();

        Stage newStage = new Stage();
        newStage.setResizable(false);
        newStage.setTitle("edit doc");
        newStage.initStyle(StageStyle.DECORATED);

        GridPane gp = new GridPane();
        Scene sc = new Scene(gp);

        gp.setHgap(10);
        gp.setVgap(10);
        gp.setPadding(new Insets(10));
        ColumnConstraints cc1 = new ColumnConstraints();
        ColumnConstraints cc2 = new ColumnConstraints();
        gp.getColumnConstraints().addAll(cc1,cc2);
        Text nameText = new Text("ФИО врача:");
        gp.add(nameText, 0,0);
        Text departmentText = new Text("Отделение:");
        gp.add(departmentText, 0,1);
        Text phoneText = new Text("Телефон:");
        gp.add(phoneText,0,2);
        Button cancel = new Button("cancel");
        gp.add(cancel, 0, 3);
        cancel.setPrefHeight(40);

        cancel.setOnAction((e) -> {
            doctor.setPhoneNumber(docPhone);
            doctor.setDepartment(docDept);
            doctor.setNameSurname(docName);
            newStage.close();
            editDoctorStageOpened = false;
            Lab.stages.remove(newStage);
        });

        Button edit = new Button("edit doc");
        gp.add(edit, 1, 3);
        edit.setPrefHeight(40);
        edit.setPrefWidth(80);
        TextField nameTextField = new TextField();
        gp.add(nameTextField, 1, 0);
        nameTextField.setTooltip(new Tooltip("ФИО врача: Фамилия Имя Отчество или Фамилия И.О."));
        nameTextField.setText(docName);
        TextField departmentTextField = new TextField();
        departmentTextField.setTooltip(new Tooltip("Отделение"));
        gp.add(departmentTextField, 1, 1);
        departmentTextField.setText(docDept.getDepartmantName());
        TextField phoneTextField = new TextField();
        phoneTextField.setTooltip(new Tooltip("Телефон: +71234567890 или 12-34"));
        gp.add(phoneTextField, 1, 2);
        phoneTextField.setText(docPhone);
        edit.setDisable(true);

        Tooltip tp = new Tooltip("Невозможно добавить, так как такой врач уже есть");
        edit.setTooltip(tp);

        //Кнопка edit остается неактивной, если значения в текстовых полях не проходят валидацию

        nameTextField.textProperty().addListener((ov, o, nv) -> {
            if(Validations.isNameValid(nameTextField.textProperty().get())
                    && Validations.isDepartmentValid(departmentTextField.textProperty().get())
                    && Validations.isPhoneValid(phoneTextField.textProperty().get()))
            {
                edit.setDisable(false);
            }
            else edit.setDisable(true);
        });

        departmentTextField.textProperty().addListener((ov, o, nv) -> {
            if(Validations.isNameValid(nameTextField.textProperty().get())
                    && Validations.isDepartmentValid(departmentTextField.textProperty().get())
                    && Validations.isPhoneValid(phoneTextField.textProperty().get()))
            {
                edit.setDisable(false);
            }
            else edit.setDisable(true);
        });

        phoneTextField.textProperty().addListener((ov, o, nv) -> {
            if(Validations.isNameValid(nameTextField.textProperty().get())
                    && Validations.isDepartmentValid(departmentTextField.textProperty().get())
                    && Validations.isPhoneValid(phoneTextField.textProperty().get()))
            {
                edit.setDisable(false);
            }
            else edit.setDisable(true);
        });

        edit.setOnAction((e) -> {
            boolean nameChanged;
            boolean phoneChanged;
            boolean departmentChanged;

            nameChanged = !nameTextField.getText().equals(docName);
            phoneChanged = !phoneTextField.getText().equals(docPhone);

            //создаем объект, с введенными в текстовых полях данными

            Doctor doc = new Doctor(nameTextField.getText());
            doc.setDoctorId(docId);
            doc.setPhoneNumber(phoneTextField.getText());

            if(departmentTextField.getText().equals(docDept.getDepartmantName())){
               doc.setDepartment(docDept);
               departmentChanged = false;
            }
            else{
                Department newDept = new Department(departmentTextField.getText());
                int newDepId = DepartmentDAO.addDepartment(newDept);
                newDept.setDepartmentId(newDepId);
                doc.setDepartment(newDept);
                departmentChanged = true;
            }
            //Если что-то поменялось - вызываем UPDATE
            if(nameChanged || departmentChanged || phoneChanged) {
                if(nameChanged) {
                    try {
                        DoctorDAO.loadList();
                    } catch (Exception exc) {
                        System.out.println(exc);
                    }
                    for (Doctor d : DoctorDAO.getDoctorList()) {
                        if (d.equals(doc)) {
                            tp.show(newStage);
                            return;
                        }
                    }
                }
                    DoctorDAO.updateDoctor(doc, nameChanged);
                }
                editDoctorStageOpened = false;
                newStage.close();
                Lab.stages.remove(newStage);
        });

        newStage.setScene(sc);
        newStage.setAlwaysOnTop(true);
        newStage.setOnCloseRequest((e) -> {
            editDoctorStageOpened = false;
            newStage.close();
            Lab.stages.remove(newStage);
        });
        return newStage;
    }

    public static Stage dbSettingsStage(){
        Stage newStage = new Stage();
        newStage.setResizable(false);
        GridPane gp = new GridPane();
        gp.setPadding(new Insets(10));
        gp.setVgap(10);
        gp.setHgap(10);
        Scene scene = new Scene(gp);

        ColumnConstraints cs1 = new ColumnConstraints();

        ColumnConstraints cs2 = new ColumnConstraints();
        ColumnConstraints cs3 = new ColumnConstraints();
        gp.getColumnConstraints().addAll(cs1, cs2, cs3);

        TextField dbNameTextField = new TextField(Lab.getDbName());
        Button connectButton = new Button("Connect");
        gp.add(new Label("DB name:"), 0, 0);
        gp.add(dbNameTextField, 1,0);
        gp.add(connectButton, 2, 0);

        TextField portTextField = new TextField(Lab.getDbPort());
        Label connectedLabel = new Label();
        gp.addRow(1,new Label("Port:"), portTextField,connectedLabel);
        Button cancelButton = new Button("Cancel");
        cancelButton.setPrefHeight(25);
        cancelButton.setPrefWidth(75);
        gp.add(cancelButton, 0, 2);
        Button saveButton = new Button("Save and exit");
        saveButton.setPrefHeight(25);
        saveButton.setPrefWidth(75);
        gp.add(saveButton,2,2);
        newStage.setScene(scene);

        connectButton.setOnAction((e) -> {
            DBUtil.dbConnect();
        });

        cancelButton.setOnAction((e) -> {
            Lab.setDbSettingsStageOpened(false);
            newStage.close();
        });
        saveButton.setOnAction((e) -> {
                    Lab.setDbSettingsStageOpened(false);
                    Properties props = Lab.getProperties();
                    try (OutputStream os = new FileOutputStream("config.properties")) {
                        props.setProperty("DBname", dbNameTextField.getText());
                        props.setProperty("DBport", portTextField.getText());
                        props.store(os, null);
                    } catch (IOException exc) {
                    }
                    Lab.setdbName(dbNameTextField.getText());
                    Lab.setDbPort(portTextField.getText());
                    newStage.close();
                }
        );
        newStage.setOnCloseRequest((e) -> {
            Lab.setDbSettingsStageOpened(false);
            newStage.close();
        });
        return newStage;

    }

    //создание сцены со списком врачей, содержащей таблицу, кнопки добавления записей,
    // кнопку обновления данных и лэйблы для отображения данных о выбранном враче

    public static Stage doctorStage(){
        // Создание Stage и запрет на передвижение ее слишком высоко

        Stage doctorStage = new Stage();
        Lab.stages.add(doctorStage);
        doctorStage.setTitle("Doctor list");
        doctorStage.initStyle(StageStyle.UTILITY);
        doctorStage.yProperty().addListener((ov, nv, olv) -> {
            if(doctorStage.getY() <= 60) doctorStage.setY(60);
        });
        doctorStage.setAlwaysOnTop(true);

        //Выбор положения Stage, исходя из сохраненных данных в конфигурации
        //Будет убрано в отдельный класс, содержащий все свойства всех окон

        double DocWindowX = 0;
        double DocWindowY = 0;
        double DocWindowHeight = 0;
        double DocWindowWidth = 0;
        try(InputStream is = new FileInputStream("config.properties"))
        {
            Properties props = Lab.getProperties();
            props.load(is);
            DocWindowX = Double.parseDouble(props.getProperty("DocWindowX"));
            DocWindowY = Double.parseDouble(props.getProperty("DocWindowY"));
            DocWindowWidth = Double.parseDouble(props.getProperty("DocWindowWidth"));
            DocWindowHeight = Double.parseDouble(props.getProperty("DocWindowHeight"));
        }
        catch (IOException e){

        }

        doctorStage.setHeight(DocWindowHeight);
        doctorStage.setWidth(DocWindowWidth);
        doctorStage.setY(DocWindowY);
        doctorStage.setX(DocWindowX);

        //Все элементы будут размещены в таблице GridPane, содержащей 3 колонки.

        GridPane doctorPane = new GridPane();
        doctorPane.setVgap(10);
        doctorPane.setHgap(10);
        doctorPane.setPadding(new Insets(5));
        ColumnConstraints doccol1 = new ColumnConstraints();
        doccol1.setHgrow(Priority.ALWAYS);
        ColumnConstraints doccol2 = new ColumnConstraints(150,150,200);
        ColumnConstraints doccol3 = new ColumnConstraints(200,500, Double.MAX_VALUE);
        doctorPane.getColumnConstraints().addAll(doccol1, doccol2, doccol3);

        //В первой колонке - таблица, а также поле для поиска и кнопка обновления данных, все внутри VBox

        VBox vbox1 = new VBox();
        vbox1.setSpacing(20);
        HBox hbox1 = new HBox();
        hbox1.setSpacing(10);
        TextField docSearchField = new TextField();
        hbox1.getChildren().addAll(new Text("Search:"), docSearchField);
        Button refreshTable = new Button();

        TableView docTableView = new TableView();
        docTableView.setPrefWidth(650);

        TableColumn<Doctor, String> nameCol = new TableColumn<>("Classes.Doctor");
        nameCol.setCellValueFactory(new PropertyValueFactory<Doctor, String>("NameSurname"));
        nameCol.setPrefWidth(docTableView.getPrefWidth() * 0.8);
        TableColumn<Doctor, String> depCol = new TableColumn<Doctor, String>("Department");
        depCol.setCellValueFactory(p->new ReadOnlyObjectWrapper(p.getValue().getDepartment().getDepartmantName()));
        depCol.setPrefWidth(docTableView.getPrefWidth() *0.2);
        ObservableList<Doctor> doctorList = DoctorDAO.getDoctorListWithLoad();
        docTableView.getColumns().setAll(nameCol, depCol);
        docTableView.prefHeightProperty().bind(doctorStage.heightProperty());

        //Создание FilteredList на основе docList
        //Связь с полем поиска
        //Создание SortedList и привязка к колонкам таблицы

        FilteredList<Doctor> doctorFilteredList = new FilteredList<>(doctorList, p->true);
        docSearchField.textProperty().addListener((ov, o, nv) -> {
            doctorFilteredList.setPredicate(doctor -> {
                if (nv == null || nv.equals("")) {
                    return true;
                }
                String filter = nv.toLowerCase();
                if (doctor.getNameSurname().toLowerCase().contains(filter)) {
                    return true;
                } else if (doctor.getDepartment().getDepartmantName().toLowerCase().contains(filter)&&doctor.getDepartment()!=null) {
                    return true;
                } else return false;
            });
        });

        SortedList<Doctor> sortedList = new SortedList<>(doctorFilteredList);
        sortedList.comparatorProperty().bind(docTableView.comparatorProperty());
        docTableView.setItems(sortedList);

        refreshTable.setOnAction((e) -> {
            try {
                DoctorDAO.loadList();
                System.gc();
            }
            catch (Exception exc){
                System.out.println(exc);
            }
        });

        //Добавление всех элементов в vbox и в GridPane
        vbox1.getChildren().addAll(hbox1, docTableView, refreshTable);
        doctorPane.add(vbox1, 0,0);

        //VBOX with labels Col#2 -  неизменяемые текст-боксы
        VBox textBoxDoc = new VBox();
        Text idText = new Text("id:");
        Text docNameText = new Text("ФИО врача:");
        Text departmentText = new Text("Отделение:");
        Text phoneText = new Text("Телефон:");

        //Button for new Analysis
        //Вызывает окрытие новой сцены для добавления доктора

        Button addDocButton = new Button("ADD Doctor");
        addDocButton.prefWidthProperty().bind(doccol2.prefWidthProperty());
        addDocButton.prefHeightProperty().bind(addDocButton.prefWidthProperty());
        addDocButton.setOnAction((e) ->{
            if(!addDoctorStageOpened) {
                System.out.println("add button clicked");
                addDoctorStageOpened = true;
                Stage newStage = StagesFactory.addNewDocStage(docTableView);
                newStage.setAlwaysOnTop(true);
                Lab.stages.add(newStage);
                newStage.show();
            }
        });

        textBoxDoc.getChildren().addAll(idText, docNameText, departmentText, phoneText, addDocButton);
        doctorPane.add(textBoxDoc, 1, 0);

        //Добавление изменяемых в зависимости от выбранного в таблице врача Label

        VBox docLabelBox = new VBox();
        Label idLabel = new Label();
        Label docNameLabel = new Label();
        Label docDepartmentLabel = new Label();
        Label docPhoneLabel = new Label();

        docLabelBox.getChildren().addAll(idLabel, docNameLabel, docDepartmentLabel, docPhoneLabel);
        doctorPane.add(docLabelBox, 2, 0);

        //Необходимость в ReadOnlyObjectWrapper обусловлена тем, что у нас поля классов не property

        docTableView.getSelectionModel().selectedItemProperty().addListener((o, ov, nv) ->{
                    if(o != null && o.getValue()!= null) {
                        Doctor selected = (Doctor) docTableView.getSelectionModel().getSelectedItem();
                        idLabel.textProperty().bind(new ReadOnlyObjectWrapper(new Integer(selected.getDoctorId()).toString()));
                        docNameLabel.textProperty().bind(selected.NameSurnameProperty());
                        docDepartmentLabel.textProperty().bind(new ReadOnlyObjectWrapper<>(selected.getDepartment().getDepartmantName()));
                        docPhoneLabel.textProperty().bind(new ReadOnlyObjectWrapper<>(selected.getPhoneNumber()));
                    }
                }
        );

        //Обработка действия по двойному клику мыши

        docTableView.setOnMouseClicked((e) -> {
            if(!e.isPrimaryButtonDown()&& e.getClickCount() == 2 && !editDoctorStageOpened){
                Doctor selected = (Doctor) docTableView.getSelectionModel().getSelectedItem();
                if(selected != null) {
                    Stage st = StagesFactory.editDocStage(selected);
                    editDoctorStageOpened = true;
                    st.show();
                }
                docTableView.getSelectionModel().clearSelection();
            }
        });

        Scene doctorScene = new Scene(doctorPane);
        doctorStage.setScene(doctorScene);

        //Сохранение свойст окна, будет убранов
        doctorStage.setOnCloseRequest((e) -> {
            doctorStage.close();
            //docMenuItem.setSelected(false);
            try(OutputStream os = new FileOutputStream("config.properties")){
                Lab.getProperties().setProperty("DocWindowX", Double.toString(doctorStage.getX()));
                Lab.getProperties().setProperty("DocWindowY", Double.toString(doctorStage.getY()));
                Lab.getProperties().setProperty("DocWindowWidth", Double.toString(doctorStage.getWidth()));
                Lab.getProperties().setProperty("DocWindowHeight", Double.toString(doctorStage.getHeight()));
                Lab.getProperties().store(os,null);
            }
            catch (IOException exc){}
        });
        //Check box in menu action
        return doctorStage;

    }


}
