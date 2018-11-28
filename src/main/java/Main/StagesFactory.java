package Main;

import Classes.Doctor;
import Classes.DoctorDAO;
import Main.Lab;
import Utils.Corrections;
import Utils.DBUtil;
import Utils.Validations;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.Properties;

public class StagesFactory {
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
     public static Stage addNewDocStage(){
        Stage newStage = new Stage();
        newStage.setResizable(false);
        newStage.setTitle("Add new doc");
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
            Lab.setAddDoctorStageOpened(false);
            newStage.close();
            Lab.stages.remove(newStage);
        });

      Button add = new Button("add doc");
      gp.add(add, 1, 3);
      add.setPrefHeight(40);
      add.setPrefWidth(80);
        TextField nameTextField = new TextField();
        nameTextField.setTooltip(new Tooltip("ФИО врача: Фамилия Имя Отчество или Фамилия И.О."));
        gp.add(nameTextField, 1, 0);
        TextField departmentTextField = new TextField();
         departmentTextField.setTooltip(new Tooltip("Отделение"));
        gp.add(departmentTextField, 1, 1);
        TextField phoneTextField = new TextField();
        phoneTextField.setTooltip(new Tooltip("Телефон: +71234567890 или 12-34"));
        gp.add(phoneTextField, 1, 2);

        add.setDisable(true);

nameTextField.textProperty().addListener((ov, o, nv) -> {
nameTextField.setText(Corrections.nameCorrection(nv));


    if(Validations.isNameValid(nameTextField.textProperty().get())
            && Validations.isDepartmentValid(departmentTextField.textProperty().get())
            && Validations.isPhoneValid(phoneTextField.textProperty().get()))
    {

        add.setDisable(false);
    }
    else add.setDisable(true);
});

departmentTextField.textProperty().addListener((ov, o, nv) -> {
    departmentTextField.setText(Corrections.departmentCorrection(nv));
    if(Validations.isNameValid(nameTextField.textProperty().get())
            && Validations.isDepartmentValid(departmentTextField.textProperty().get())
            && Validations.isPhoneValid(phoneTextField.textProperty().get()))
    {
        add.setDisable(false);
    }
    else add.setDisable(true);
});

phoneTextField.textProperty().addListener((ov, o, nv) -> {
             phoneTextField.setText(Corrections.phoneCorrection(nv));

             if(Validations.isNameValid(nameTextField.textProperty().get())
                     && Validations.isDepartmentValid(departmentTextField.textProperty().get())
                     && Validations.isPhoneValid(phoneTextField.textProperty().get()))
             {
                 add.setDisable(false);
             }
             else add.setDisable(true);
         });



add.setOnAction((e)  -> {
    Doctor doc = new Doctor(0, nameTextField.getText(), departmentTextField.getText(), phoneTextField.getText());
    if(!Doctor.checkPresence(Lab.getDoctorList(), doc)){
        Lab.getDoctorList().add(new Doctor(0, nameTextField.getText(), departmentTextField.getText(), phoneTextField.getText()));
        try {
            DoctorDAO.insertDoc(nameTextField.getText(), departmentTextField.getText());

        }
        catch (Exception exc){}
        newStage.close();
        Lab.setAddDoctorStageOpened(false);
        Lab.stages.remove(newStage);
    }
    else{
        Tooltip tp = new Tooltip("Невозможно добавить, так как такой врач уже есть");
        add.setTooltip(tp);
        tp.show(newStage);
    }
});



        newStage.setScene(sc);
        newStage.setOnCloseRequest((e) -> {

            Lab.setAddDoctorStageOpened(false);
            newStage.close();
            Lab.stages.remove(newStage);
        });
        return newStage;
    }



    public static Stage editDocStage(Doctor doctor){
        String docName = doctor.getNameSurname();
        String docDept = doctor.getDepartment();
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
            Lab.setEditDoctorStageOpened(false);
            Lab.stages.remove(newStage);
        });

        Button add = new Button("edit doc");
        gp.add(add, 1, 3);
        add.setPrefHeight(40);
        add.setPrefWidth(80);
        TextField nameTextField = new TextField();
        gp.add(nameTextField, 1, 0);
        nameTextField.setTooltip(new Tooltip("ФИО врача: Фамилия Имя Отчество или Фамилия И.О."));
        nameTextField.textProperty().bindBidirectional(doctor.NameSurnameProperty());
        TextField departmentTextField = new TextField();
        departmentTextField.setTooltip(new Tooltip("Отделение"));
        gp.add(departmentTextField, 1, 1);
        departmentTextField.textProperty().bindBidirectional(doctor.departmentProperty());
        TextField phoneTextField = new TextField();
        phoneTextField.setTooltip(new Tooltip("Телефон: +71234567890 или 12-34"));
        gp.add(phoneTextField, 1, 2);
        phoneTextField.textProperty().bindBidirectional(doctor.phoneNumberProperty());
        add.setDisable(true);
        nameTextField.textProperty().addListener((ov, o, nv) -> {
            nameTextField.setText(Corrections.nameCorrection(nv));


            if(Validations.isNameValid(nameTextField.textProperty().get())
                    && Validations.isDepartmentValid(departmentTextField.textProperty().get())
                    && Validations.isPhoneValid(phoneTextField.textProperty().get()))
            {

                add.setDisable(false);
            }
            else add.setDisable(true);
        });

        departmentTextField.textProperty().addListener((ov, o, nv) -> {
            departmentTextField.setText(Corrections.departmentCorrection(nv));
            if(Validations.isNameValid(nameTextField.textProperty().get())
                    && Validations.isDepartmentValid(departmentTextField.textProperty().get())
                    && Validations.isPhoneValid(phoneTextField.textProperty().get()))
            {
                add.setDisable(false);
            }
            else add.setDisable(true);
        });

        phoneTextField.textProperty().addListener((ov, o, nv) -> {
            phoneTextField.setText(Corrections.phoneCorrection(nv));

            if(Validations.isNameValid(nameTextField.textProperty().get())
                    && Validations.isDepartmentValid(departmentTextField.textProperty().get())
                    && Validations.isPhoneValid(phoneTextField.textProperty().get()))
            {
                add.setDisable(false);
            }
            else add.setDisable(true);
        });
        add.setOnAction((e) -> {

            Doctor doc = new Doctor(0, nameTextField.getText(), departmentTextField.getText(), phoneTextField.getText());
            if(!Doctor.checkPresence(Lab.getDoctorList(), doc)){

                Lab.setEditDoctorStageOpened(false);
                newStage.close();

                Lab.stages.remove(newStage);
            }
            else{
                Tooltip tp = new Tooltip("Невозможно добавить, так как такой врач уже есть");
                add.setTooltip(tp);
                tp.show(newStage);
            }
        });



        newStage.setScene(sc);
        newStage.setAlwaysOnTop(true);
        newStage.setOnCloseRequest((e) -> {
            Lab.setEditDoctorStageOpened(false);
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
}
